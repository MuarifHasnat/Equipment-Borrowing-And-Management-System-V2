package com.example.equipmentborrowingapp.data.model

data class ComputerSoftwareStatus(
    val id: String = "",
    val institutionId: String = "",
    val roomId: String = "",
    val computerId: String = "",
    val softwareName: String = "",
    val version: String = "",
    val softwareLogoUrl: String = "",
    val installed: Boolean = false,
    val launchesProperly: Boolean = false,
    val compileWorks: Boolean = false,
    val runWorks: Boolean = false,
    val remarks: String = "",
    val checkedAt: Long = System.currentTimeMillis()
)