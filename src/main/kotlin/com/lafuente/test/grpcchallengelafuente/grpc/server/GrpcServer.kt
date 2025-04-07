package com.lafuente.test.grpcchallengelafuente.grpc.server

import com.lafuente.test.grpcchallengelafuente.service.UserServiceImpl
import io.grpc.Server
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.IOException
import javax.annotation.PreDestroy

@Component
class GrpcServer(
    private val userService: UserServiceImpl,
    @Value("\${grpc.server.port:9090}") private val port: Int
) {

    private val logger = LoggerFactory.getLogger(GrpcServer::class.java)
    private var server: Server? = null

    @jakarta.annotation.PostConstruct
    fun start() {
        try {
            server = NettyServerBuilder
                .forPort(port)
                .addService(userService)
                .build()
                .start()

            logger.info("✅ Servidor gRPC iniciado en el puerto $port")

            Runtime.getRuntime().addShutdownHook(Thread {
                logger.info("🛑 Señal de apagado recibida. Deteniendo servidor gRPC...")
                stop()
            })

            server?.awaitTermination()
        } catch (e: IOException) {
            logger.error("❌ Error al iniciar el servidor gRPC", e)
        }
    }

    @PreDestroy
    fun stop() {
        server?.shutdown()?.also {
            logger.info("🛑 Servidor gRPC detenido.")
        }
    }
}