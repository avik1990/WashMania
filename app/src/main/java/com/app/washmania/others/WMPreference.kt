@file:JvmName("PreferenceFile")
@file:JvmMultifileClass
package com.app.washmania.others

import android.content.Context

object WMPreference {

    fun getisVerified(mContext: Context): Boolean {
        val loginPreferences = mContext.getSharedPreferences("Kppref", 0) // 0 - for private mode
        return loginPreferences.getBoolean("d_ride_later", false)
    }

    fun setisVerified(mContext: Context, isVerified: Boolean) {
        val preferences = mContext.getSharedPreferences("Kppref", 0) // 0 - for private mode
        val editor = preferences.edit()
        editor.putBoolean("d_ride_later", isVerified)
        editor.apply()
    }

    fun get_userPhone(mContext: Context): String {
        val loginPreferences = mContext.getSharedPreferences("Kppref", 0) // 0 - for private mode
        return loginPreferences.getString("user_phone", "0")
    }

    fun set_userPhone(mContext: Context, a_key: String) {
        val preferences = mContext.getSharedPreferences("Kppref", 0) // 0 - for private mode
        val editor = preferences.edit()
        editor.putString("user_phone", a_key)
        editor.commit()
    }


    fun get_firstName(mContext: Context): String {
        val loginPreferences = mContext.getSharedPreferences("Kppref", 0) // 0 - for private mode
        return loginPreferences.getString("fuser_name", "0")
    }

    fun set_firstuserName(mContext: Context, a_key: String) {
        val preferences = mContext.getSharedPreferences("Kppref", 0) // 0 - for private mode
        val editor = preferences.edit()
        editor.putString("fuser_name", a_key)
        editor.commit()
    }


    fun get_lastName(mContext: Context): String {
        val loginPreferences = mContext.getSharedPreferences("Kppref", 0) // 0 - for private mode
        return loginPreferences.getString("luser_name", "0")
    }

    fun set_lastName(mContext: Context, a_key: String) {
        val preferences = mContext.getSharedPreferences("Kppref", 0) // 0 - for private mode
        val editor = preferences.edit()
        editor.putString("luser_name", a_key)
        editor.commit()
    }


    fun get_userEmail(mContext: Context): String {
        val loginPreferences = mContext.getSharedPreferences("Kppref", 0) // 0 - for private mode
        return loginPreferences.getString("user_Email", "0")
    }

    fun set_userEmail(mContext: Context, a_key: String) {
        val preferences = mContext.getSharedPreferences("Kppref", 0) // 0 - for private mode
        val editor = preferences.edit()
        editor.putString("user_Email", a_key)
        editor.commit()
    }

    fun get_userId(mContext: Context): String {
        val loginPreferences = mContext.getSharedPreferences("Kppref", 0) // 0 - for private mode
        return loginPreferences.getString("user_ID", "0")
    }

    fun set_userId(mContext: Context, value: String) {
        val preferences = mContext.getSharedPreferences("Kppref", 0) // 0 - for private mode
        val editor = preferences.edit()
        editor.putString("user_ID", value)
        editor.commit()
    }


    fun get_UniqueId(mContext: Context): String {
        val loginPreferences = mContext.getSharedPreferences("Kppref", 0) // 0 - for private mode
        return loginPreferences.getString("_UniqueId", "0")
    }

    fun set_UniqueId(mContext: Context, a_key: String) {
        val preferences = mContext.getSharedPreferences("Kppref", 0) // 0 - for private mode
        val editor = preferences.edit()
        editor.putString("_UniqueId", a_key)
        editor.commit()
    }

    fun isgenerateUniqueKey(mContext: Context): Boolean {
        val loginPreferences = mContext.getSharedPreferences("Kppref", 0) // 0 - for private mode
        return loginPreferences.getBoolean("isCheckedOut", false)
    }

