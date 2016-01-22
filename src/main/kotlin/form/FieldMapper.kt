package com.github.jsocle.form

public abstract class FieldMapper<T: Any> {
    public abstract fun fromString(field: Field<T, *>, string: String?): T?
    public abstract fun toString(value: T?): String?
}