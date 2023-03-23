package com.example.calculator20

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var mainBuffer : TextView
    private lateinit var secondBuffer : TextView
    private var sign : String = " "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainBuffer = findViewById(R.id.mainBuffer)
        secondBuffer = findViewById(R.id.secondBuffer)
    }

    private fun isNum(char: String): Boolean{
        val numbs: Array<String> = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0")

        if(char in numbs)
            return true
        return false
    }

    private fun deleteLastZero(string: String): String {
        if("," !in string) return string

        var iterator: Int = string.length

        while (string[iterator - 1] == '0') iterator--

        if(string[iterator - 1] == ',') iterator--

        return string.substring(0, iterator)
    }

    @SuppressLint("SetTextI18n")
    fun calculator(view: View){
        if(mainBuffer.text == "Error"){
            mainBuffer.text = ""
            mainBuffer.setTextColor(getColor(R.color.normal_color))
        }

        if(view.id != R.id.buttonDelete){
            val button: Button = findViewById(view.id)

            if(isNum(button.text.toString()) || (button.text == "," && "," !in mainBuffer.text.toString())){
                if(button.text == "," && (mainBuffer.text.isEmpty() || mainBuffer.text == "0")){
                    mainBuffer.text = "0,"
                } else if((mainBuffer.text.length != 1 || mainBuffer.text != "0") && mainBuffer.text.length < 12){
                    mainBuffer.text = mainBuffer.text.toString() + button.text.toString()
                } else if(button.text != "0" && mainBuffer.text.length < 12){
                    mainBuffer.text = button.text
                }
            } else if(button.text != ",") {
                if(button.text == "±"){
                    if(mainBuffer.text != "0" && mainBuffer.text.isNotEmpty()){
                        if(mainBuffer.text[0] != '-') {
                            mainBuffer.text = "-" + mainBuffer.text
                        } else {
                            mainBuffer.text = mainBuffer.text.substring(1, mainBuffer.text.length)
                        }
                    }
                } else if (button.text == "AC") {
                    mainBuffer.text = ""
                    secondBuffer.text = ""
                    sign = ""
                } else {
                    mainBuffer.text = deleteLastZero(mainBuffer.text.toString())

                    if(secondBuffer.text.isEmpty() || (isNum(secondBuffer.text[secondBuffer.text.length - 1].toString())) && mainBuffer.text.isNotEmpty()){
                        sign = button.text.toString()

                        if(sign == "=") sign = ""

                        if(mainBuffer.text.isEmpty()) mainBuffer.text = "0"

                        secondBuffer.text = mainBuffer.text.toString() + sign
                        mainBuffer.text = ""
                    } else {
                        if(mainBuffer.text.isEmpty()){
                            sign = button.text.toString()

                            if(sign == "=") sign = ""

                            if(isNum(secondBuffer.text[secondBuffer.text.length - 1].toString())){
                                secondBuffer.text = secondBuffer.text.toString() + sign
                            } else {
                                secondBuffer.text = secondBuffer.text.substring(0, secondBuffer.text.length - 1) + sign
                            }
                        } else {
                            val leftOperand: String = secondBuffer.text.substring(0, secondBuffer.text.length - 1).replace(',','.')
                            val rightOperand: String  = mainBuffer.text.toString().replace(',','.')
                            var answer = 0.0

                            when(sign){
                                "+" -> answer = leftOperand.toDouble() + rightOperand.toDouble()
                                "-" -> answer = leftOperand.toDouble() - rightOperand.toDouble()
                                "÷" -> answer = leftOperand.toDouble() / rightOperand.toDouble()
                                "×" -> answer = leftOperand.toDouble() * rightOperand.toDouble()
                                "%" -> answer = leftOperand.toDouble() % rightOperand.toDouble()
                            }

                            sign = button.text.toString()

                            if(sign == "=") sign = ""

                            val stringAnswer = deleteLastZero(String.format("%.10f", answer))

                            if(stringAnswer == "NaN" || stringAnswer == "Infinity" || stringAnswer.length > 19){
                                mainBuffer.text = "Error"
                                mainBuffer.setTextColor(getColor(R.color.error_color))
                                secondBuffer.text = ""
                                sign = ""
                            } else {
                                secondBuffer.text = stringAnswer + sign
                                mainBuffer.text = ""
                            }
                        }
                    }
                }
            }
        } else if(mainBuffer.text.isNotEmpty()){
            mainBuffer.text = mainBuffer.text.substring(0, mainBuffer.text.length - 1)
            if(mainBuffer.text == "-") mainBuffer.text = ""
        }
    }
}
