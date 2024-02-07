package com.rizkima.cyberbullyingffapi.model

import com.rizkima.cyberbullyingffapi.enum.NotificationTargetType
import jakarta.persistence.*
import java.util.Date

@Entity
class Session(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        @Column(nullable = false)
        var sessionId: String,

        @Column(nullable = false)
        @Enumerated(EnumType.STRING)
        var notificationTargetType: NotificationTargetType,

        @Column(nullable = false)
        var notificationTargetValue: String,

        @Column(nullable = false)
        var thresholdCount: Long,

        @Column(nullable = false)
        var thresholdDurationInSeconds: Long,

        @Column
        var createdAt: Date? = null,

        @Column
        var updatedAt: Date? = null
)