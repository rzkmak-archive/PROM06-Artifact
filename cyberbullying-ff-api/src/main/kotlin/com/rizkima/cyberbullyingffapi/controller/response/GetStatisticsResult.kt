package com.rizkima.cyberbullyingffapi.controller.response

import java.time.Instant
import java.time.ZonedDateTime

data class GetStatisticsResult(
        val lastAlert: Instant?,
        val analyzedCyberBullyCount: Long,
        val cyberBullyDetectedCount: Long
)