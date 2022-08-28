package com.task.flybytsassign.ui.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ValidatorTest {

    @Test
    fun isValidExpression() {
        val result = Validator.isValidInput("50+20/10")
        assertThat(result).isTrue()
    }
}