    fun setUniqueKey(mContext: Context, isVerified: Boolean) {
        val preferences = mContext.getSharedPreferences("Kppref", 0) // 0 - for private mode
        val editor = preferences.edit()
        editor.putBoolean("isCheckedOut", isVerified)
        editor.apply()
    }

    fun get_Zip(mContext: Context): String {
        val loginPreferences = mContext.getSharedPreferences("Kppref", 0) // 0 - for private mode
        return loginPreferences.getString("zip", "0")
    }

    fun set_Zip(mContext: Context, a_key: String) {
        val preferences = mContext.getSharedPreferences("Kppref", 0) // 0 - for private mode
        val editor = preferences.edit()
        editor.putString("zip", a_key)
        editor.commit()
    }


    fun get_Cartount(mContext: Context): String {
        val loginPreferences = mContext.getSharedPreferences("Kppref", 0) // 0 - for private mode
        return loginPreferences.getString("cartcount", "0")
    }

    fun set_Cartount(mContext: Context, a_key: String) {
        val preferences = mContext.getSharedPreferences("Kppref", 0) // 0 - for private mode
        val editor = preferences.edit()
        editor.putString("cartcount", a_key)
        editor.commit()
    }


    fun get_address(mContext: Context): String {
        val loginPreferences = mContext.getSharedPreferences("Kppref", 0) // 0 - for private mode
        return loginPreferences.getString("address", "0")
    }

    fun set_address(mContext: Context, a_key: String) {
        val preferences = mContext.getSharedPreferences("Kppref", 0) // 0 - for private mode
        val editor = preferences.edit()
        editor.putString("address", a_key)
        editor.commit()
    }


    fun get_city(mContext: Context): String {
        val loginPreferences = mContext.getSharedPreferences("Kppref", 0) // 0 - for private mode
        return loginPreferences.getString("city", "0")
    }

    fun set_city(mContext: Context, a_key: String) {
        val preferences = mContext.getSharedPreferences("Kppref", 0) // 0 - for private mode
        val editor = preferences.edit()
        editor.putString("city", a_key)
        editor.commit()
    }

    fun get_state(mContext: Context): String {
        val loginPreferences = mContext.getSharedPreferences("Kppref", 0) // 0 - for private mode
        return loginPreferences.getString("state", "0")
    }

    fun set_state(mContext: Context, a_key: String) {
        val preferences = mContext.getSharedPreferences("Kppref", 0) // 0 - for private mode
        val editor = preferences.edit()
        editor.putString("state", a_key)
        editor.commit()
    }


    fun get_checkClicked(mContext: Context): String {
        val loginPreferences = mContext.getSharedPreferences("Kppref", 0) // 0 - for private mode
        return loginPreferences.getString("checkClicked", "0")
    }

    fun set_checkClicked(mContext: Context, a_key: String) {
        val preferences = mContext.getSharedPreferences("Kppref", 0) // 0 - for private mode
        val editor = preferences.edit()
        editor.putString("checkClicked", a_key)
        editor.commit()
    }


    fun get_dashCatId(mContext: Context): String {
        val loginPreferences = mContext.getSharedPreferences("Kppref", 0) // 0 - for private mode
        return loginPreferences.getString("dashCatId", "0")
    }

    fun set_dashCatId(mContext: Context, value: String) {
        val preferences = mContext.getSharedPreferences("Kppref", 0) // 0 - for private mode
        val editor = preferences.edit()
        editor.putString("dashCatId", value)
        editor.commit()
    }


    fun get_contactNo(mContext: Context): String {
        val loginPreferences = mContext.getSharedPreferences("Kppref", 0) // 0 - for private mode
        return loginPreferences.getString("user_phone1231231", "0")
    }

    fun set_contactNo(mContext: Context, a_key: String) {
        val preferences = mContext.getSharedPreferences("Kppref", 0) // 0 - for private mode
        val editor = preferences.edit()
        editor.putString("user_phone1231231", a_key)
        editor.commit()
    }


}