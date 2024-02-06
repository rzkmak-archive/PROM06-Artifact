package com.rizkima.cyberbullyingffapi.service

import com.rizkima.cyberbullyingffapi.controller.request.ScreeningTwitterRequest
import com.rizkima.cyberbullyingffapi.enum.ScreeningResultType
import com.rizkima.cyberbullyingffapi.feign.CyberBullyTwitterAPIFeign
import com.rizkima.cyberbullyingffapi.model.AlertHistory
import com.rizkima.cyberbullyingffapi.model.Session
import com.rizkima.cyberbullyingffapi.repository.AlertHistoryRepository
import com.rizkima.cyberbullyingffapi.repository.ScreeningHistoryRepository
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

                screeningHistoryRepository.findBySessionId(sessionId = session.sessionId).let { screeningHistory ->
                    screeningHistory!!.screeningCount++
                    screeningHistoryRepository.save(screeningHistory)
                }

                if (response.result == ScreeningResultType.GREEN) {
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

                    notificationService.sendNotificationEmailForBreachedThreshold(session)
                }
            }
        } catch (e: Exception) {
            log.error("Unexpected error happen", e)
            throw e
        }
    }
}