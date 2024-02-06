package com.rizkima.cyberbullyingffapi.repository

import com.rizkima.cyberbullyingffapi.model.ScreeningHistory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ScreeningHistoryRepository : JpaRepository<ScreeningHistory, Long> {
    fun countAllBySessionId(sessionId: String): Long
    fun findBySessionId(sessionId: String): ScreeningHistory?
}
