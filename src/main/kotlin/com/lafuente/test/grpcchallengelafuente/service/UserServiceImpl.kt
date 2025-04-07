package com.lafuente.test.grpcchallengelafuente.service

import com.lafuente.test.grpcchallengelafuente.generated.GetUserRequest
import com.lafuente.test.grpcchallengelafuente.generated.UserActivity
import com.lafuente.test.grpcchallengelafuente.generated.UserResponse
import com.lafuente.test.grpcchallengelafuente.generated.UserServiceGrpc
import io.grpc.Status
import io.grpc.stub.StreamObserver
import org.springframework.stereotype.Service
import org.slf4j.LoggerFactory


@Service
class UserServiceImpl(
    private val validator: UserRequestValidator,
    private val responseFactory: UserResponseFactory,
    private val activityFactory: UserActivityFactory
) : UserServiceGrpc.UserServiceImplBase() {

    private val logger = LoggerFactory.getLogger(UserServiceImpl::class.java)

    override fun getUser(request: GetUserRequest, responseObserver: StreamObserver<UserResponse>) {
        try {
            logger.info("🔍 Solicitud recibida: ${request.userId}")
            validator.validate(request)

            val response = responseFactory.createUserResponse(request.userId)

            responseObserver.onNext(response)
            responseObserver.onCompleted()

            logger.info("✅ Respuesta enviada correctamente")
        } catch (ex: IllegalArgumentException) {
            logger.error("⚠️ Validación fallida: ${ex.message}")
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription(ex.message).asRuntimeException())
        } catch (ex: NoSuchElementException) {
            logger.error("⚠️ Usuario no encontrado: ${ex.message}")
            responseObserver.onError(Status.NOT_FOUND.withDescription(ex.message).asRuntimeException())
        } catch (ex: Exception) {
            logger.error("❌ Error inesperado: ${ex.message}")
            responseObserver.onError(Status.INTERNAL.withDescription("Error interno del servidor").asRuntimeException())
        }
    }

    override fun getUserActivityStream(request: GetUserRequest, responseObserver: StreamObserver<UserActivity>) {
        try {
            validator.validate(request)

            val activities = activityFactory.generateActivities(request.userId)
            activities.forEach {
                activityFactory.delayIfConfigured()
                responseObserver.onNext(it)
            }

            responseObserver.onCompleted()
        } catch (ex: IllegalArgumentException) {
            logger.error("⚠️ Validación fallida: ${ex.message}")
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription(ex.message).asRuntimeException())
        } catch (ex: Exception) {
            logger.error("❌ Error inesperado: ${ex.message}")
            responseObserver.onError(Status.INTERNAL.withDescription("Error al obtener actividades").asRuntimeException())
        }
    }
}
