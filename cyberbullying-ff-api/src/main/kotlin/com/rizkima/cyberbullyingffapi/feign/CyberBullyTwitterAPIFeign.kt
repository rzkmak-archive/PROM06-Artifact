package com.rizkima.cyberbullyingffapi.feign

import com.rizkima.cyberbullyingffapi.feign.request.ScreeningTwitterRequest
import com.rizkima.cyberbullyingffapi.feign.response.ScreeningTwitterResponse
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping

@ConfigurationPropertiesScan
@FeignClient(
        name = "cyber-bully-api-feign",
        url = "\${cyberBullyApiFeign.url}"
)
interface CyberBullyTwitterAPIFeign {
    @PostMapping("/v1/screening/twitter")
    fun screeningTwitterAPIExecute(screeningTwitterRequest: ScreeningTwitterRequest):
            ResponseEntity<ScreeningTwitterResponse>
}