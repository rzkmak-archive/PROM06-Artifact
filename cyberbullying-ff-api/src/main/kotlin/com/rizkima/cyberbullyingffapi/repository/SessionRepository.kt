package com.rizkima.cyberbullyingffapi.repository

import com.rizkima.cyberbullyingffapi.model.Session
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SessionRepository : JpaRepository<Session, Long> {
    fun findBySessionId(sessionId: String): Session?
}