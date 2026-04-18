package com.example.equipmentborrowingapp.navigation

fun isAdminScreen(screen: AppScreen): Boolean {
    return screen in listOf(
        AppScreen.AdminDashboard,
        AppScreen.AddEquipment,
        AppScreen.ManageEquipment,
        AppScreen.EditEquipment,
        AppScreen.PendingRequests,
        AppScreen.ApprovedRequests,
        AppScreen.ManageLabComputers,
        AppScreen.AddLabComputer,
        AppScreen.EditLabComputer,
        AppScreen.ManageSoftwareStatus,
        AppScreen.SoftwareIssueReports
    )
}

fun isStudentScreen(screen: AppScreen): Boolean {
    return screen in listOf(
        AppScreen.StudentDashboard,
        AppScreen.EquipmentList,
        AppScreen.BorrowRequest,
        AppScreen.MyRequests,
        AppScreen.LabComputerList,
        AppScreen.ReportSoftwareIssue
    )
}