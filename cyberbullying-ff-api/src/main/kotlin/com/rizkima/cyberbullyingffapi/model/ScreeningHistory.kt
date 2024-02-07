package com.rizkima.cyberbullyingffapi.model

import com.rizkima.cyberbullyingffapi.enum.ScreeningType
import jakarta.persistence.*
import java.util.*

@Entity
class ScreeningHistory(
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Id
        var id: Long? = null,

        @Column(nullable = false)
        var sessionId: String,

        @Column(nullable = false)
        @Enumerated(EnumType.STRING)
        var screeningType: ScreeningType,

        @Column(nullable = false)
        var screeningCount: Long,

        @Column
        var createdAt: Date? = null,

        @Column
        var updatedAt: Date? = null
)