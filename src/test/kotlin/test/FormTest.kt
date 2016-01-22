package com.github.jsocle.form.test

import com.github.jsocle.form.Form
import com.github.jsocle.form.add
import com.github.jsocle.form.fields.*
import com.github.jsocle.form.validators
import com.github.jsocle.form.validators.Required
import com.github.jsocle.html.elements.Input
import org.junit.Assert
import org.junit.Test

public class FormTest {
    @Test
    fun test() {
        class TestForm : Form() {
            val firstName by StringField()
            val lastName by StringField()
            val age by IntField()
            val page by IntField(0)
            val birthYear by IntField()
            val content by TextareaField()
            val style by StringField()
            val checked by BooleanField()
            val notChecked by BooleanField()
            val password by PasswordField()
            val sex by SelectStringField(choices = listOf("m" to "Male", "f" to "Female"))
            val hobbies by MultipleSelectStringField(
                    listOf("collector" to "Collecting", "artist" to "Performing arts",
                            "producer" to "Creative hobbies")
            )
            val trustJava by RadioStringField(listOf("yes" to "Yes", "no" to "No", "neutral" to "Neutral"))
            val genres by CheckboxStingField(
                    listOf("rock" to "Rock", "pop" to "Pop", "alternative" to "Alternative")
            )
        }

        parameters(
                "firstName" to " john ", "age" to "31", "birthYear" to "1980s", "content" to "<p>content</p>",
                "checked" to "true", "sex" to "f", "hobbies" to "artist", "hobbies" to "producer",
                "trustJava" to "no", "genres" to "rock", "genres" to "pop"
        )
        val form = TestForm()

        Assert.assertEquals("firstName", form.firstName.name)
        Assert.assertArrayEquals(arrayOf(" john "), form.firstName.raw)
        Assert.assertEquals("john", form.firstName.value)
        Assert.assertEquals(Input(name = "firstName", type = "text", value = " john "), form.firstName.render())
        // test value string setter
        form.firstName.value = "noah";
        Assert.assertEquals("noah", form.firstName.value)
        Assert.assertArrayEquals(arrayOf("noah"), form.firstName.raw)
        Assert.assertEquals(Input(name = "firstName", type = "text", value = "noah"), form.firstName.render())

        // test nullable data
        Assert.assertEquals("lastName", form.lastName.name)
        Assert.assertArrayEquals(arrayOf(), form.lastName.raw)
        Assert.assertNull(form.lastName.value)
        Assert.assertEquals(Input(name = "lastName", type = "text"), form.lastName.render())

        // test integer field
        Assert.assertEquals("age", form.age.name)
        Assert.assertArrayEquals(arrayOf("31"), form.age.raw)
        Assert.assertEquals(31, form.age.value)
        Assert.assertEquals(Input(type = "text", name = "age", value = "31"), form.age.render())
        // test integer value setter
        form.age.value = 42
        Assert.assertEquals(42, form.age.value)
        Assert.assertArrayEquals(arrayOf("42"), form.age.raw)
        Assert.assertEquals(Input(type = "text", name = "age", value = "42"), form.age.render())

        // test convert failed
        Assert.assertEquals(listOf("Not a valid integer value"), form.birthYear.errors)
        Assert.assertArrayEquals(arrayOf("1980s"), form.birthYear.raw)
        Assert.assertEquals(null, form.birthYear.value)
        Assert.assertEquals(Input(type = "text", name = "birthYear", value = "1980s"), form.birthYear.render())
        // test integer value setter when user input wrong values
        form.birthYear.value = 1980
        Assert.assertArrayEquals(arrayOf("1980"), form.birthYear.raw)
        Assert.assertEquals(1980, form.birthYear.value)
        Assert.assertEquals(Input(type = "text", name = "birthYear", value = "1980"), form.birthYear.render())

        // test default value was not applied.
        Assert.assertEquals(null, form.page.value)
        Assert.assertEquals(0, form.page.value ?: form.page.default)


        // test textarea
        Assert.assertEquals("<p>content</p>", form.content.value)
        Assert.assertEquals("""<textarea name="content">&lt;p&gt;content&lt;/p&gt;</textarea>""", form.content.render().toString())

        Assert.assertEquals(
                Input(name = "style", type = "text", style = "display: none;"),
                form.style.render { style = "display: none;" }
        )

        // test boolean
        Assert.assertTrue(form.checked.value ?: false)
        Assert.assertEquals(
                Input(name = "checked", type = "checkbox", value = "true", checked = "checked"), form.checked.render()
        )
        Assert.assertFalse(form.notChecked.value ?: false)
        Assert.assertEquals(Input(name = "notChecked", type = "checkbox", value = "true"), form.notChecked.render())

        // test password field
        form.password.value = "password"
        Assert.assertEquals("""<input name="password" type="password">""", form.password.render().toString())

        // test select field
        Assert.assertEquals(
                """<select name="sex"><option value="m">Male</option><option selected="selected" value="f">Female</option></select>""",
                form.sex.render().toString()
        )

        // test select multiple
        Assert.assertEquals(listOf("artist", "producer"), form.hobbies.values)
        Assert.assertEquals(
                """<select multiple="multiple" name="hobbies"><option value="collector">Collecting</option><option selected="selected" value="artist">Performing arts</option><option selected="selected" value="producer">Creative hobbies</option></select>""",
                form.hobbies.render().toString()
        )

        // test radio field
        Assert.assertEquals("no", form.trustJava.value)
        Assert.assertEquals(
                """<ul class="jsocle-form-field-radio"><li><label><input name="trustJava" type="radio" value="yes">Yes</label></li><li><label><input checked="checked" name="trustJava" type="radio" value="no">No</label></li><li><label><input name="trustJava" type="radio" value="neutral">Neutral</label></li></ul>""",
                form.trustJava.render().toString()
        )

        Assert.assertEquals(setOf("rock", "pop"), form.genres.values.toSet())
        Assert.assertEquals(
                """<ul class="jsocle-form-field-checkbox"><li><label><input checked="checked" name="genres" type="checkbox" value="rock">Rock</label></li><li><label><input checked="checked" name="genres" type="checkbox" value="pop">Pop</label></li><li><label><input name="genres" type="checkbox" value="alternative">Alternative</label></li></ul>""",
                form.genres.render().toString()
        )

        // test default value was applied.
        parameters()
        val defaultForm = TestForm()
        Assert.assertEquals(0, defaultForm.page.value)
    }

    @Test
    fun testValidate() {
        class TestForm : Form() {
            val string by StringField().apply { validators.add(Required()) }
        }

        parameters()
        // validateOnPost() should return false on GET
        Assert.assertFalse(TestForm().validateOnPost())

        parameters(method = "POST")
        val form = TestForm()
        Assert.assertFalse(form.validateOnPost())
        Assert.assertTrue(form.hasErrors)
        Assert.assertTrue(form.string.hasErrors)
        Assert.assertEquals(listOf("This field is required."), form.string.errors)
    }

    @Test
    fun testFields() {
        val form = object : Form() {
            val id by StringField()
            val password by PasswordField()
        }

        Assert.assertArrayEquals(arrayOf(form.id, form.password), form.fields)
    }

    @Test
    fun testCollectionAdd() {
        val list = arrayListOf(1)
        list.add(2, 3, 4)
        Assert.assertEquals(listOf(1, 2, 3, 4), list)
    }
}