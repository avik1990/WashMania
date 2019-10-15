package com.app.washmania

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.util.Log
import com.app.washmania.others.BaseActivity
import com.app.washmania.others.Preference.isgenerateUniqueKey
import com.app.washmania.others.Preference.setUniqueKey
import com.app.washmania.others.Preference.set_UniqueId
import java.util.*

class SplashActivity : BaseActivity() {

    lateinit var handler: Handler
    val waitingTime: Long = 3000
    var appdata: Boolean = true
    var context: Context? = null

    companion object {
        val CLASS_NAME = "SplashActivity"
    }


    override fun initResources() {
    }

    override fun initListeners() {
        context = this
        if (!isgenerateUniqueKey(context!!)) {
            generateUniqueId()
            setUniqueKey(context!!, true)
        }

        if (appdata) {
            movetopasscodescreen()
        } else {
            waitScreen()
        }
    }

    override fun getLayout(): Int {
        return R.layout.activity_splash
    }

    private fun waitScreen() {
        handler = Handler()
        handler.postDelayed(
            object : Runnable {
                override fun run() {
                    startActivity(Intent(this@SplashActivity, DashboardActivity::class.java))
                    finish()
                }
            }, waitingTime
        )
    }

    private fun movetopasscodescreen() {
        handler = Handler()
        handler.postDelayed(
            object : Runnable {
                override fun run() {
                    startActivity(Intent(this@SplashActivity, WelcomeActivity::class.java))
                    finish()
                }
            }, waitingTime
        )
    }

    private fun generateUniqueId() {
        val rand = Random()
        val uniqueId = System.currentTimeMillis().toString() + "" + rand.nextInt(500)
        Log.d("uniqueId", uniqueId)
        set_UniqueId(context!!, uniqueId)
    }
}
