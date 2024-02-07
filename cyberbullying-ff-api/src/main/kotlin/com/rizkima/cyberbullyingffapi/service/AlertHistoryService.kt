package com.rizkima.cyberbullyingffapi.service

import com.rizkima.cyberbullyingffapi.repository.AlertHistoryRepository
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.ZonedDateTime

@Service
class AlertHistoryService(
        val alertHistoryRepository: AlertHistoryRepository
) {
    fun getAlertTriggerCount(sessionId: String): Long {
        return alertHistoryRepository.countAllBySessionId(sessionId)
    }

    fun getLastTriggeredDate(sessionId: String): Instant? {
        alertHistoryRepository
            .findFirstBySessionIdOrderByCreatedAtDesc(sessionId).let {
                    it?.createdAt.let {
                        if (it == null) return null
                        else return it.toInstant()
                    }
                }
    }
}