package com.rizkima.cyberbullyingffapi.controller.request

import jakarta.validation.constraints.NotBlank

data class ScreeningTwitterRequest(
        @NotBlank val tweetValue: String
)