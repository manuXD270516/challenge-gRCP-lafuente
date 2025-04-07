package com.lafuente.test.grpcchallengelafuente.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "grpc.server")
class GrpcServerProperties {
    var port: Int = 9090
    var securityEnabled: Boolean = false
}
