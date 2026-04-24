package com.example.equipmentborrowingapp.ui.common

data class AdminDashboardCounts(
    val totalEquipmentCount: Int = 0,
    val availableItemsCount: Int = 0,
    val lowStockCount: Int = 0,
    val pendingRequestsCount: Int = 0,
    val approvedRequestsCount: Int = 0,
    val returnedItemsCount: Int = 0,
    val overdueItemsCount: Int = 0
)

data class StudentDashboardCounts(
    val myPendingRequestsCount: Int = 0,
    val myApprovedRequestsCount: Int = 0,
    val myReturnedRequestsCount: Int = 0
)