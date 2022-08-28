package com.task.flybytsassign.ui.activity

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.task.flybytsassign.*
import com.task.flybytsassign.databinding.ActivityMainBinding
import com.task.flybytsassign.repository.UserRepository
import com.task.flybytsassign.ui.adapter.CustomAdapter
import com.task.flybytsassign.ui.model.LastOperationData
import com.task.flybytsassign.ui.utils.Validator.getExpressionList
import com.task.flybytsassign.ui.utils.Validator.isValidInput
import com.task.flybytsassign.ui.viewmodel.MainViewModel
import com.task.flybytsassign.ui.viewmodel.MainViewModelFactory
import java.util.*
import kotlin.collections.ArrayDeque


class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityMainBinding
    lateinit var mainViewModel: MainViewModel
    lateinit var adapter: CustomAdapter
    var lastOperations = LinkedList<LastOperationData>()
    var isHistory: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val repository = UserRepository()
        mainViewModel =
            ViewModelProvider(this, MainViewModelFactory(repository)).get(MainViewModel::class.java)

        initView()
        setOnClickListener()
    }

    private fun initView() = with(binding) {
        setAdapter()
        editTextExp.setOnEditorActionListener(
            OnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || (event.action === KeyEvent.ACTION_DOWN
                            && event.keyCode === KeyEvent.KEYCODE_ENTER)
                ) {
                    onOkClick()
                    return@OnEditorActionListener true
                }
                false
            })
    }

    private fun setAdapter() = with(binding) {
        recyclerview.layoutManager = LinearLayoutManager(this@MainActivity)
        adapter = CustomAdapter(lastOperations)
        recyclerview.adapter = adapter
    }

    private fun setOnClickListener() = with(binding) {
        btnHistory.setOnClickListener(this@MainActivity)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnHistory -> {
                onHistoryClick()
            }
        }
    }

    private fun onOkClick() = with(binding) {
        val exp = editTextExp.text.toString().trim()
        if (exp.isNullOrEmpty().not()) {
            val expList = getExpressionList(exp)
            if (isValidInput(exp)) {
                val postFixExp = infixToPostfix(expList)!!
                val evaluateExp = evaluatePostfix(postFixExp)
                editTextExp.setText(evaluateExp.toString())
                editTextExp.setSelection(editTextExp.length())
                if (lastOperations.size > 10)
                    lastOperations.removeFirst()
                lastOperations.add(LastOperationData(exp, evaluateExp.toString()))
                if (isHistory)
                    adapter.notifyDataSetChanged()
            } else {
                showToast(getString(R.string.label_enter_valid_expression))
            }
        } else {
            showToast(getString(R.string.label_please_enter_expression))
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(
            this, message,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun onHistoryClick() = with(binding) {
        isHistory = !isHistory
        if (isHistory) {
            recyclerview.visibility = View.VISIBLE
            btnHistory.text = getString(R.string.label_hide_history)
            adapter.notifyDataSetChanged()
        } else {
            recyclerview.visibility = View.GONE
            btnHistory.text = getString(R.string.label_show_history)
        }
    }

    private fun prec(ch: String): Int {
        when (ch) {
            "+", "-" -> return 1
            "*", "/" -> return 2
            "^" -> return 3
        }
        return -1
    }

    private fun customPrec(ch: String): Int {
        when (ch) {
            "-" -> return 1
            "/" -> return 2
            "+" -> return 3
            "*" -> return 4
        }
        return -1
    }

    private fun checkIsDigits(c: String): Boolean {
        return !(c == "+" || c == "-" || c == "*" || c == "/")
    }

    private fun infixToPostfix(exp: ArrayList<String>): String? {
        var result: String? = ""

        val stack: ArrayDeque<String> = ArrayDeque<String>()
        for (i in 0 until exp.size) {
            val c = exp[i]

            if (checkIsDigits(c)) result += " $c" else if (c == "(") stack.addLast(c) else if (c == ")") {
                while (!stack.isEmpty()
                    && stack.last() !== "("
                ) {
                    result += " " + stack.last()
                    stack.removeLast()
                }
                stack.removeLast()
            } else {
                while (!stack.isEmpty()
                    && customPrec(c) <= customPrec(stack.last())
                ) {
                    result += " " + stack.last()
                    stack.removeLast()
                }
                stack.addLast(c)
            }
        }

        while (!stack.isEmpty()) {
            if (stack.last() === "(") return "Invalid Expression"
            result = result + " " + stack.last()
            stack.removeLast()
        }
        return result?.trim()
    }

    private fun evaluatePostfix(exp: String): Int {
        val stack: Stack<Int> = Stack()

        var i = 0
        while (i < exp.length) {
            var c = exp[i]
            if (c == ' ') {
                i++
                continue
            } else if (Character.isDigit(c)) {
                var n = 0

                while (Character.isDigit(c)) {
                    n = n * 10 + (c - '0')
                    i++
                    c = exp[i]
                }
                i--

                stack.push(n)
            } else {
                val val1: Int = if (!stack.isEmpty()) stack.pop() else 0
                val val2: Int = if (!stack.isEmpty()) stack.pop() else 0
                when (c) {
                    '+' -> stack.push(val2 + val1)
                    '-' -> stack.push(val2 - val1)
                    '/' -> stack.push(val2 / val1)
                    '*' -> stack.push(val2 * val1)
                }
            }
            i++
        }
        return stack.pop()
    }


}