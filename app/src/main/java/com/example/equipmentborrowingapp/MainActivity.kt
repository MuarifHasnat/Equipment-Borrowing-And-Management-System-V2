package com.example.equipmentborrowingapp
import com.example.equipmentborrowingapp.ui.common.UiMessages
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import com.example.equipmentborrowingapp.viewmodel.EquipmentViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.equipmentborrowingapp.data.model.BorrowRequest
import com.example.equipmentborrowingapp.data.model.ComputerSoftwareStatus
import com.example.equipmentborrowingapp.data.model.Equipment
import com.example.equipmentborrowingapp.data.model.LabComputer
import com.example.equipmentborrowingapp.data.model.SoftwareIssueReport
import com.example.equipmentborrowingapp.data.repository.AuthRepository
import com.example.equipmentborrowingapp.data.repository.EquipmentRepository
import com.example.equipmentborrowingapp.data.repository.LabComputerRepository
import com.example.equipmentborrowingapp.data.repository.RequestRepository
import com.example.equipmentborrowingapp.navigation.AppScreen
import com.example.equipmentborrowingapp.ui.admin.AddEquipmentScreen
import com.example.equipmentborrowingapp.ui.admin.AddLabComputerScreen
import com.example.equipmentborrowingapp.ui.admin.AdminDashboardScreen
import com.example.equipmentborrowingapp.ui.admin.ApprovedRequestsScreen
import com.example.equipmentborrowingapp.ui.admin.EditEquipmentScreen
import com.example.equipmentborrowingapp.ui.admin.EditLabComputerScreen
import com.example.equipmentborrowingapp.ui.admin.ManageEquipmentScreen
import com.example.equipmentborrowingapp.ui.admin.ManageLabComputersScreen
import com.example.equipmentborrowingapp.ui.admin.ManageSoftwareStatusScreen
import com.example.equipmentborrowingapp.ui.admin.PendingRequestsScreen
import com.example.equipmentborrowingapp.ui.admin.SoftwareIssueReportsScreen
import com.example.equipmentborrowingapp.ui.auth.LoginScreen
import com.example.equipmentborrowingapp.ui.auth.RegisterScreen
import com.example.equipmentborrowingapp.ui.common.AdminDashboardCounts
import com.example.equipmentborrowingapp.ui.student.BorrowRequestScreen
import com.example.equipmentborrowingapp.ui.student.EquipmentListScreen
import com.example.equipmentborrowingapp.ui.student.LabComputerListScreen
import com.example.equipmentborrowingapp.ui.student.MyRequestsScreen
import com.example.equipmentborrowingapp.ui.student.ReportSoftwareIssueScreen
import com.example.equipmentborrowingapp.ui.student.StudentDashboardScreen
import com.example.equipmentborrowingapp.ui.theme.EquipmentBorrowingAppTheme
import com.example.equipmentborrowingapp.viewmodel.RequestViewModel
import com.example.equipmentborrowingapp.viewmodel.LabComputerViewModel
import com.example.equipmentborrowingapp.utils.UiState
import com.example.equipmentborrowingapp.ui.common.LoadingScreen
import com.example.equipmentborrowingapp.ui.common.EmptyStateView
import com.example.equipmentborrowingapp.viewmodel.AdminRequestViewModel
import androidx.compose.runtime.Composable
import com.example.equipmentborrowingapp.viewmodel.AdminEquipmentViewModel
import com.example.equipmentborrowingapp.ui.common.ErrorStateView
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import com.example.equipmentborrowingapp.navigation.isAdminScreen
import com.example.equipmentborrowingapp.navigation.isStudentScreen
import androidx.activity.compose.BackHandler
import com.example.equipmentborrowingapp.ui.student.StudentProfileScreen
import com.example.equipmentborrowingapp.ui.admin.AdminProfileScreen
import com.example.equipmentborrowingapp.viewmodel.NotificationViewModel
import com.example.equipmentborrowingapp.ui.common.NotificationScreen
import com.example.equipmentborrowingapp.data.model.AppNotification
import com.example.equipmentborrowingapp.data.repository.NotificationRepository
import com.example.equipmentborrowingapp.ui.student.EquipmentDetailsScreen
import com.example.equipmentborrowingapp.ui.student.RequestSubmittedScreen
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import androidx.compose.runtime.mutableIntStateOf
class MainActivity : ComponentActivity() {

