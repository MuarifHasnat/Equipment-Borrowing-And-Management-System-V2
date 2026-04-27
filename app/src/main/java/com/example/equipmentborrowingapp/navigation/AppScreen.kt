package com.example.equipmentborrowingapp.navigation

sealed class AppScreen(val route: String) {
    object StudentProfile : AppScreen("student_profile")
    object AdminProfile : AppScreen("admin_profile")
    object Login : AppScreen("login")
    object Register : AppScreen("register")
    object Notifications : AppScreen("notifications")
    object StudentDashboard : AppScreen("student_dashboard")
    object EquipmentList : AppScreen("equipment_list")
    object EquipmentDetails : AppScreen("equipment_details")
    
    object BorrowRequest : AppScreen("borrow_request")

    object MyRequests : AppScreen("my_requests")
    object LabComputerList : AppScreen("lab_computer_list")
    object ReportSoftwareIssue : AppScreen("report_software_issue")

    object AdminDashboard : AppScreen("admin_dashboard")
    object AddEquipment : AppScreen("add_equipment")
    object EditEquipment : AppScreen("edit_equipment")
    object ManageEquipment : AppScreen("manage_equipment")
    object PendingRequests : AppScreen("pending_requests")
    object ApprovedRequests : AppScreen("approved_requests")
    object ManageLabComputers : AppScreen("manage_lab_computers")
    object AddLabComputer : AppScreen("add_lab_computer")
    object EditLabComputer : AppScreen("edit_lab_computer")
    object ManageSoftwareStatus : AppScreen("manage_software_status")
    object SoftwareIssueReports : AppScreen("software_issue_reports")
}