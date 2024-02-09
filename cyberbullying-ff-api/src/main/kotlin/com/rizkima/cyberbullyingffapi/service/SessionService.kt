package com.rizkima.cyberbullyingffapi.service

import com.rizkima.cyberbullyingffapi.controller.request.InitializeRequest
import com.rizkima.cyberbullyingffapi.enum.ScreeningType
import com.rizkima.cyberbullyingffapi.model.ScreeningHistory
import com.rizkima.cyberbullyingffapi.model.Session
import com.rizkima.cyberbullyingffapi.repository.ScreeningHistoryRepository
import com.rizkima.cyberbullyingffapi.repository.SessionRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class SessionService(
        val sessionRepository: SessionRepository,
        val screeningHistoryRepository: ScreeningHistoryRepository
) {
    fun initializeSession(initializeRequest: InitializeRequest): Session {
        val session = sessionRepository.save(
                Session(
                    sessionId = UUID.randomUUID().toString(),
                    notificationTargetType = initializeRequest.notificationTargetType,
                    notificationTargetValue = initializeRequest.notificationTargetValue,
                    thresholdCount = initializeRequest.thresholdCount,
                    thresholdDurationInSeconds = initializeRequest.thresholdDurationInSeconds
                )
        )

        screeningHistoryRepository.save(ScreeningHistory(
                sessionId = session.sessionId,
                screeningCount = 0L,
                screeningType = ScreeningType.TWITTER
        ))

        return session
    }

    fun getSession(sessionId: String): Session {
        return sessionRepository.findBySessionId(sessionId).let {
            it ?: throw Exception("Invalid session")
        }
    }
}