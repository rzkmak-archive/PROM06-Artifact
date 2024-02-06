package com.rizkima.cyberbullyingffapi.controller.request

import com.rizkima.cyberbullyingffapi.enum.NotificationTargetType
import jakarta.validation.constraints.NotBlank

class InitializeRequest (
        @NotBlank val notificationTargetType: NotificationTargetType,
        @NotBlank val notificationTargetValue: String,
        val thresholdDurationInSeconds: Long,
        val thresholdCount: Long,
)