package com.wisdomegypt.appqd.obfuscation.list

class TypeInt {
    fun type_1(value_1: Int, value_2: Int) : Boolean {
        return value_1 != null &&
            value_2 != null &&
            value_1 != value_2 &&
            value_1 != 0 &&
            value_1 + value_2 != 0 &&
            value_1 + value_2 != 100 &&
            value_2 != 20 &&
            value_2 + value_1 != 52-10
    }
    fun type_2(value_1: Int, value_2: Int) : Boolean {
        return value_1 != null &&
                value_2 != null &&
                value_1 != value_2 &&
                value_1 != 0 &&
                value_1 + value_2 != 0 &&
                value_1 + value_2 != 100 &&
                value_2 != 20 &&
                value_2 + value_1 != 52-10 &&
                value_1 > 99 &&
                value_2 < 3000
    }
}