package com.example.equipmentborrowingapp.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
private const val USERS_COLLECTION = "users"
class AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    fun registerUser(
        name: String,
        email: String,
        password: String,
        role: String,
        institutionId: String,
        studentId: String = "",
        department: String = "",
        semester: String = "",
        phone: String = "",
        onResult: (Boolean, String) -> Unit
    ) {
        val normalizedName = name.trim()
        val normalizedEmail = email.trim()
        val normalizedRole = role.trim().lowercase()
        val normalizedInstitutionId = institutionId.trim()
        val normalizedStudentId = studentId.trim()
        val normalizedDepartment = department.trim()
        val normalizedSemester = semester.trim()
        val normalizedPhone = phone.trim()

        if (
            normalizedName.isBlank() ||
            normalizedEmail.isBlank() ||
            password.isBlank() ||
            normalizedRole.isBlank() ||
            normalizedInstitutionId.isBlank()
        ) {
            onResult(false, "Required fields are missing")
            return
        }

        auth.createUserWithEmailAndPassword(normalizedEmail, password)
            .addOnSuccessListener { authResult ->
                val uid = authResult.user?.uid ?: ""

                if (uid.isBlank()) {
                    onResult(false, "User ID not found")
                    return@addOnSuccessListener
                }

                val userMap = hashMapOf(
                    "uid" to uid,
                    "name" to normalizedName,
                    "email" to normalizedEmail,
                    "role" to normalizedRole,
                    "institutionId" to normalizedInstitutionId,
                    "verificationStatus" to "pending",
                    "studentId" to normalizedStudentId,
                    "department" to normalizedDepartment,
                    "semester" to normalizedSemester,
                    "phone" to normalizedPhone,
                    "createdAt" to System.currentTimeMillis()
                )

                firestore.collection(USERS_COLLECTION)
                    .document(uid)
                    .set(userMap)
                    .addOnSuccessListener {
                        onResult(
                            true,
                            "Registration successful. Please wait for admin verification."
                        )
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

                val uid = firebaseUser.uid.trim()

                if (uid.isBlank()) {
                    onResult(false, "Google user ID not found")
                    return@addOnSuccessListener
                }

                val userRef = firestore.collection(USERS_COLLECTION).document(uid)

                userRef.get()
                    .addOnSuccessListener { document ->
                        if (document.exists()) {
                            val institutionId = document.getString("institutionId")?.trim().orEmpty()

                            if (institutionId.isBlank()) {
                                onResult(false, "GOOGLE_PROFILE_REQUIRED")
                            } else {
                                onResult(true, "Google login successful")
                            }
                        } else {
                            onResult(false, "GOOGLE_PROFILE_REQUIRED")
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

    fun completeGoogleRegistration(
        name: String,
        email: String,
        institutionId: String,
        studentId: String,
        department: String,
        semester: String,
        phone: String,
        onResult: (Boolean, String) -> Unit
    ) {
        val firebaseUser = auth.currentUser

        if (firebaseUser == null) {
            onResult(false, "Google user not found. Please try again.")
            return
        }

        val uid = firebaseUser.uid.trim()
        val normalizedName = name.trim().ifBlank {
            firebaseUser.displayName?.trim().orEmpty().ifBlank { "Student" }
        }
        val normalizedEmail = email.trim().ifBlank {
            firebaseUser.email?.trim().orEmpty()
        }
        val normalizedInstitutionId = institutionId.trim()
        val normalizedStudentId = studentId.trim()
        val normalizedDepartment = department.trim()
        val normalizedSemester = semester.trim()
        val normalizedPhone = phone.trim()

        if (
            uid.isBlank() ||
            normalizedEmail.isBlank() ||
            normalizedInstitutionId.isBlank() ||
            normalizedStudentId.isBlank() ||
            normalizedDepartment.isBlank()
        ) {
            onResult(false, "Required fields are missing")
            return
        }

        val userMap = hashMapOf(
            "uid" to uid,
            "name" to normalizedName,
            "email" to normalizedEmail,
            "role" to "student",
            "institutionId" to normalizedInstitutionId,
            "verificationStatus" to "pending",
            "studentId" to normalizedStudentId,
            "department" to normalizedDepartment,
            "semester" to normalizedSemester,
            "phone" to normalizedPhone,
            "createdAt" to System.currentTimeMillis()
        )

        firestore.collection(USERS_COLLECTION)
            .document(uid)
            .set(userMap)
            .addOnSuccessListener {
                onResult(true, "Registration completed. Please wait for admin verification.")
            }
            .addOnFailureListener { e ->
                onResult(false, e.message ?: "Failed to complete Google registration")
            }
    }

    fun getUserRole(
        uid: String,
        onResult: (String?) -> Unit
    ) {
        firestore.collection(USERS_COLLECTION)
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
        firestore.collection(USERS_COLLECTION)
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
        onResult: (com.example.equipmentborrowingapp.data.model.User?) -> Unit
    ) {
        val uid = auth.currentUser?.uid
        if (uid.isNullOrBlank()) {
            onResult(null)
            return
        }

        firestore.collection(USERS_COLLECTION)
            .document(uid)
            .get()
            .addOnSuccessListener { document ->
                val user = document.toObject(com.example.equipmentborrowingapp.data.model.User::class.java)
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
                    "Password reset link sent. Please check Inbox, Spam, or Promotions folder."
                )
            }
            .addOnFailureListener { e ->
                val errorMessage = when {
                    e.message?.contains("badly formatted", ignoreCase = true) == true ->
                        "Please enter a valid email address"

                    e.message?.contains("network", ignoreCase = true) == true ->
                        "Network error. Please check your internet connection"

                    else ->
                        "Unable to send reset email. Make sure this email was registered with Email/Password, not only Google Sign-In."
                }

                onResult(false, errorMessage)
            }
    }

    fun logout() {
        auth.signOut()
    }
}
