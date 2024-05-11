package com.example.horsebooking.SinCuenta

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
class OlvidoContrasenaActivityTest {

    @Test
    fun `verificacion email`() {
        val email = "almudenaiparraguirre@gmail.com"
        val result = OlvidoContrasenaActivity.isValidEmail(email)
        assertThat(result).isTrue()
    }
}