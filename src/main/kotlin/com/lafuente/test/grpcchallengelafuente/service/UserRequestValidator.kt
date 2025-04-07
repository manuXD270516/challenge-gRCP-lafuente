package com.lafuente.test.grpcchallengelafuente.service

import com.lafuente.test.grpcchallengelafuente.generated.GetUserRequest
import org.springframework.stereotype.Component

@Component
class UserRequestValidator {

    fun validate(request: GetUserRequest) {
        require(request.userId.isNotBlank()) { "El campo userId no puede estar vacío." }
    }
}
