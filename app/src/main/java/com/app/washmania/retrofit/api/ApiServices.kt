package com.app.washmania.retrofit.api

import com.app.washmania.model.*
import retrofit2.Call
import retrofit2.http.GET

interface ApiServices {

    @GET("service.php?action=about_us")
    abstract fun GetAboutUS(): Call<AboutUsmodel>

    @GET("service.php?action=our_contacts")
    abstract fun GetContactUs(): Call<ContactUsModel>

    @GET("service.php?action=terms")
    abstract fun GetTermData(): Call<TermsConditionmodel>

    @GET("service.php?action=privacy")
    abstract fun GetPrivacyData(): Call<Privacymodel>

    @GET("service.php?action=banner_list")
    abstract fun GetBanners(): Call<Banners>

    @GET("service.php?action=category")
    abstract fun GeCategory(): Call<Category>
}