package com.lafuente.test.grpcchallengelafuente.domain.repository

import com.lafuente.test.grpcchallengelafuente.domain.model.User
import org.springframework.stereotype.Component

interface UserRepository {
    fun findUserById(userId: String): User
}

@Component
class InMemoryUserRepository : UserRepository {
    override fun findUserById(userId: String): User {
        return User(userId, "Usuario $userId", "$userId@email.com")
    }
}
