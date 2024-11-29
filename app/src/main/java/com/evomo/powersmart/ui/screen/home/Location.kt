package com.evomo.powersmart.ui.screen.home

enum class Location(val display: String, val location: String) {
    AHU_LANTAI_2(
        display = "AHU Lantai 2",
        location = "AHU_Lantai_2"
    ),
    CHILLER_WITEL_JAKSEL(
        display = "Chiller Witel Jaksel",
        location = "Chiller_Witel_Jaksel"
    ),
    LIFT_WITEL_JAKSEL(
        display = "Lift Witel Jaksel",
        location = "Lift_Witel_Jaksel"
    ),
    LIFT_OPMC(
        display = "Lift OPMC",
        location = "Lift_OPMC"
    ),
}