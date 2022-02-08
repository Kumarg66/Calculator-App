package com.example.calculatorapp

class Operations  {
    private fun replaceN(string: String): String {

        var array = StringBuffer(string)
        if (array[0] == '-')
            array.setCharAt(0, 'n')
        var i = 0
        while (i < array.length) {
            if (array[i] == '-') {
                if (array[i - 1] == '+' || array[i - 1] == '/' || array[i - 1] == '*' || array[i - 1] == '(')
                    array.setCharAt(i, 'n')
            }
            i++
        }
        return array.toString()
    }

    private fun replaceR(string: String): String {

        var array = StringBuffer(string)
        if (array[0] == '√')
            array.setCharAt(0, 'r')
        var i = 0
        while (i < array.length) {
            if (array[i] == '√') {
                if (array[i - 1] == '+' || array[i - 1] == '/' || array[i - 1] == '*' || array[i - 1] == '(')
                    array.setCharAt(i, 'r')
            }
            i++
        }
        return array.toString()
    }

    fun result(string: String): String {
        var stringN = replaceN(string)
        var stringNR=replaceR(stringN)
        var postfix = MainActivity.InfixToPostfix().postfixConversion(stringNR)
        if (postfix == "Error")
            return postfix
        return try {
            var evaluation = MainActivity.ArithmeticEvaluation().evaluation(postfix)
            evaluation.toString()
        } catch (e: Exception) {
            "Error"
        }
    }

}
