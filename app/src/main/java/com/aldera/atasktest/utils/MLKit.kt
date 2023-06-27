package com.aldera.atasktest.utils

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.aldera.atasktest.model.MathResults
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import net.objecthunter.exp4j.ExpressionBuilder

class MLKit {
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
    private val validationNumber = validationNumber()
    private val OperatorMathRegex = OperatorMath()
    fun TextRecognition(
        imageBitmap: Bitmap,
        context: Context,
        resultTextView: TextView,
    ) {
        val image = InputImage.fromBitmap(imageBitmap, 0)
        val resultsMath = ArrayList<MathResults>()
        val result = recognizer.process(image)
            .addOnSuccessListener { visionText ->
                val result = visionText.text
                val replaceString = validationNumber.isNumberValid(result)
                if (replaceString.contains("(")) {
                    val splitText = replaceString.split("\n")
                    if (splitText.size > 1) {
                        for (text in splitText) {
                            try {
                                val exp4jExpression = ExpressionBuilder(text).build()
                                val resultMath = exp4jExpression.evaluate()
                                val formattedResult = if (resultMath % 1 == 0.0) {
                                    resultMath.toLong().toString()
                                } else {
                                    resultMath.toString().removeSuffix(".0")
                                }
                                resultsMath.add(MathResults(text, formattedResult))
                            } catch (e: Exception) {
                                Log.d("TAG", "TextRecognition: ${e.message}")
                            }
                        }
                    } else {
                        try {
                            val exp4jExpression = ExpressionBuilder(splitText[0]).build()
                            val resultMath = exp4jExpression.evaluate()
                            val formattedResult = if (resultMath % 1 == 0.0) {
                                resultMath.toLong().toString()
                            } else {
                                resultMath.toString().removeSuffix(".0")
                            }
                            resultsMath.add(MathResults(splitText[0], formattedResult))
                        } catch (e: Exception) {
                            Log.d("TAG", "TextRecognition: ${e.message}")
                        }
                    }
                } else {
                    for (regex in OperatorMathRegex.Regex()) {
                        val matches = regex.regex.findAll(replaceString)
                        val splitMatches = replaceString.split("\n")
                        if (splitMatches.size > 1) {
                            try {
                                for (match in matches) {
                                    val exp4jExpression = ExpressionBuilder(match.value).build()
                                    val resultMath = exp4jExpression.evaluate()
                                    val formattedResult = if (resultMath % 1 == 0.0) {
                                        resultMath.toLong().toString()
                                    } else {
                                        resultMath.toString().removeSuffix(".0")
                                    }
                                    resultsMath.add(MathResults(match.value, formattedResult))
                                }
                            } catch (e: Exception) {
                                Log.d("TAG", "TextRecognition: ${e.message}")
                            }
                        } else {
                            try {
                                val exp4jExpression = ExpressionBuilder(splitMatches[0]).build()
                                val resultMath = exp4jExpression.evaluate()
                                val formattedResult = if (resultMath % 1 == 0.0) {
                                    resultMath.toLong().toString()
                                } else {
                                    resultMath.toString().removeSuffix(".0")
                                }
                                resultsMath.add(MathResults(splitMatches[0], formattedResult))
                            } catch (e: Exception) {
                                Log.d("TAG", "TextRecognition: ${e.message}")
                            }
                        }
                    }
                }
                val stringBuilder = StringBuilder()
                for ((index, value) in resultsMath.withIndex()) {
                    stringBuilder.append("${index + 1}: ${value.name} = ${value.result}\n")
                }
                resultTextView?.text = stringBuilder.toString()
            }
            .addOnFailureListener { e ->
                Log.d("TAG", "TextRecognition: ${e.message}")
            }
    }
}