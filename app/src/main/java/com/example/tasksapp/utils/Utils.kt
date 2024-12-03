package com.example.tasksapp.utils

import org.json.JSONObject

object Utils {
    fun IsJwtValid(jwt: String?): Boolean {
        if (jwt.isNullOrEmpty()) return false

        return try {
            val parts = jwt.split(".")
            if (parts.size != 3) return false

            val payload = String(android.util.Base64.decode(parts[1], android.util.Base64.URL_SAFE))
            val expiry = JSONObject(payload).getLong("exp") * 1000
            System.currentTimeMillis() < expiry
        } catch (e: Exception) {
            false
        }
    }
}