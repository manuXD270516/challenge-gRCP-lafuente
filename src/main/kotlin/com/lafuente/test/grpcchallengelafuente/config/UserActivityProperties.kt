package com.lafuente.test.grpcchallengelafuente.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "user.activity")
class UserActivityProperties {
    var intervalSeconds: Long = 60L // Intervalo por defecto de 60 segundos
}
