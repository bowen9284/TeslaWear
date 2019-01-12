package com.example.android.teslawear.Model

data class WakeUp (
    val id: Int,
    val userId: Int,
    val vehicleId: Int,
    val vin: String,
    val displayName: String?,
    val optionCodes: String,
    val color: String?,
    val tokens: List<String>,
    val state: String,
    val inService: Boolean?,
    val idS: String,
    val calendarEnabled: Boolean,
    val backseatToken: String?,
    val backseatTokenUpdatedAt: String?
)