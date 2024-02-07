package com.rizkima.cyberbullyingffapi.controller

import com.rizkima.cyberbullyingffapi.controller.request.InitializeRequest
import com.rizkima.cyberbullyingffapi.controller.request.ScreeningTwitterRequest
import com.rizkima.cyberbullyingffapi.controller.response.GetStatisticsResult
import com.rizkima.cyberbullyingffapi.controller.response.InitializeResult
import com.rizkima.cyberbullyingffapi.service.AlertHistoryService
import com.rizkima.cyberbullyingffapi.service.ScreeningService
import com.rizkima.cyberbullyingffapi.service.SessionHistoryService
import com.rizkima.cyberbullyingffapi.service.SessionService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/self/cyberbully")
class CyberBullyingUserController(
        val sessionHistoryService: SessionHistoryService,
        val alertHistoryService: AlertHistoryService,
        val screeningService: ScreeningService,
        val sessionService: SessionService
) {
    @PostMapping("/initialize")
    fun initialize(@RequestBody initializeRequest: InitializeRequest): ResponseEntity<InitializeResult> {
        val session = sessionService.initializeSession(initializeRequest)
        return ResponseEntity.ok(InitializeResult(sessionId = session.sessionId))
    }

    @GetMapping("/statistics")
    fun getStatistics(@RequestHeader("X-Session-Id") sessionId: String): ResponseEntity<GetStatisticsResult> {
        return ResponseEntity.ok(GetStatisticsResult(
                lastAlert = alertHistoryService.getLastTriggeredDate(sessionId),
                analyzedCyberBullyCount = sessionHistoryService.getScreeningCount(sessionId),
                cyberBullyDetectedCount = alertHistoryService.getAlertTriggerCount(sessionId),
        ))
    }

    @PostMapping("/screening/twitter")
    fun screeningTwitter(@RequestHeader("X-Session-Id") sessionId: String,
                         @RequestBody screeningTwitterRequest: ScreeningTwitterRequest): ResponseEntity<Void> {
        screeningService.screeningTwitter(screeningTwitterRequest, sessionService.getSession(sessionId))
        return ResponseEntity(HttpStatus.ACCEPTED)
    } }