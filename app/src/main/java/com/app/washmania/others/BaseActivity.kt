package com.app.washmania.others

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.os.Build

abstract class BaseActivity : AppCompatActivity() {

    var isActivityVisible: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(getLayout())
        initResources()
        initListeners()

    }

    override fun onResume() {
        super.onResume()
        isActivityVisible = true
    }

    override fun onPause() {
        super.onPause()
        isActivityVisible = false
    }


    abstract fun initResources()
    abstract fun initListeners()
    abstract fun getLayout(): Int

}