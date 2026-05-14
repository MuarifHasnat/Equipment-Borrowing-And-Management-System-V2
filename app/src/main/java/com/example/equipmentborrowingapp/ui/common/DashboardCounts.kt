package com.example.equipmentborrowingapp.ui.common

data class AdminDashboardCounts(
    // Room / equipment
    val totalRoomsCount: Int = 0,
    val totalEquipmentCount: Int = 0,
    val availableItemsCount: Int = 0,
    val lowStockCount: Int = 0,

    // Borrow request lifecycle
    val pendingRequestsCount: Int = 0,
    val approvedRequestsCount: Int = 0,
    val issuedItemsCount: Int = 0,
    val returnedItemsCount: Int = 0,
    val overdueItemsCount: Int = 0,

    // Student management
    val pendingStudentsCount: Int = 0,
    val verifiedStudentsCount: Int = 0,

    // Lab computer / software issue
    val totalLabComputersCount: Int = 0,
    val openSoftwareIssuesCount: Int = 0
)