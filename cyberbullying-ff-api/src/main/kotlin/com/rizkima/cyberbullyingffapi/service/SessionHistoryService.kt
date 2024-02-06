package com.rizkima.cyberbullyingffapi.service

import com.rizkima.cyberbullyingffapi.repository.ScreeningHistoryRepository
import org.springframework.stereotype.Service

@Service
class SessionHistoryService(
        val screeningHistoryRepository: ScreeningHistoryRepository
) {
    fun getScreeningCount(sessionId: String): Long {
        return screeningHistoryRepository.countAllBySessionId(sessionId)
    }
}