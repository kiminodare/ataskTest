package com.aldera.atasktest

import com.aldera.atasktest.model.MathRegex

class Utils {
    companion object {
        fun OperatorMathRegex(): ArrayList<MathRegex> {

            val ArrayListRegexMath = arrayListOf<MathRegex>()
            ArrayListRegexMath.add(MathRegex("Addition", Regex("[0-9]+[+][0-9]+")))
            ArrayListRegexMath.add(MathRegex("Subtraction", Regex("[0-9]+[-][0-9]+")))
            ArrayListRegexMath.add(MathRegex("Multiplication", Regex("[0-9]+[*][0-9]+")))
            ArrayListRegexMath.add(MathRegex("Division", Regex("[0-9]+[/][0-9]+")))

            return ArrayListRegexMath
        }
    }
}