package com.example.equipmentborrowingapp
import com.example.equipmentborrowingapp.ui.screen.SplashScreen
import com.example.equipmentborrowingapp.data.model.AppNotification
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
import com.example.equipmentborrowingapp.ui.student.MySoftwareIssuesScreen
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
import com.example.equipmentborrowingapp.data.repository.NotificationRepository
import com.example.equipmentborrowingapp.ui.student.EquipmentDetailsScreen
import com.example.equipmentborrowingapp.ui.student.RequestSubmittedScreen
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import androidx.compose.runtime.mutableIntStateOf
import com.example.equipmentborrowingapp.data.repository.RoomRepository
import com.example.equipmentborrowingapp.data.model.Room
import com.example.equipmentborrowingapp.viewmodel.RoomViewModel
import com.example.equipmentborrowingapp.ui.admin.AddRoomScreen
import com.example.equipmentborrowingapp.ui.admin.ManageRoomsScreen
import com.example.equipmentborrowingapp.ui.student.RoomSelectionScreen
import com.example.equipmentborrowingapp.data.model.AppUser
import com.example.equipmentborrowingapp.data.repository.UserRepository
import com.example.equipmentborrowingapp.ui.admin.PendingStudentsScreen
import com.example.equipmentborrowingapp.data.model.Institution
import com.example.equipmentborrowingapp.data.repository.InstitutionRepository
import com.example.equipmentborrowingapp.navigation.isSuperAdminScreen
import com.example.equipmentborrowingapp.ui.superadmin.SuperAdminDashboardScreen
import com.example.equipmentborrowingapp.ui.superadmin.ManageInstitutionsScreen
import com.example.equipmentborrowingapp.ui.superadmin.CreateInstitutionScreen
import com.example.equipmentborrowingapp.ui.superadmin.CreateInstitutionAdminScreen
import com.example.equipmentborrowingapp.data.model.InstitutionAdminRequest
import com.example.equipmentborrowingapp.ui.superadmin.ManageInstitutionAdminRequestsScreen
import com.example.equipmentborrowingapp.ui.admin.ManageStudentsScreen
import com.example.equipmentborrowingapp.ui.admin.ReportsDashboardScreen
import com.example.equipmentborrowingapp.ui.admin.RoomWiseEquipmentReportScreen
import com.example.equipmentborrowingapp.ui.admin.StudentBorrowHistoryReportScreen
import com.example.equipmentborrowingapp.ui.admin.BorrowRequestReportScreen
import com.example.equipmentborrowingapp.ui.admin.LowStockReportScreen
import com.example.equipmentborrowingapp.ui.admin.SoftwareIssueReportAdminScreen
import com.example.equipmentborrowingapp.ui.screen.SplashScreen
import com.example.equipmentborrowingapp.data.model.SoftwareInstallRequest
import com.example.equipmentborrowingapp.ui.student.SoftwareInstallRequestScreen
import com.example.equipmentborrowingapp.ui.admin.SoftwareInstallRequestsAdminScreen
class MainActivity : ComponentActivity() {

