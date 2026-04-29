package com.example.equipmentborrowingapp.data.repository

import com.example.equipmentborrowingapp.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    fun registerUser(
        name: String,
        email: String,
        password: String,
        role: String,
        onResult: (Boolean, String) -> Unit
    ) {
        val normalizedName = name.trim()
        val normalizedEmail = email.trim()
        val normalizedRole = role.trim().lowercase()

        auth.createUserWithEmailAndPassword(normalizedEmail, password)
            .addOnSuccessListener { authResult ->
                val uid = authResult.user?.uid ?: ""

                val user = User(
                    uid = uid,
                    name = normalizedName,
                    email = normalizedEmail,
                    role = normalizedRole
                )

                firestore.collection("users")
                    .document(uid)
                    .set(user)
                    .addOnSuccessListener {
                        onResult(true, "Registration successful")
                    }
                    .addOnFailureListener { e ->
                        onResult(false, e.message ?: "Failed to save user data")
                    }
            }
            .addOnFailureListener { e ->
                onResult(false, e.message ?: "Registration failed")
            }
    }

    fun loginUser(
        email: String,
        password: String,
        onResult: (Boolean, String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email.trim(), password)
            .addOnSuccessListener {
                onResult(true, "Login successful")
            }
            .addOnFailureListener { e ->
                onResult(false, e.message ?: "Login failed")
            }
    }

    fun loginWithGoogle(
        idToken: String,
        onResult: (Boolean, String) -> Unit
    ) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(credential)
            .addOnSuccessListener { authResult ->
                val firebaseUser = authResult.user

                if (firebaseUser == null) {
                    onResult(false, "Google login failed")
                    return@addOnSuccessListener
                }

                val uid = firebaseUser.uid
                val email = firebaseUser.email?.trim() ?: ""
                val name = firebaseUser.displayName?.trim().orEmpty().ifBlank { "Student" }

                val userRef = firestore.collection("users").document(uid)

                userRef.get()
                    .addOnSuccessListener { document ->
                        if (document.exists()) {
                            onResult(true, "Google login successful")
                        } else {
                            val user = User(
                                uid = uid,
                                name = name,
                                email = email,
                                role = "student"
                            )

                            userRef.set(user)
                                .addOnSuccessListener {
                                    onResult(true, "Google account created successfully")
                                }
                                .addOnFailureListener { e ->
                                    onResult(false, e.message ?: "Failed to save Google user")
                                }
                        }
                    }
                    .addOnFailureListener { e ->
                        onResult(false, e.message ?: "Failed to check user data")
                    }
            }
            .addOnFailureListener { e ->
                onResult(false, e.message ?: "Google login failed")
            }
    }

    fun getUserRole(
        uid: String,
        onResult: (String?) -> Unit
    ) {
        firestore.collection("users")
            .document(uid.trim())
            .get()
            .addOnSuccessListener { document ->
                val role = document.getString("role")?.trim()?.lowercase()
                onResult(role)
            }
            .addOnFailureListener {
                onResult(null)
            }
    }

    fun getUserName(
        uid: String,
        onResult: (String?) -> Unit
    ) {
        firestore.collection("users")
            .document(uid.trim())
            .get()
            .addOnSuccessListener { document ->
                val name = document.getString("name")?.trim()
                onResult(name)
            }
            .addOnFailureListener {
                onResult(null)
            }
    }

    fun getCurrentUser(
        onResult: (User?) -> Unit
    ) {
        val uid = auth.currentUser?.uid
        if (uid.isNullOrBlank()) {
            onResult(null)
            return
        }

        firestore.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { document ->
                val user = document.toObject(User::class.java)
                onResult(
                    user?.copy(
                        uid = user.uid.trim(),
                        name = user.name.trim(),
                        email = user.email.trim(),
                        role = user.role.trim().lowercase()
                    )
                )
            }
            .addOnFailureListener {
                onResult(null)
            }
    }

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun getCurrentUserUid(): String? {
        return auth.currentUser?.uid
    }

    fun sendPasswordResetEmail(
        email: String,
        onResult: (Boolean, String) -> Unit
    ) {
        val normalizedEmail = email.trim()

        if (normalizedEmail.isBlank()) {
            onResult(false, "Please enter your email first")
            return
        }

        auth.sendPasswordResetEmail(normalizedEmail)
            .addOnSuccessListener {
                onResult(
                    true,
                    "If an account exists with this email, a password reset link has been sent."
                )
            }
            .addOnFailureListener { e ->
                val errorMessage = when {
                    e.message?.contains("badly formatted", ignoreCase = true) == true ->
                        "Please enter a valid email address"

                    e.message?.contains("network", ignoreCase = true) == true ->
                        "Network error. Please check your internet connection"

                    else ->
                        "Unable to send reset email. Please try again later."
                }

                onResult(false, errorMessage)
            }
    }

    fun logout() {
        auth.signOut()
    }
}