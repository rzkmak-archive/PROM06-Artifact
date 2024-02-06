package com.rizkima.cyberbullyingffapi.service

import com.rizkima.cyberbullyingffapi.model.Session
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class NotificationService {

    private val log = LoggerFactory.getLogger(this.javaClass)

    fun sendNotificationEmailForBreachedThreshold(session: Session) {
        log.info("sending notification mail for session: {}", session.sessionId)
    }
}