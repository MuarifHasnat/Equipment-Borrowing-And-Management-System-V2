package com.example.equipmentborrowingapp.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.equipmentborrowingapp.data.model.ComputerSoftwareStatus
import com.example.equipmentborrowingapp.data.model.LabComputer
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private object SoftwareManageColors {
    val ModernBg = Color(0xFFF4F7FB)
    val CardWhite = Color(0xFFFFFFFF)
    val TextDark = Color(0xFF1E293B)
    val TextMuted = Color(0xFF64748B)
    val PrimaryIndigo = Color(0xFF4F46E5)
    val PurpleAccent = Color(0xFF7C3AED)

    val GreenLight = Color(0xFFF0FDF4)
    val GreenText = Color(0xFF16A34A)

    val RedLight = Color(0xFFFEF2F2)
    val RedText = Color(0xFFDC2626)

    val OrangeLight = Color(0xFFFFF7ED)
    val OrangeText = Color(0xFFEA580C)

    val BlueLight = Color(0xFFEFF6FF)
    val BlueText = Color(0xFF2563EB)

    val PurpleLight = Color(0xFFF5F3FF)
    val PurpleText = Color(0xFF7C3AED)
}

@Composable
fun ManageSoftwareStatusScreen(
    computer: LabComputer,
    softwareList: List<ComputerSoftwareStatus>,
    onAddSoftwareClick: (
        String, String, Boolean, Boolean, Boolean, Boolean, String
    ) -> Unit,
    onUpdateSoftwareClick: (ComputerSoftwareStatus) -> Unit = {},
    onDeleteSoftwareClick: (ComputerSoftwareStatus) -> Unit = {},
    onBackClick: () -> Unit
) {
    var softwareName by remember { mutableStateOf("") }
    var version by remember { mutableStateOf("") }
    var installed by remember { mutableStateOf(true) }
    var launchesProperly by remember { mutableStateOf(false) }
    var compileWorks by remember { mutableStateOf(false) }
    var runWorks by remember { mutableStateOf(false) }
    var remarks by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }
    var selectedSort by remember { mutableStateOf("Name A-Z") }

    var editItem by remember { mutableStateOf<ComputerSoftwareStatus?>(null) }
    var deleteItem by remember { mutableStateOf<ComputerSoftwareStatus?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val filteredSoftwareList = softwareList
        .filter { item ->
            val query = searchQuery.trim().lowercase()

            val matchesSearch =
                query.isBlank() ||
                        item.softwareName.lowercase().contains(query) ||
                        item.version.lowercase().contains(query) ||
                        item.remarks.lowercase().contains(query)

            val isWorking =
                item.installed &&
                        item.launchesProperly &&
                        item.compileWorks &&
                        item.runWorks

            val isProblematic =
                item.installed &&
                        (!item.launchesProperly || !item.compileWorks || !item.runWorks)

            val matchesFilter = when (selectedFilter) {
                "Working" -> isWorking
                "Problematic" -> isProblematic
                "Installed" -> item.installed
                "Not Installed" -> !item.installed
                "Launch Issue" -> item.installed && !item.launchesProperly
                "Compile Issue" -> item.installed && !item.compileWorks
                "Run Issue" -> item.installed && !item.runWorks
                else -> true
            }

            matchesSearch && matchesFilter
        }
        .let { list ->
            when (selectedSort) {
                "Name Z-A" -> list.sortedByDescending { it.softwareName.lowercase() }
                "Checked Newest" -> list.sortedByDescending { it.checkedAt }
                "Checked Oldest" -> list.sortedBy { it.checkedAt }
                "Problem First" -> list.sortedWith(
                    compareBy<ComputerSoftwareStatus> {
                        softwareHealthOrder(it)
                    }.thenBy { it.softwareName.lowercase() }
                )

                else -> list.sortedBy { it.softwareName.lowercase() }
            }
        }

    val totalSoftware = softwareList.size
    val installedCount = softwareList.count { it.installed }
    val notInstalledCount = softwareList.count { !it.installed }
    val problemCount = softwareList.count {
        it.installed &&
                (!it.launchesProperly || !it.compileWorks || !it.runWorks)
    }

    editItem?.let { item ->
        EditSoftwareStatusDialog(
            softwareStatus = item,
            onDismiss = {
                editItem = null
            },
            onSave = { updatedItem ->
                onUpdateSoftwareClick(updatedItem)
                editItem = null

                scope.launch {
                    snackbarHostState.showSnackbar("Software status update requested")
                }
            }
        )
    }

    deleteItem?.let { item ->
        AlertDialog(
            onDismissRequest = {
                deleteItem = null
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = SoftwareManageColors.CardWhite,
            title = {
                Text(
                    text = "Delete Software Status",
                    fontWeight = FontWeight.Bold,
                    color = SoftwareManageColors.TextDark
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to delete ${item.softwareName.ifBlank { "this software" }} status?",
                    color = SoftwareManageColors.TextMuted
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteSoftwareClick(item)
                        deleteItem = null

                        scope.launch {
                            snackbarHostState.showSnackbar("Software status delete requested")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SoftwareManageColors.RedText,
                        contentColor = Color.White
                    )
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        deleteItem = null
                    }
                ) {
                    Text(
                        text = "Cancel",
                        color = SoftwareManageColors.TextMuted
                    )
                }
            }
        )
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        containerColor = SoftwareManageColors.ModernBg
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            color = SoftwareManageColors.ModernBg
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp, top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .background(
                                SoftwareManageColors.CardWhite,
                                RoundedCornerShape(12.dp)
                            )
                            .shadow(
                                2.dp,
                                RoundedCornerShape(12.dp),
                                spotColor = Color.Black.copy(alpha = 0.05f)
                            )
                    ) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back",
                            tint = SoftwareManageColors.TextDark
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "Software Tracking",
                            style = MaterialTheme.typography.titleLarge,
                            color = SoftwareManageColors.TextDark,
                            fontWeight = FontWeight.ExtraBold
                        )

                        Text(
                            text = computer.pcName.ifBlank { "Lab PC" },
                            style = MaterialTheme.typography.bodyMedium,
                            color = SoftwareManageColors.TextMuted
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    item {
                        SoftwareHeroCard(
                            computerName = computer.pcName.ifBlank { "Lab PC" },
                            totalSoftware = totalSoftware,
                            problemCount = problemCount
                        )
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            SoftwareSummaryCard(
                                title = "Total",
                                value = totalSoftware.toString(),
                                bgColor = SoftwareManageColors.BlueLight,
                                textColor = SoftwareManageColors.BlueText,
                                modifier = Modifier.weight(1f)
                            )

                            SoftwareSummaryCard(
                                title = "Installed",
                                value = installedCount.toString(),
                                bgColor = SoftwareManageColors.GreenLight,
                                textColor = SoftwareManageColors.GreenText,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            SoftwareSummaryCard(
                                title = "Not Installed",
                                value = notInstalledCount.toString(),
                                bgColor = SoftwareManageColors.OrangeLight,
                                textColor = SoftwareManageColors.OrangeText,
                                modifier = Modifier.weight(1f)
                            )

                            SoftwareSummaryCard(
                                title = "Problems",
                                value = problemCount.toString(),
                                bgColor = SoftwareManageColors.RedLight,
                                textColor = SoftwareManageColors.RedText,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    item {
                        AddSoftwareStatusCard(
                            softwareName = softwareName,
                            version = version,
                            installed = installed,
                            launchesProperly = launchesProperly,
                            compileWorks = compileWorks,
                            runWorks = runWorks,
                            remarks = remarks,
                            errorMessage = errorMessage,
                            onSoftwareNameChange = {
                                softwareName = it
                                errorMessage = ""
                            },
                            onVersionChange = {
                                version = it
                                errorMessage = ""
                            },
                            onInstalledChange = {
                                installed = it
                                if (!it) {
                                    launchesProperly = false
                                    compileWorks = false
                                    runWorks = false
                                }
                            },
                            onLaunchesChange = {
                                launchesProperly = it
                            },
                            onCompileChange = {
                                compileWorks = it
                            },
                            onRunChange = {
                                runWorks = it
                            },
                            onRemarksChange = {
                                remarks = it
                                errorMessage = ""
                            },
                            onAddClick = {
                                errorMessage = when {
                                    computer.id.isBlank() -> "Invalid computer selected"
                                    softwareName.isBlank() -> "Software name is required"
                                    else -> ""
                                }

                                if (errorMessage.isBlank()) {
                                    onAddSoftwareClick(
                                        softwareName.trim(),
                                        version.trim(),
                                        installed,
                                        launchesProperly,
                                        compileWorks,
                                        runWorks,
                                        remarks.trim()
                                    )

                                    softwareName = ""
                                    version = ""
                                    installed = true
                                    launchesProperly = false
                                    compileWorks = false
                                    runWorks = false
                                    remarks = ""

                                    scope.launch {
                                        snackbarHostState.showSnackbar("Software status add requested")
                                    }
                                }
                            }
                        )
                    }

                    item {
                        Text(
                            text = "Tracked Software",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = SoftwareManageColors.TextDark,
                            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = {
                                searchQuery = it
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(
                                    4.dp,
                                    RoundedCornerShape(14.dp),
                                    spotColor = Color.Black.copy(alpha = 0.05f)
                                ),
                            shape = RoundedCornerShape(14.dp),
                            placeholder = {
                                Text(
                                    text = "Search software, version or remarks",
                                    color = SoftwareManageColors.TextMuted
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Rounded.Search,
                                    contentDescription = "Search",
                                    tint = SoftwareManageColors.TextMuted
                                )
                            },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = SoftwareManageColors.CardWhite,
                                unfocusedContainerColor = SoftwareManageColors.CardWhite,
                                focusedBorderColor = SoftwareManageColors.PrimaryIndigo,
                                unfocusedBorderColor = Color.Transparent,
                                focusedTextColor = SoftwareManageColors.TextDark,
                                unfocusedTextColor = SoftwareManageColors.TextDark
                            )
                        )
                    }

                    item {
                        SoftwareFilterTitle("Filter")
                        SoftwareHorizontalFilterRow {
                            listOf(
                                "All",
                                "Working",
                                "Problematic",
                                "Installed",
                                "Not Installed",
                                "Launch Issue",
                                "Compile Issue",
                                "Run Issue"
                            ).forEach { filter ->
                                SoftwareFilterChip(
                                    text = filter,
                                    selected = selectedFilter == filter,
                                    onClick = {
                                        selectedFilter = filter
                                    }
                                )
                            }
                        }
                    }

                    item {
                        SoftwareFilterTitle("Sort")
                        SoftwareHorizontalFilterRow {
                            listOf(
                                "Name A-Z",
                                "Name Z-A",
                                "Problem First",
                                "Checked Newest",
                                "Checked Oldest"
                            ).forEach { sort ->
                                SoftwareFilterChip(
                                    text = sort,
                                    selected = selectedSort == sort,
                                    onClick = {
                                        selectedSort = sort
                                    }
                                )
                            }
                        }
                    }

                    if (
                        searchQuery.isNotBlank() ||
                        selectedFilter != "All" ||
                        selectedSort != "Name A-Z"
                    ) {
                        item {
                            OutlinedButton(
                                onClick = {
                                    searchQuery = ""
                                    selectedFilter = "All"
                                    selectedSort = "Name A-Z"
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Text("Clear Search, Filter and Sort")
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Showing ${filteredSoftwareList.size} of ${softwareList.size} software record(s)",
                            style = MaterialTheme.typography.titleMedium,
                            color = SoftwareManageColors.TextDark,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (filteredSoftwareList.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 20.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (softwareList.isEmpty()) {
                                        "No software status found"
                                    } else {
                                        "No software matches your search/filter"
                                    },
                                    color = SoftwareManageColors.TextMuted,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    } else {
                        items(filteredSoftwareList, key = { it.id }) { item ->
                            ModernSoftwareCard(
                                item = item,
                                onEditClick = {
                                    editItem = item
                                },
                                onDeleteClick = {
                                    deleteItem = item
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SoftwareHeroCard(
    computerName: String,
    totalSoftware: Int,
    problemCount: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        SoftwareManageColors.PrimaryIndigo,
                        SoftwareManageColors.PurpleAccent
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(20.dp)
    ) {
        Column {
            Text(
                text = "Software Status",
                color = Color.White,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .background(
                        color = Color.White.copy(alpha = 0.18f),
                        shape = RoundedCornerShape(50.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = computerName,
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "$totalSoftware software record(s), $problemCount problem record(s)",
                color = Color.White.copy(alpha = 0.85f),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun SoftwareSummaryCard(
    title: String,
    value: String,
    bgColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(76.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SoftwareManageColors.CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(bgColor)
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontSize = 22.sp,
                color = textColor,
                fontWeight = FontWeight.Black
            )

            Text(
                text = title,
                color = textColor.copy(alpha = 0.8f),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
private fun AddSoftwareStatusCard(
    softwareName: String,
    version: String,
    installed: Boolean,
    launchesProperly: Boolean,
    compileWorks: Boolean,
    runWorks: Boolean,
    remarks: String,
    errorMessage: String,
    onSoftwareNameChange: (String) -> Unit,
    onVersionChange: (String) -> Unit,
    onInstalledChange: (Boolean) -> Unit,
    onLaunchesChange: (Boolean) -> Unit,
    onCompileChange: (Boolean) -> Unit,
    onRunChange: (Boolean) -> Unit,
    onRemarksChange: (String) -> Unit,
    onAddClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                6.dp,
                RoundedCornerShape(20.dp),
                spotColor = Color.Black.copy(alpha = 0.05f)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SoftwareManageColors.CardWhite)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Add Software Status",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = SoftwareManageColors.TextDark
            )

            Spacer(modifier = Modifier.height(16.dp))

            ModernTextField(
                value = softwareName,
                onValueChange = onSoftwareNameChange,
                label = "Software Name"
            )

            ModernTextField(
                value = version,
                onValueChange = onVersionChange,
                label = "Version (e.g., v1.4)"
            )

            Spacer(modifier = Modifier.height(8.dp))

            ModernSwitchRow(
                title = "Is Installed?",
                checked = installed,
                onCheckedChange = onInstalledChange
            )

            ModernSwitchRow(
                title = "Launches Properly",
                checked = launchesProperly,
                enabled = installed,
                onCheckedChange = onLaunchesChange
            )

            ModernSwitchRow(
                title = "Compile Works",
                checked = compileWorks,
                enabled = installed,
                onCheckedChange = onCompileChange
            )

            ModernSwitchRow(
                title = "Run Works",
                checked = runWorks,
                enabled = installed,
                onCheckedChange = onRunChange
            )

            Spacer(modifier = Modifier.height(12.dp))

            ModernTextField(
                value = remarks,
                onValueChange = onRemarksChange,
                label = "Remarks / Notes",
                singleLine = false,
                modifier = Modifier.height(80.dp)
            )

            if (errorMessage.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))

                ErrorMessageBox(errorMessage)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onAddClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SoftwareManageColors.PrimaryIndigo
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    Icons.Rounded.AddCircle,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Add Status",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun EditSoftwareStatusDialog(
    softwareStatus: ComputerSoftwareStatus,
    onDismiss: () -> Unit,
    onSave: (ComputerSoftwareStatus) -> Unit
) {
    var softwareName by remember(softwareStatus) {
        mutableStateOf(softwareStatus.softwareName)
    }
    var version by remember(softwareStatus) {
        mutableStateOf(softwareStatus.version)
    }
    var installed by remember(softwareStatus) {
        mutableStateOf(softwareStatus.installed)
    }
    var launchesProperly by remember(softwareStatus) {
        mutableStateOf(softwareStatus.launchesProperly)
    }
    var compileWorks by remember(softwareStatus) {
        mutableStateOf(softwareStatus.compileWorks)
    }
    var runWorks by remember(softwareStatus) {
        mutableStateOf(softwareStatus.runWorks)
    }
    var remarks by remember(softwareStatus) {
        mutableStateOf(softwareStatus.remarks)
    }
    var errorMessage by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(20.dp),
        containerColor = SoftwareManageColors.CardWhite,
        title = {
            Text(
                text = "Edit Software Status",
                fontWeight = FontWeight.Bold,
                color = SoftwareManageColors.TextDark
            )
        },
        text = {
            Column {
                ModernTextField(
                    value = softwareName,
                    onValueChange = {
                        softwareName = it
                        errorMessage = ""
                    },
                    label = "Software Name"
                )

                ModernTextField(
                    value = version,
                    onValueChange = {
                        version = it
                        errorMessage = ""
                    },
                    label = "Version"
                )

                ModernSwitchRow(
                    title = "Is Installed?",
                    checked = installed,
                    onCheckedChange = {
                        installed = it
                        if (!it) {
                            launchesProperly = false
                            compileWorks = false
                            runWorks = false
                        }
                    }
                )

                ModernSwitchRow(
                    title = "Launches Properly",
                    checked = launchesProperly,
                    enabled = installed,
                    onCheckedChange = {
                        launchesProperly = it
                    }
                )

                ModernSwitchRow(
                    title = "Compile Works",
                    checked = compileWorks,
                    enabled = installed,
                    onCheckedChange = {
                        compileWorks = it
                    }
                )

                ModernSwitchRow(
                    title = "Run Works",
                    checked = runWorks,
                    enabled = installed,
                    onCheckedChange = {
                        runWorks = it
                    }
                )

                ModernTextField(
                    value = remarks,
                    onValueChange = {
                        remarks = it
                    },
                    label = "Remarks",
                    singleLine = false,
                    modifier = Modifier.height(80.dp)
                )

                if (errorMessage.isNotBlank()) {
                    ErrorMessageBox(errorMessage)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    errorMessage = when {
                        softwareName.isBlank() -> "Software name is required"
                        else -> ""
                    }

                    if (errorMessage.isBlank()) {
                        onSave(
                            softwareStatus.copy(
                                softwareName = softwareName.trim(),
                                version = version.trim(),
                                installed = installed,
                                launchesProperly = launchesProperly,
                                compileWorks = compileWorks,
                                runWorks = runWorks,
                                remarks = remarks.trim(),
                                checkedAt = System.currentTimeMillis()
                            )
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = SoftwareManageColors.PrimaryIndigo,
                    contentColor = Color.White
                )
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cancel",
                    color = SoftwareManageColors.TextMuted
                )
            }
        }
    )
}

@Composable
private fun ModernTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(label, color = SoftwareManageColors.TextMuted)
        },
        singleLine = singleLine,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .shadow(
                2.dp,
                RoundedCornerShape(12.dp),
                spotColor = Color.Black.copy(alpha = 0.05f)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = SoftwareManageColors.ModernBg,
            unfocusedContainerColor = SoftwareManageColors.ModernBg,
            focusedBorderColor = SoftwareManageColors.PrimaryIndigo,
            unfocusedBorderColor = Color.Transparent,
            focusedTextColor = SoftwareManageColors.TextDark,
            unfocusedTextColor = SoftwareManageColors.TextDark
        )
    )
}

@Composable
private fun ModernSwitchRow(
    title: String,
    checked: Boolean,
    enabled: Boolean = true,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = if (enabled) {
                SoftwareManageColors.TextDark
            } else {
                SoftwareManageColors.TextMuted
            }
        )

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            colors = SwitchDefaults.colors(
                checkedTrackColor = SoftwareManageColors.PrimaryIndigo
            )
        )
    }
}

@Composable
private fun ErrorMessageBox(errorMessage: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                SoftwareManageColors.RedLight,
                RoundedCornerShape(12.dp)
            )
            .border(
                1.dp,
                SoftwareManageColors.RedText.copy(alpha = 0.5f),
                RoundedCornerShape(12.dp)
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Rounded.Warning,
            contentDescription = "Error",
            tint = SoftwareManageColors.RedText
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = errorMessage,
            color = SoftwareManageColors.RedText,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun SoftwareFilterTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = SoftwareManageColors.TextMuted,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun SoftwareHorizontalFilterRow(
    content: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        content()
    }
}

@Composable
private fun SoftwareFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    if (selected) {
        Button(
            onClick = onClick,
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SoftwareManageColors.PrimaryIndigo,
                contentColor = Color.White
            )
        ) {
            Text(text)
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            shape = RoundedCornerShape(50.dp)
        ) {
            Text(text)
        }
    }
}

@Composable
private fun ModernSoftwareCard(
    item: ComputerSoftwareStatus,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val isWorking =
        item.installed &&
                item.launchesProperly &&
                item.compileWorks &&
                item.runWorks

    val isProblematic =
        item.installed &&
                (!item.launchesProperly || !item.compileWorks || !item.runWorks)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                4.dp,
                RoundedCornerShape(16.dp),
                spotColor = Color.Black.copy(alpha = 0.05f)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SoftwareManageColors.CardWhite)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.softwareName.ifBlank { "Unknown Software" },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SoftwareManageColors.TextDark,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (item.version.isNotBlank()) {
                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = "Version: ${item.version}",
                            style = MaterialTheme.typography.bodySmall,
                            color = SoftwareManageColors.TextMuted,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                SoftwareHealthBadge(
                    text = when {
                        !item.installed -> "Not Installed"
                        isWorking -> "Working"
                        isProblematic -> "Problematic"
                        else -> "Unknown"
                    },
                    isSuccess = isWorking,
                    isWarning = isProblematic || !item.installed
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ModernStatusBadge(
                        text = if (item.installed) "Installed" else "Not Installed",
                        isSuccess = item.installed
                    )

                    ModernStatusBadge(
                        text = if (item.launchesProperly) "Launch OK" else "Launch Issue",
                        isSuccess = item.launchesProperly,
                        isWarning = !item.launchesProperly && item.installed
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ModernStatusBadge(
                        text = if (item.compileWorks) "Compile OK" else "Compile Issue",
                        isSuccess = item.compileWorks,
                        isWarning = !item.compileWorks && item.installed
                    )

                    ModernStatusBadge(
                        text = if (item.runWorks) "Run OK" else "Run Issue",
                        isSuccess = item.runWorks,
                        isWarning = !item.runWorks && item.installed
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Checked: ${formatCheckedDate(item.checkedAt)}",
                style = MaterialTheme.typography.bodySmall,
                color = SoftwareManageColors.TextMuted
            )

            if (item.remarks.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))

                Surface(
                    color = SoftwareManageColors.ModernBg,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Remarks: ${item.remarks}",
                        style = MaterialTheme.typography.bodySmall,
                        color = SoftwareManageColors.TextDark,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            HorizontalDivider(color = SoftwareManageColors.ModernBg)

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onEditClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SoftwareManageColors.BlueLight,
                        contentColor = SoftwareManageColors.BlueText
                    ),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Icon(
                        Icons.Rounded.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(17.dp)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = "Edit",
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = onDeleteClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = SoftwareManageColors.RedText
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = SoftwareManageColors.RedText.copy(alpha = 0.3f)
                    ),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Icon(
                        Icons.Rounded.Delete,
                        contentDescription = null,
                        modifier = Modifier.size(17.dp)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = "Delete",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun SoftwareHealthBadge(
    text: String,
    isSuccess: Boolean,
    isWarning: Boolean = false
) {
    val bgColor = when {
        isSuccess -> SoftwareManageColors.GreenLight
        isWarning -> SoftwareManageColors.OrangeLight
        else -> SoftwareManageColors.RedLight
    }

    val textColor = when {
        isSuccess -> SoftwareManageColors.GreenText
        isWarning -> SoftwareManageColors.OrangeText
        else -> SoftwareManageColors.RedText
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun ModernStatusBadge(
    text: String,
    isSuccess: Boolean,
    isWarning: Boolean = false
) {
    val bgColor = when {
        isSuccess -> SoftwareManageColors.GreenLight
        isWarning -> SoftwareManageColors.OrangeLight
        else -> SoftwareManageColors.RedLight
    }

    val textColor = when {
        isSuccess -> SoftwareManageColors.GreenText
        isWarning -> SoftwareManageColors.OrangeText
        else -> SoftwareManageColors.RedText
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}

private fun softwareHealthOrder(item: ComputerSoftwareStatus): Int {
    val isWorking =
        item.installed &&
                item.launchesProperly &&
                item.compileWorks &&
                item.runWorks

    val isProblematic =
        item.installed &&
                (!item.launchesProperly || !item.compileWorks || !item.runWorks)

    return when {
        isProblematic -> 0
        !item.installed -> 1
        isWorking -> 2
        else -> 3
    }
}

private fun formatCheckedDate(timestamp: Long): String {
    return try {
        if (timestamp <= 0L) {
            "N/A"
        } else {
            SimpleDateFormat(
                "dd MMM yyyy, hh:mm a",
                Locale.getDefault()
            ).format(Date(timestamp))
        }
    } catch (_: Exception) {
        "N/A"
    }
}