package no.iktdev.streamit.library.db.objects.authentication

import java.time.LocalDateTime

data class RegisteredDevicesTableObject(
    val deviceId: String,
    val applicationPackageName: String,
    val osVersion: String,
    val osPlatform: String,
    val registered: LocalDateTime,
    val lastSeen: LocalDateTime
)