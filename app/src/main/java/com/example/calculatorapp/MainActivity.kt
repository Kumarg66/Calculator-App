package com.example.calculatorapp


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.calculatorapp.databinding.ActivityMainBinding
import java.util.*
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    var dotValue: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.getRoot())
        supportActionBar?.hide()

        //Numerical values
        binding.zero.setOnClickListener { showView("0") }
        binding.showView1.setOnClickListener { showView("1") }
        binding.showView2.setOnClickListener { showView("2") }
        binding.showView3.setOnClickListener { showView("3") }
        binding.showView4.setOnClickListener { showView("4") }
        binding.showView5.setOnClickListener { showView("5") }
        binding.showView6.setOnClickListener { showView("6") }
        binding.showView7.setOnClickListener { showView("7") }
        binding.showView8.setOnClickListener { showView("8") }
        binding.showView9.setOnClickListener { showView("9") }

        //operations
        binding.operatorplus.setOnClickListener { operator(it) }
        binding.operatorminus.setOnClickListener { operator(it) }
        binding.operatormul.setOnClickListener { operator(it) }
        binding.operatordivide.setOnClickListener { operator(it) }
        binding.operatorroot.setOnClickListener { operator(it) }
        binding.clearAll.setOnClickListener { clearAll(it) }
        binding.clearOne.setOnClickListener { clearOne(it) }
        binding.bracketstart.setOnClickListener { brackets(it) }
        binding.bracketend.setOnClickListener { brackets(it) }
        binding.dot.setOnClickListener { dot(it) }


    }


    private fun showView(s: String) {
        binding.textView.append(s.trim())
        return

        var str = binding.textView.text.last()
        if (str == ')') {
            binding.textView.append("*" + (s.trim()))
            return
        }
        binding.textView.append(s.trim())
    }


    fun dot(view: View) {
        if (!dotValue) {
            binding.textView.append(".")
            dotValue = true
        }
    }

    fun equal(view: View) {
        dotValue = false
        var string = binding.textView.text.toString()
        if (string != "") {
            var resultString = Operations().result(string)
            if (resultString != "Error" && resultString.last() == '0')
                binding.tvResult.text = resultString.substring(0, resultString.length - 2)
            else
                binding.tvResult.text = resultString
        }
    }

    fun brackets(view: View) {
        if ((view as TextView).text == "(" && binding.textView.text.toString() == "") {
            binding.textView.append((view).text.trim())
        } else if ((view).text == "(") {
            var str = binding.textView.text.last()
            if (str.isDigit())
                binding.textView.append("*(")
            else
                binding.textView.append("(")
        } else if (binding.textView.text.toString() != "" && (view).text == ")") {
            var openCount = binding.textView.text.toString().filter { it == '(' }.count()
            var closeCount = binding.textView.text.toString().filter { it == ')' }.count()
            if (closeCount < openCount)
                binding.textView.append((view).text.trim())
        }
    }

    fun clearAll(view: View) {
        dotValue = false
        binding.textView.text = ""
        binding.tvResult.text = "0"
    }

    fun root(view: View) {
        try {
            dotValue = false
            var string = binding.textView.text.toString()
            if (string != "") {
                var resultString = Operations().result(string)
                if (resultString != "Error" && resultString.last() == '0') {
                    val res = sqrt((resultString.substring(0, resultString.length - 2)).toDouble())
                    binding.tvResult.text = res.toString()
                } else {
                    binding.tvResult.text = "$resultString"
                }
            }

        } catch (e: Exception) {
            binding.tvResult.text = e.toString()

        }
    }

    fun clearOne(view: View) {

        if (binding.textView.text.toString() == "") {
            return
        } else if (binding.textView.text.last() == '.') {
            binding.textView.text = binding.textView.text.dropLast(1)
            dotValue = false
        } else
            binding.textView.text = binding.textView.text.dropLast(1)
    }

    fun operator(view: View) {
        dotValue = false
        if (binding.textView.text.toString() == "" && (view as TextView).text == "√") {
            Toast.makeText(this, "Please put a value before √", Toast.LENGTH_SHORT).show()
            return
        }
        if (binding.textView.text.toString() == "" && (view as TextView).text != "-") {
            return
        } else if (binding.textView.text.toString() == "" && (view as TextView).text == "-") {
            binding.textView.append(view.text.trim())
            return
        }
        var str = binding.textView.text.last()
        if (((view as TextView).text.toString() == "-" && str != '-') || ((view as TextView).text.toString() == "√" && str != '√')) {
            binding.textView.append((view).text.trim())
            return
        }
        if (str == '+' || str == '-' || str == '*' || str == '/' || str == '√')
            return
        binding.textView.append((view).text.trim())
    }

    class ArithmeticEvaluation {
        private fun notOperator(char: Char): Boolean = when (char) {
            '+', '-', '/', '*', '√' -> false
            else -> true
        }

        fun evaluation(string: String): Double {
            var str = ""
            var stack = Stack<Double>()
            for (ch in string) {
                if (notOperator(ch) && ch != ' ') {
                    str += ch
                } else if (ch == ' ' && str != "") {
                    var str1 = str.replace('n', '-')
                    var str2 = str1.replace('r', '√')
                    if (str2.contains("√")) {
                        str2 = str2.split("√")[1]
                        stack.push(Math.sqrt(str2.toDouble()))
                    } else {
                        stack.push(str2.toDouble())
                    }

                    str = ""
                } else if (!notOperator(ch)) {
                    var val1: Double
                    var val2: Double
                    if (ch.equals("√")) {
                        val2 = stack.pop()
                        val1 = stack.pop()
                    } else {
                        val1 = stack.pop()
                        val2 = stack.pop()
                    }
                    when (ch) {
                        '+' -> stack.push(val2 + val1)
                        '-' -> stack.push(val2 - val1)
                        '/' -> stack.push(val2 / val1)
                        '*' -> stack.push(val2 * val1)
                        '√' -> stack.push(val2 * Math.sqrt(val1))
                    }
                }
            }
            return stack.pop();
        }
    }


    class InfixToPostfix {
        private fun notNumeric(ch: Char): Boolean = when (ch) {
            '+', '-', '*', '/', '(', ')', '√' -> true
            else -> false
        }

        private fun operatorPrecedence(ch: Char): Int = when (ch) {
            '+', '-' -> 1
            '*', '/', '√' -> 2
            else -> -1
        }

        fun postfixConversion(string: String): String {
            var result = ""
            val st = Stack<Char>()
            for (s in string) {
                if (!notNumeric(s)) {
                    result += s
                } else if (s == '(')
                    st.push(s)
                else if (s == ')') {
                    while (!st.isEmpty() && st.peek() != '(')
                        result += " " + st.pop()
                    st.pop()
                } else {
                    while (!st.isEmpty() && operatorPrecedence(s) <= operatorPrecedence(st.peek())) {
                        result += " ${st.pop()}"
                    }
                    st.push(s)
                    result += " "
                }
            }
            result += " "
            while (!st.isEmpty()) {
                if (st.peek() == '(') return "Error"
                result += st.pop() + " "
            }
            return result.trim()
        }
    }


}

