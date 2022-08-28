package com.task.flybytsassign.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.task.flybytsassign.R
import com.task.flybytsassign.databinding.ActivityLoginBinding
import com.task.flybytsassign.repository.UserRepository
import com.task.flybytsassign.ui.viewmodel.MainViewModel
import com.task.flybytsassign.ui.viewmodel.MainViewModelFactory


class LoginActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityLoginBinding
    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        val repository = UserRepository()
        mainViewModel =
            ViewModelProvider(this, MainViewModelFactory(repository)).get(MainViewModel::class.java)

        initView()
        setOnClickListener()
    }

    private fun initView() {
    }

    private fun setOnClickListener() = with(binding) {
        btnLogin.setOnClickListener(this@LoginActivity)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnLogin -> {
                onLoginClick()
            }
        }
    }

    private fun onLoginClick() = with(binding) {
        val userName = editTextUserName.text?.trim().toString()
        val password = editTextPassword.text?.trim().toString()
        if (userName.isNullOrEmpty().not() && password.isNullOrEmpty()
                .not() && userName == "flytbase_assignment" && password == "123456789"
        ) {
            val i = Intent(applicationContext, MainActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(i)
        } else {
            Toast.makeText(
                this@LoginActivity, "Please enter valid username and password",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}