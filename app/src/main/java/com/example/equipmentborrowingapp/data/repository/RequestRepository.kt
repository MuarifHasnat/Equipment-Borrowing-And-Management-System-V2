package com.example.equipmentborrowingapp.data.repository

import com.example.equipmentborrowingapp.data.model.BorrowRequest
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale

class RequestRepository {

    private val firestore = FirebaseFirestore.getInstance()

    fun submitBorrowRequest(
        institutionId: String,
        roomId: String,
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
        if (institutionId.isBlank()) {
            onResult(false, "Institution not found")
            return
        }

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
                    .whereEqualTo("institutionId", institutionId)
                    .whereEqualTo("userId", userId)
                    .get()
                    .addOnSuccessListener { requestResult ->
                        val hasDuplicateRequest = requestResult.documents
                            .mapNotNull { it.toObject(BorrowRequest::class.java) }
                            .any { request ->
                                request.equipmentId == equipmentId &&
                                        isActiveRequestStatus(request.status)
                            }

                        if (hasDuplicateRequest) {
                            onResult(false, "You already have an active request for this equipment")
                            return@addOnSuccessListener
                        }

                        val docRef = firestore.collection("borrow_requests").document()

                        val request = BorrowRequest(
                            requestId = docRef.id,
                            institutionId = institutionId,
                            roomId = roomId,
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
        institutionId: String,
        onResult: (List<BorrowRequest>) -> Unit
    ) {
        if (institutionId.isBlank()) {
            onResult(emptyList())
            return
        }

        firestore.collection("borrow_requests")
            .whereEqualTo("institutionId", institutionId)
            .get()
            .addOnSuccessListener { result ->
                val allList = result.documents.mapNotNull {
                    it.toObject(BorrowRequest::class.java)
                }

                syncOverdueStatuses(allList)

                val pendingList = allList
                    .map { normalizeRequestStatus(it) }
                    .filter {
                        it.status.equals("Pending", ignoreCase = true)
                    }

                onResult(pendingList.sortedByDescending { it.requestTimestamp })
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

    fun getApprovedRequests(
        institutionId: String,
        onResult: (List<BorrowRequest>) -> Unit
    ) {
        if (institutionId.isBlank()) {
            onResult(emptyList())
            return
        }

        firestore.collection("borrow_requests")
            .whereEqualTo("institutionId", institutionId)
            .get()
            .addOnSuccessListener { result ->
                val allList = result.documents.mapNotNull {
                    it.toObject(BorrowRequest::class.java)
                }

                syncOverdueStatuses(allList)

                val activeBorrowedList = allList
                    .map { normalizeRequestStatus(it) }
                    .filter {
                        it.status.equals("Approved", ignoreCase = true) ||
                                it.status.equals("Issued", ignoreCase = true) ||
                                it.status.equals("Overdue", ignoreCase = true)
                    }

                onResult(
                    activeBorrowedList.sortedWith(
                        compareBy<BorrowRequest> { requestStatusOrder(it.status) }
                            .thenByDescending { it.requestTimestamp }
                    )
                )
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

    fun getAllRequests(
        institutionId: String,
        onResult: (List<BorrowRequest>) -> Unit
    ) {
        if (institutionId.isBlank()) {
            onResult(emptyList())
            return
        }

        firestore.collection("borrow_requests")
            .whereEqualTo("institutionId", institutionId)
            .get()
            .addOnSuccessListener { result ->
                val list = result.documents.mapNotNull {
                    it.toObject(BorrowRequest::class.java)
                }

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
        institutionId: String,
        userId: String,
        onResult: (List<BorrowRequest>) -> Unit
    ) {
        if (institutionId.isBlank()) {
            onResult(emptyList())
            return
        }

        firestore.collection("borrow_requests")
            .whereEqualTo("institutionId", institutionId)
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                val list = result.documents.mapNotNull {
                    it.toObject(BorrowRequest::class.java)
                }

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
        approvedBy: String = "",
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

            val availableQuantity =
                equipmentSnapshot.getLong("availableQuantity")?.toInt() ?: 0

            val requestedQuantity =
                requestSnapshot.getLong("quantity")?.toInt() ?: request.quantity

            if (requestedQuantity <= 0) {
                throw Exception("Invalid request quantity")
            }

            if (availableQuantity < requestedQuantity) {
                throw Exception("Not enough stock available")
            }

            val newAvailableQuantity = availableQuantity - requestedQuantity

            if (newAvailableQuantity < 0) {
                throw Exception("Available quantity cannot be negative")
            }

            transaction.update(
                equipmentRef,
                "availableQuantity",
                newAvailableQuantity
            )

            transaction.update(
                requestRef,
                mapOf(
                    "status" to "Approved",
                    "approvedBy" to approvedBy,
                    "approvedAt" to System.currentTimeMillis()
                )
            )
        }.addOnSuccessListener {
            onResult(true, "Request approved successfully")
        }.addOnFailureListener { e ->
            onResult(false, e.message ?: "Failed to approve request")
        }
    }

    fun rejectRequest(
        request: BorrowRequest,
        rejectedReason: String = "",
        adminNote: String = "",
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

            transaction.update(
                requestRef,
                mapOf(
                    "status" to "Rejected",
                    "rejectedReason" to rejectedReason,
                    "adminNote" to adminNote
                )
            )
        }.addOnSuccessListener {
            onResult(true, "Request rejected successfully")
        }.addOnFailureListener { e ->
            onResult(false, e.message ?: "Failed to reject request")
        }
    }

    fun markRequestIssued(
        request: BorrowRequest,
        issuedBy: String = "",
        adminNote: String = "",
        onResult: (Boolean, String) -> Unit
    ) {
        val requestRef = firestore.collection("borrow_requests").document(request.requestId)

        firestore.runTransaction { transaction ->
            val requestSnapshot = transaction.get(requestRef)

            if (!requestSnapshot.exists()) {
                throw Exception("Request not found")
            }

            val currentStatus = requestSnapshot.getString("status") ?: "Pending"

            if (!currentStatus.equals("Approved", ignoreCase = true)) {
                throw Exception("Only approved requests can be issued")
            }

            transaction.update(
                requestRef,
                mapOf(
                    "status" to "Issued",
                    "issuedBy" to issuedBy,
                    "issuedAt" to System.currentTimeMillis(),
                    "adminNote" to adminNote
                )
            )
        }.addOnSuccessListener {
            onResult(true, "Request marked as issued")
        }.addOnFailureListener { e ->
            onResult(false, e.message ?: "Failed to mark as issued")
        }
    }

    fun markRequestReturned(
        request: BorrowRequest,
        returnedBy: String = "",
        returnCondition: String = "",
        adminNote: String = "",
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
                !currentStatus.equals("Issued", ignoreCase = true) &&
                !currentStatus.equals("Overdue", ignoreCase = true)
            ) {
                throw Exception("Only approved, issued or overdue requests can be returned")
            }

            val currentAvailable =
                equipmentSnapshot.getLong("availableQuantity")?.toInt() ?: 0

            val totalQuantity =
                equipmentSnapshot.getLong("totalQuantity")?.toInt() ?: 0

            val requestQuantity =
                requestSnapshot.getLong("quantity")?.toInt() ?: request.quantity

            if (requestQuantity <= 0) {
                throw Exception("Invalid request quantity")
            }

            val newAvailable = currentAvailable + requestQuantity

            if (newAvailable < 0) {
                throw Exception("Available quantity cannot be negative")
            }

            if (newAvailable > totalQuantity) {
                throw Exception("Available quantity cannot exceed total quantity")
            }

            val today = todayString()

            transaction.update(
                equipmentRef,
                "availableQuantity",
                newAvailable
            )

            transaction.update(
                requestRef,
                mapOf(
                    "status" to "Returned",
                    "returnedDate" to today,
                    "returnedBy" to returnedBy,
                    "returnedTo" to returnedBy,
                    "returnedAt" to System.currentTimeMillis(),
                    "returnCondition" to returnCondition,
                    "adminNote" to adminNote
                )
            )
        }.addOnSuccessListener {
            onResult(true, "Equipment returned successfully")
        }.addOnFailureListener { e ->
            onResult(false, e.message ?: "Failed to mark returned")
        }
    }

    fun markRequestLost(
        request: BorrowRequest,
        adminNote: String = "",
        onResult: (Boolean, String) -> Unit
    ) {
        updateFinalNoQuantityReturnStatus(
            request = request,
            newStatus = "Lost",
            returnCondition = "Lost",
            adminNote = adminNote,
            successMessage = "Request marked as lost",
            onResult = onResult
        )
    }

    fun markRequestDamaged(
        request: BorrowRequest,
        adminNote: String = "",
        onResult: (Boolean, String) -> Unit
    ) {
        updateFinalNoQuantityReturnStatus(
            request = request,
            newStatus = "Damaged",
            returnCondition = "Damaged",
            adminNote = adminNote,
            successMessage = "Request marked as damaged",
            onResult = onResult
        )
    }

    fun cancelRequest(
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
                throw Exception("Only pending requests can be cancelled")
            }

            transaction.update(requestRef, "status", "Cancelled")
        }.addOnSuccessListener {
            onResult(true, "Request cancelled successfully")
        }.addOnFailureListener { e ->
            onResult(false, e.message ?: "Failed to cancel request")
        }
    }

    fun updateRequestStatus(
        requestId: String,
        newStatus: String,
        onResult: (Boolean, String) -> Unit
    ) {
        val validStatuses = listOf(
            "Pending",
            "Approved",
            "Issued",
            "Returned",
            "Rejected",
            "Cancelled",
            "Overdue",
            "Lost",
            "Damaged"
        )

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

                val request = snapshot.toObject(BorrowRequest::class.java)?.copy(
                    requestId = snapshot.id
                )

                if (request == null) {
                    onResult(false, "Failed to read request")
                    return@addOnSuccessListener
                }

                val currentStatus = snapshot.getString("status") ?: "Pending"

                if (currentStatus.equals(newStatus, ignoreCase = true)) {
                    onResult(false, "Request is already $newStatus")
                    return@addOnSuccessListener
                }

                when (newStatus) {
                    "Approved" -> approveRequest(request, onResult = onResult)
                    "Issued" -> markRequestIssued(request, onResult = onResult)
                    "Returned" -> markRequestReturned(request, onResult = onResult)
                    "Rejected" -> rejectRequest(request, onResult = onResult)
                    "Cancelled" -> cancelRequest(request, onResult = onResult)
                    "Lost" -> markRequestLost(request, onResult = onResult)
                    "Damaged" -> markRequestDamaged(request, onResult = onResult)
                    "Overdue" -> forceOverdue(request, onResult)
                    else -> onResult(false, "Invalid status change")
                }
            }
            .addOnFailureListener { e ->
                onResult(false, e.message ?: "Failed to load request")
            }
    }

