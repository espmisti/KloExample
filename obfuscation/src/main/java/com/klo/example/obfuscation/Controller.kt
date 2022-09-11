package com.klo.example.obfuscation

import com.klo.example.obfuscation.list.TypeBoolean
import com.klo.example.obfuscation.list.TypeInt
import com.klo.example.obfuscation.list.TypeString
import java.lang.reflect.Type

class Controller {
    fun obf() : Boolean{
        val list = arrayListOf<Boolean>(
            TypeInt().type_1((1..10).random(), (11..20).random()),
            TypeInt().type_2((100..200).random(), (1000..2000).random()),
            TypeBoolean().type_1(value_1 = true, value_2 = false),
            TypeBoolean().type_2(value_1 = true, value_2 = true),
            TypeString().type_1(value_1 = "Автобус", value_2 = "Шлюзовской"),
            TypeString().type_2(value_1 = "Ксгоущики", value_2 = "Друмдрум")
        )
        return list.random()
    }
}