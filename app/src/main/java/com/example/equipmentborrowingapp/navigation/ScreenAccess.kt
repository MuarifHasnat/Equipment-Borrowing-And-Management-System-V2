package com.example.equipmentborrowingapp.navigation

fun isSuperAdminScreen(screen: AppScreen): Boolean {
    return screen in listOf(
        AppScreen.SuperAdminDashboard,
        AppScreen.ManageInstitutions,
        AppScreen.CreateInstitution,
        AppScreen.CreateInstitutionAdmin,
        AppScreen.ManageInstitutionAdminRequests
    )
}

fun isAdminScreen(screen: AppScreen): Boolean {
    return screen in listOf(
        AppScreen.AdminDashboard,
        AppScreen.AdminProfile,
        AppScreen.PendingStudents,
        AppScreen.ManageStudents,
        AppScreen.AddEquipment,
        AppScreen.ManageEquipment,
        AppScreen.EditEquipment,
        AppScreen.PendingRequests,
        AppScreen.ApprovedRequests,
        AppScreen.ManageRooms,
        AppScreen.AddRoom,
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
        AppScreen.StudentProfile,
        AppScreen.RoomSelection,
        AppScreen.EquipmentList,
        AppScreen.EquipmentDetails,
        AppScreen.BorrowRequest,
        AppScreen.RequestSubmitted,
        AppScreen.MyRequests,
        AppScreen.LabComputerList,
        AppScreen.ReportSoftwareIssue
    )
}