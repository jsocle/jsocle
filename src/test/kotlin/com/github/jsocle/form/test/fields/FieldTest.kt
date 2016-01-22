package com.github.jsocle.form.test.fields

import com.github.jsocle.form.Form
import com.github.jsocle.form.fields.DoubleField
import com.github.jsocle.form.test.parameters
import org.junit.Assert
import org.junit.Test

class FieldTest {
    @Test
    fun testDoubleField() {
        parameters("validInt" to "123", "valid" to "123.456", "notValid" to "notValid") {
            val form = object : Form() {
                val validInt by DoubleField()
                val valid by DoubleField()
                val notValid by DoubleField()
            }
            Assert.assertEquals(123.0, form.validInt.value)
            Assert.assertEquals(123.456, form.valid.value)
            Assert.assertEquals(null, form.notValid.value)
        }
    }
}