    private val authRepository = AuthRepository()
    private val equipmentRepository = EquipmentRepository()
    private val requestRepository = RequestRepository()
    private val labComputerRepository = LabComputerRepository()
    private val notificationRepository = NotificationRepository()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            EquipmentBorrowingAppTheme {

                // Auth / session state
                var currentScreen by remember { mutableStateOf<AppScreen>(AppScreen.Login) }
                var currentUserRole by remember { mutableStateOf<String?>(null) }
                var currentUserName by remember { mutableStateOf("") }
                var currentUserEmail by remember { mutableStateOf("") }
                val equipmentViewModel = remember { EquipmentViewModel() }
                val requestViewModel = remember { RequestViewModel() }
                val labComputerViewModel = remember { LabComputerViewModel() }
                val adminRequestViewModel = remember { AdminRequestViewModel() }
                val adminEquipmentViewModel = remember { AdminEquipmentViewModel() }
                val snackbarHostState = remember { SnackbarHostState() }
                val scope = rememberCoroutineScope()
                val notificationViewModel = remember { NotificationViewModel() }
                fun showMessage(message: String) {
                    scope.launch {
                        snackbarHostState.showSnackbar(message)
                    }
                }



                // Screen data state
                fun sendNotification(
                    userId: String,
                    role: String,
                    title: String,
                    message: String,
                    type: String = "info"
                ) {
                    notificationRepository.sendNotification(
                        AppNotification(
                            userId = userId,
                            role = role,
                            title = title,
                            message = message,
                            type = type,
                            isRead = false,
                            timestamp = System.currentTimeMillis()
                        )
                    )
                }

                var adminAllRequests by remember { mutableStateOf<List<BorrowRequest>>(emptyList()) }
                var labComputerList by remember { mutableStateOf<List<LabComputer>>(emptyList()) }
                var computerSoftwareList by remember {
                    mutableStateOf<List<ComputerSoftwareStatus>>(emptyList())
                }
                var softwareIssueReports by remember {
                    mutableStateOf<List<SoftwareIssueReport>>(emptyList())
                }

                // Selected item state
                var selectedEquipment by remember { mutableStateOf<Equipment?>(null) }
                var submittedQuantity by remember { mutableIntStateOf(1) }
                var submittedBorrowDate by remember { mutableStateOf("") }
                var submittedDueDate by remember { mutableStateOf("") }
                var submittedPurpose by remember { mutableStateOf("Lab Project") }
                var selectedLabComputer by remember { mutableStateOf<LabComputer?>(null) }

                // Dashboard state
                var adminCounts by remember { mutableStateOf(AdminDashboardCounts()) }

                fun recalculateAdminCounts() {
                    adminCounts = AdminDashboardCounts(
                        totalEquipmentCount = adminEquipmentViewModel.equipmentList.size,
                        availableItemsCount = adminEquipmentViewModel.equipmentList.count { it.availableQuantity > 0 },
                        lowStockCount = adminEquipmentViewModel.equipmentList.count { it.availableQuantity in 1..2 },
                        pendingRequestsCount = adminAllRequests.count {
                            it.status.equals("Pending", ignoreCase = true)
                        },
                        approvedRequestsCount = adminAllRequests.count {
                            it.status.equals("Approved", ignoreCase = true)
                        },
                        returnedItemsCount = adminAllRequests.count {
                            it.status.equals("Returned", ignoreCase = true)
                        },
                        overdueItemsCount = adminAllRequests.count {
                            it.status.equals("Overdue", ignoreCase = true)
                        }
                    )
                }

                fun resetSessionState() {
                    currentUserRole = null
                    selectedEquipment = null
                    selectedLabComputer = null

                    equipmentViewModel.clearEquipment()
                    adminRequestViewModel.clearAdminRequests()
                    requestViewModel.clearMyRequests()
                    adminEquipmentViewModel.clearEquipment()
                    adminAllRequests = emptyList()
                    labComputerList = emptyList()
                    computerSoftwareList = emptyList()
                    softwareIssueReports = emptyList()

                    adminCounts = AdminDashboardCounts()
                    labComputerViewModel.clearStudentLabComputers()

                    currentUserName = ""
                    currentUserEmail = ""
                }

                fun isAdmin(): Boolean = currentUserRole == "admin"
                fun isStudent(): Boolean = currentUserRole == "student"

// Auth helpers

                fun safeLogoutToLogin() {
                    authRepository.logout()
                    resetSessionState()
                    currentScreen = AppScreen.Login
                }

                fun redirectUnauthorized(targetScreen: AppScreen) {
                    when {
                        isAdminScreen(targetScreen) && isStudent() -> {
                            showMessage(UiMessages.ACCESS_DENIED)
                            currentScreen = AppScreen.StudentDashboard
                        }

                        isStudentScreen(targetScreen) && isAdmin() -> {
                            showMessage(UiMessages.ACCESS_DENIED)
                            currentScreen = AppScreen.AdminDashboard
                        }

                        currentUserRole.isNullOrBlank() -> {
                            showMessage(UiMessages.LOGIN_REQUIRED)
                            safeLogoutToLogin()
                        }
                    }
                }
                fun loadLoggedInUserRole(onReady: (() -> Unit)? = null) {
                    val uid = authRepository.getCurrentUserUid()

                    if (uid.isNullOrBlank()) {
                        resetSessionState()
                        currentScreen = AppScreen.Login
                        return
                    }

                    authRepository.getCurrentUser { user ->
                        runOnUiThread {
                            if (user == null) {
                                showMessage(UiMessages.UNKNOWN_ROLE)
                                safeLogoutToLogin()
                                return@runOnUiThread
                            }

                            currentUserName = user.name
                            currentUserEmail = user.email
                            currentUserRole = user.role.trim().lowercase()

                            when (currentUserRole) {
                                "admin", "student" -> onReady?.invoke()
                                else -> {
                                    showMessage(UiMessages.UNKNOWN_ROLE)
                                    safeLogoutToLogin()
                                    notificationViewModel.clearNotifications()
                                }
                            }
                            notificationViewModel.startListening(
                                userId = uid,
                                role = currentUserRole ?: "student"
                            )
                        }
                    }
                }
                // Admin helpers
                fun refreshRequestsForAdmin(
                    openScreen: AppScreen? = null
                ) {
                    requestRepository.getAllRequests { allRequestsList ->
                        runOnUiThread {
                            adminAllRequests = allRequestsList
                            recalculateAdminCounts()

                            adminRequestViewModel.loadPendingRequests()
                            adminRequestViewModel.loadApprovedRequests()

                            openScreen?.let {
                                currentScreen = it
                            }
                        }
                    }
                }

                fun refreshEquipmentForAdmin(
                    openScreen: AppScreen? = null
                ) {
                    adminEquipmentViewModel.loadEquipment {
                        runOnUiThread {
                            recalculateAdminCounts()

                            openScreen?.let {
                                currentScreen = it
                            }
                        }
                    }
                }

                fun openAdminDashboardWithFreshData() {
                    refreshEquipmentForAdmin()
                    refreshRequestsForAdmin(AppScreen.AdminDashboard)
                }
                fun refreshLabComputersAndOpenManage() {
                    labComputerRepository.getLabComputers { list ->
                        runOnUiThread {
                            labComputerList = list
                            currentScreen = AppScreen.ManageLabComputers
                        }
                    }
                }
                fun refreshAdminDashboardData(
                    refreshPending: Boolean = false,
                    refreshApproved: Boolean = false,
                    onComplete: (() -> Unit)? = null
                ) {
                    adminEquipmentViewModel.loadEquipment {
                        requestRepository.getAllRequests { allRequestsResult ->
                            runOnUiThread {
                                adminAllRequests = allRequestsResult
                                recalculateAdminCounts()

                                if (refreshPending) {
                                    adminRequestViewModel.loadPendingRequests()
                                }

                                if (refreshApproved) {
                                    adminRequestViewModel.loadApprovedRequests()
                                }

                                onComplete?.invoke()
                            }
                        }
                    }
                }

                fun openDashboardAfterLogin() {
                    loadLoggedInUserRole {
                        when (currentUserRole) {
                            "student" -> {
                                currentScreen = AppScreen.StudentDashboard
                            }

                            "admin" -> {
                                refreshAdminDashboardData(
                                    refreshPending = true,
                                    refreshApproved = true
                                ) {
                                    currentScreen = AppScreen.AdminDashboard
                                }
                            }

                            else -> {
                                showMessage(UiMessages.UNKNOWN_ROLE)
                                safeLogoutToLogin()
                            }
                        }
                    }
                }

                fun startGoogleSignIn() {
                    scope.launch {
                        try {
                            val credentialManager = CredentialManager.create(this@MainActivity)

                            val googleIdOption = GetGoogleIdOption.Builder()
                                .setFilterByAuthorizedAccounts(false)
                                .setServerClientId("276611421496-rdsl2hnb8pgrh1edke7nu42jobsooc90.apps.googleusercontent.com")
                                .build()

                            val request = GetCredentialRequest.Builder()
                                .addCredentialOption(googleIdOption)
                                .build()

                            val result = credentialManager.getCredential(
                                request = request,
                                context = this@MainActivity
                            )

                            val credential = result.credential

                            if (
                                credential is CustomCredential &&
                                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                            ) {
                                val googleIdTokenCredential =
                                    GoogleIdTokenCredential.createFrom(credential.data)

                                authRepository.loginWithGoogle(
                                    idToken = googleIdTokenCredential.idToken
                                ) { success, message ->
                                    runOnUiThread {
                                        showMessage(message)

                                        if (success) {
                                            openDashboardAfterLogin()
                                        }
                                    }
                                }
                            } else {
                                showMessage("Invalid Google credential")
                            }

                        } catch (e: Exception) {
                            showMessage(e.message ?: "Google Sign-In cancelled or failed")
                        }
                    }
                }
                fun loadAdminEquipmentAndOpenManage() {
                    refreshEquipmentForAdmin(AppScreen.ManageEquipment)
                }

                fun loadPendingRequestsAndOpen() {
                    adminRequestViewModel.loadPendingRequests {
                        runOnUiThread {
                            currentScreen = AppScreen.PendingRequests
                        }
                    }
                }

                fun loadApprovedRequestsAndOpen() {
                    adminRequestViewModel.loadApprovedRequests {
                        runOnUiThread {
                            currentScreen = AppScreen.ApprovedRequests
                        }
                    }
                }

                fun loadLabComputersForAdmin() {
                    labComputerRepository.getLabComputers { list ->
                        runOnUiThread {
                            labComputerList = list
                            currentScreen = AppScreen.ManageLabComputers
                        }
                    }
                }
                fun loadAllSoftwareReportsAndOpen() {
                    labComputerRepository.getSoftwareIssueReports { list ->
                        runOnUiThread {
                            selectedLabComputer = null
                            softwareIssueReports = list
                            currentScreen = AppScreen.SoftwareIssueReports
                        }
                    }
                }
                // Request action helpers
                fun handleApproveRequest(request: BorrowRequest) {
                    requestRepository.approveRequest(request) { success, message ->
                        runOnUiThread {
                            showMessage(
                                if (success) UiMessages.REQUEST_APPROVED else message
                            )
                            if (success) {
                                sendNotification(
                                    userId = request.userId,
                                    role = "student",
                                    title = "Request Approved",
                                    message = "Your request for ${request.equipmentName} has been approved.",
                                    type = "success"
                                )

                                refreshRequestsForAdmin()
                            }
                        }
                    }
                }

                fun handleRejectRequest(request: BorrowRequest) {
                    requestRepository.rejectRequest(request) { success, message ->
                        runOnUiThread {
                            showMessage(
                                if (success) UiMessages.REQUEST_REJECTED else message
                            )

                            if (success) {
                                sendNotification(
                                    userId = request.userId,
                                    role = "student",
                                    title = "Request Rejected",
                                    message = "Your request for ${request.equipmentName} has been rejected.",
                                    type = "error"
                                )

                                refreshRequestsForAdmin()
                            }
                        }
                    }
                }

                fun handleReturnRequest(request: BorrowRequest) {
                    requestRepository.markRequestReturned(request) { success, message ->
                        runOnUiThread {
                            showMessage(
                                if (success) UiMessages.REQUEST_RETURNED else message
                            )

                            if (success) {
                                sendNotification(
                                    userId = request.userId,
                                    role = "student",
                                    title = "Item Returned",
                                    message = "${request.equipmentName} has been marked as returned.",
                                    type = "info"
                                )

                                refreshRequestsForAdmin()
                            }
                        }
                    }
                }
// Student helpers

                fun loadStudentRequestsAndOpenMyRequests() {
                    val uid = authRepository.getCurrentUserUid()
                    if (uid != null) {
                        requestViewModel.loadUserRequests(uid) {
                            runOnUiThread {
                                currentScreen = AppScreen.MyRequests
                            }
                        }
                    } else {
                        showMessage(UiMessages.USER_NOT_LOGGED_IN)
                        safeLogoutToLogin()
                    }
                }

                fun loadStudentEquipmentAndOpenList() {
                    equipmentViewModel.loadEquipment {
                        runOnUiThread {
                            currentScreen = AppScreen.EquipmentList
                        }
                    }
                }

                fun loadLabComputersForStudent() {
                    labComputerViewModel.loadStudentLabComputers {
                        runOnUiThread {
                            currentScreen = AppScreen.LabComputerList
                        }
                    }
                }

                @Composable
                fun renderPendingRequestsScreen() {
                    PendingRequestsScreen(
                        requestList = adminRequestViewModel.pendingRequests,
                        onApproveClick = { request ->
                            handleApproveRequest(request)
                        },
                        onRejectClick = { request ->
                            handleRejectRequest(request)
                        },
                        onBackClick = {
                            openAdminDashboardWithFreshData()
                        }
                    )
                }

                @Composable
                fun renderApprovedRequestsScreen() {
                    ApprovedRequestsScreen(
                        requestList = adminRequestViewModel.approvedRequests,
                        onReturnedClick = { request ->
                            handleReturnRequest(request)
                        },
                        onBackClick = {
                            openAdminDashboardWithFreshData()
                        }
                    )
                }



                @Composable
                fun renderStudentScreens() {
                    when (currentScreen) {
                        AppScreen.StudentDashboard -> {
                            if (!isStudent()) {
                                redirectUnauthorized(AppScreen.StudentDashboard)
                            } else {
                                StudentDashboardScreen(
                                    onViewEquipmentClick = {
                                        loadStudentEquipmentAndOpenList()
                                    },
                                    onMyRequestsClick = {
                                        loadStudentRequestsAndOpenMyRequests()
                                    },
                                    onLabComputersClick = {
                                        loadLabComputersForStudent()
                                    },
                                    onProfileClick = {
                                        currentScreen = AppScreen.StudentProfile
                                    },
                                    onNotificationClick = {
                                        currentScreen = AppScreen.Notifications
                                    },
                                    onLogout = {
                                        safeLogoutToLogin()
                                    }
                                )
                            }
                        }
                        AppScreen.StudentProfile -> {
                            if (!isStudent()) {
                                redirectUnauthorized(AppScreen.StudentProfile)
                            } else {
                                StudentProfileScreen(
                                    userName = currentUserName.ifBlank { "Student" },
                                    userEmail = currentUserEmail.ifBlank { "No email found" },
                                    role = currentUserRole ?: "student",
                                    onBackClick = {
                                        currentScreen = AppScreen.StudentDashboard
                                    }
                                )
                            }
                        }
                        AppScreen.EquipmentList -> {
                            if (!isStudent()) {
                                redirectUnauthorized(AppScreen.EquipmentList)
                            } else {
                                when (equipmentViewModel.equipmentUiState) {
                                    UiState.Loading -> {
                                        LoadingScreen(message = "Loading equipment...")
                                    }

                                    is UiState.Error -> {
                                        ErrorStateView(
                                            title = "Failed to load equipment",
                                            message = (equipmentViewModel.equipmentUiState as UiState.Error).message,
                                            onRetryClick = {
                                                loadStudentEquipmentAndOpenList()
                                            }
                                        )
                                    }

                                    else -> {
                                        if (equipmentViewModel.equipmentList.isEmpty()) {
                                            EmptyStateView(
                                                title = "No equipment found",
                                                subtitle = "No equipment is available right now."
                                            )
                                        } else {
                                            EquipmentListScreen(
                                                equipmentList = equipmentViewModel.equipmentList,
                                                onViewDetailsClick = { equipment ->
                                                    selectedEquipment = equipment
                                                    currentScreen = AppScreen.EquipmentDetails
                                                },
                                                onBackClick = {
                                                    currentScreen = AppScreen.StudentDashboard
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        AppScreen.EquipmentDetails -> {
                            selectedEquipment?.let { equipment ->
                                EquipmentDetailsScreen(
                                    equipment = equipment,
                                    onBorrowClick = { equipment ->
                                        selectedEquipment = equipment
                                        currentScreen = AppScreen.BorrowRequest
                                    },
                                    onBackClick = {
                                        currentScreen = AppScreen.EquipmentList
                                    }

                                )
                            }
                        }
                        AppScreen.BorrowRequest -> {
                            if (!isStudent()) {
                                redirectUnauthorized(AppScreen.BorrowRequest)
                            } else {
                                val equipment = selectedEquipment

                                if (equipment != null) {
                                    BorrowRequestScreen(
                                        equipment = equipment,
                                        onSubmitClick = { quantity, borrowDate, dueDate ->
                                            when {
                                                equipment.id.isBlank() -> {
                                                    showMessage(UiMessages.EQUIPMENT_NOT_FOUND)
                                                }

                                                !equipment.isBorrowable -> {
                                                    showMessage("This equipment is lab-use-only")
                                                }

                                                equipment.availableQuantity <= 0 -> {
                                                    showMessage(UiMessages.OUT_OF_STOCK)
                                                }

                                                quantity <= 0 -> {
                                                    showMessage("Quantity must be greater than 0")
                                                }

                                                quantity > equipment.availableQuantity -> {
                                                    showMessage("Requested quantity exceeds available stock")
                                                }

                                                borrowDate.isBlank() || dueDate.isBlank() -> {
                                                    showMessage("Borrow date and due date are required")
                                                }

                                                else -> {
                                                    val uid = authRepository.getCurrentUserUid()

                                                    if (uid == null) {
                                                        showMessage(UiMessages.USER_NOT_LOGGED_IN)
                                                        safeLogoutToLogin()
                                                    } else {
                                                        authRepository.getUserName(uid) { userName: String? ->
                                                            runOnUiThread {
                                                                if (userName.isNullOrBlank()) {
                                                                    showMessage(UiMessages.USER_NAME_NOT_FOUND)
                                                                } else {
                                                                    requestRepository.submitBorrowRequest(
                                                                        userId = uid,
                                                                        userName = userName,
                                                                        equipmentId = equipment.id,
                                                                        equipmentName = equipment.name.ifBlank {
                                                                            UiMessages.UNKNOWN_EQUIPMENT
                                                                        },
                                                                        equipmentCategory = equipment.category,
                                                                        equipmentImageName = equipment.imageName,
                                                                        equipmentImageUrl = equipment.imageUrl,
                                                                        quantity = quantity,
                                                                        borrowDate = borrowDate,
                                                                        dueDate = dueDate
                                                                    ) { success, message ->
                                                                        runOnUiThread {
                                                                            showMessage(
                                                                                if (success) UiMessages.REQUEST_SUBMITTED else message
                                                                            )

                                                                            if (success) {
                                                                                sendNotification(
                                                                                    userId = "admin", // or actual admin UID list
                                                                                    role = "admin",
                                                                                    title = "New Borrow Request",
                                                                                    message = "$userName requested ${equipment.name}",
                                                                                    type = "warning"
                                                                                )

                                                                                equipmentViewModel.loadEquipment {
                                                                                    runOnUiThread {
                                                                                        submittedQuantity = quantity
                                                                                        submittedBorrowDate = borrowDate
                                                                                        submittedDueDate = dueDate
                                                                                        submittedPurpose = "Lab Project"

                                                                                        currentScreen = AppScreen.RequestSubmitted
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        },
                                        onBackClick = {
                                            currentScreen = AppScreen.EquipmentList
                                        }
                                    )
                                } else {
                                    showMessage(UiMessages.EQUIPMENT_NOT_FOUND)
                                    currentScreen = AppScreen.EquipmentList
                                }
                            }
                        }
                        AppScreen.RequestSubmitted -> {
                            selectedEquipment?.let { equipment ->
                                RequestSubmittedScreen(
                                    equipment = equipment,
                                    quantity = submittedQuantity,
                                    borrowDate = submittedBorrowDate,
                                    dueDate = submittedDueDate,
                                    purpose = submittedPurpose,
                                    onViewRequestClick = {
                                        loadStudentRequestsAndOpenMyRequests()
                                    },
                                    onBackHomeClick = {
                                        currentScreen = AppScreen.StudentDashboard
                                    }
                                )
                            }
                        }
                        AppScreen.MyRequests -> {
                            if (!isStudent()) {
                                redirectUnauthorized(AppScreen.MyRequests)
                            } else {
                                when (requestViewModel.myRequestsUiState) {
                                    UiState.Loading -> {
                                        LoadingScreen(message = "Loading requests...")
                                    }

                                    is UiState.Error -> {
                                        ErrorStateView(
                                            title = "Failed to load requests",
                                            message = (requestViewModel.myRequestsUiState as UiState.Error).message,
                                            onRetryClick = {
                                                loadStudentRequestsAndOpenMyRequests()
                                            }
                                        )
                                    }

                                    else -> {
                                        if (requestViewModel.myRequests.isEmpty()) {
                                            EmptyStateView(
                                                title = "No requests found",
                                                subtitle = "You have not made any borrowing requests yet."
                                            )
                                        } else {
                                            MyRequestsScreen(
                                                requestList = requestViewModel.myRequests,
                                                onBackClick = {
                                                    currentScreen = AppScreen.StudentDashboard
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        AppScreen.LabComputerList -> {
                            if (!isStudent()) {
                                redirectUnauthorized(AppScreen.LabComputerList)
                            } else {
                                when (labComputerViewModel.studentLabComputerUiState) {
                                    UiState.Loading -> {
                                        LoadingScreen(message = "Loading lab computers...")
                                    }

                                    is UiState.Error -> {
                                        ErrorStateView(
                                            title = "Failed to load lab computers",
                                            message = (labComputerViewModel.studentLabComputerUiState as UiState.Error).message,
                                            onRetryClick = {
                                                loadLabComputersForStudent()
                                            }
                                        )
                                    }

                                    else -> {
                                        if (labComputerViewModel.studentLabComputerList.isEmpty()) {
                                            EmptyStateView(
                                                title = "No lab computers found",
                                                subtitle = "No lab computer information is available right now."
                                            )
                                        } else {
                                            LabComputerListScreen(
                                                computerList = labComputerViewModel.studentLabComputerList,
                                                onReportClick = { computer ->
                                                    selectedLabComputer = computer
                                                    currentScreen = AppScreen.ReportSoftwareIssue
                                                },
                                                onBackClick = {
                                                    currentScreen = AppScreen.StudentDashboard
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        AppScreen.ReportSoftwareIssue -> {
                            if (!isStudent()) {
                                redirectUnauthorized(AppScreen.ReportSoftwareIssue)
                            } else {
                                val computer = selectedLabComputer

                                if (computer != null) {
                                    ReportSoftwareIssueScreen(
                                        computer = computer,
                                        onSubmitClick = { softwareName, issueType, description, severity ->
                                            val uid = authRepository.getCurrentUserUid()

                                            if (uid == null) {
                                                showMessage(UiMessages.USER_NOT_LOGGED_IN)
                                                safeLogoutToLogin()
                                            } else {
                                                authRepository.getUserName(uid) { userName ->
                                                    runOnUiThread {
                                                        if (userName.isNullOrBlank()) {
                                                            showMessage(UiMessages.USER_NAME_NOT_FOUND)
                                                        } else {
                                                            labComputerRepository.submitSoftwareIssueReport(
                                                                computerId = computer.id,
                                                                computerName = computer.pcName,
                                                                softwareName = softwareName,
                                                                reportedByUserId = uid,
                                                                reportedByUserName = userName,
                                                                issueType = issueType,
                                                                description = description,
                                                                severity = severity
                                                            ) { success, message ->
                                                                runOnUiThread {
                                                                    showMessage(message)
                                                                    if (success) {
                                                                        sendNotification(
                                                                            userId = "",
                                                                            role = "admin",
                                                                            title = "New Software Issue",
                                                                            message = "$userName reported issue in $softwareName on ${computer.pcName}",
                                                                            type = "warning"
                                                                        )

                                                                        currentScreen = AppScreen.LabComputerList
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        },
                                        onBackClick = {
                                            currentScreen = AppScreen.LabComputerList
                                        }
                                    )
                                } else {
                                    showMessage(UiMessages.LAB_COMPUTER_NOT_FOUND)
                                    currentScreen = AppScreen.LabComputerList
                                }
                            }
                        }

                        else -> Unit
                    }
                }
                LaunchedEffect(Unit) {
                    if (authRepository.isUserLoggedIn()) {
                        loadLoggedInUserRole {
                            currentScreen = when (currentUserRole) {
                                "admin" -> AppScreen.AdminDashboard
                                "student" -> AppScreen.StudentDashboard
                                else -> AppScreen.Login
                            }
                        }
                    } else {
                        resetSessionState()
                        currentScreen = AppScreen.Login
                    }
                }

                LaunchedEffect(currentScreen, currentUserRole) {
                    if (currentScreen == AppScreen.Login || currentScreen == AppScreen.Register) return@LaunchedEffect

                    if (!authRepository.isUserLoggedIn()) {
                        showMessage(UiMessages.LOGIN_REQUIRED)
                        safeLogoutToLogin()
                        return@LaunchedEffect
                    }

                    if (isAdminScreen(currentScreen) && !isAdmin()) {
                        redirectUnauthorized(currentScreen)
                        return@LaunchedEffect
                    }

                    if (isStudentScreen(currentScreen) && !isStudent()) {
                        redirectUnauthorized(currentScreen)
                        return@LaunchedEffect
                    }

                    if (currentScreen == AppScreen.AdminDashboard && isAdmin()) {
                        refreshAdminDashboardData()
                    }
                }
                BackHandler {
                    when (currentScreen) {

                        AppScreen.Login -> {
                            // Login screen e back press korle app close hobe na
                        }

                        AppScreen.Register -> {
                            currentScreen = AppScreen.Login
                        }
                        AppScreen.Notifications -> {
                            if (isAdmin()) {
                                currentScreen = AppScreen.AdminDashboard
                            } else {
                                currentScreen = AppScreen.StudentDashboard
                            }
                        }
                        AppScreen.AdminProfile -> {
                            // future use
                            currentScreen = AppScreen.AdminDashboard
                        }
                        AppScreen.StudentProfile -> {
                            currentScreen = AppScreen.StudentDashboard
                        }
                        AppScreen.StudentDashboard -> {
                            // Dashboard e back dile logout hoye login e jabe
                            safeLogoutToLogin()
                        }

                        AppScreen.EquipmentList,
                        AppScreen.RequestSubmitted -> {
                            currentScreen = AppScreen.StudentDashboard
                        }
                        AppScreen.MyRequests,
                        AppScreen.LabComputerList -> {
                            currentScreen = AppScreen.StudentDashboard
                        }
                        AppScreen.EquipmentDetails -> {
                            currentScreen = AppScreen.EquipmentList
                        }
                        AppScreen.BorrowRequest -> {
                            currentScreen = AppScreen.EquipmentList
                        }

                        AppScreen.ReportSoftwareIssue -> {
                            currentScreen = AppScreen.LabComputerList
                        }

                        AppScreen.AdminDashboard -> {
                            // Admin dashboard e back dile logout hoye login e jabe
                            safeLogoutToLogin()
                        }

                        AppScreen.AddEquipment,
                        AppScreen.ManageEquipment,
                        AppScreen.PendingRequests,
                        AppScreen.ApprovedRequests,
                        AppScreen.ManageLabComputers -> {
                            currentScreen = AppScreen.AdminDashboard
                        }

                        AppScreen.EditEquipment -> {
                            currentScreen = AppScreen.ManageEquipment
                        }

                        AppScreen.AddLabComputer,
                        AppScreen.EditLabComputer,
                        AppScreen.ManageSoftwareStatus -> {
                            currentScreen = AppScreen.ManageLabComputers
                        }

                        AppScreen.SoftwareIssueReports -> {
                            currentScreen =
                                if (selectedLabComputer == null) {
                                    AppScreen.AdminDashboard
                                } else {
                                    AppScreen.ManageLabComputers
                                }
                        }
                    }
                }
                Scaffold(
                    snackbarHost = {
                        SnackbarHost(hostState = snackbarHostState)
                    }
                ) { innerPadding ->

                    Box(
                        modifier = Modifier.padding(innerPadding)
                    ) {

                        when (currentScreen) {
                            in listOf(
                                AppScreen.StudentProfile,
                                AppScreen.StudentDashboard,
                                AppScreen.EquipmentList,
                                AppScreen.EquipmentDetails,
                                AppScreen.BorrowRequest,
                                AppScreen.RequestSubmitted,
                                AppScreen.MyRequests,
                                AppScreen.LabComputerList,
                                AppScreen.ReportSoftwareIssue

                            ) -> {
                                renderStudentScreens()
                            }
                            AppScreen.Login -> {
                                LoginScreen(
                                    onLoginClick = { email, password ->
                                        if (email.isBlank() || password.isBlank()) {
                                            showMessage(UiMessages.EMAIL_PASSWORD_REQUIRED)
                                        } else {
                                            authRepository.loginUser(email, password) { success, message ->
                                                runOnUiThread {
                                                    showMessage(message)

                                                    if (success) {
                                                        openDashboardAfterLogin()
                                                    }
                                                }
                                            }
                                        }
                                    },

                                    onGoToRegister = {
                                        currentScreen = AppScreen.Register
                                    },

                                    // ✅ FORGOT PASSWORD
                                    onForgotPasswordClick = { email ->
                                        authRepository.sendPasswordResetEmail(email) { _, message ->
                                            runOnUiThread {
                                                showMessage(message)
                                            }
                                        }
                                    },


                                    // ✅ GOOGLE CLICK
                                    onGoogleClick = {
                                        startGoogleSignIn()
                                    }
                                )
                            }

                            AppScreen.Register -> {
                                RegisterScreen(
                                    onRegisterClick = { name, email, password, role ->
                                        if (
                                            name.isBlank() ||
                                            email.isBlank() ||
                                            password.isBlank() ||
                                            role.isBlank()
                                        ) {
                                            showMessage(UiMessages.REQUIRED_FIELDS)
                                        } else {
                                            authRepository.registerUser(
                                                name = name,
                                                email = email,
                                                password = password,
                                                role = role.lowercase()
                                            ) { success, message ->
                                                runOnUiThread {
                                                    showMessage(message)
                                                    if (success) {
                                                        currentScreen = AppScreen.Login
                                                    }
                                                }
                                            }
                                        }
                                    },
                                    onGoToLogin = {
                                        currentScreen = AppScreen.Login
                                    }
                                )
                            }

                            AppScreen.Notifications -> {
                                NotificationScreen(
                                    notificationList = notificationViewModel.notificationList,
                                    onMarkReadClick = { notification ->
                                        notificationViewModel.markAsRead(notification.id)
                                    },
                                    onDeleteClick = { notification ->
                                        notificationViewModel.deleteNotification(notification.id)
                                    },
                                    onBackClick = {
                                        if (isAdmin()) {
                                            currentScreen = AppScreen.AdminDashboard
                                        } else {
                                            currentScreen = AppScreen.StudentDashboard
                                        }
                                    }
                                )
                            }

                            AppScreen.AdminDashboard -> {
                                if (!isAdmin()) {
                                    redirectUnauthorized(AppScreen.AdminDashboard)
                                } else {
                                    AdminDashboardScreen(
                                        totalEquipmentCount = adminCounts.totalEquipmentCount,
                                        availableItemsCount = adminCounts.availableItemsCount,
                                        lowStockCount = adminCounts.lowStockCount,
                                        pendingRequestsCount = adminCounts.pendingRequestsCount,
                                        approvedRequestsCount = adminCounts.approvedRequestsCount,
                                        returnedItemsCount = adminCounts.returnedItemsCount,
                                        overdueItemsCount = adminCounts.overdueItemsCount,
                                        onAddEquipmentClick = {
                                            currentScreen = AppScreen.AddEquipment
                                        },
                                        onViewPendingRequestsClick = {
                                            loadPendingRequestsAndOpen()
                                        },
                                        onViewApprovedRequestsClick = {
                                            loadApprovedRequestsAndOpen()
                                        },
                                        onManageEquipmentClick = {
                                            loadAdminEquipmentAndOpenManage()
                                        },
                                        onManageLabComputersClick = {
                                            loadLabComputersForAdmin()
                                        },
                                        onViewSoftwareReportsClick = {
                                            loadAllSoftwareReportsAndOpen()
                                        },
                                        onProfileClick = {
                                            currentScreen = AppScreen.AdminProfile
                                        },
                                        onNotificationClick = {
                                            currentScreen = AppScreen.Notifications
                                        },
                                        onLogout = {
                                            safeLogoutToLogin()
                                        }
                                    )
                                }
                            }
                            AppScreen.AdminProfile -> {
                                if (!isAdmin()) {
                                    redirectUnauthorized(AppScreen.AdminProfile)
                                } else {
                                    AdminProfileScreen(
                                        userName = currentUserName.ifBlank { "Admin" },
                                        userEmail = currentUserEmail.ifBlank { "No email found" },
                                        role = currentUserRole ?: "admin",
                                        onBackClick = {
                                            currentScreen = AppScreen.AdminDashboard
                                        }
                                    )
                                }
                            }
                            AppScreen.AddEquipment -> {
                                if (!isAdmin()) {
                                    redirectUnauthorized(AppScreen.AddEquipment)
                                } else {
                                    AddEquipmentScreen(
                                        onAddClick = { name, description, condition, totalQuantity, availableQuantity, category, imageName, imageUrl, isBorrowable ->
                                            if (
                                                name.isBlank() ||
                                                description.isBlank() ||
                                                condition.isBlank() ||
                                                totalQuantity.isBlank() ||
                                                availableQuantity.isBlank() ||
                                                category.isBlank() ||
                                                (imageName.isBlank() && imageUrl.isBlank())
                                            ) {
                                                showMessage(UiMessages.REQUIRED_FIELDS)
                                            } else {
                                                val totalQty = totalQuantity.toIntOrNull()
                                                val availableQty = availableQuantity.toIntOrNull()

                                                when {
                                                    totalQty == null || totalQty <= 0 -> {
                                                        showMessage("Enter a valid total quantity")
                                                    }

                                                    availableQty == null || availableQty < 0 -> {
                                                        showMessage("Enter a valid available quantity")
                                                    }

                                                    availableQty > totalQty -> {
                                                        showMessage("Available quantity cannot be greater than total quantity")
                                                    }

                                                    else -> {
                                                        equipmentRepository.addEquipment(
                                                            name = name,
                                                            description = description,
                                                            condition = condition,
                                                            totalQuantity = totalQty,
                                                            availableQuantity = availableQty,
                                                            category = category,
                                                            imageName = imageName,
                                                            imageUrl = imageUrl,
                                                            isBorrowable = isBorrowable
                                                        ) { success, message ->
                                                            runOnUiThread {
                                                                showMessage(message)
                                                                if (success) {
                                                                    openAdminDashboardWithFreshData()
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        },
                                        onBackClick = {
                                            currentScreen = AppScreen.AdminDashboard
                                        }
                                    )
                                }
                            }

                            AppScreen.ManageEquipment -> {
                                if (!isAdmin()) {
                                    redirectUnauthorized(AppScreen.ManageEquipment)
                                } else {
                                    when (adminEquipmentViewModel.equipmentUiState) {
                                        UiState.Loading -> {
                                            LoadingScreen(message = "Loading equipment...")
                                        }

                                        is UiState.Error -> {
                                            ErrorStateView(
                                                title = "Failed to load equipment",
                                                message = (adminEquipmentViewModel.equipmentUiState as UiState.Error).message,
                                                onRetryClick = {
                                                    loadAdminEquipmentAndOpenManage()
                                                }
                                            )
                                        }

                                        else -> {
                                            if (adminEquipmentViewModel.equipmentList.isEmpty()) {
                                                EmptyStateView(
                                                    title = "No equipment found",
                                                    subtitle = "There is no equipment available to manage right now."
                                                )
                                            } else {
                                                ManageEquipmentScreen(
                                                    equipmentList = adminEquipmentViewModel.equipmentList,
                                                    onEditClick = { equipment ->
                                                        selectedEquipment = equipment
                                                        currentScreen = AppScreen.EditEquipment
                                                    },
                                                    onBackClick = {
                                                        openAdminDashboardWithFreshData()
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            AppScreen.EditEquipment -> {
                                if (!isAdmin()) {
                                    redirectUnauthorized(AppScreen.EditEquipment)
                                } else {
                                    val equipment = selectedEquipment

                                    if (equipment != null) {
                                        EditEquipmentScreen(
                                            equipment = equipment,
                                            onSaveClick = { updatedEquipment ->
                                                equipmentRepository.updateEquipment(updatedEquipment) { success, message ->
                                                    runOnUiThread {
                                                        showMessage(
                                                            if (success) UiMessages.UPDATE_SUCCESS else message
                                                        )

                                                        if (success) {
                                                            refreshEquipmentForAdmin(AppScreen.ManageEquipment)
                                                            refreshRequestsForAdmin()
                                                        }
                                                    }
                                                }
                                            },
                                            onDeleteClick = { equipmentToDelete ->
                                                equipmentRepository.deleteEquipment(
                                                    equipmentToDelete.id
                                                ) { success, message ->
                                                    runOnUiThread {
                                                        showMessage(message)

                                                        if (success) {
                                                            selectedEquipment = null
                                                            refreshEquipmentForAdmin(AppScreen.ManageEquipment)
                                                            refreshRequestsForAdmin()
                                                        }
                                                    }
                                                }
                                            },
                                            onBackClick = {
                                                currentScreen = AppScreen.ManageEquipment
                                            }
                                        )
                                    } else {
                                        showMessage(UiMessages.EQUIPMENT_NOT_FOUND)
                                        currentScreen = AppScreen.ManageEquipment
                                    }
                                }
                            }




                            AppScreen.PendingRequests -> {
                                if (!isAdmin()) {
                                    redirectUnauthorized(AppScreen.PendingRequests)
                                } else {
                                    when (adminRequestViewModel.pendingUiState) {
                                        UiState.Loading -> {
                                            LoadingScreen(message = "Loading pending requests...")
                                        }

                                        is UiState.Error -> {
                                            ErrorStateView(
                                                title = "Failed to load pending requests",
                                                message = (adminRequestViewModel.pendingUiState as UiState.Error).message,
                                                onRetryClick = {
                                                    loadPendingRequestsAndOpen()
                                                }
                                            )
                                        }

                                        else -> {
                                            if (adminRequestViewModel.pendingRequests.isEmpty()) {
                                                EmptyStateView(
                                                    title = "No pending requests",
                                                    subtitle = "There are no pending borrowing requests right now."
                                                )
                                            } else {
                                                renderPendingRequestsScreen()
                                            }
                                        }
                                    }
                                }
                            }

                            AppScreen.ApprovedRequests -> {
                                if (!isAdmin()) {
                                    redirectUnauthorized(AppScreen.ApprovedRequests)
                                } else {
                                    when (adminRequestViewModel.approvedUiState) {
                                        UiState.Loading -> {
                                            LoadingScreen(message = "Loading approved requests...")
                                        }

                                        is UiState.Error -> {
                                            ErrorStateView(
                                                title = "Failed to load approved requests",
                                                message = (adminRequestViewModel.approvedUiState as UiState.Error).message,
                                                onRetryClick = {
                                                    loadApprovedRequestsAndOpen()
                                                }
                                            )
                                        }

                                        else -> {
                                            if (adminRequestViewModel.approvedRequests.isEmpty()) {
                                                EmptyStateView(
                                                    title = "No approved requests",
                                                    subtitle = "There are no approved or active requests right now."
                                                )
                                            } else {
                                                renderApprovedRequestsScreen()
                                            }
                                        }
                                    }
                                }
                            }


                            AppScreen.ManageLabComputers -> {
                                if (!isAdmin()) {
                                    redirectUnauthorized(AppScreen.ManageLabComputers)
                                } else {
                                    ManageLabComputersScreen(
                                        computerList = labComputerList,
                                        onAddComputerClick = {
                                            currentScreen = AppScreen.AddLabComputer
                                        },
                                        onEditComputerClick = { computer ->
                                            selectedLabComputer = computer
                                            currentScreen = AppScreen.EditLabComputer
                                        },
                                        onOpenSoftwareClick = { computer ->
                                            selectedLabComputer = computer
                                            labComputerRepository.getSoftwareStatusForComputer(
                                                computer.id
                                            ) { list ->
                                                runOnUiThread {
                                                    computerSoftwareList = list
                                                    currentScreen = AppScreen.ManageSoftwareStatus
                                                }
                                            }
                                        },
                                        onViewReportsClick = { computer ->
                                            selectedLabComputer = computer
                                            labComputerRepository.getSoftwareIssueReports { list ->
                                                runOnUiThread {
                                                    softwareIssueReports = list.filter {
                                                        it.computerId == computer.id
                                                    }
                                                    currentScreen = AppScreen.SoftwareIssueReports
                                                }
                                            }
                                        },
                                        onBackClick = {
                                            currentScreen = AppScreen.AdminDashboard
                                        }
                                    )
                                }
                            }

                            AppScreen.AddLabComputer -> {
                                if (!isAdmin()) {
                                    redirectUnauthorized(AppScreen.AddLabComputer)
                                } else {
                                    AddLabComputerScreen(
                                        onAddClick = { pcName, labRoom, locationNote, ipAddress, status, remarks ->
                                            labComputerRepository.addLabComputer(
                                                pcName = pcName,
                                                labRoom = labRoom,
                                                locationNote = locationNote,
                                                ipAddress = ipAddress,
                                                status = status,
                                                remarks = remarks
                                            ) { success, message ->
                                                runOnUiThread {
                                                    showMessage(message)
                                                    if (success) {
                                                        refreshLabComputersAndOpenManage()
                                                    }
                                                }
                                            }
                                        },
                                        onBackClick = {
                                            currentScreen = AppScreen.ManageLabComputers
                                        }
                                    )
                                }
                            }

                            AppScreen.EditLabComputer -> {
                                if (!isAdmin()) {
                                    redirectUnauthorized(AppScreen.EditLabComputer)
                                } else {
                                    val computer = selectedLabComputer

                                    if (computer != null) {
                                        EditLabComputerScreen(
                                            computer = computer,
                                            onSaveClick = { updatedComputer ->
                                                labComputerRepository.updateLabComputer(
                                                    updatedComputer
                                                ) { success, message ->
                                                    runOnUiThread {
                                                        showMessage(message)
                                                        if (success) {
                                                            refreshLabComputersAndOpenManage()
                                                        }
                                                    }
                                                }
                                            },
                                            onDeleteClick = { computerToDelete ->
                                                labComputerRepository.deleteLabComputer(
                                                    computerToDelete.id
                                                ) { success, message ->
                                                    runOnUiThread {
                                                        showMessage(message)
                                                        if (success) {
                                                            selectedLabComputer = null
                                                            refreshLabComputersAndOpenManage()
                                                        }
                                                    }
                                                }
                                            },
                                            onBackClick = {
                                                currentScreen = AppScreen.ManageLabComputers
                                            }
                                        )
                                    } else {
                                        showMessage(UiMessages.LAB_COMPUTER_NOT_FOUND)
                                        currentScreen = AppScreen.ManageLabComputers
                                    }
                                }
                            }

                            AppScreen.ManageSoftwareStatus -> {
                                if (!isAdmin()) {
                                    redirectUnauthorized(AppScreen.ManageSoftwareStatus)
                                } else {
                                    val computer = selectedLabComputer

                                    if (computer != null) {
                                        ManageSoftwareStatusScreen(
                                            computer = computer,
                                            softwareList = computerSoftwareList,
                                            onAddSoftwareClick = { softwareName, version, installed, launchesProperly, compileWorks, runWorks, remarks ->
                                                labComputerRepository.addSoftwareStatus(
                                                    computerId = computer.id,
                                                    softwareName = softwareName,
                                                    version = version,
                                                    installed = installed,
                                                    launchesProperly = launchesProperly,
                                                    compileWorks = compileWorks,
                                                    runWorks = runWorks,
                                                    remarks = remarks
                                                ) { success, message ->
                                                    runOnUiThread {
                                                        showMessage(message)
                                                        if (success) {
                                                            labComputerRepository.getSoftwareStatusForComputer(
                                                                computer.id
                                                            ) { list ->
                                                                runOnUiThread {
                                                                    computerSoftwareList = list
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            },
                                            onBackClick = {
                                                currentScreen = AppScreen.ManageLabComputers
                                            }
                                        )
                                    } else {
                                        showMessage(UiMessages.LAB_COMPUTER_NOT_FOUND)
                                        currentScreen = AppScreen.ManageLabComputers
                                    }
                                }
                            }




                            AppScreen.SoftwareIssueReports -> {
                                if (!isAdmin()) {
                                    redirectUnauthorized(AppScreen.SoftwareIssueReports)
                                } else {
                                    SoftwareIssueReportsScreen(
                                        reportList = softwareIssueReports,
                                        onStatusUpdateClick = { report, newStatus ->
                                            labComputerRepository.updateIssueReportStatus(
                                                reportId = report.id,
                                                newStatus = newStatus
                                            ) { success, message ->
                                                runOnUiThread {
                                                    showMessage(message)
                                                    if (success) {
                                                        val selectedComputerId = selectedLabComputer?.id
                                                        labComputerRepository.getSoftwareIssueReports { list ->
                                                            runOnUiThread {
                                                                softwareIssueReports =
                                                                    if (selectedComputerId.isNullOrBlank()) {
                                                                        list
                                                                    } else {
                                                                        list.filter { it.computerId == selectedComputerId }
                                                                    }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        },
                                        onBackClick = {
                                            currentScreen = AppScreen.ManageLabComputers
                                        }
                                    )
                                }
                            }

                            else -> Unit
                        }
                    }
                }
            }
        }
    }
}