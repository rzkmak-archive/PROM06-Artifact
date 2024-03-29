package com.rizkima.cyberbullyingffapi.service

import com.rizkima.cyberbullyingffapi.repository.ScreeningHistoryRepository
import org.springframework.stereotype.Service

@Service
class SessionHistoryService(
        val screeningHistoryRepository: ScreeningHistoryRepository
) {
    fun getScreeningCount(sessionId: String): Long {
        val screeningHistory = screeningHistoryRepository.findBySessionId(sessionId) ?: return 0L

        return screeningHistory.screeningCount
    }
}