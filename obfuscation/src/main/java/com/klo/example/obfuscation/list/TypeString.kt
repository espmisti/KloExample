package com.klo.example.obfuscation.list

class TypeString {
    fun type_1 (value_1: String, value_2: String) : Boolean{
        return value_1 != null &&
                value_2 != null &&
                value_1.isNotEmpty() &&
                value_2.isNotEmpty() &&
                value_1.length != 0 &&
                value_2.length != 0 &&
                value_1.length > 5 &&
                value_2.length > 5
    }
    fun type_2 (value_1: String, value_2: String) : Boolean {
        return value_1 != null &&
                value_2 != null &&
                value_1.isNotEmpty() &&
                value_2.isNotEmpty() &&
                value_1.length != 0 &&
                value_2.length != 0 &&
                value_1.length + value_2.length != 0 &&
                value_1.length + value_2.length != 1000
    }
}