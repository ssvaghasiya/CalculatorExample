package com.task.flybytsassign.ui.utils

import android.util.Log
import java.util.ArrayList

object Validator {

    fun isValidInput(exp: String): Boolean {
        val list = getExpressionList(exp)
        return isValidExpression(list)
    }

    private fun isValidExpression(expList: ArrayList<String>): Boolean {
        if (expList.size < 1)
            return false
//        else if (expList.size % 2 != 1)
//            return false
        else if (!checkOddPos(expList))
            return false
        return true
    }

    private fun checkOddPos(expList: ArrayList<String>): Boolean {
        /*expList.forEachIndexed { index, s ->
            if (index % 2 == 0) {
                if ((!isNumber(s) && isNumber(expList[index - 1])))
                    return false
            }
        }*/
        for (index in 1 until expList.size) {
            if (!((!isNumber(expList[index]) && isNumber(expList[index - 1])) || (isNumber(expList[index]) && !isNumber(
                    expList[index - 1]
                ))
                        )
            ) {
                return false
            }
        }
        return true
    }

    private fun isNumber(str: String): Boolean {
        return str.matches("-?\\d+(\\.\\d+)?".toRegex())
    }

    fun getExpressionList(exp: String): ArrayList<String> {
        val list = ArrayList<String>()
        var temp = ""
        for (i in exp.indices) {
            if (Character.isLetterOrDigit(exp[i]))
                temp += exp[i]
            else {
                if (temp.isNullOrEmpty().not())
                    list.add(temp)
                list.add(exp[i].toString())
                temp = ""
            }

            if (i == (exp.length - 1)) {
                list.add(temp)
                temp = ""
            }
        }
        return list
    }

}