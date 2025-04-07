package com.lafuente.test.grpcchallengelafuente

import com.lafuente.test.grpcchallengelafuente.generated.GetUserRequest
import com.lafuente.test.grpcchallengelafuente.generated.UserActivity
import com.lafuente.test.grpcchallengelafuente.generated.UserResponse
import com.lafuente.test.grpcchallengelafuente.service.UserActivityFactory
import com.lafuente.test.grpcchallengelafuente.service.UserRequestValidator
import com.lafuente.test.grpcchallengelafuente.service.UserResponseFactory
import com.lafuente.test.grpcchallengelafuente.service.UserServiceImpl
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.grpc.stub.StreamObserver
import org.junit.jupiter.api.*
import org.junit.platform.commons.logging.LoggerFactory
import org.mockito.Mockito.*
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever


class UserServiceImplTest {

    private val logger = LoggerFactory.getLogger(UserServiceImplTest::class.java)

    private val validator: UserRequestValidator = mock()
    private val responseFactory: UserResponseFactory = mock()
    private val activityFactory: UserActivityFactory = mock()

    private lateinit var service: UserServiceImpl
    private lateinit var responseObserver: StreamObserver<UserResponse>
    private lateinit var activityObserver: StreamObserver<UserActivity>

    @BeforeEach
    fun setUp() {
        service = UserServiceImpl(validator, responseFactory, activityFactory)
        responseObserver = mock()
        activityObserver = mock()
    }

    @Test
    fun `getUser should return user response successfully`() {
        val request = GetUserRequest.newBuilder().setUserId("123").build()
        val expectedResponse = UserResponse.newBuilder()
            .setUserId("123")
            .setName("Usuario 123")
            .setEmail("123@example.com")
            .build()

        whenever(responseFactory.createUserResponse("123")).thenReturn(expectedResponse)

        service.getUser(request, responseObserver)

        verify(validator).validate(request)
        verify(responseObserver).onNext(expectedResponse)
        verify(responseObserver).onCompleted()
        verifyNoMoreInteractions(responseObserver)
    }

    @Test
    fun `getUser should return INVALID_ARGUMENT if userId is blank`() {
        val request = GetUserRequest.newBuilder().setUserId("").build()

        doThrow(IllegalArgumentException("El campo userId no puede estar vacío."))
            .whenever(validator).validate(request)

        service.getUser(request, responseObserver)

        argumentCaptor<Throwable>().apply {
            verify(responseObserver).onError(capture())
            assert(firstValue is StatusRuntimeException)
            assert((firstValue as StatusRuntimeException).status.code == Status.Code.INVALID_ARGUMENT)
        }
    }

    @Test
    fun `getUser should return NOT_FOUND if user not found`() {
        val request = GetUserRequest.newBuilder().setUserId("0").build()

        whenever(responseFactory.createUserResponse("0"))
            .thenThrow(NoSuchElementException("No se encontró el usuario con ID 0"))

        service.getUser(request, responseObserver)

        argumentCaptor<Throwable>().apply {
            verify(responseObserver).onError(capture())
            assert(firstValue is StatusRuntimeException)
            assert((firstValue as StatusRuntimeException).status.code == Status.Code.NOT_FOUND)
        }
    }

    @Test
    fun `getUserActivityStream should return list of activities`() {
        val request = GetUserRequest.newBuilder().setUserId("123").build()

        val activities = listOf(
            UserActivity.newBuilder().setTimestamp("t1").setActivity("a1").build(),
            UserActivity.newBuilder().setTimestamp("t2").setActivity("a2").build()
        )

        whenever(activityFactory.generateActivities("123")).thenReturn(activities)

        service.getUserActivityStream(request, activityObserver)

        activities.forEach {
            verify(activityObserver).onNext(it)
        }
        verify(activityFactory, atLeastOnce()).delayIfConfigured()
        verify(activityObserver).onCompleted()
    }

    @Test
    fun `getUserActivityStream should return INTERNAL error if factory fails`() {
        val request = GetUserRequest.newBuilder().setUserId("999").build()

        whenever(activityFactory.generateActivities("999"))
            .thenThrow(RuntimeException("Simulación de error"))

        service.getUserActivityStream(request, activityObserver)

        argumentCaptor<Throwable>().apply {
            verify(activityObserver).onError(capture())
            assert(firstValue is StatusRuntimeException)
            assert((firstValue as StatusRuntimeException).status.code == Status.Code.INTERNAL)
        }
    }
}