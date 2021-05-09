package com.volio.order_food.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity(private val layout:Int) : AppCompatActivity(){
    protected abstract fun onCreatedView()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout)
        onCreatedView()
    }


}