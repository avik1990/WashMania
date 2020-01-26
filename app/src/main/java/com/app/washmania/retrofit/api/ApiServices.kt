package com.app.washmania.retrofit.api

import com.app.washmania.model.MyProfile
import com.app.washmania.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import com.app.washmania.model.CartDeleteAction



interface ApiServices {

    @GET("service.php?action=about_us")
    fun GetAboutUS(): Call<AboutUsmodel>

    @GET("service.php?action=our_contacts")
    fun GetContactUs(): Call<ContactUsModel>

    @GET("service.php?action=terms")
    fun GetTermData(): Call<TermsConditionmodel>

    @GET("service.php?action=privacy")
    fun GetPrivacyData(): Call<Privacymodel>

    @GET("service.php?action=banner_list")
    fun GetBanners(): Call<Banners>

    @GET("service.php?action=category")
    fun GeCategory(): Call<Category>

    @GET("service.php?action=update_item")
    fun UpdateMyCart(
        @Query("user_id") user_id: String, @Query("cart_id") cart_id: String, @Query("unique_id") unique_id: String, @Query(
            "quantity"
        ) quantity: String, @Query("isCartAdd") isCartAdd: String
    ): Call<CartDeleteAction>

    @GET("service.php?action=add_to_cart")
    fun AddToCart(
        @Query("user_id") user_id: String,  @Query("unique_id") unique_id: String, @Query(
            "quantity") quantity: String, @Query("isCartAdd") isCartAdd: String, @Query("dress_id") dress_id: String
    ): Call<CartAdditionModel>


    @GET("service.php?action=dress_list")
    fun GetProductListResponse(
        @Query("category_id") category_id: String, @Query("dress_for") subcategory_id: String, @Query(
            "user_id"
        ) user_id: String, @Query("unique_id") unique_id: String
    ): Call<ProductsMod>


    @GET("service.php?action=zipcode_list")
    fun GetZipCodeList(): Call<ZipCodemodel>

    @GET("service.php?action=login")
    fun UserLogin(
        @Query("phone") phone: String, @Query("password") password: String, @Query("flag") flag: String, @Query(
            "unique_id"
        ) unique_id: String
    ): Call<LoginResponse>


    @GET("service.php?action=forgot_password")
    fun UserForgotPassword(@Query("email") email: String): Call<BaseResponse>

    @GET("service.php?action=post_feedback")
    abstract fun PostFeedback(
        @Query("user_id") user_id: String, @Query("name") name: String, @Query("email") email: String, @Query(
            "phone"
        ) phone: String, @Query("comment") comment: String
    ): Call<CartDeleteAction>


    @GET("service.php?action=registration")
    fun UserRegistration(
        @Query("fname") fname: String,
        @Query("lname") lname: String,
        @Query("phone") phone: String,
        @Query("email") email: String,
        @Query("password") password: String,
        @Query("confirm_password") confirm_password: String,
        @Query("address") address: String,
        @Query("state") state: String,
        @Query("city") city: String,
        @Query("zip") zip: String,
        @Query("flag") flag: String, @Query("unique_id") unique_id: String
    ): Call<RegistrationResponse>

    @GET("service.php?action=zipcode_availability")
    fun VerifyZipCode(@Query("zip") zip: String): Call<ZipCodeVerify>

    @GET("service.php?action=otp_validation")
    fun VerifyOTP(@Query("otp") otp: String, @Query("user_id") user_id: String, @Query("unique_id") unique_id: String): Call<LoginResponse>

    @GET("service.php?action=my_account")
    fun GetMYProfile(@Query("user_id") user_id: String): Call<MyProfile>

    @GET("service.php?action=pickup_time")
    fun GetPickupDate(@Query("pickup_date") pickup_date: String): Call<Timermodel>

    @GET("service.php?action=edit_profile")
    fun UserUpdateDetails(
        @Query("user_id") user_id: String,
        @Query("fname") fname: String,
        @Query("lname") lname: String,
        @Query("phone") phone: String,
        @Query("email") email: String,
        @Query("address") address: String,
        @Query("state") state: String,
        @Query("city") city: String,
        @Query("zip") zip: String
    ): Call<BaseResponse>

    @GET("service.php?action=change_password")
    fun ChangePassword(
        @Query("old_password") old_password: String,
        @Query("new_password") new_password: String,
        @Query("confirm_password") confirm_password: String,
        @Query("user_id") user_id: String
    ): Call<BaseResponse>

    @GET("service.php?action=view_cart")
    fun GetMyCart(@Query("user_id") user_id: String, @Query("unique_id") unique_id: String): Call<MyCart>

    @GET("service.php?action=delete_item")
    fun GetCartDeleteAction(@Query("user_id") user_id: String, @Query("cart_id") cart_id: String, @Query("unique_id") unique_id: String): Call<CartDeleteAction>

    @GET("service.php?action=my_orders")
    fun GetOrderListResponse(
        @Query("user_id") user_id: String, @Query("payment_status") payment_status: String
    ): Call<Myorders>

    @GET("service.php?action=my_orders")
    fun GetOrderListResponse(
        @Query("user_id") user_id: String
    ): Call<Myorders>

    @GET("service.php?action=checkout")
    fun CheckoutCall(
        @Query("fname") fname: String,
        @Query("lname") lname: String,
        @Query("email") email: String,
        @Query("phone") phone: String,
        @Query("address") address: String,
        @Query("city") city: String,
        @Query("state") state: String,
        @Query("zip") zip: String,
        @Query("user_id") user_id: String,
        @Query("unique_id") unique_id: String,
        @Query("quick_delivery") quick_delivery: String,
        @Query("floor") floor: String,
        @Query("pickup_instruction") pickup_instruction: String,
        @Query("pickup_date") pickup_date: String,
        @Query("pickup_time") pickup_time: String
    ): Call<BaseResponse>


    @GET("service.php?action=cancel_order")
    fun GetCancelOrder(@Query("user_id") user_id: String, @Query("cart_id") cart_id: String, @Query("unique_id") unique_id: String): Call<CartDeleteAction>

    @GET("service.php?action=order_details")
    fun GetOrderDetails(@Query("user_id") user_id: String, @Query("order_id") order_id: String): Call<OrderDetailsModel>


}

