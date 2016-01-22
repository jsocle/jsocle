package com.github.jsocle.form.test.fields

import com.github.jsocle.form.Form
import com.github.jsocle.form.fields.IntField
import com.github.jsocle.form.test.parameters
import org.junit.Assert
import org.junit.Test

public class IntFieldTest {
    public class TestForm : Form() {
        val age by IntField()
    }

    @Test public fun testValue() {
        parameters("age" to "23") {
            val form = TestForm()
            Assert.assertEquals(23, form.age.value)
        }
    }

    @Test public fun testNullValue() {
        parameters {
            val form = TestForm()
            Assert.assertNull(form.age.value)
        }
    }
}