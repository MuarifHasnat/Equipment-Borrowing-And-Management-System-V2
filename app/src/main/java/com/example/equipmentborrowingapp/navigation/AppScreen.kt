package com.example.equipmentborrowingapp.navigation

sealed class AppScreen(val route: String) {

    object StudentProfile : AppScreen("student_profile")
    object AdminProfile : AppScreen("admin_profile")
    object Login : AppScreen("login")
    object Register : AppScreen("register")
    object Notifications : AppScreen("notifications")

    object SuperAdminDashboard : AppScreen("super_admin_dashboard")
    object ManageInstitutions : AppScreen("manage_institutions")
    object CreateInstitution : AppScreen("create_institution")
    object CreateInstitutionAdmin : AppScreen("create_institution_admin")
    object ManageInstitutionAdminRequests : AppScreen("manage_institution_admin_requests")

    object StudentDashboard : AppScreen("student_dashboard")
    object RoomSelection : AppScreen("room_selection")
    object EquipmentList : AppScreen("equipment_list")
    object EquipmentDetails : AppScreen("equipment_details")
    object BorrowRequest : AppScreen("borrow_request")
    object RequestSubmitted : AppScreen("request_submitted")
    object MyRequests : AppScreen("my_requests")
    object LabComputerList : AppScreen("lab_computer_list")
    object ReportSoftwareIssue : AppScreen("report_software_issue")

    object AdminDashboard : AppScreen("admin_dashboard")
    object PendingStudents : AppScreen("pending_students")
    object ManageStudents : AppScreen("manage_students")

    object AddEquipment : AppScreen("add_equipment")
    object EditEquipment : AppScreen("edit_equipment")
    object ManageEquipment : AppScreen("manage_equipment")
    object PendingRequests : AppScreen("pending_requests")
    object ApprovedRequests : AppScreen("approved_requests")

    object ManageRooms : AppScreen("manage_rooms")
    object AddRoom : AppScreen("add_room")

    object ManageLabComputers : AppScreen("manage_lab_computers")
    object AddLabComputer : AppScreen("add_lab_computer")
    object EditLabComputer : AppScreen("edit_lab_computer")
    object ManageSoftwareStatus : AppScreen("manage_software_status")
    object SoftwareIssueReports : AppScreen("software_issue_reports")


    object ReportsDashboard : AppScreen("reports_dashboard")
    object RoomWiseEquipmentReport : AppScreen("room_wise_equipment_report")
    object StudentBorrowHistoryReport : AppScreen("student_borrow_history_report")
    object PendingRequestReport : AppScreen("pending_request_report")
    object ApprovedRequestReport : AppScreen("approved_request_report")
    object IssuedItemReport : AppScreen("issued_item_report")
    object ReturnedItemReport : AppScreen("returned_item_report")
    object OverdueItemReport : AppScreen("overdue_item_report")
    object LostDamagedReport : AppScreen("lost_damaged_report")
    object LowStockReport : AppScreen("low_stock_report")
    object SoftwareIssueReportAdmin : AppScreen("software_issue_report_admin")
}