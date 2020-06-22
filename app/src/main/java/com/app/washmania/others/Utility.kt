@file:JvmName("Utility")
@file:JvmMultifileClass

package com.app.washmania.others

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.Gravity
import android.widget.Toast
import com.app.washmania.*
import com.app.washmania.others.WMPreference.get_contactNo
import com.app.washmania.others.WMPreference.getisVerified
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.prefs.Preferences

object Utility {

    var cd: ConnectionDetector? = null
    var PACKAGE_NAME: String = ""

    fun openNavDrawer(id: Int, mContext: Context) {

        cd = ConnectionDetector(mContext)

        if (id == R.id.nav_aboutus) {
            if (cd!!.isConnected()) {
                if (mContext !is AboutUs) {
                    val profileintent = Intent(mContext, AboutUs::class.java)
                    mContext.startActivity(profileintent)
                }
            } else {
                showToastShort(mContext, "No Internet")
            }
        }

        if (id == R.id.nav_privacypolicy) {
            if (cd!!.isConnected()) {
                if (mContext !is PrivacyActivity) {
                    val profileintent = Intent(mContext, PrivacyActivity::class.java)
                    mContext.startActivity(profileintent)
                }
            } else {
                showToastShort(mContext, "No Internet")
            }
            //showLogoutAlert(mContext, "Are you sure?", "Logout");
        }

        if (id == R.id.nav_terms) {
            if (cd!!.isConnected()) {
                if (mContext !is TermsCondition) {
                    val profileintent = Intent(mContext, TermsCondition::class.java)
                    mContext.startActivity(profileintent)
                }
            } else {
                showToastShort(mContext, "No Internet")
            }
        }

        if (id == R.id.nav_contacts) {
            //showLogoutAlert(mContext, "Are you sure?", "Logout");
        }
        /*if (id == R.id.nav_ordercancellation) {
            if (cd!!.isConnected()) {
                if (mContext !is OrderCancellation) {
                    val profileintent = Intent(mContext, OrderCancellation::class.java)
                    mContext.startActivity(profileintent)
                }
            } else {
                showToastShort(mContext, "No Internet")
            }
        }*/
        /*if (id == R.id.nav_replacement) {
            if (cd!!.isConnected()) {
                if (mContext !is Replacement) {
                    val profileintent = Intent(mContext, Replacement::class.java)
                    mContext.startActivity(profileintent)
                }
            } else {
                showToastShort(mContext, "No Internet")
            }
        }*/
        /*if (id == R.id.nav_refund) {
            if (cd!!.isConnected()) {
                if (mContext !is Refund) {
                    val profileintent = Intent(mContext, Refund::class.java)
                    mContext.startActivity(profileintent)
                }
            } else {
                showToastShort(mContext, "No Internet")
            }
        }*/
        if (id == R.id.nav_shareapp) {
            PACKAGE_NAME = mContext.applicationContext.packageName
            shareAll(
                mContext,
                "",
                "",
                mContext.resources.getString(R.string.share_pkg) + PACKAGE_NAME + "&hl=en"
            )
        }

        if (id == R.id.nav_feedback) {
            if (cd!!.isConnected()) {
                if (mContext !is Feedbackactivity) {
                    val profileintent = Intent(mContext, Feedbackactivity::class.java)
                    mContext.startActivity(profileintent)
                }
            } else {
                showToastShort(mContext, "No Internet")
            }
        }
        if (id == R.id.nav_rateapp) {
            val appPackageName =
                mContext.packageName // getPackageName() from Context or Activity object
            try {
                mContext.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$appPackageName")
                    )
                )
            } catch (anfe: android.content.ActivityNotFoundException) {
                mContext.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                    )
                )
            }
        }

        if (id == R.id.nav_contacts) {
            if (cd!!.isConnected()) {
                if (mContext !is ContactUs) {
                    val profileintent = Intent(mContext, ContactUs::class.java)
                    mContext.startActivity(profileintent)
                }
            } else {
                showToastShort(mContext, "No Internet")
            }
        }
        /*if (id == R.id.nav_shipping) {
            if (cd!!.isConnected()) {
                if (mContext !is ShippingContent) {
                    val profileintent = Intent(mContext, ShippingContent::class.java)
                    mContext.startActivity(profileintent)
                }
            } else {
                showToastShort(mContext, "No Internet")
            }
        }*/

        if (id == R.id.nav_myprofile) {
            if (cd!!.isConnected) {
                if (WMPreference.getisVerified(mContext)) {
                    if (mContext !is MyProfileActivity) {
                        val profileintent = Intent(mContext, MyProfileActivity::class.java)
                        mContext.startActivity(profileintent)
                    }
                } else {
                    val profileintent = Intent(mContext, LoginActivity::class.java)
                    mContext.startActivity(profileintent)
                }
            } else {
                showToastShort(mContext, "No Internet")
            }
        }

        if (id == R.id.nav_myorders) {
            if (cd!!.isConnected) {
                if (getisVerified(mContext)) {
                    if (mContext !is MyOrderActivity) {
                        val profileintent = Intent(mContext, MyOrderActivity::class.java)
                        mContext.startActivity(profileintent)
                    }
                } else {
                    val profileintent = Intent(mContext, LoginActivity::class.java)
                    mContext.startActivity(profileintent)
                }
            } else {
                showToastShort(mContext, "No Internet")
            }
        }

        if (id == R.id.nav_changepass) {
            if (cd!!.isConnected) {
                if (getisVerified(mContext)) {
                    if (mContext !is ChangePasswordActivity) {
                        val profileintent = Intent(mContext, ChangePasswordActivity::class.java)
                        mContext.startActivity(profileintent)
                    }
                } else {
                    val profileintent = Intent(mContext, LoginActivity::class.java)
                    mContext.startActivity(profileintent)
                }
            } else {
                showToastShort(mContext, "No Internet")
            }
        }

        if (id == R.id.nav_address) {
            if (cd!!.isConnected) {
                if (getisVerified(mContext)) {
                    if (mContext !is ChangeLocationActivity) {
                        val profileintent = Intent(mContext, ChangeLocationActivity::class.java)
                        mContext.startActivity(profileintent)
                    }
                } else {
                    val profileintent = Intent(mContext, LoginActivity::class.java)
                    mContext.startActivity(profileintent)
                }
            } else {
                showToastShort(mContext, "No Internet")
            }
        }


        if (id == R.id.nav_logout) {
            showLogoutAlert(mContext, "Are you sure?", "Logout")
        }

        if (id == R.id.nav_customercare) {
            if (getisVerified(mContext)) {
                if (mContext !is CustomerSupportActivity) {
                    val profileintent = Intent(mContext, CustomerSupportActivity::class.java)
                    mContext.startActivity(profileintent)
                }
            } else {
                val profileintent = Intent(mContext, LoginActivity::class.java)
                mContext.startActivity(profileintent)
            }
        }


    }

    fun showToastShort(mContext: Context, msg: String) {
        val toast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    fun isValidEmail(target: CharSequence): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    fun shareAll(mContext: Context, heading: String, sub: String, links: String) {
        //String title = heading;
        val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, links)
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, heading)
        mContext.startActivity(Intent.createChooser(sharingIntent, "Share Using"))
    }

    fun CallContactNo(mContext: Context) {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:" + get_contactNo(mContext))
        mContext.startActivity(callIntent)
    }


    fun showLogoutAlert(context: Context, msg: String, title: String) {
        val builder = android.app.AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle)
        builder.setTitle(title)
        builder.setMessage(msg)
        builder.setPositiveButton("OK") { dialog, which ->
            val profileintent = Intent(context, LoginActivity::class.java)
            context.startActivity(profileintent)
            (context as Activity).finishAffinity()
            cleardata(context)
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, which -> dialog.dismiss() }
        builder.show()
    }

    fun cleardata(mContext: Context) {
        //DatabaseHandler db = new DatabaseHandler(mContext);
        val settings = mContext.getSharedPreferences("Kppref", Context.MODE_PRIVATE)
        settings.edit().clear().apply()
        // db.drop_all_data();
    }


    fun getFormattedDate(normal_date: String): String {
        Log.d("DateFormat", normal_date)
        var formated_date = ""
        if (normal_date.length > 6) {
            val originalFormat = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
            // originalFormat.timeZone = TimeZone.getTimeZone("GMT")
            val targetFormat = SimpleDateFormat("yyyy-MM-dd")
            val date: Date
            try {
                date = originalFormat.parse(normal_date.trim())
                formated_date = targetFormat.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        } else {
            formated_date = normal_date
        }
        return formated_date
    }
}