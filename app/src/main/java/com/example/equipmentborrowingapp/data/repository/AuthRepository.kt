package com.example.equipmentborrowingapp.data.repository

import com.example.equipmentborrowingapp.data.model.User
import com.google.firebase.auth.FirebaseAuth
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
                onResult(true, "Password reset email sent")
            }
            .addOnFailureListener { e ->
                onResult(false, e.message ?: "Failed to send reset email")
            }
    }
    fun logout() {
        auth.signOut()
    }
}