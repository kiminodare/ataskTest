package com.aldera.atasktest.utils

class validationNumber {
    fun isNumberValid(number: String): String {
        val replaceString = number
            .replace(" ", "")
            .replace("|", "1")
            .replace("I", "1")
            .replace("l", "1")
            .replace("o", "0")
            .replace("O", "0")
            .replace("S", "5")
            .replace("s", "5")
            .replace("B", "8")
            .replace("b", "8")
            .replace("q", "9")
            .replace("Q", "9")
            .replace("g", "9")
            .replace("G", "9")
            .replace("D", "0")
            .replace("d", "0")
            .replace("A", "4")
            .replace("a", "4")
            .replace("t", "7")
            .replace("T", "7")
            .replace("Z", "2")
            .replace("z", "2")
            .replace("E", "3")
            .replace("e", "3")
            .replace("L", "1")
            .replace("I", "1")
            .replace("i", "1")
            .replace("O", "0")
            .replace(":", "/")
            .replace(";", "/")
            .replace("=", "")
        val replacedWords = replaceString.split(" ")
            .map { word ->
                if (word.matches(Regex("[xX]+"))) {
                    word // Leave 'x' or 'X' unchanged
                } else {
                    word.replace("x", "*").replace("X", "*")
                }
            }
            .joinToString(" ")

        return replacedWords
    }
}