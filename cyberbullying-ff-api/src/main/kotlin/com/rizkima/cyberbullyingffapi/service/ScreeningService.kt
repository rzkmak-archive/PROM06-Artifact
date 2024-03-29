package com.rizkima.cyberbullyingffapi.service

import com.rizkima.cyberbullyingffapi.controller.request.ScreeningTwitterRequest
import com.rizkima.cyberbullyingffapi.enum.ScreeningResultType
import com.rizkima.cyberbullyingffapi.enum.ScreeningType
import com.rizkima.cyberbullyingffapi.feign.CyberBullyTwitterAPIFeign
import com.rizkima.cyberbullyingffapi.model.AlertHistory
import com.rizkima.cyberbullyingffapi.model.ScreeningHistory
import com.rizkima.cyberbullyingffapi.model.Session
import com.rizkima.cyberbullyingffapi.repository.AlertHistoryRepository
import com.rizkima.cyberbullyingffapi.repository.ScreeningHistoryRepository
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.time.Duration


@Service
class ScreeningService(
        val screeningHistoryRepository: ScreeningHistoryRepository,
        val alertHistoryRepository: AlertHistoryRepository,
        val cyberBullyTwitterAPIFeign: CyberBullyTwitterAPIFeign,
        val redisTemplate: RedisTemplate<String, Any>?,
        val notificationService: NotificationService,
) {
    private val log = LoggerFactory.getLogger(this.javaClass)

    @Transactional
    fun screeningTwitter(screeningTwitterRequest: ScreeningTwitterRequest, session: Session) {
        doScreeningTwitterAsync(screeningTwitterRequest, session)
    }

    @Async
    fun doScreeningTwitterAsync(screeningTwitterRequest: ScreeningTwitterRequest, session: Session) {

        try {
            cyberBullyTwitterAPIFeign.screeningTwitterAPIExecute(
                    com.rizkima.cyberbullyingffapi.feign.request.ScreeningTwitterRequest(
                            value = screeningTwitterRequest.tweetValue
                    )
            ).let {
                val response = it.body!!

                log.info("response from screening: {} raw: {}", screeningTwitterRequest.tweetValue, response.result)

                incrementScreeningHistoryCount(sessionId = session.sessionId)

                if (response.result.type == ScreeningResultType.GREEN) {
                    return
                }

                if (redisTemplate?.opsForValue()?.get(session.sessionId) == null) {
                    log.info("initializing new counter for sessionId: {}", session.sessionId)
                    redisTemplate?.opsForValue()?.set(session.sessionId, session.thresholdCount)
                    redisTemplate?.opsForValue()?.getAndExpire(session.sessionId, Duration.ofSeconds(session.thresholdDurationInSeconds))
                }

                val currentThreshold = redisTemplate?.opsForValue()?.decrement(session.sessionId)

                if (currentThreshold != null && currentThreshold <= 0L) {
                    alertHistoryRepository.save(
                            AlertHistory(
                                    sessionId = session.sessionId
                            )
                    )
                    log.info("alert threshold breached for sessionId: {}", session.sessionId)
                    redisTemplate?.delete(session.sessionId)

                    notificationService.sendNotificationEmailForBreachedThreshold(session)
                }
            }
        } catch (e: Exception) {
            log.error("Unexpected error happen", e)
            throw e
        }
    }

    fun incrementScreeningHistoryCount(sessionId: String) {
        screeningHistoryRepository.findBySessionId(sessionId = sessionId).let { screeningHistory ->
            if (screeningHistory == null) {
                log.error("screening history is null for sessionId: {}", sessionId)
            } else {
                screeningHistory.screeningCount++
                screeningHistoryRepository.save(screeningHistory)
            }
        }
    }
}