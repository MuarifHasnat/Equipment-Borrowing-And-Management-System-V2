package com.example.equipmentborrowingapp.ui.superadmin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SuperAdminDashboardScreen(
    onManageInstitutionsClick: () -> Unit,
    onCreateInstitutionClick: () -> Unit,
    onCreateInstitutionAdminClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Super Admin Dashboard",
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = "Manage institutions, admins, and platform access.",
            style = MaterialTheme.typography.bodyMedium
        )

        Button(
            onClick = onManageInstitutionsClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Manage Institutions")
        }

        Button(
            onClick = onCreateInstitutionClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Institution")
        }

        Button(
            onClick = onCreateInstitutionAdminClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Institution Admin")
        }

        OutlinedButton(
            onClick = onNotificationClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Notifications")
        }

        OutlinedButton(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logout")
        }
    }
}