    private fun updateFinalNoQuantityReturnStatus(
        request: BorrowRequest,
        newStatus: String,
        returnCondition: String,
        adminNote: String,
        successMessage: String,
        onResult: (Boolean, String) -> Unit
    ) {
        val requestRef = firestore.collection("borrow_requests").document(request.requestId)

        firestore.runTransaction { transaction ->
            val requestSnapshot = transaction.get(requestRef)

            if (!requestSnapshot.exists()) {
                throw Exception("Request not found")
            }

            val currentStatus = requestSnapshot.getString("status") ?: "Pending"

            if (
                !currentStatus.equals("Approved", ignoreCase = true) &&
                !currentStatus.equals("Issued", ignoreCase = true) &&
                !currentStatus.equals("Overdue", ignoreCase = true)
            ) {
                throw Exception("Only approved, issued or overdue requests can be marked as $newStatus")
            }

            transaction.update(
                requestRef,
                mapOf(
                    "status" to newStatus,
                    "returnCondition" to returnCondition,
                    "adminNote" to adminNote
                )
            )
        }.addOnSuccessListener {
            onResult(true, successMessage)
        }.addOnFailureListener { e ->
            onResult(false, e.message ?: "Failed to update request")
        }
    }

    private fun forceOverdue(
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

            if (!currentStatus.equals("Issued", ignoreCase = true)) {
                throw Exception("Only issued requests can be marked as overdue")
            }

            transaction.update(requestRef, "status", "Overdue")
        }.addOnSuccessListener {
            onResult(true, "Request marked as overdue")
        }.addOnFailureListener { e ->
            onResult(false, e.message ?: "Failed to mark overdue")
        }
    }

    private fun normalizeRequestStatus(request: BorrowRequest): BorrowRequest {
        return if (
            request.status.equals("Issued", ignoreCase = true) &&
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
                request.status.equals("Issued", ignoreCase = true) &&
                isDueDatePast(request.dueDate) &&
                request.requestId.isNotBlank()
            ) {
                firestore.collection("borrow_requests")
                    .document(request.requestId)
                    .update("status", "Overdue")
            }
        }
    }

    private fun isActiveRequestStatus(status: String): Boolean {
        return status.equals("Pending", ignoreCase = true) ||
                status.equals("Approved", ignoreCase = true) ||
                status.equals("Issued", ignoreCase = true) ||
                status.equals("Overdue", ignoreCase = true)
    }

    private fun requestStatusOrder(status: String): Int {
        return when (status.trim().lowercase()) {
            "approved" -> 0
            "issued" -> 1
            "overdue" -> 2
            else -> 3
        }
    }

    private fun todayString(): String {
        return SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.getDefault()
        ).format(System.currentTimeMillis())
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