    private val authRepository = AuthRepository()
    private val institutionRepository = InstitutionRepository()
    private val equipmentRepository = EquipmentRepository()
    private val requestRepository = RequestRepository()
    private val labComputerRepository = LabComputerRepository()
    private val notificationRepository = NotificationRepository()
    private val roomRepository = RoomRepository()
    private val userRepository = UserRepository()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            EquipmentBorrowingAppTheme {

                // Auth / session state
                var currentScreen by remember { mutableStateOf<AppScreen>(AppScreen.Splash) }
                var splashFinished by remember { mutableStateOf(false) }
                var currentUserRole by remember { mutableStateOf<String?>(null) }
                var currentUserName by remember { mutableStateOf("") }
                var currentUserEmail by remember { mutableStateOf("") }
                var currentStudentId by remember { mutableStateOf("") }
                var currentDepartment by remember { mutableStateOf("") }
                var currentInstitutionId by remember { mutableStateOf("") }
                var currentVerificationStatus by remember { mutableStateOf("") }
                var currentAppUser by remember {
                    mutableStateOf<AppUser?>(null)
                }
                var roomList by remember { mutableStateOf<List<Room>>(emptyList()) }
                val equipmentViewModel = remember { EquipmentViewModel() }
                val requestViewModel = remember { RequestViewModel() }
                val labComputerViewModel = remember { LabComputerViewModel() }
                val adminRequestViewModel = remember { AdminRequestViewModel() }
                val adminEquipmentViewModel = remember { AdminEquipmentViewModel() }
                val snackbarHostState = remember { SnackbarHostState() }
                val scope = rememberCoroutineScope()
                val notificationViewModel = remember { NotificationViewModel() }
                val roomViewModel = remember { RoomViewModel() }
                var institutionList by remember { mutableStateOf<List<Institution>>(emptyList()) }
                var allInstitutionList by remember { mutableStateOf<List<Institution>>(emptyList()) }
                var isGoogleRegisterMode by remember { mutableStateOf(false) }
                var googleRegisterName by remember { mutableStateOf("") }
                var googleRegisterEmail by remember { mutableStateOf("") }
                fun showMessage(message: String) {
                    scope.launch {
                        snackbarHostState.showSnackbar(message)
                    }
                }





                var adminAllRequests by remember { mutableStateOf<List<BorrowRequest>>(emptyList()) }
                var labComputerList by remember { mutableStateOf<List<LabComputer>>(emptyList()) }
                var computerSoftwareList by remember {
                    mutableStateOf<List<ComputerSoftwareStatus>>(emptyList())
                }
                var softwareIssueReports by remember {
                    mutableStateOf<List<SoftwareIssueReport>>(emptyList())
                }

                var mySoftwareIssueReports by remember {
                    mutableStateOf<List<SoftwareIssueReport>>(emptyList())
                }
                var softwareInstallRequestList by remember {
                    mutableStateOf<List<SoftwareInstallRequest>>(emptyList())
                }
                var institutionAdminRequestList by remember {
                    mutableStateOf<List<InstitutionAdminRequest>>(emptyList())
                }
                var pendingStudentList by remember { mutableStateOf<List<AppUser>>(emptyList()) }
                var studentList by remember { mutableStateOf<List<AppUser>>(emptyList()) }
                // Selected item state
                var selectedEquipment by remember { mutableStateOf<Equipment?>(null) }
                var submittedQuantity by remember { mutableIntStateOf(1) }
                var submittedBorrowDate by remember { mutableStateOf("") }
                var submittedDueDate by remember { mutableStateOf("") }
                var submittedPurpose by remember { mutableStateOf("Lab Project") }
                var selectedLabComputer by remember { mutableStateOf<LabComputer?>(null) }
                var selectedRoom by remember { mutableStateOf<Room?>(null) }
                // Dashboard state
                var adminCounts by remember { mutableStateOf(AdminDashboardCounts()) }

                fun recalculateAdminCounts() {
                    adminCounts = AdminDashboardCounts(
                        totalRoomsCount = roomList.size,

                        totalEquipmentCount = adminEquipmentViewModel.equipmentList.size,
                        availableItemsCount = adminEquipmentViewModel.equipmentList.count {
                            it.availableQuantity > 0
                        },
                        lowStockCount = adminEquipmentViewModel.equipmentList.count {
                            it.availableQuantity in 1..2
                        },

                        pendingRequestsCount = adminAllRequests.count {
                            it.status.equals("Pending", ignoreCase = true)
                        },
                        approvedRequestsCount = adminAllRequests.count {
                            it.status.equals("Approved", ignoreCase = true)
                        },
                        issuedItemsCount = adminAllRequests.count {
                            it.status.equals("Issued", ignoreCase = true)
                        },
                        returnedItemsCount = adminAllRequests.count {
                            it.status.equals("Returned", ignoreCase = true)
                        },
                        overdueItemsCount = adminAllRequests.count {
                            it.status.equals("Overdue", ignoreCase = true)
                        },

                        pendingStudentsCount = studentList.count {
                            it.verificationStatus.equals("pending", ignoreCase = true)
                        },
                        verifiedStudentsCount = studentList.count {
                            it.verificationStatus.equals("verified", ignoreCase = true)
                        },

                        totalLabComputersCount = labComputerList.size,
                        openSoftwareIssuesCount = softwareIssueReports.count {
                            it.status.equals("Open", ignoreCase = true)
                        }
                    )
                }

                fun resetSessionState() {
                    currentUserRole = null
                    selectedEquipment = null
                    selectedLabComputer = null
                    selectedRoom = null
                    equipmentViewModel.clearEquipment()
                    adminRequestViewModel.clearAdminRequests()
                    requestViewModel.clearMyRequests()
                    adminEquipmentViewModel.clearEquipment()
                    adminAllRequests = emptyList()
                    labComputerList = emptyList()
                    computerSoftwareList = emptyList()
                    softwareIssueReports = emptyList()
                    pendingStudentList = emptyList()
                    studentList = emptyList()
                    adminCounts = AdminDashboardCounts()
                    labComputerViewModel.clearStudentLabComputers()
                    roomViewModel.clearRooms()
                    roomList = emptyList()
                    institutionList = emptyList()
                    allInstitutionList = emptyList()
                    institutionAdminRequestList = emptyList()
                    currentUserName = ""
                    currentUserEmail = ""
                    currentInstitutionId = ""
                    currentVerificationStatus = ""
                    currentAppUser = null
                }
                fun isSuperAdmin(): Boolean = currentUserRole == "super_admin"
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
                            currentStudentId = user.studentId
                            currentDepartment = user.department
                            currentUserRole = user.role.trim().lowercase()
                            currentInstitutionId = user.institutionId.trim()
                            currentVerificationStatus = user.verificationStatus.trim().lowercase()

                            when (currentUserRole) {
                                "super_admin", "admin", "student" -> {
                                    onReady?.invoke()
                                }

                                else -> {
                                    showMessage(UiMessages.UNKNOWN_ROLE)
                                    safeLogoutToLogin()
                                    notificationViewModel.clearNotifications()
                                    return@runOnUiThread
                                }
                            }

                            notificationViewModel.startListening(
                                institutionId = currentInstitutionId.trim(),
                                userId = uid.trim(),
                                role = currentUserRole ?: "student"
                            )
                        }
                    }
                }
                fun loadInstitutionsAndOpenRegister() {
                    institutionRepository.getApprovedInstitutions { list ->
                        runOnUiThread {
                            institutionList = list
                            isGoogleRegisterMode = false
                            googleRegisterName = ""
                            googleRegisterEmail = ""

                            if (institutionList.isEmpty()) {
                                showMessage("No approved institution found. Please contact admin.")
                            } else {
                                currentScreen = AppScreen.Register
                            }
                        }
                    }
                }
                fun loadAllInstitutionsAndOpenManage() {
                    institutionRepository.getAllInstitutions { list ->
                        runOnUiThread {
                            allInstitutionList = list
                            currentScreen = AppScreen.ManageInstitutions
                        }
                    }
                }

                fun updateInstitutionStatus(
                    institution: Institution,
                    status: String
                ) {
                    institutionRepository.updateInstitutionStatus(
                        institutionId = institution.id,
                        status = status
                    ) { success, message ->
                        runOnUiThread {
                            showMessage(message)

                            if (success) {
                                loadAllInstitutionsAndOpenManage()
                            }
                        }
                    }
                }
                fun createInstitution(
                    institutionId: String,
                    name: String,
                    shortName: String,
                    emailDomain: String,
                    type: String,
                    status: String
                ) {
                    val createdByUid = authRepository.getCurrentUserUid().orEmpty()

                    institutionRepository.createInstitution(
                        institutionId = institutionId,
                        name = name,
                        shortName = shortName,
                        emailDomain = emailDomain,
                        type = type,
                        status = status,
                        createdBy = createdByUid
                    ) { success, message ->
                        runOnUiThread {
                            showMessage(message)

                            if (success) {
                                loadAllInstitutionsAndOpenManage()
                            }
                        }
                    }
                }
                fun createInstitutionAdminRequest(
                    institutionId: String,
                    institutionName: String,
                    adminName: String,
                    adminEmail: String
                ) {
                    val createdByUid = authRepository.getCurrentUserUid().orEmpty()

                    institutionRepository.createInstitutionAdminRequest(
                        institutionId = institutionId,
                        institutionName = institutionName,
                        adminName = adminName,
                        adminEmail = adminEmail,
                        createdBy = createdByUid
                    ) { success, message ->
                        runOnUiThread {
                            showMessage(message)

                            if (success) {
                                currentScreen = AppScreen.SuperAdminDashboard
                            }
                        }
                    }
                }
                fun loadInstitutionAdminRequestsAndOpen() {
                    institutionRepository.getInstitutionAdminRequests { list ->
                        runOnUiThread {
                            institutionAdminRequestList = list
                            currentScreen = AppScreen.ManageInstitutionAdminRequests
                        }
                    }
                }

                fun updateInstitutionAdminRequestStatus(
                    request: InstitutionAdminRequest,
                    status: String
                ) {
                    institutionRepository.updateInstitutionAdminRequestStatus(
                        requestId = request.id,
                        status = status
                    ) { success, message ->
                        runOnUiThread {
                            showMessage(message)

                            if (success) {
                                loadInstitutionAdminRequestsAndOpen()
                            }
                        }
                    }
                }
                // Admin helpers
                fun refreshRequestsForAdmin(
                    openScreen: AppScreen? = null
                ) {
                    if (currentInstitutionId.isBlank()) {
                        showMessage("Institution not found. Please login again.")
                        return
                    }

                    requestRepository.getAllRequests(
                        institutionId = currentInstitutionId
                    ) { allRequestsList ->
                        runOnUiThread {
                            adminAllRequests = allRequestsList
                            recalculateAdminCounts()

                            adminRequestViewModel.loadPendingRequests(currentInstitutionId)
                            adminRequestViewModel.loadApprovedRequests(currentInstitutionId)

                            openScreen?.let {
                                currentScreen = it
                            }
                        }
                    }
                }

                fun refreshEquipmentForAdmin(
                    openScreen: AppScreen? = null
                ) {
                    if (currentInstitutionId.isBlank()) {
                        showMessage("Institution not found. Please login again.")
                        return
                    }

                    adminEquipmentViewModel.loadEquipment(
                        institutionId = currentInstitutionId
                    ) {
                        runOnUiThread {
                            recalculateAdminCounts()

                            openScreen?.let {
                                currentScreen = it
                            }
                        }
                    }
                }
                fun loadRoomsAndOpenAddEquipment() {
                    if (currentInstitutionId.isBlank()) {
                        showMessage("Institution not found. Please login again.")
                        return
                    }

                    roomViewModel.loadRooms(
                        institutionId = currentInstitutionId
                    ) {
                        runOnUiThread {
                            roomList = roomViewModel.roomList

                            if (roomList.isEmpty()) {
                                showMessage("Please add a room/lab first")
                                currentScreen = AppScreen.ManageRooms
                            } else {
                                currentScreen = AppScreen.AddEquipment
                            }
                        }
                    }
                }
                fun openAdminDashboardWithFreshData() {
                    refreshEquipmentForAdmin()
                    refreshRequestsForAdmin(AppScreen.AdminDashboard)
                }
                fun refreshLabComputersAndOpenManage() {
                    if (currentInstitutionId.isBlank()) {
                        showMessage("Institution not found. Please login again.")
                        return
                    }

                    labComputerRepository.getLabComputers(
                        institutionId = currentInstitutionId
                    ) { list ->
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
                    if (currentInstitutionId.isBlank()) {
                        showMessage("Institution not found. Please login again.")
                        return
                    }

                    adminEquipmentViewModel.loadEquipment(
                        institutionId = currentInstitutionId
                    ) {
                        requestRepository.getAllRequests(
                            institutionId = currentInstitutionId
                        ) { allRequestsResult ->
                            userRepository.getAllStudents(
                                institutionId = currentInstitutionId
                            ) { allStudentsResult ->
                                labComputerRepository.getLabComputers(
                                    institutionId = currentInstitutionId
                                ) { allLabComputersResult ->
                                    labComputerRepository.getSoftwareIssueReports(
                                        institutionId = currentInstitutionId
                                    ) { allSoftwareReportsResult ->
                                        roomViewModel.loadRooms(
                                            institutionId = currentInstitutionId
                                        ) {
                                            runOnUiThread {
                                                adminAllRequests = allRequestsResult
                                                studentList = allStudentsResult
                                                labComputerList = allLabComputersResult
                                                softwareIssueReports = allSoftwareReportsResult
                                                roomList = roomViewModel.roomList

                                                recalculateAdminCounts()

                                                if (refreshPending) {
                                                    adminRequestViewModel.loadPendingRequests(currentInstitutionId)
                                                }

                                                if (refreshApproved) {
                                                    adminRequestViewModel.loadApprovedRequests(currentInstitutionId)
                                                }

                                                onComplete?.invoke()
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                fun openDashboardAfterLogin() {
                    loadLoggedInUserRole {
                        when (currentUserRole) {
                            "super_admin" -> {
                                currentScreen = AppScreen.SuperAdminDashboard
                            }

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
                                        if (message == "GOOGLE_PROFILE_REQUIRED") {
                                            googleRegisterName = googleIdTokenCredential.displayName ?: ""
                                            googleRegisterEmail = googleIdTokenCredential.id

                                            institutionRepository.getApprovedInstitutions { list ->
                                                runOnUiThread {
                                                    institutionList = list

                                                    if (institutionList.isEmpty()) {
                                                        showMessage("No approved institution found. Please contact admin.")
                                                    } else {
                                                        isGoogleRegisterMode = true
                                                        currentScreen = AppScreen.Register
                                                    }
                                                }
                                            }
                                        } else {
                                            showMessage(message)

                                            if (success) {
                                                openDashboardAfterLogin()
                                            }
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
                    if (currentInstitutionId.isBlank()) {
                        showMessage("Institution not found. Please login again.")
                        return
                    }

                    roomViewModel.loadRooms(
                        institutionId = currentInstitutionId
                    ) {
                        adminEquipmentViewModel.loadEquipment(
                            institutionId = currentInstitutionId
                        ) {
                            runOnUiThread {
                                roomList = roomViewModel.roomList
                                currentScreen = AppScreen.ManageEquipment
                            }
                        }
                    }
                }

                fun loadPendingRequestsAndOpen() {
                    if (currentInstitutionId.isBlank()) {
                        showMessage("Institution not found. Please login again.")
                        return
                    }

                    adminRequestViewModel.loadPendingRequests(
                        institutionId = currentInstitutionId
                    ) {
                        runOnUiThread {
                            currentScreen = AppScreen.PendingRequests
                        }
                    }
                }

                fun loadApprovedRequestsAndOpen() {
                    if (currentInstitutionId.isBlank()) {
                        showMessage("Institution not found. Please login again.")
                        return
                    }

                    adminRequestViewModel.loadApprovedRequests(
                        institutionId = currentInstitutionId
                    ) {
                        runOnUiThread {
                            currentScreen = AppScreen.ApprovedRequests
                        }
                    }
                }

                fun loadLabComputersForAdmin() {
                    if (currentInstitutionId.isBlank()) {
                        showMessage("Institution not found. Please login again.")
                        return
                    }

                    labComputerRepository.getLabComputers(
                        institutionId = currentInstitutionId
                    ) { list ->
                        runOnUiThread {
                            labComputerList = list
                            currentScreen = AppScreen.ManageLabComputers
                        }
                    }
                }
                fun loadAllSoftwareReportsAndOpen() {
                    if (currentInstitutionId.isBlank()) {
                        showMessage("Institution not found. Please login again.")
                        return
                    }

                    labComputerRepository.getSoftwareIssueReports(
                        institutionId = currentInstitutionId
                    ) { list ->
                        runOnUiThread {
                            selectedLabComputer = null
                            softwareIssueReports = list
                            currentScreen = AppScreen.SoftwareIssueReports
                        }
                    }
                }
                fun loadSoftwareInstallRequests() {
                    if (currentInstitutionId.isBlank()) {
                        showMessage("Institution not found. Please login again.")
                        return
                    }

                    labComputerRepository.getAllSoftwareInstallRequests(
                        institutionId = currentInstitutionId
                    ) { list ->
                        runOnUiThread {
                            softwareInstallRequestList = list
                            currentScreen = AppScreen.SoftwareInstallRequestsAdmin
                        }
                    }
                }
                fun loadPendingStudentsAndOpen() {
                    if (currentInstitutionId.isBlank()) {
                        showMessage("Institution not found. Please login again.")
                        return
                    }

                    userRepository.getPendingStudents(
                        institutionId = currentInstitutionId
                    ) { list ->
                        runOnUiThread {
                            pendingStudentList = list
                            currentScreen = AppScreen.PendingStudents
                        }
                    }
                }
                fun loadStudentsAndOpenManage() {
                    if (currentInstitutionId.isBlank()) {
                        showMessage("Institution not found. Please login again.")
                        return
                    }

                    userRepository.getAllStudents(
                        institutionId = currentInstitutionId
                    ) { list ->
                        runOnUiThread {
                            studentList = list
                            currentScreen = AppScreen.ManageStudents
                        }
                    }
                }

                fun approveStudent(student: AppUser) {
                    userRepository.approveStudent(student.uid) { success, message ->
                        runOnUiThread {
                            showMessage(message)

                            if (success) {
                                notificationRepository.sendNotificationToStudent(
                                    institutionId = currentInstitutionId,
                                    studentUserId = student.uid,
                                    title = "Account Verified",
                                    message = "Your student account has been verified. You can now borrow equipment.",
                                    type = "success"
                                )

                                loadPendingStudentsAndOpen()
                            }
                        }
                    }
                }

                fun rejectStudent(student: AppUser) {
                    userRepository.rejectStudent(student.uid) { success, message ->
                        runOnUiThread {
                            showMessage(message)

                            if (success) {
                                notificationRepository.sendNotificationToStudent(
                                    institutionId = currentInstitutionId,
                                    studentUserId = student.uid,
                                    title = "Account Rejected",
                                    message = "Your student verification request has been rejected.",
                                    type = "error"
                                )

                                loadPendingStudentsAndOpen()
                            }
                        }
                    }
                }
                fun updateStudentStatus(student: AppUser, status: String) {
                    userRepository.updateStudentVerificationStatus(
                        studentUid = student.uid,
                        status = status
                    ) { success, message ->
                        runOnUiThread {
                            showMessage(message)

                            if (success) {
                                val notificationTitle = when (status.lowercase()) {
                                    "verified" -> "Account Verified"
                                    "rejected" -> "Account Rejected"
                                    "suspended" -> "Account Suspended"
                                    "pending" -> "Account Set to Pending"
                                    "blocked" -> "Account Blocked"
                                    else -> "Account Status Updated"
                                }

                                val notificationMessage = when (status.lowercase()) {
                                    "verified" -> "Your student account has been verified. You can now borrow equipment."
                                    "rejected" -> "Your student verification request has been rejected."
                                    "suspended" -> "Your student account has been suspended. Please contact your admin."
                                    "pending" -> "Your student account has been moved back to pending verification."
                                    "blocked" -> "Your student account has been blocked. Please contact your admin."
                                    else -> "Your student account status has been updated."
                                }

                                val notificationType = when (status.lowercase()) {
                                    "verified" -> "success"
                                    "rejected" -> "error"
                                    "suspended" -> "warning"
                                    "blocked" -> "warning"
                                    else -> "info"
                                }

                                notificationRepository.sendNotificationToStudent(
                                    institutionId = currentInstitutionId,
                                    studentUserId = student.uid,
                                    title = notificationTitle,
                                    message = notificationMessage,
                                    type = notificationType
                                )

                                loadStudentsAndOpenManage()
                            }
                        }
                    }
                }
                // Request action helpers
                // Request action helpers
                fun handleApproveRequest(request: BorrowRequest) {
                    requestRepository.approveRequest(
                        request = request,
                        approvedBy = authRepository.getCurrentUserUid().orEmpty()
                    ) { success, message ->
                        runOnUiThread {
                            showMessage(
                                if (success) UiMessages.REQUEST_APPROVED else message
                            )

                            if (success) {
                                notificationRepository.sendNotificationToStudent(
                                    institutionId = currentInstitutionId,
                                    studentUserId = request.userId,
                                    title = "Request Approved",
                                    message = "Your request for ${request.equipmentName} has been approved.",
                                    type = "success"
                                )

                                refreshAdminDashboardData(
                                    refreshPending = true,
                                    refreshApproved = true
                                ) {
                                    currentScreen = AppScreen.PendingRequests
                                }
                            }
                        }
                    }
                }

                fun handleRejectRequest(request: BorrowRequest) {
                    requestRepository.rejectRequest(
                        request = request,
                        rejectedReason = "Rejected by admin",
                        adminNote = ""
                    ) { success, message ->
                        runOnUiThread {
                            showMessage(
                                if (success) UiMessages.REQUEST_REJECTED else message
                            )

                            if (success) {
                                notificationRepository.sendNotificationToStudent(
                                    institutionId = currentInstitutionId,
                                    studentUserId = request.userId,
                                    title = "Request Rejected",
                                    message = "Your request for ${request.equipmentName} has been rejected.",
                                    type = "error"
                                )

                                refreshAdminDashboardData(
                                    refreshPending = true,
                                    refreshApproved = true
                                ) {
                                    currentScreen = AppScreen.PendingRequests
                                }
                            }
                        }
                    }
                }

                fun handleIssueRequest(request: BorrowRequest) {
                    requestRepository.markRequestIssued(
                        request = request,
                        issuedBy = authRepository.getCurrentUserUid().orEmpty(),
                        adminNote = ""
                    ) { success, message ->
                        runOnUiThread {
                            showMessage(
                                if (success) "Request marked as issued" else message
                            )

                            if (success) {
                                notificationRepository.sendNotificationToStudent(
                                    institutionId = currentInstitutionId,
                                    studentUserId = request.userId,
                                    title = "Item Issued",
                                    message = "${request.equipmentName} has been issued to you.",
                                    type = "success"
                                )

                                refreshAdminDashboardData(
                                    refreshPending = true,
                                    refreshApproved = true
                                ) {
                                    currentScreen = AppScreen.ApprovedRequests
                                }
                            }
                        }
                    }
                }

                fun handleReturnRequest(request: BorrowRequest) {
                    requestRepository.markRequestReturned(
                        request = request,
                        returnedBy = authRepository.getCurrentUserUid().orEmpty(),
                        returnCondition = "Good",
                        adminNote = ""
                    ) { success, message ->
                        runOnUiThread {
                            showMessage(
                                if (success) UiMessages.REQUEST_RETURNED else message
                            )

                            if (success) {
                                notificationRepository.sendNotificationToStudent(
                                    institutionId = currentInstitutionId,
                                    studentUserId = request.userId,
                                    title = "Item Returned",
                                    message = "${request.equipmentName} has been marked as returned.",
                                    type = "info"
                                )

                                refreshAdminDashboardData(
                                    refreshPending = true,
                                    refreshApproved = true
                                ) {
                                    currentScreen = AppScreen.ApprovedRequests
                                }
                            }
                        }
                    }
                }

                fun handleLostRequest(
                    request: BorrowRequest,
                    fineAmount: Int,
                    fineReason: String
                ) {
                    requestRepository.markRequestLost(
                        request = request,
                        adminNote = "Marked as lost by admin",
                        fineAmount = fineAmount,
                        fineReason = fineReason
                    ) { success, message ->
                        runOnUiThread {
                            showMessage(
                                if (success) "Request marked as lost" else message
                            )

                            if (success) {
                                val fineText = if (fineAmount > 0) {
                                    " Fine: $fineAmount taka."
                                } else {
                                    ""
                                }

                                notificationRepository.sendNotificationToStudent(
                                    institutionId = currentInstitutionId,
                                    studentUserId = request.userId,
                                    title = "Item Marked as Lost",
                                    message = "${request.equipmentName} has been marked as lost.$fineText Please contact your admin.",
                                    type = "error"
                                )

                                refreshAdminDashboardData(
                                    refreshPending = true,
                                    refreshApproved = true
                                ) {
                                    currentScreen = AppScreen.ApprovedRequests
                                }
                            }
                        }
                    }
                }

                fun handleDamagedRequest(
                    request: BorrowRequest,
                    fineAmount: Int,
                    fineReason: String
                ) {
                    requestRepository.markRequestDamaged(
                        request = request,
                        adminNote = "Marked as damaged by admin",
                        fineAmount = fineAmount,
                        fineReason = fineReason
                    ) { success, message ->
                        runOnUiThread {
                            showMessage(
                                if (success) "Request marked as damaged" else message
                            )

                            if (success) {
                                val fineText = if (fineAmount > 0) {
                                    " Fine: $fineAmount taka."
                                } else {
                                    ""
                                }

                                notificationRepository.sendNotificationToStudent(
                                    institutionId = currentInstitutionId,
                                    studentUserId = request.userId,
                                    title = "Item Marked as Damaged",
                                    message = "${request.equipmentName} has been marked as damaged.$fineText Please contact your admin.",
                                    type = "warning"
                                )

                                refreshAdminDashboardData(
                                    refreshPending = true,
                                    refreshApproved = true
                                ) {
                                    currentScreen = AppScreen.ApprovedRequests
                                }
                            }
                        }
                    }
                }
                fun handleFinePaid(request: BorrowRequest) {
                    requestRepository.markFinePaid(
                        requestId = request.requestId
                    ) { success, message ->
                        runOnUiThread {
                            showMessage(
                                if (success) "Fine marked as paid" else message
                            )

                            if (success) {
                                notificationRepository.sendNotificationToStudent(
                                    institutionId = currentInstitutionId,
                                    studentUserId = request.userId,
                                    title = "Fine Paid",
                                    message = "Your fine for ${request.equipmentName} has been marked as paid.",
                                    type = "success"
                                )

                                refreshAdminDashboardData(
                                    refreshPending = true,
                                    refreshApproved = true
                                ) {
                                    currentScreen = AppScreen.ApprovedRequests
                                }
                            }
                        }
                    }
                }

                fun handleFineWaived(request: BorrowRequest) {
                    requestRepository.waiveFine(
                        requestId = request.requestId,
                        waiveReason = "Fine waived by admin"
                    ) { success, message ->
                        runOnUiThread {
                            showMessage(
                                if (success) "Fine waived successfully" else message
                            )

                            if (success) {
                                notificationRepository.sendNotificationToStudent(
                                    institutionId = currentInstitutionId,
                                    studentUserId = request.userId,
                                    title = "Fine Waived",
                                    message = "Your fine for ${request.equipmentName} has been waived by admin.",
                                    type = "success"
                                )

                                refreshAdminDashboardData(
                                    refreshPending = true,
                                    refreshApproved = true
                                ) {
                                    currentScreen = AppScreen.ApprovedRequests
                                }
                            }
                        }
                    }
                }
                fun loadRoomsAndOpenManage() {
                    if (currentInstitutionId.isBlank()) {
                        showMessage("Institution not found. Please login again.")
                        return
                    }

                    roomViewModel.loadRooms(
                        institutionId = currentInstitutionId
                    ) {
                        runOnUiThread {
                            roomList = roomViewModel.roomList
                            currentScreen = AppScreen.ManageRooms
                        }
                    }
                }
                fun loadRoomsAndOpenAddLabComputer() {
                    if (currentInstitutionId.isBlank()) {
                        showMessage("Institution not found. Please login again.")
                        return
                    }

                    roomViewModel.loadRooms(
                        institutionId = currentInstitutionId
                    ) {
                        runOnUiThread {
                            roomList = roomViewModel.roomList

                            if (roomList.isEmpty()) {
                                showMessage("Please add a room/lab first")
                                currentScreen = AppScreen.ManageRooms
                            } else {
                                currentScreen = AppScreen.AddLabComputer
                            }
                        }
                    }
                }
                fun openReportsDashboard() {
                    refreshAdminDashboardData(
                        refreshPending = true,
                        refreshApproved = true
                    ) {
                        currentScreen = AppScreen.ReportsDashboard
                    }
                }
// Student helpers

                fun loadStudentRequestsAndOpenMyRequests() {
                    val uid = authRepository.getCurrentUserUid()

                    if (uid == null) {
                        showMessage(UiMessages.USER_NOT_LOGGED_IN)
                        safeLogoutToLogin()
                        return
                    }

                    if (currentInstitutionId.isBlank()) {
                        showMessage("Institution not found. Please login again.")
                        return
                    }

                    requestViewModel.loadUserRequests(
                        institutionId = currentInstitutionId,
                        userId = uid
                    ) {
                        runOnUiThread {
                            currentScreen = AppScreen.MyRequests
                        }
                    }
                }
                fun loadRoomsAndOpenStudentRoomSelection() {
                    if (currentInstitutionId.isBlank()) {
                        showMessage("Institution not found. Please login again.")
                        return
                    }

                    roomViewModel.loadRooms(
                        institutionId = currentInstitutionId
                    ) {
                        runOnUiThread {
                            roomList = roomViewModel.roomList

                            if (roomList.isEmpty()) {
                                showMessage("No room/lab found")
                            } else {
                                currentScreen = AppScreen.RoomSelection
                            }
                        }
                    }
                }
                fun loadStudentEquipmentAndOpenList(room: Room) {
                    if (currentInstitutionId.isBlank()) {
                        showMessage("Institution not found. Please login again.")
                        return
                    }

                    if (room.id.isBlank()) {
                        showMessage("Room ID missing. Please update RoomRepository getRooms()")
                        return
                    }

                    selectedRoom = room

                    equipmentViewModel.loadEquipment(
                        institutionId = currentInstitutionId,
                        roomId = room.id
                    ) {
                        runOnUiThread {
                            currentScreen = AppScreen.EquipmentList
                        }
                    }
                }

                fun loadLabComputersForStudent() {
                    if (currentInstitutionId.isBlank()) {
                        showMessage("Institution not found. Please login again.")
                        return
                    }

                    labComputerViewModel.loadStudentLabComputers(
                        institutionId = currentInstitutionId
                    ) {
                        labComputerRepository.getAllSoftwareStatusForInstitution(
                            institutionId = currentInstitutionId
                        ) { list ->
                            runOnUiThread {
                                computerSoftwareList = list
                                currentScreen = AppScreen.LabComputerList
                            }
                        }
                    }
                }
                fun loadMySoftwareIssuesAndOpen() {
                    val uid = authRepository.getCurrentUserUid()

                    if (uid == null) {
                        showMessage(UiMessages.USER_NOT_LOGGED_IN)
                        safeLogoutToLogin()
                        return
                    }

                    labComputerRepository.getStudentSoftwareIssueReports(
                        institutionId = currentInstitutionId.trim(),
                        userId = uid.trim()
                    ) { list ->
                        runOnUiThread {
                            mySoftwareIssueReports = list
                            currentScreen = AppScreen.MySoftwareIssues

                        }
                    }
                }
                fun handleNotificationNavigation(notification: AppNotification) {
                    val type = notification.type.trim().lowercase()
                    val title = notification.title.trim().lowercase()
                    val message = notification.message.trim().lowercase()
                    val combinedText = "$type $title $message"

                    fun containsAny(vararg keywords: String): Boolean {
                        return keywords.any { keyword -> combinedText.contains(keyword) }
                    }

                    when {
                        type in listOf(
                            "request_approved",
                            "request_rejected",
                            "request_issued",
                            "request_returned",
                            "request_lost",
                            "request_damaged",
                            "fine_paid",
                            "fine_waived"
                        ) || containsAny(
                            "request approved",
                            "request rejected",
                            "item issued",
                            "item returned",
                            "marked as lost",
                            "marked as damaged",
                            "fine paid",
                            "fine waived"
                        ) -> {
                            loadStudentRequestsAndOpenMyRequests()
                        }

                        type in listOf(
                            "software_issue_solved",
                            "software_issue_rejected"
                        ) || containsAny(
                            "software issue solved",
                            "software issue rejected"
                        ) -> {
                            loadMySoftwareIssuesAndOpen()
                        }
                        type == "software_install_request" || containsAny("software install request", "new software install request") -> {
                            loadSoftwareInstallRequests()
                        }
                        type == "new_borrow_request" || containsAny("new borrow request", "requested") -> {
                            loadPendingRequestsAndOpen()
                        }

                        type == "student_registered" || containsAny("student registered", "new student") -> {
                            loadPendingStudentsAndOpen()
                        }

                        type == "software_issue" || containsAny("new software issue", "reported issue") -> {
                            loadAllSoftwareReportsAndOpen()
                        }

                        type == "low_stock" || containsAny("low stock") -> {
                            currentScreen = AppScreen.LowStockReport
                        }

                        type == "overdue_request" || containsAny("overdue") -> {
                            currentScreen = AppScreen.OverdueItemReport
                        }

                        type == "lost_damaged" || containsAny("lost/damaged", "lost damaged") -> {
                            currentScreen = AppScreen.LostDamagedReport
                        }

                        else -> {
                            currentScreen = when {
                                isSuperAdmin() -> AppScreen.SuperAdminDashboard
                                isAdmin() -> AppScreen.AdminDashboard
                                else -> AppScreen.StudentDashboard
                            }
                        }
                    }
                }
                fun loadStudentProfileAndOpen() {
                    val uid = authRepository.getCurrentUserUid()

                    if (uid.isNullOrBlank()) {
                        showMessage(UiMessages.USER_NOT_LOGGED_IN)
                        safeLogoutToLogin()
                        return
                    }

                    userRepository.getUserById(uid) { user ->
                        runOnUiThread {
                            if (user == null) {
                                showMessage("Profile not found")
                                return@runOnUiThread
                            }

                            currentAppUser = user
                            currentUserName = user.name
                            currentUserEmail = user.email
                            currentInstitutionId = user.institutionId.trim()
                            currentVerificationStatus = user.verificationStatus.trim().lowercase()
                            currentScreen = AppScreen.StudentProfile
                        }
                    }
                }
                fun loadAdminProfileAndOpen() {
                    val uid = authRepository.getCurrentUserUid()

                    if (uid.isNullOrBlank()) {
                        showMessage(UiMessages.USER_NOT_LOGGED_IN)
                        safeLogoutToLogin()
                        return
                    }

                    userRepository.getUserById(uid) { user ->
                        runOnUiThread {
                            if (user == null) {
                                showMessage("Profile not found")
                                return@runOnUiThread
                            }

                            currentAppUser = user
                            currentUserName = user.name
                            currentUserEmail = user.email
                            currentInstitutionId = user.institutionId.trim()
                            currentScreen = AppScreen.AdminProfile
                        }
                    }
                }

                fun handleUpdateAdminProfile(
                    phone: String,
                    profileImageUrl: String
                ) {
                    val uid = authRepository.getCurrentUserUid()

                    if (uid.isNullOrBlank()) {
                        showMessage(UiMessages.USER_NOT_LOGGED_IN)
                        safeLogoutToLogin()
                        return
                    }

                    userRepository.updateAdminProfile(
                        userId = uid,
                        phone = phone,
                        profileImageUrl = profileImageUrl
                    ){ success, message ->
                        runOnUiThread {
                            showMessage(message)

                            if (success) {
                                loadAdminProfileAndOpen()
                            }
                        }
                    }
                }
                fun handleUpdateStudentProfile(
                    phone: String,
                    profileImageUrl: String
                )  {
                    val uid = authRepository.getCurrentUserUid()

                    if (uid.isNullOrBlank()) {
                        showMessage(UiMessages.USER_NOT_LOGGED_IN)
                        safeLogoutToLogin()
                        return
                    }

                    userRepository.updateStudentProfile(
                        userId = uid,
                        phone = phone,
                        profileImageUrl = profileImageUrl
                    ){ success, message ->
                        runOnUiThread {
                            showMessage(message)

                            if (success) {
                                loadStudentProfileAndOpen()
                            }
                        }
                    }
                }

                fun sendProfilePasswordReset(email: String) {
                    if (email.isBlank()) {
                        showMessage("Email not found")
                        return
                    }

                    authRepository.sendPasswordResetEmail(email) { _, message ->
                        runOnUiThread {
                            showMessage(message)
                        }
                    }
                }
                fun handleStudentSoftwareIssueFeedback(
                    report: SoftwareIssueReport,
                    feedback: String
                ) {
                    if (report.id.isBlank()) {
                        showMessage("Invalid issue report")
                        return
                    }

                    if (feedback.isBlank()) {
                        showMessage("Feedback is required")
                        return
                    }

                    labComputerRepository.addStudentFeedbackToIssueReport(
                        reportId = report.id,
                        studentFeedback = feedback
                    ) { success, message ->
                        runOnUiThread {
                            showMessage(message)

                            if (success) {
                                loadMySoftwareIssuesAndOpen()
                            }
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
                        onIssuedClick = { request ->
                            handleIssueRequest(request)
                        },
                        onReturnedClick = { request ->
                            handleReturnRequest(request)
                        },onLostClick = { request, fineAmount, fineReason ->
                            handleLostRequest(
                                request = request,
                                fineAmount = fineAmount,
                                fineReason = fineReason
                            )
                        },
                        onDamagedClick = { request, fineAmount, fineReason ->
                            handleDamagedRequest(
                                request = request,
                                fineAmount = fineAmount,
                                fineReason = fineReason
                            )
                        },
                        onFinePaidClick = { request ->
                            handleFinePaid(request)
                        },
                        onFineWaivedClick = { request ->
                            handleFineWaived(request)
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
                                LaunchedEffect(currentScreen, currentInstitutionId) {
                                    val uid = authRepository.getCurrentUserUid()

                                    if (
                                        currentScreen == AppScreen.StudentDashboard &&
                                        uid != null &&
                                        currentInstitutionId.isNotBlank()
                                    ) {
                                        requestViewModel.loadUserRequests(
                                            institutionId = currentInstitutionId,
                                            userId = uid
                                        )
                                    }
                                }

                                StudentDashboardScreen(
                                    recentRequests = requestViewModel.myRequests
                                        .sortedByDescending { request -> request.requestTimestamp },
                                    onViewEquipmentClick = {
                                        loadRoomsAndOpenStudentRoomSelection()
                                    },
                                    onMyRequestsClick = {
                                        loadStudentRequestsAndOpenMyRequests()
                                    },
                                    onLabComputersClick = {
                                        loadLabComputersForStudent()
                                    },
                                    onMySoftwareIssuesClick = {
                                        loadMySoftwareIssuesAndOpen()
                                    },
                                    hasUnreadNotifications = notificationViewModel.notificationList.any {
                                        !it.read
                                    },
                                    onProfileClick = {
                                        loadStudentProfileAndOpen()
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
                        AppScreen.RoomSelection -> {
                            if (!isStudent()) {
                                redirectUnauthorized(AppScreen.RoomSelection)
                            } else {
                                RoomSelectionScreen(
                                    roomList = roomList,
                                    onRoomClick = { room ->
                                        loadStudentEquipmentAndOpenList(room)
                                    },
                                    onBackClick = {
                                        currentScreen = AppScreen.StudentDashboard
                                    }
                                )
                            }
                        }
                        AppScreen.StudentProfile -> {
                            if (!isStudent()) {
                                redirectUnauthorized(AppScreen.StudentProfile)
                            } else {
                                StudentProfileScreen(
                                    user = currentAppUser,
                                    onSaveClick = { phone, profileImageUrl ->
                                        handleUpdateStudentProfile(
                                            phone = phone,
                                            profileImageUrl = profileImageUrl
                                        )
                                    },
                                    onPasswordResetClick = { email ->
                                        sendProfilePasswordReset(email)
                                    },
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
                                                selectedRoom?.let { room ->
                                                    loadStudentEquipmentAndOpenList(room)
                                                } ?: run {
                                                    loadRoomsAndOpenStudentRoomSelection()
                                                }
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
                                                    currentScreen = AppScreen.RoomSelection
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
                                        onSubmitClick = { quantity, borrowDate, dueDate, purpose ->
                                            when {
                                                !currentVerificationStatus.equals("verified", ignoreCase = true) ->  {
                                                    showMessage("Your account is not verified yet")
                                                }
                                                equipment.id.isBlank() -> {
                                                    showMessage(UiMessages.EQUIPMENT_NOT_FOUND)
                                                }

                                                (!equipment.isBorrowable || equipment.borrowType == "LabUseOnly") -> {
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
                                                                }
                                                                else {
                                                                    val selectedRoomId = selectedRoom?.id
                                                                        ?.takeIf { it.isNotBlank() }
                                                                        ?: equipment.roomId

                                                                    if (selectedRoomId.isBlank()) {
                                                                        showMessage("No lab room selected. Please select a room/lab again.")
                                                                        currentScreen = AppScreen.RoomSelection
                                                                        return@runOnUiThread
                                                                    }
                                                                    requestRepository.submitBorrowRequest(
                                                                        institutionId = currentInstitutionId,
                                                                        roomId = selectedRoomId,
                                                                        userId = uid,
                                                                        userName = userName,
                                                                        userEmail = currentUserEmail,
                                                                        studentId = currentStudentId,
                                                                        department = currentDepartment,
                                                                        equipmentId = equipment.id,
                                                                        equipmentName = equipment.name.ifBlank {
                                                                            UiMessages.UNKNOWN_EQUIPMENT
                                                                        },
                                                                        equipmentCategory = equipment.category,
                                                                        equipmentImageName = equipment.imageName,
                                                                        equipmentImageUrl = equipment.imageUrl,
                                                                        quantity = quantity,
                                                                        borrowDate = borrowDate,
                                                                        dueDate = dueDate,
                                                                        purpose = purpose
                                                                    ) { success, message ->
                                                                        runOnUiThread {
                                                                            showMessage(
                                                                                if (success) UiMessages.REQUEST_SUBMITTED else message
                                                                            )

                                                                            if (success) {
                                                                                notificationRepository.sendNotificationToInstitutionAdmins(
                                                                                    institutionId = currentInstitutionId,
                                                                                    title = "New Borrow Request",
                                                                                    message = "$userName (${currentStudentId.ifBlank { "No ID" }} • ${currentDepartment.ifBlank { "No Department" }}) requested ${equipment.name}",
                                                                                    type = "warning"
                                                                                )
                                                                                equipmentViewModel.loadEquipment(
                                                                                    institutionId = currentInstitutionId,
                                                                                    roomId = selectedRoomId
                                                                                ) {
                                                                                    runOnUiThread {
                                                                                        submittedQuantity = quantity
                                                                                        submittedBorrowDate = borrowDate
                                                                                        submittedDueDate = dueDate
                                                                                        submittedPurpose = purpose.ifBlank { "Lab Project" }

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
                                                softwareStatusList = computerSoftwareList,
                                                onInstallRequestClick = { computer ->
                                                    selectedLabComputer = computer
                                                    currentScreen = AppScreen.SoftwareInstallRequest
                                                },
                                                onReportClick = { computer ->
                                                    selectedLabComputer = computer

                                                    labComputerRepository.getSoftwareStatusForComputer(
                                                        institutionId = currentInstitutionId,
                                                        computerId = computer.id
                                                    ) { list ->
                                                        runOnUiThread {
                                                            computerSoftwareList = computerSoftwareList
                                                                .filterNot { it.computerId == computer.id } + list
                                                            currentScreen = AppScreen.ReportSoftwareIssue
                                                        }
                                                    }
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
                        AppScreen.SoftwareInstallRequest -> {
                            if (!isStudent()) {
                                redirectUnauthorized(AppScreen.SoftwareInstallRequest)
                            } else {
                                val computer = selectedLabComputer

                                if (computer != null) {
                                    SoftwareInstallRequestScreen(
                                        computer = computer,
                                        studentName = currentUserName,
                                        studentId = currentStudentId,
                                        department = currentDepartment,
                                        onSubmitClick = { softwareName, version, softwareLogoUrl, reason ->
                                            val uid = authRepository.getCurrentUserUid()

                                            if (uid.isNullOrBlank()) {
                                                showMessage(UiMessages.USER_NOT_LOGGED_IN)
                                                safeLogoutToLogin()
                                            } else {
                                                val request = SoftwareInstallRequest(
                                                    institutionId = currentInstitutionId,
                                                    computerId = computer.id,
                                                    computerName = computer.pcName,
                                                    computerImageUrl = computer.computerImageUrl,
                                                    softwareName = softwareName,
                                                    version = version,
                                                    softwareLogoUrl = softwareLogoUrl,
                                                    requestedByUserId = uid,
                                                    requestedByUserName = currentUserName,
                                                    requestedByStudentId = currentStudentId,
                                                    requestedByDepartment = currentDepartment,
                                                    requestedByEmail = currentUserEmail,
                                                    reason = reason
                                                )

                                                labComputerRepository.submitSoftwareInstallRequest(
                                                    request = request
                                                ) { success, message ->
                                                    runOnUiThread {
                                                        showMessage(message)

                                                        if (success) {
                                                            notificationRepository.sendNotificationToInstitutionAdmins(
                                                                institutionId = currentInstitutionId,
                                                                title = "New Software Install Request",
                                                                message = "${currentUserName.ifBlank { "Student" }} (${currentStudentId.ifBlank { "No ID" }} • ${currentDepartment.ifBlank { "No Department" }}) requested $softwareName for ${computer.pcName}",
                                                                type = "software_install_request"
                                                            )

                                                            currentScreen = AppScreen.LabComputerList
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
                        AppScreen.ReportSoftwareIssue -> {
                            if (!isStudent()) {
                                redirectUnauthorized(AppScreen.ReportSoftwareIssue)
                            } else {
                                val computer = selectedLabComputer

                                if (computer != null) {
                                    ReportSoftwareIssueScreen(
                                        computer = computer,
                                        softwareList = computerSoftwareList.filter { it.computerId == computer.id },
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
                                                        } else {labComputerRepository.submitSoftwareIssueReport(
                                                            institutionId = currentInstitutionId,
                                                            roomId = computer.roomId,
                                                            computerId = computer.id,
                                                            computerName = computer.pcName,
                                                            computerImageUrl = computer.computerImageUrl,
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
                                                                    notificationRepository.sendNotificationToInstitutionAdmins(
                                                                        institutionId = currentInstitutionId,
                                                                        title = "New Software Issue",
                                                                        message = "$userName reported issue in $softwareName on ${computer.pcName}",
                                                                        type = "warning"
                                                                    )

                                                                    loadMySoftwareIssuesAndOpen()
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
                        AppScreen.MySoftwareIssues -> {
                            if (!isStudent()) {
                                redirectUnauthorized(AppScreen.MySoftwareIssues)
                            } else {
                                MySoftwareIssuesScreen(
                                    issueList = mySoftwareIssueReports,
                                    onRefreshClick = {
                                        loadMySoftwareIssuesAndOpen()
                                    },
                                    onFeedbackSubmitClick = { report, feedback ->
                                        handleStudentSoftwareIssueFeedback(report, feedback)
                                    },
                                    onBackClick = {
                                        currentScreen = AppScreen.StudentDashboard
                                    }
                                )
                            }
                        }
                        else -> Unit
                    }
                }
                LaunchedEffect(splashFinished) {
                    if (!splashFinished) return@LaunchedEffect

                    if (authRepository.isUserLoggedIn()) {
                        loadLoggedInUserRole {
                            currentScreen = when (currentUserRole) {
                                "super_admin" -> AppScreen.SuperAdminDashboard
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

                    if (
                        currentScreen == AppScreen.Splash ||
                        currentScreen == AppScreen.Login ||
                        currentScreen == AppScreen.Register
                    ) return@LaunchedEffect

                    if (!authRepository.isUserLoggedIn()) {
                        showMessage(UiMessages.LOGIN_REQUIRED)
                        safeLogoutToLogin()
                        return@LaunchedEffect
                    }
                    if (isSuperAdminScreen(currentScreen) && !isSuperAdmin()) {
                        showMessage(UiMessages.ACCESS_DENIED)

                        currentScreen = when {
                            isAdmin() -> AppScreen.AdminDashboard
                            isStudent() -> AppScreen.StudentDashboard
                            else -> AppScreen.Login
                        }

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

                        }

                        AppScreen.Register -> {
                            currentScreen = AppScreen.Login
                        }
                        AppScreen.Notifications -> {
                            currentScreen = when {
                                isSuperAdmin() -> AppScreen.SuperAdminDashboard
                                isAdmin() -> AppScreen.AdminDashboard
                                else -> AppScreen.StudentDashboard
                            }
                        }
                        AppScreen.SuperAdminDashboard -> {
                            safeLogoutToLogin()
                        }

                        AppScreen.ManageInstitutions,
                        AppScreen.CreateInstitution,
                        AppScreen.CreateInstitutionAdmin,
                        AppScreen.ManageInstitutionAdminRequests -> {
                            currentScreen = AppScreen.SuperAdminDashboard
                        }
                        AppScreen.AdminProfile -> {
                            // future use
                            currentScreen = AppScreen.AdminDashboard
                        }
                        AppScreen.ManageRooms -> {
                            currentScreen = AppScreen.AdminDashboard
                        }
                        AppScreen.PendingStudents,
                        AppScreen.ManageStudents -> {
                            currentScreen = AppScreen.AdminDashboard
                        }
                        AppScreen.AddRoom -> {
                            currentScreen = AppScreen.ManageRooms
                        }
                        AppScreen.StudentProfile -> {
                            currentScreen = AppScreen.StudentDashboard
                        }
                        AppScreen.StudentDashboard -> {
                            // Dashboard e back dile logout hoye login e jabe
                            safeLogoutToLogin()
                        }
                        AppScreen.RoomSelection -> {
                            currentScreen = AppScreen.StudentDashboard
                        }
                        AppScreen.EquipmentList -> {
                            currentScreen = AppScreen.RoomSelection
                        }

                        AppScreen.RequestSubmitted -> {
                            currentScreen = AppScreen.StudentDashboard
                        }
                        AppScreen.MyRequests,
                        AppScreen.LabComputerList,
                        AppScreen.MySoftwareIssues -> {
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
                        AppScreen.ManageLabComputers,
                        AppScreen.ReportsDashboard -> {
                            currentScreen = AppScreen.AdminDashboard
                        }

                        AppScreen.RoomWiseEquipmentReport,
                        AppScreen.StudentBorrowHistoryReport,
                        AppScreen.PendingRequestReport,
                        AppScreen.ApprovedRequestReport,
                        AppScreen.IssuedItemReport,
                        AppScreen.ReturnedItemReport,
                        AppScreen.OverdueItemReport,
                        AppScreen.LostDamagedReport,
                        AppScreen.LowStockReport,
                        AppScreen.SoftwareIssueReportAdmin -> {
                            currentScreen = AppScreen.ReportsDashboard
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
                        else -> Unit
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
                            AppScreen.Splash -> {
                                SplashScreen(
                                    onSplashFinished = {
                                        splashFinished = true
                                    }
                                )
                            }
                            in listOf(
                                AppScreen.StudentProfile,
                                AppScreen.StudentDashboard,
                                AppScreen.RoomSelection,
                                AppScreen.EquipmentList,
                                AppScreen.EquipmentDetails,
                                AppScreen.BorrowRequest,
                                AppScreen.RequestSubmitted,
                                AppScreen.MyRequests,
                                AppScreen.LabComputerList,
                                AppScreen.ReportSoftwareIssue,
                                AppScreen.MySoftwareIssues,
                                AppScreen.SoftwareInstallRequest,
                                AppScreen.MySoftwareInstallRequests
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
                                        loadInstitutionsAndOpenRegister()
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
                                    institutionList = institutionList,
                                    isGoogleMode = isGoogleRegisterMode,
                                    prefilledName = googleRegisterName,
                                    prefilledEmail = googleRegisterEmail,
                                    onRegisterClick = { name, email, password, institutionId, studentId, department, semester, phone ->
                                        if (isGoogleRegisterMode) {
                                            authRepository.completeGoogleRegistration(
                                                name = name,
                                                email = email,
                                                institutionId = institutionId,
                                                studentId = studentId,
                                                department = department,
                                                semester = semester,
                                                phone = phone
                                            ) { success, message ->
                                                runOnUiThread {
                                                    showMessage(message)

                                                    if (success) {
                                                        isGoogleRegisterMode = false
                                                        googleRegisterName = ""
                                                        googleRegisterEmail = ""
                                                        currentScreen = AppScreen.Login
                                                    }
                                                }
                                            }
                                        } else {
                                            authRepository.registerUser(
                                                name = name,
                                                email = email,
                                                password = password,
                                                role = "student",
                                                institutionId = institutionId,
                                                studentId = studentId,
                                                department = department,
                                                semester = semester,
                                                phone = phone
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
                                        isGoogleRegisterMode = false
                                        googleRegisterName = ""
                                        googleRegisterEmail = ""
                                        currentScreen = AppScreen.Login
                                    }
                                )
                            }

                            AppScreen.Notifications -> {
                                NotificationScreen(
                                    notifications = notificationViewModel.notificationList,
                                    onMarkReadClick = { notification ->
                                        notificationViewModel.markAsRead(notification.id)
                                    },
                                    onDeleteClick = { notification ->
                                        notificationViewModel.deleteNotification(notification.id)
                                    },
                                    onNotificationClick = { notification ->
                                        if (!notification.read) {
                                            notificationViewModel.markAsRead(notification.id)
                                        }
                                        handleNotificationNavigation(notification)
                                    },
                                    onBackClick = {
                                        currentScreen = when {
                                            isSuperAdmin() -> AppScreen.SuperAdminDashboard
                                            isAdmin() -> AppScreen.AdminDashboard
                                            else -> AppScreen.StudentDashboard
                                        }
                                    }
                                )
                            }
                            AppScreen.SuperAdminDashboard -> {
                                if (!isSuperAdmin()) {
                                    showMessage(UiMessages.ACCESS_DENIED)

                                    currentScreen = when {
                                        isAdmin() -> AppScreen.AdminDashboard
                                        isStudent() -> AppScreen.StudentDashboard
                                        else -> AppScreen.Login
                                    }
                                } else {
                                    SuperAdminDashboardScreen(
                                        onManageInstitutionsClick = {
                                            loadAllInstitutionsAndOpenManage()
                                        },
                                        onCreateInstitutionClick = {
                                            currentScreen = AppScreen.CreateInstitution
                                        },
                                        onCreateInstitutionAdminClick = {
                                            institutionRepository.getApprovedInstitutions { list ->
                                                runOnUiThread {
                                                    institutionList = list

                                                    if (institutionList.isEmpty()) {
                                                        showMessage("No approved institution found")
                                                    } else {
                                                        currentScreen = AppScreen.CreateInstitutionAdmin
                                                    }
                                                }
                                            }
                                        },
                                        onManageAdminRequestsClick = {
                                            loadInstitutionAdminRequestsAndOpen()
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
                            AppScreen.ManageInstitutions -> {
                                if (!isSuperAdmin()) {
                                    showMessage(UiMessages.ACCESS_DENIED)
                                    currentScreen = AppScreen.Login
                                } else {
                                    ManageInstitutionsScreen(
                                        institutionList = allInstitutionList,
                                        onCreateInstitutionClick = {
                                            currentScreen = AppScreen.CreateInstitution
                                        },
                                        onApproveClick = { institution ->
                                            updateInstitutionStatus(
                                                institution = institution,
                                                status = "Approved"
                                            )
                                        },
                                        onRejectClick = { institution ->
                                            updateInstitutionStatus(
                                                institution = institution,
                                                status = "Rejected"
                                            )
                                        },
                                        onSuspendClick = { institution ->
                                            updateInstitutionStatus(
                                                institution = institution,
                                                status = "Suspended"
                                            )
                                        },
                                        onBackClick = {
                                            currentScreen = AppScreen.SuperAdminDashboard
                                        }
                                    )
                                }
                            }



                            AppScreen.CreateInstitution -> {
                                if (!isSuperAdmin()) {
                                    showMessage(UiMessages.ACCESS_DENIED)
                                    currentScreen = AppScreen.Login
                                } else {
                                    CreateInstitutionScreen(
                                        onCreateClick = { institutionId, name, shortName, emailDomain, type, status ->
                                            createInstitution(
                                                institutionId = institutionId,
                                                name = name,
                                                shortName = shortName,
                                                emailDomain = emailDomain,
                                                type = type,
                                                status = status
                                            )
                                        },
                                        onBackClick = {
                                            currentScreen = AppScreen.ManageInstitutions
                                        }
                                    )
                                }
                            }
                            AppScreen.CreateInstitutionAdmin -> {
                                if (!isSuperAdmin()) {
                                    showMessage(UiMessages.ACCESS_DENIED)
                                    currentScreen = AppScreen.Login
                                } else {
                                    CreateInstitutionAdminScreen(
                                        institutionList = institutionList,
                                        onCreateRequestClick = { institutionId, institutionName, adminName, adminEmail ->
                                            createInstitutionAdminRequest(
                                                institutionId = institutionId,
                                                institutionName = institutionName,
                                                adminName = adminName,
                                                adminEmail = adminEmail
                                            )
                                        },
                                        onBackClick = {
                                            currentScreen = AppScreen.SuperAdminDashboard
                                        }
                                    )
                                }
                            }
                            AppScreen.ManageInstitutionAdminRequests -> {
                                if (!isSuperAdmin()) {
                                    showMessage(UiMessages.ACCESS_DENIED)
                                    currentScreen = AppScreen.Login
                                } else {
                                    ManageInstitutionAdminRequestsScreen(
                                        requestList = institutionAdminRequestList,
                                        onApproveClick = { request ->
                                            updateInstitutionAdminRequestStatus(
                                                request = request,
                                                status = "Approved"
                                            )
                                        },
                                        onRejectClick = { request ->
                                            updateInstitutionAdminRequestStatus(
                                                request = request,
                                                status = "Rejected"
                                            )
                                        },
                                        onMarkCreatedClick = { request ->
                                            updateInstitutionAdminRequestStatus(
                                                request = request,
                                                status = "Created"
                                            )
                                        },
                                        onBackClick = {
                                            currentScreen = AppScreen.SuperAdminDashboard
                                        }
                                    )
                                }
                            }
                            AppScreen.AdminDashboard -> {
                                if (!isAdmin()) {
                                    redirectUnauthorized(AppScreen.AdminDashboard)
                                } else {
                                    AdminDashboardScreen(
                                        totalRoomsCount = adminCounts.totalRoomsCount,
                                        totalEquipmentCount = adminCounts.totalEquipmentCount,
                                        availableItemsCount = adminCounts.availableItemsCount,
                                        lowStockCount = adminCounts.lowStockCount,
                                        pendingRequestsCount = adminCounts.pendingRequestsCount,
                                        approvedRequestsCount = adminCounts.approvedRequestsCount,
                                        issuedItemsCount = adminCounts.issuedItemsCount,
                                        returnedItemsCount = adminCounts.returnedItemsCount,
                                        overdueItemsCount = adminCounts.overdueItemsCount,
                                        pendingStudentsCount = adminCounts.pendingStudentsCount,
                                        verifiedStudentsCount = adminCounts.verifiedStudentsCount,
                                        totalLabComputersCount = adminCounts.totalLabComputersCount,
                                        openSoftwareIssuesCount = adminCounts.openSoftwareIssuesCount,
                                        softwareInstallRequestsCount = softwareInstallRequestList.count {
                                            it.status.equals("Pending", ignoreCase = true)
                                        },
                                        hasUnreadNotifications = notificationViewModel.notificationList.any {
                                            !it.read
                                        },
                                        onManageRoomsClick = {
                                            loadRoomsAndOpenManage()
                                        },
                                        onVerifyStudentsClick = {
                                            loadStudentsAndOpenManage()
                                        },
                                        onAddEquipmentClick = {
                                            loadRoomsAndOpenAddEquipment()
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
                                        onReportsClick = {
                                            openReportsDashboard()
                                        },
                                        onSoftwareInstallRequestsClick = {
                                            loadSoftwareInstallRequests()
                                        },
                                        onProfileClick = {
                                            loadAdminProfileAndOpen()
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
                            AppScreen.PendingStudents -> {
                                if (!isAdmin()) {
                                    redirectUnauthorized(AppScreen.PendingStudents)
                                } else {
                                    PendingStudentsScreen(
                                        studentList = pendingStudentList,
                                        onApproveClick = { student ->
                                            approveStudent(student)
                                        },
                                        onRejectClick = { student ->
                                            rejectStudent(student)
                                        },
                                        onBackClick = {
                                            currentScreen = AppScreen.AdminDashboard
                                        }
                                    )
                                }
                            }
                            AppScreen.ManageStudents -> {
                                if (!isAdmin()) {
                                    redirectUnauthorized(AppScreen.ManageStudents)
                                } else {
                                    ManageStudentsScreen(
                                        studentList = studentList,
                                        onStatusChangeClick = { student, status ->
                                            updateStudentStatus(student, status)
                                        },
                                        onBackClick = {
                                            currentScreen = AppScreen.AdminDashboard
                                        }
                                    )
                                }
                            }
                            AppScreen.AdminProfile -> {
                                if (!isAdmin()) {
                                    redirectUnauthorized(AppScreen.AdminProfile)
                                } else {
                                    AdminProfileScreen(
                                        user = currentAppUser,
                                        onSaveClick = { phone, profileImageUrl ->
                                            handleUpdateAdminProfile(
                                                phone = phone,
                                                profileImageUrl = profileImageUrl
                                            )
                                        },
                                        onPasswordResetClick = { email ->
                                            sendProfilePasswordReset(email)
                                        },
                                        onBackClick = {
                                            currentScreen = AppScreen.AdminDashboard
                                        }
                                    )
                                }
                            }
                            AppScreen.ManageRooms -> {
                                if (!isAdmin()) {
                                    redirectUnauthorized(AppScreen.ManageRooms)
                                } else {
                                    ManageRoomsScreen(
                                        roomList = roomList,
                                        onAddRoomClick = {
                                            currentScreen = AppScreen.AddRoom
                                        },
                                        onDeleteRoomClick = { room ->
                                            roomRepository.deleteRoom(
                                                roomId = room.id
                                            ) { success, message ->
                                                runOnUiThread {
                                                    showMessage(message)

                                                    if (success) {
                                                        loadRoomsAndOpenManage()
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

                            AppScreen.AddRoom -> {
                                if (!isAdmin()) {
                                    redirectUnauthorized(AppScreen.AddRoom)
                                } else {
                                    AddRoomScreen(
                                        onAddClick = { name, building, floor, roomType, department ->
                                            roomRepository.addRoom(
                                                institutionId = currentInstitutionId,
                                                name = name,
                                                building = building,
                                                floor = floor,
                                                roomType = roomType,
                                                department = department
                                            ) { success, message ->
                                                runOnUiThread {
                                                    showMessage(message)

                                                    if (success) {
                                                        loadRoomsAndOpenManage()
                                                    }
                                                }
                                            }
                                        },
                                        onBackClick = {
                                            currentScreen = AppScreen.ManageRooms
                                        }
                                    )
                                }
                            }
                            AppScreen.AddEquipment -> {
                                if (!isAdmin()) {
                                    redirectUnauthorized(AppScreen.AddEquipment)
                                } else {
                                    AddEquipmentScreen(
                                        roomList = roomList,
                                        onAddClick = { name, description, condition, totalQuantity, availableQuantity, category, imageName, imageUrl, isBorrowable, roomId ->
                                            if (
                                                name.isBlank() ||
                                                description.isBlank() ||
                                                condition.isBlank() ||
                                                totalQuantity.isBlank() ||
                                                availableQuantity.isBlank() ||
                                                category.isBlank() ||
                                                roomId.isBlank() ||
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
                                                            institutionId = currentInstitutionId,
                                                            roomId = roomId,
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
                                                    roomList = roomList,
                                                    onEditClick = { equipment ->
                                                        selectedEquipment = equipment
                                                        currentScreen = AppScreen.EditEquipment
                                                    },
                                                    onBackClick = {
                                                        currentScreen = AppScreen.AdminDashboard
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

                            AppScreen.ReportsDashboard -> {
                                if (!isAdmin()) {
                                    redirectUnauthorized(AppScreen.ReportsDashboard)
                                } else {
                                    ReportsDashboardScreen(
                                        totalRoomsCount = adminCounts.totalRoomsCount,
                                        totalEquipmentCount = adminCounts.totalEquipmentCount,
                                        totalStudentsCount = adminCounts.pendingStudentsCount + adminCounts.verifiedStudentsCount,
                                        pendingRequestsCount = adminCounts.pendingRequestsCount,
                                        approvedRequestsCount = adminCounts.approvedRequestsCount,
                                        issuedItemsCount = adminCounts.issuedItemsCount,
                                        returnedItemsCount = adminCounts.returnedItemsCount,
                                        overdueItemsCount = adminCounts.overdueItemsCount,
                                        lostDamagedItemsCount = adminAllRequests.count {
                                            it.status.equals("Lost", ignoreCase = true) ||
                                                    it.status.equals("Damaged", ignoreCase = true)
                                        },
                                        lowStockCount = adminCounts.lowStockCount,
                                        softwareIssuesCount = softwareIssueReports.size,

                                        onRoomWiseEquipmentReportClick = {
                                            currentScreen = AppScreen.RoomWiseEquipmentReport
                                        },
                                        onStudentBorrowHistoryClick = {
                                            currentScreen = AppScreen.StudentBorrowHistoryReport
                                        },
                                        onPendingRequestReportClick = {
                                            currentScreen = AppScreen.PendingRequestReport
                                        },
                                        onApprovedRequestReportClick = {
                                            currentScreen = AppScreen.ApprovedRequestReport
                                        },
                                        onIssuedItemReportClick = {
                                            currentScreen = AppScreen.IssuedItemReport
                                        },
                                        onReturnedItemReportClick = {
                                            currentScreen = AppScreen.ReturnedItemReport
                                        },
                                        onOverdueItemReportClick = {
                                            currentScreen = AppScreen.OverdueItemReport
                                        },
                                        onLostDamagedReportClick = {
                                            currentScreen = AppScreen.LostDamagedReport
                                        },
                                        onLowStockReportClick = {
                                            currentScreen = AppScreen.LowStockReport
                                        },
                                        onSoftwareIssueReportClick = {
                                            currentScreen = AppScreen.SoftwareIssueReportAdmin
                                        },
                                        onBackClick = {
                                            currentScreen = AppScreen.AdminDashboard
                                        }
                                    )
                                }
                            }
                            AppScreen.RoomWiseEquipmentReport -> {
                                if (!isAdmin()) {
                                    redirectUnauthorized(AppScreen.RoomWiseEquipmentReport)
                                } else {
                                    RoomWiseEquipmentReportScreen(
                                        roomList = roomList,
                                        equipmentList = adminEquipmentViewModel.equipmentList,
                                        onBackClick = {
                                            currentScreen = AppScreen.ReportsDashboard
                                        }
                                    )
                                }
                            }
                            AppScreen.StudentBorrowHistoryReport -> {
                                if (!isAdmin()) {
                                    redirectUnauthorized(AppScreen.StudentBorrowHistoryReport)
                                } else {
                                    StudentBorrowHistoryReportScreen(
                                        studentList = studentList,
                                        requestList = adminAllRequests,
                                        onBackClick = {
                                            currentScreen = AppScreen.ReportsDashboard
                                        }
                                    )
                                }
                            }
                            AppScreen.PendingRequestReport -> {
                                if (!isAdmin()) {
                                    redirectUnauthorized(AppScreen.PendingRequestReport)
                                } else {
                                    BorrowRequestReportScreen(
                                        title = "Pending Request Report",
                                        subtitle = "Borrow requests waiting for admin approval",
                                        requestList = adminAllRequests,
                                        fixedStatus = "Pending",
                                        onBackClick = {
                                            currentScreen = AppScreen.ReportsDashboard
                                        }
                                    )
                                }
                            }

                            AppScreen.ApprovedRequestReport -> {
                                if (!isAdmin()) {
                                    redirectUnauthorized(AppScreen.ApprovedRequestReport)
                                } else {
                                    BorrowRequestReportScreen(
                                        title = "Approved Request Report",
                                        subtitle = "Approved requests waiting to be issued",
                                        requestList = adminAllRequests,
                                        fixedStatus = "Approved",
                                        onBackClick = {
                                            currentScreen = AppScreen.ReportsDashboard
                                        }
                                    )
                                }
                            }

                            AppScreen.IssuedItemReport -> {
                                if (!isAdmin()) {
                                    redirectUnauthorized(AppScreen.IssuedItemReport)
                                } else {
                                    BorrowRequestReportScreen(
                                        title = "Issued Item Report",
                                        subtitle = "Items currently issued to students",
                                        requestList = adminAllRequests,
                                        fixedStatus = "Issued",
                                        onBackClick = {
                                            currentScreen = AppScreen.ReportsDashboard
                                        }
                                    )
                                }
                            }

                            AppScreen.ReturnedItemReport -> {
                                if (!isAdmin()) {
                                    redirectUnauthorized(AppScreen.ReturnedItemReport)
                                } else {
                                    BorrowRequestReportScreen(
                                        title = "Returned Item Report",
                                        subtitle = "Completed returned borrow records",
                                        requestList = adminAllRequests,
                                        fixedStatus = "Returned",
                                        onBackClick = {
                                            currentScreen = AppScreen.ReportsDashboard
                                        }
                                    )
                                }
                            }

                            AppScreen.OverdueItemReport -> {
                                if (!isAdmin()) {
                                    redirectUnauthorized(AppScreen.OverdueItemReport)
                                } else {
                                    BorrowRequestReportScreen(
                                        title = "Overdue Item Report",
                                        subtitle = "Issued items that passed due date",
                                        requestList = adminAllRequests,
                                        fixedStatus = "Overdue",
                                        onBackClick = {
                                            currentScreen = AppScreen.ReportsDashboard
                                        }
                                    )
                                }
                            }

                            AppScreen.LostDamagedReport -> {
                                if (!isAdmin()) {
                                    redirectUnauthorized(AppScreen.LostDamagedReport)
                                } else {
                                    BorrowRequestReportScreen(
                                        title = "Lost / Damaged Report",
                                        subtitle = "Items marked as lost or damaged",
                                        requestList = adminAllRequests,
                                        fixedStatus = "Lost/Damaged",
                                        onFinePaidClick = { request ->
                                            handleFinePaid(request)
                                        },
                                        onFineWaivedClick = { request ->
                                            handleFineWaived(request)
                                        },
                                        onBackClick = {
                                            currentScreen = AppScreen.ReportsDashboard
                                        }
                                    )
                                }
                            }
                            AppScreen.LowStockReport -> {
                                if (!isAdmin()) {
                                    redirectUnauthorized(AppScreen.LowStockReport)
                                } else {
                                    LowStockReportScreen(
                                        roomList = roomList,
                                        equipmentList = adminEquipmentViewModel.equipmentList,
                                        onBackClick = {
                                            currentScreen = AppScreen.ReportsDashboard
                                        }
                                    )
                                }
                            }
                            AppScreen.SoftwareIssueReportAdmin -> {
                                if (!isAdmin()) {
                                    redirectUnauthorized(AppScreen.SoftwareIssueReportAdmin)
                                } else {
                                    SoftwareIssueReportAdminScreen(
                                        reportList = softwareIssueReports,
                                        onBackClick = {
                                            currentScreen = AppScreen.ReportsDashboard
                                        }
                                    )
                                }
                            }
                            AppScreen.ManageLabComputers -> {
                                if (!isAdmin()) {
                                    redirectUnauthorized(AppScreen.ManageLabComputers)
                                } else {
                                    ManageLabComputersScreen(
                                        computerList = labComputerList,
                                        onAddComputerClick = {
                                            loadRoomsAndOpenAddLabComputer()
                                        },
                                        onEditComputerClick = { computer ->
                                            selectedLabComputer = computer
                                            currentScreen = AppScreen.EditLabComputer
                                        },
                                        onOpenSoftwareClick = { computer ->
                                            selectedLabComputer = computer

                                            labComputerRepository.getSoftwareStatusForComputer(
                                                institutionId = currentInstitutionId,
                                                computerId = computer.id
                                            ) { list ->
                                                runOnUiThread {
                                                    computerSoftwareList = list
                                                    currentScreen = AppScreen.ManageSoftwareStatus
                                                }
                                            }
                                        },
                                        onViewReportsClick = { computer ->
                                            selectedLabComputer = computer

                                            labComputerRepository.getSoftwareIssueReports(
                                                institutionId = currentInstitutionId
                                            ) { list ->
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
                                        roomList = roomList,
                                        onAddClick = { roomId, pcName, labRoom, locationNote, ipAddress, computerImageUrl, status, remarks ->
                                            if (
                                                roomId.isBlank() ||
                                                pcName.isBlank() ||
                                                labRoom.isBlank() ||
                                                status.isBlank()
                                            ) {
                                                showMessage(UiMessages.REQUIRED_FIELDS)
                                            } else {
                                                labComputerRepository.addLabComputer(
                                                    institutionId = currentInstitutionId,
                                                    roomId = roomId,
                                                    pcName = pcName,
                                                    labRoom = labRoom,
                                                    locationNote = locationNote,
                                                    ipAddress = ipAddress,
                                                    computerImageUrl = computerImageUrl,
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
                                            onAddSoftwareClick = { softwareName, version, softwareLogoUrl, installed, launchesProperly, compileWorks, runWorks, remarks ->
                                                labComputerRepository.addSoftwareStatus(
                                                    institutionId = currentInstitutionId,
                                                    roomId = computer.roomId,
                                                    computerId = computer.id,
                                                    softwareName = softwareName,
                                                    version = version,
                                                    softwareLogoUrl = softwareLogoUrl,
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
                                                                institutionId = currentInstitutionId,
                                                                computerId = computer.id
                                                            ) { list ->
                                                                runOnUiThread {
                                                                    computerSoftwareList = list
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            },
                                            onUpdateSoftwareClick = { updatedSoftware ->
                                                labComputerRepository.updateSoftwareStatus(
                                                    softwareStatus = updatedSoftware
                                                ) { success, message ->
                                                    runOnUiThread {
                                                        showMessage(message)

                                                        if (success) {
                                                            labComputerRepository.getSoftwareStatusForComputer(
                                                                institutionId = currentInstitutionId,
                                                                computerId = computer.id
                                                            ) { list ->
                                                                runOnUiThread {
                                                                    computerSoftwareList = list
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            },
                                            onDeleteSoftwareClick = { softwareStatus ->
                                                labComputerRepository.deleteSoftwareStatus(
                                                    softwareStatusId = softwareStatus.id
                                                ) { success, message ->
                                                    runOnUiThread {
                                                        showMessage(message)

                                                        if (success) {
                                                            labComputerRepository.getSoftwareStatusForComputer(
                                                                institutionId = currentInstitutionId,
                                                                computerId = computer.id
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
                                        onUpdateStatusClick = { report, newStatus, adminComment ->
                                            labComputerRepository.updateIssueReportStatus(
                                                reportId = report.id,
                                                status = newStatus,
                                                adminComment = adminComment,
                                                handledBy = authRepository.getCurrentUserUid().orEmpty()
                                            ) { success, message ->
                                                runOnUiThread {
                                                    showMessage(message)

                                                    if (success) {
                                                        notificationRepository.sendNotificationToStudent(
                                                            institutionId = report.institutionId,
                                                            studentUserId = report.reportedByUserId,
                                                            title = "Software Issue Updated",
                                                            message = "Your software issue for ${report.softwareName} is now $newStatus.",
                                                            type = when (newStatus.lowercase()) {
                                                                "solved" -> "success"
                                                                "rejected" -> "error"
                                                                "in progress" -> "warning"
                                                                else -> "info"
                                                            }
                                                        )

                                                        labComputerRepository.getSoftwareIssueReports(
                                                            institutionId = currentInstitutionId
                                                        ) { reports ->
                                                            runOnUiThread {
                                                                softwareIssueReports = reports
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
                            AppScreen.SoftwareInstallRequestsAdmin -> {
                                SoftwareInstallRequestsAdminScreen(
                                    requestList = softwareInstallRequestList,
                                    onApproveClick = { request ->
                                        labComputerRepository.updateSoftwareInstallRequestStatus(
                                            requestId = request.id,
                                            status = "Approved",
                                            adminMessage = "Your software install request has been approved."
                                        ) { success, message ->
                                            runOnUiThread {
                                                showMessage(message)
                                                if (success) {
                                                    loadSoftwareInstallRequests()
                                                }
                                            }
                                        }
                                    },
                                    onRejectClick = { request ->
                                        labComputerRepository.updateSoftwareInstallRequestStatus(
                                            requestId = request.id,
                                            status = "Rejected",
                                            adminMessage = "Your software install request has been rejected."
                                        ) { success, message ->
                                            runOnUiThread {
                                                showMessage(message)
                                                if (success) {
                                                    loadSoftwareInstallRequests()
                                                }
                                            }
                                        }
                                    },
                                    onInstalledClick = { request ->
                                        labComputerRepository.updateSoftwareInstallRequestStatus(
                                            requestId = request.id,
                                            status = "Installed",
                                            adminMessage = "Requested software has been installed."
                                        ) { success, message ->
                                            runOnUiThread {
                                                showMessage(message)
                                                if (success) {
                                                    loadSoftwareInstallRequests()
                                                }
                                            }
                                        }
                                    },
                                    onBackClick = {
                                        currentScreen = AppScreen.AdminDashboard
                                    }
                                )
                            }
                            else -> Unit
                        }

                    }
                }
            }
        }
    }
}