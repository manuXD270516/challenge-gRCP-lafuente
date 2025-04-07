package com.lafuente.test.grpcchallengelafuente.service

import com.lafuente.test.grpcchallengelafuente.generated.UserResponse
import org.springframework.stereotype.Component

@Component
class UserResponseFactory {

    fun createUserResponse(userId: String): UserResponse {
        if (userId == "0") {
            throw NoSuchElementException("No se encontró el usuario con ID $userId")
        }

        return UserResponse.newBuilder()
            .setUserId(userId)
            .setName("Usuario $userId")
            .setEmail("$userId@example.com")
            .build()
    }
}
