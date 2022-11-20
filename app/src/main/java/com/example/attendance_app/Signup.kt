package com.example.attendance_app

import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.attendance_app.databinding.ActivitySignupBinding

class Signup : AppCompatActivity() {

     lateinit var username: EditText
     lateinit var password: EditText
     lateinit var email: EditText
     lateinit var signupBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        username=findViewById(R.id.usernameSignup)
        password=findViewById(R.id.passwordSignup)
        email=findViewById(R.id.emailSignup)
        signupBtn=findViewById(R.id.signupBtn)


    }
}