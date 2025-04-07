package com.lafuente.test.grpcchallengelafuente.service

import com.lafuente.test.grpcchallengelafuente.generated.UserActivity
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class UserActivityFactory(
    @Value("\${user.activity.delay-ms:500}")
    private val delayMillis: Long
) {

    fun generateActivities(userId: String): List<UserActivity> {
        if (userId == "999") {
            throw IllegalStateException("Simulación de error en actividades del usuario $userId")
        }

        return listOf(
            "Inicio de sesión",
            "Visualizó perfil",
            "Consultó historial",
            "Cierre de sesión"
        ).mapIndexed { index, activity ->
            val timestamp = Instant.now().plusSeconds(index * 60L).toString()
            UserActivity.newBuilder()
                .setTimestamp(timestamp)
                .setActivity(activity)
                .build()
        }
    }

    fun delayIfConfigured() {
        if (delayMillis > 0) {
            Thread.sleep(delayMillis)
        }
    }
}
