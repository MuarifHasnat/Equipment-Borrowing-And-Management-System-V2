package com.example.equipmentborrowingapp.data.repository

import com.example.equipmentborrowingapp.data.model.BorrowRequest
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale

class RequestRepository {
    private val firestore = FirebaseFirestore.getInstance()

    fun submitBorrowRequest(
        userId: String,
        userName: String,
        equipmentId: String,
        equipmentName: String,
        equipmentCategory: String,
        equipmentImageName: String,
        equipmentImageUrl: String,
        quantity: Int,
        borrowDate: String,
        dueDate: String,
        onResult: (Boolean, String) -> Unit
    ) {
        if (borrowDate.isBlank() || dueDate.isBlank()) {
            onResult(false, "Please select borrow date and due date")
            return
        }

        if (quantity <= 0) {
            onResult(false, "Quantity must be greater than 0")
            return
        }

        firestore.collection("equipment")
            .document(equipmentId)
            .get()
            .addOnSuccessListener { equipmentSnapshot ->
                if (!equipmentSnapshot.exists()) {
                    onResult(false, "Equipment not found")
                    return@addOnSuccessListener
                }

                val availableQuantity =
                    equipmentSnapshot.getLong("availableQuantity")?.toInt() ?: 0
                val isBorrowable =
                    equipmentSnapshot.getBoolean("isBorrowable") ?: true

                if (!isBorrowable) {
                    onResult(false, "This equipment is lab-use-only")
                    return@addOnSuccessListener
                }

                if (availableQuantity <= 0) {
                    onResult(false, "Equipment is out of stock")
                    return@addOnSuccessListener
                }

                if (quantity > availableQuantity) {
                    onResult(false, "Requested quantity exceeds available stock")
                    return@addOnSuccessListener
                }

                firestore.collection("borrow_requests")
                    .whereEqualTo("userId", userId)
                    .get()
                    .addOnSuccessListener { requestResult ->
                        val hasDuplicateRequest = requestResult.documents
                            .mapNotNull { it.toObject(BorrowRequest::class.java) }
                            .any { request ->
                                request.equipmentId == equipmentId &&
                                        (
                                                request.status.equals("Pending", ignoreCase = true) ||
                                                        request.status.equals("Approved", ignoreCase = true) ||
                                                        request.status.equals("Overdue", ignoreCase = true)
                                                )
                            }

                        if (hasDuplicateRequest) {
                            onResult(false, "You already have an active request for this equipment")
                            return@addOnSuccessListener
                        }

                        val docRef = firestore.collection("borrow_requests").document()
                        val request = BorrowRequest(
                            requestId = docRef.id,
                            userId = userId,
                            userName = userName,
                            equipmentId = equipmentId,
                            equipmentName = equipmentName,
                            equipmentCategory = equipmentCategory,
                            equipmentImageName = equipmentImageName.trim(),
                            equipmentImageUrl = equipmentImageUrl.trim(),
                            quantity = quantity,
                            borrowDate = borrowDate,
                            dueDate = dueDate,
                            returnedDate = "",
                            status = "Pending",
                            requestTimestamp = System.currentTimeMillis()
                        )

                        docRef.set(request)
                            .addOnSuccessListener {
                                onResult(true, "Borrow request submitted")
                            }
                            .addOnFailureListener { e ->
                                onResult(false, e.message ?: "Failed to submit request")
                            }
                    }
                    .addOnFailureListener { e ->
                        onResult(false, e.message ?: "Failed to check existing requests")
                    }
            }
            .addOnFailureListener { e ->
                onResult(false, e.message ?: "Failed to check equipment")
            }
    }

