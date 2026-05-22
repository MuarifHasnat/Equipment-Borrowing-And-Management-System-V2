package com.example.equipmentborrowingapp.ui.common

import com.example.equipmentborrowingapp.R

object EquipmentImageMapper {

    const val DEFAULT_IMAGE_NAME = "default"

    fun getImageRes(imageName: String?): Int {
        return when (imageName.orEmpty().trim().lowercase()) {

            // Arduino
            "arduino_uno",
            "arduino uno",
            "uno" -> R.drawable.arduino_uno

            "arduino_nano",
            "arduino nano",
            "nano" -> R.drawable.arduino_uno

            // Electronics fallback
            "breadboard",
            "resistor",
            "capacitor",
            "multimeter",
            "sensor",
            "jumper_wire",
            "jumper wire",
            "cable",
            "oscilloscope",
            "power_supply",
            "power supply" -> R.drawable.ic_launcher_foreground

            // Default fallback
            "",
            DEFAULT_IMAGE_NAME -> R.drawable.ic_launcher_foreground

            else -> R.drawable.ic_launcher_foreground
        }
    }

    fun getSafeImageUrl(imageUrl: String?): String {
        return imageUrl.orEmpty().trim()
    }

    fun hasValidImageUrl(imageUrl: String?): Boolean {
        val cleanUrl = getSafeImageUrl(imageUrl)

        return cleanUrl.startsWith("http://", ignoreCase = true) ||
                cleanUrl.startsWith("https://", ignoreCase = true)
    }
}