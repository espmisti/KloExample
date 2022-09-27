package com.wisdomegypt.appqd.obfuscation.list

class TypeBoolean {
    fun type_1(value_1: Boolean, value_2: Boolean) : Boolean {
        return value_1 != null &&
                value_2 != null &&
                value_1 != value_2
    }
    fun type_2(value_1: Boolean, value_2: Boolean) : Boolean {
        return value_1 != null &&
                value_2 != null &&
                value_1 == value_2 &&
                value_1 &&
                value_2
    }
}