    fun getPendingRequests(
        onResult: (List<BorrowRequest>) -> Unit
    ) {
        firestore.collection("borrow_requests")
            .get()
            .addOnSuccessListener { result ->
                val allList = result.documents.mapNotNull { it.toObject(BorrowRequest::class.java) }
                syncOverdueStatuses(allList)

                val pendingList = allList.filter {
                    it.status.trim().equals("Pending", ignoreCase = true)
                }

                onResult(pendingList.sortedByDescending { it.requestTimestamp })
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

    fun getApprovedRequests(
        onResult: (List<BorrowRequest>) -> Unit
    ) {
        firestore.collection("borrow_requests")
            .get()
            .addOnSuccessListener { result ->
                val allList = result.documents.mapNotNull { it.toObject(BorrowRequest::class.java) }
                syncOverdueStatuses(allList)

                val activeBorrowedList = allList
                    .map { normalizeRequestStatus(it) }
                    .filter {
                        it.status.equals("Approved", ignoreCase = true) ||
                                it.status.equals("Overdue", ignoreCase = true)
                    }

                onResult(activeBorrowedList.sortedByDescending { it.requestTimestamp })
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

    fun getAllRequests(
        onResult: (List<BorrowRequest>) -> Unit
    ) {
        firestore.collection("borrow_requests")
            .get()
            .addOnSuccessListener { result ->
                val list = result.documents.mapNotNull { it.toObject(BorrowRequest::class.java) }
                syncOverdueStatuses(list)
                val normalizedList = list
                    .map { normalizeRequestStatus(it) }
                    .sortedByDescending { it.requestTimestamp }

                onResult(normalizedList)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

    fun getUserRequests(
        userId: String,
        onResult: (List<BorrowRequest>) -> Unit
    ) {
        firestore.collection("borrow_requests")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                val list = result.documents.mapNotNull { it.toObject(BorrowRequest::class.java) }
                syncOverdueStatuses(list)
                val normalizedList = list
                    .map { normalizeRequestStatus(it) }
                    .sortedByDescending { it.requestTimestamp }

                onResult(normalizedList)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

    fun approveRequest(
        request: BorrowRequest,
        onResult: (Boolean, String) -> Unit
    ) {
        val requestRef = firestore.collection("borrow_requests").document(request.requestId)
        val equipmentRef = firestore.collection("equipment").document(request.equipmentId)

        firestore.runTransaction { transaction ->
            val requestSnapshot = transaction.get(requestRef)
            val equipmentSnapshot = transaction.get(equipmentRef)

            if (!requestSnapshot.exists()) {
                throw Exception("Request not found")
            }

            if (!equipmentSnapshot.exists()) {
                throw Exception("Equipment not found")
            }

            val currentStatus = requestSnapshot.getString("status") ?: "Pending"
            if (!currentStatus.equals("Pending", ignoreCase = true)) {
                throw Exception("Only pending requests can be approved")
            }

            val isBorrowable = equipmentSnapshot.getBoolean("isBorrowable") ?: true
            if (!isBorrowable) {
                throw Exception("This equipment is lab-use-only")
            }

            val availableQuantity = equipmentSnapshot.getLong("availableQuantity")?.toInt() ?: 0
            val requestedQuantity = requestSnapshot.getLong("quantity")?.toInt() ?: request.quantity

            if (requestedQuantity <= 0) {
                throw Exception("Invalid request quantity")
            }

            if (availableQuantity < requestedQuantity) {
                throw Exception("Not enough stock available")
            }

            transaction.update(
                equipmentRef,
                "availableQuantity",
                availableQuantity - requestedQuantity
            )
            transaction.update(
                requestRef,
                "status",
                "Approved"
            )
        }.addOnSuccessListener {
            onResult(true, "Request approved successfully")
        }.addOnFailureListener { e ->
            onResult(false, e.message ?: "Failed to approve request")
        }
    }

    fun rejectRequest(
        request: BorrowRequest,
        onResult: (Boolean, String) -> Unit
    ) {
        val requestRef = firestore.collection("borrow_requests").document(request.requestId)

        firestore.runTransaction { transaction ->
            val requestSnapshot = transaction.get(requestRef)

            if (!requestSnapshot.exists()) {
                throw Exception("Request not found")
            }

            val currentStatus = requestSnapshot.getString("status") ?: "Pending"
            if (!currentStatus.equals("Pending", ignoreCase = true)) {
                throw Exception("Only pending requests can be rejected")
            }

            transaction.update(requestRef, "status", "Rejected")
        }.addOnSuccessListener {
            onResult(true, "Request rejected successfully")
        }.addOnFailureListener { e ->
            onResult(false, e.message ?: "Failed to reject request")
        }
    }

    fun markRequestReturned(
        request: BorrowRequest,
        onResult: (Boolean, String) -> Unit
    ) {
        val requestRef = firestore.collection("borrow_requests").document(request.requestId)
        val equipmentRef = firestore.collection("equipment").document(request.equipmentId)

        firestore.runTransaction { transaction ->
            val requestSnapshot = transaction.get(requestRef)
            val equipmentSnapshot = transaction.get(equipmentRef)

            if (!requestSnapshot.exists()) {
                throw Exception("Request not found")
            }

            if (!equipmentSnapshot.exists()) {
                throw Exception("Equipment not found")
            }

            val currentStatus = requestSnapshot.getString("status") ?: "Pending"
            if (
                !currentStatus.equals("Approved", ignoreCase = true) &&
                !currentStatus.equals("Overdue", ignoreCase = true)
            ) {
                throw Exception("Only approved or overdue requests can be returned")
            }

            val currentAvailable = equipmentSnapshot.getLong("availableQuantity")?.toInt() ?: 0
            val totalQuantity = equipmentSnapshot.getLong("totalQuantity")?.toInt() ?: 0
            val requestQuantity = requestSnapshot.getLong("quantity")?.toInt() ?: request.quantity

            if (requestQuantity <= 0) {
                throw Exception("Invalid request quantity")
            }

            val newAvailable = currentAvailable + requestQuantity
            if (newAvailable > totalQuantity) {
                throw Exception("Available quantity cannot exceed total quantity")
            }

            val today = SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
            ).format(System.currentTimeMillis())

            transaction.update(
                equipmentRef,
                "availableQuantity",
                newAvailable
            )
            transaction.update(
                requestRef,
                mapOf(
                    "status" to "Returned",
                    "returnedDate" to today
                )
            )
        }.addOnSuccessListener {
            onResult(true, "Equipment returned successfully")
        }.addOnFailureListener { e ->
            onResult(false, e.message ?: "Failed to mark returned")
        }
    }

    fun updateRequestStatus(
        requestId: String,
        newStatus: String,
        onResult: (Boolean, String) -> Unit
    ) {
        val validStatuses = listOf("Pending", "Approved", "Rejected", "Returned", "Overdue")
        if (newStatus !in validStatuses) {
            onResult(false, "Invalid status")
            return
        }

        firestore.collection("borrow_requests")
            .document(requestId)
            .get()
            .addOnSuccessListener { snapshot ->
                if (!snapshot.exists()) {
                    onResult(false, "Request not found")
                    return@addOnSuccessListener
                }

                val currentStatus = snapshot.getString("status") ?: "Pending"
                if (currentStatus == newStatus) {
                    onResult(false, "Request is already $newStatus")
                    return@addOnSuccessListener
                }

                val isValidTransition = when (currentStatus) {
                    "Pending" -> newStatus == "Approved" || newStatus == "Rejected"
                    "Approved" -> newStatus == "Returned" || newStatus == "Overdue"
                    "Overdue" -> newStatus == "Returned"
                    "Rejected" -> false
                    "Returned" -> false
                    else -> false
                }

                if (!isValidTransition) {
                    onResult(false, "Invalid status change from $currentStatus to $newStatus")
                    return@addOnSuccessListener
                }

                val updates = mutableMapOf<String, Any>(
                    "status" to newStatus
                )

                if (newStatus == "Returned") {
                    val today = SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.getDefault()
                    ).format(System.currentTimeMillis())
                    updates["returnedDate"] = today
                }

                firestore.collection("borrow_requests")
                    .document(requestId)
                    .update(updates)
                    .addOnSuccessListener {
                        onResult(true, "Request $newStatus")
                    }
                    .addOnFailureListener { e ->
                        onResult(false, e.message ?: "Failed to update request")
                    }
            }
            .addOnFailureListener { e ->
                onResult(false, e.message ?: "Failed to load request")
            }
    }

    private fun normalizeRequestStatus(request: BorrowRequest): BorrowRequest {
        return if (
            request.status.equals("Approved", ignoreCase = true) &&
            isDueDatePast(request.dueDate)
        ) {
            request.copy(status = "Overdue")
        } else {
            request
        }
    }

    private fun syncOverdueStatuses(requests: List<BorrowRequest>) {
        requests.forEach { request ->
            if (
                request.status.equals("Approved", ignoreCase = true) &&
                isDueDatePast(request.dueDate) &&
                request.requestId.isNotBlank()
            ) {
                firestore.collection("borrow_requests")
                    .document(request.requestId)
                    .update("status", "Overdue")
            }
        }
    }

    private fun isDueDatePast(dueDate: String): Boolean {
        if (dueDate.isBlank()) return false

        return try {
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            format.isLenient = false

            val due = format.parse(dueDate) ?: return false
            val todayString = format.format(System.currentTimeMillis())
            val today = format.parse(todayString) ?: return false

            due.before(today)
        } catch (_: Exception) {
            false
        }
    }
}