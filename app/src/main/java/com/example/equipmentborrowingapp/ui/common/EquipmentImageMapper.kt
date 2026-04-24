package com.example.equipmentborrowingapp.ui.common

import com.example.equipmentborrowingapp.R

object EquipmentImageMapper {

    fun getImageRes(imageName: String): Int {
        return when (imageName.trim().lowercase()) {

            // Arduino
            "arduino_uno" -> R.drawable.arduino_uno
            "arduino_nano" -> R.drawable.arduino_uno

            // Electronics
            "breadboard" -> R.drawable.ic_launcher_foreground
            "resistor" -> R.drawable.ic_launcher_foreground
            "capacitor" -> R.drawable.ic_launcher_foreground

            // Default fallback
            else -> android.R.drawable.ic_menu_gallery
        }
    }
}