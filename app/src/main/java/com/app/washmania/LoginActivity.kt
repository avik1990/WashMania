package com.app.washmania

import android.content.Context
import android.content.Intent
import android.view.View
import com.app.washmania.others.BaseActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(), View.OnClickListener {

    lateinit var sontext:Context

    override fun initResources() {
        sontext=this
    }

    override fun initListeners() {
        rl_signup.setOnClickListener(View.OnClickListener {
            val i = Intent(sontext, RegisterActivity::class.java)
            i.putExtra("From", "Dashboard")
            startActivity(i)
        })

    }

    override fun getLayout(): Int {
       return R.layout.activity_login
    }

    override fun onClick(v: View?) {
    }

}