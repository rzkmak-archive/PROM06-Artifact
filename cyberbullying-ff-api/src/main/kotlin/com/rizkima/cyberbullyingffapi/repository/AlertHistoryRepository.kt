package com.rizkima.cyberbullyingffapi.repository

import com.rizkima.cyberbullyingffapi.model.AlertHistory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AlertHistoryRepository : JpaRepository<AlertHistory, Long> {
    fun countAllBySessionId(sessionId: String): Long

    fun findFirstBySessionIdOrderByCreatedAtDesc(sessionId: String): AlertHistory
}