package com.rizkima.cyberbullyingffapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class CyberbullyingFfApiApplication

fun main(args: Array<String>) {
    runApplication<CyberbullyingFfApiApplication>(*args)
}
