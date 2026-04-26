package com.example.equipmentborrowingapp.navigation

sealed class AppRoute(val route: String) {

    // Auth
    object Login : AppRoute("login")
    object Register : AppRoute("register")

    // Student
    object StudentDashboard : AppRoute("student_dashboard")
    object EquipmentList : AppRoute("equipment_list")
    object EquipmentDetails : AppRoute("equipment_details")
    object BorrowRequest : AppRoute("borrow_request")
    object MyRequests : AppRoute("my_requests")
    object LabComputers : AppRoute("lab_computers")
    object StudentProfile : AppRoute("student_profile")

    // Admin
    object AdminDashboard : AppRoute("admin_dashboard")
    object AdminProfile : AppRoute("admin_profile")
    object AddEquipment : AppRoute("add_equipment")
    object ManageEquipment : AppRoute("manage_equipment")
    object PendingRequests : AppRoute("pending_requests")
    object ApprovedRequests : AppRoute("approved_requests")
    object ManageLabComputers : AppRoute("manage_lab_computers")
    object Notifications : AppRoute("notifications")

}