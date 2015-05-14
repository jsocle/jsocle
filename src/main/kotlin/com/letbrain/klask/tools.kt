package com.letbrain.klask

fun <T, R> Iterable<T>.firstMapNotNull(function: Function1<T, R>): R {
    for (i in this) {
        val result = function(i)
        if (result != null) {
            return result;
        }
    }
    return null;
}