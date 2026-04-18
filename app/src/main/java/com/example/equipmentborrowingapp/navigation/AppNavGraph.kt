package com.example.equipmentborrowingapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.equipmentborrowingapp.ui.admin.AdminDashboardScreen
import com.example.equipmentborrowingapp.ui.auth.LoginScreen
import com.example.equipmentborrowingapp.ui.auth.RegisterScreen
import com.example.equipmentborrowingapp.ui.student.StudentDashboardScreen

@Composable
fun AppNavGraph(startDestination: String = AppRoute.Login.route) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(AppRoute.Login.route) {
            LoginScreen(
                onLoginClick = { _, _ ->
                    // temporary only
                },
                onGoToRegister = {
                    navController.navigate(AppRoute.Register.route)
                }
            )
        }

        composable(AppRoute.Register.route) {
            RegisterScreen(
                onRegisterClick = { _, _, _, _ ->
                    // temporary only
                    navController.popBackStack()
                },
                onGoToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(AppRoute.StudentDashboard.route) {
            StudentDashboardScreen(
                onViewEquipmentClick = {},
                onMyRequestsClick = {},
                onLabComputersClick = {},
                onLogout = {
                    navController.navigate(AppRoute.Login.route) {
                        popUpTo(0)
                    }
                }
            )
        }

        composable(AppRoute.AdminDashboard.route) {
            AdminDashboardScreen(
                totalEquipmentCount = 0,
                availableItemsCount = 0,
                lowStockCount = 0,
                pendingRequestsCount = 0,
                approvedRequestsCount = 0,
                returnedItemsCount = 0,
                overdueItemsCount = 0,
                onAddEquipmentClick = {},
                onViewPendingRequestsClick = {},
                onViewApprovedRequestsClick = {},
                onManageEquipmentClick = {},
                onManageLabComputersClick = {},
                onViewSoftwareReportsClick = {},
                onLogout = {
                    navController.navigate(AppRoute.Login.route) {
                        popUpTo(0)
                    }
                }
            )
        }
    }
}