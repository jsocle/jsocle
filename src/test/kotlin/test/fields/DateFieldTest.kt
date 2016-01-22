package com.github.jsocle.form.test.fields

import com.github.jsocle.form.Form
import com.github.jsocle.form.fields.DateField
import com.github.jsocle.form.test.parameters
import org.junit.Assert
import org.junit.Test
import java.util.*

class DateFieldTest {
    @Test
    fun test() {
        parameters("valid" to "1234-05-06 12:34:56", "invalid" to "invalid", "empty" to " ")
        val form = object : Form() {
            val valid by DateField()
            val invalid by DateField()
            val changed by DateField()
            val empty by DateField()
        }

        Assert.assertTrue(!form.valid.hasErrors)
        Assert.assertTrue(form.invalid.hasErrors)
        form.changed.value = Calendar.getInstance().apply { set(1234, 5, 6, 7, 8, 6) }.time
        Assert.assertEquals(
                """<input name="changed" type="text" value="1234-06-06 07:08:06">""", form.changed.render().toString()
        )
        Assert.assertFalse(form.empty.hasErrors)
    }
}

