package com.rizkima.cyberbullyingffapi.model

import jakarta.persistence.*
import java.util.*

@Entity
class AlertHistory(
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        @Id
        var id: Long? = null,

        @Column(nullable = false)
        var sessionId: String,

        @Column
        var createdAt: Date? = null,

        @Column
        var updatedAt: Date? = null
)