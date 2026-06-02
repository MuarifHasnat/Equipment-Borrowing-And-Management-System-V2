# 📦 Equipment Borrowing and Management App

![Kotlin](https://img.shields.io/badge/Kotlin-1DA1F2?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![Firebase](https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black)
![Android Studio](https://img.shields.io/badge/Android%20Studio-3DDC84?style=for-the-badge&logo=androidstudio&logoColor=white)

---

## 📖 Overview
Equipment Borrowing And Management System is an Android application built with **Kotlin**, **Jetpack Compose**, and **Firebase** for managing equipment borrowing in an academic or lab environment.

The system supports two user roles:
- 🎓 **Student**
- 🛠️ **Admin**

Students can browse available equipment, submit borrowing requests, view their request history, check lab computer information, and report software issues.  
Admins can manage equipment, review borrowing requests, manage lab computers, track software status, and review reported issues.

---

## ✨ Features

### 🔑 Authentication
- User registration & login  
- Role-based access control  
- Separate dashboards for Admin & Student  

### 🎓 Student Features
- View available equipment  
- Submit borrow requests  
- View personal request history  
- View lab computer list  
- Report software-related issues  

### 🛠️ Admin Features
- Dashboard statistics  
- Add, edit, and manage equipment  
- Approve or reject borrowing requests  
- Mark returned equipment  
- Manage lab computers  
- Track software status  
- Review software issue reports  

### 🎨 UI and State Management
- Loading screen  
- Empty state screen  
- Error state screen  
- Retry support for failed screens  
- Snackbar-based user feedback  

---

## 🛠️ Tech Stack
- **Kotlin**  
- **Jetpack Compose**  
- **Firebase Authentication**  
- **Cloud Firestore**  
- **Android Studio**  

---

## 📂 Project Structure

```text
com/example/equipmentborrowingapp/
├── data/
│   ├── model/
│   └── repository/
├── navigation/
├── ui/
│   ├── admin/
│   ├── auth/
│   ├── common/
│   ├── student/
│   └── theme/
├── utils/
└── screenshots/

## 📱 App Screenshots

### 🔑 Authentication
| Splash Screen | Login | Register |
|---------------|-------|----------|
| ![Splash Screen](screenshots/Splash_Screen.jpg) | ![Login](screenshots/Login.jpg) | ![Register](screenshots/Register.jpg) |

---

### 🎓 Student Features
| Student Dashboard | Equipment List | Equipment Details |
|-------------------|----------------|-------------------|
| ![Student Dashboard](screenshots/Student_dashboard.jpg) | ![Equipment List](screenshots/Equipment_list.jpg) | ![Equipment Details](screenshots/Equipment_details.jpg) |

| Borrow Request | Submit Request | My Requests |
|----------------|----------------|-------------|
| ![Borrow Request](screenshots/Borrow_request.jpg) | ![Submit Request](screenshots/Submit_request.jpg) | ![My Requests](screenshots/My_request.jpg) |

| Room Selection | Report Software Issue |
|----------------|------------------------|
| ![Room Selection](screenshots/room_selection.jpg) | ![Software Issue](screenshots/software_issue.jpg) |

---

### 🛠️ Admin Features
| Admin Dashboard | Manage Equipment | Pending Requests |
|-----------------|------------------|------------------|
| ![Admin Dashboard](screenshots/admin_dashboard.jpg) | ![Manage Equipment](screenshots/manage_equipment.jpg) | ![Pending Requests](screenshots/pending_request.jpg) |

| Lab Computers |
|---------------|
| ![Lab Computers](screenshots/Lab_computer.jpg) |
