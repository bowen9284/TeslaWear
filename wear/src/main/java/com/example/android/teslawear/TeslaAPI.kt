package com.example.android.teslawear
import com.example.android.teslawear.Model.Command
import com.example.android.teslawear.Model.WakeUp

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface TeslaAPI {
    companion object {
        const val BASE_URL = "https://owner-api.teslamotors.com"
    }

    @FormUrlEncoded
    @POST("oauth/token")
    fun postCredentials(@Field("grant_type") grantType: String,
                        @Field("client_id") clientId: String,
                        @Field("client_secret") clientSecret: String,
                        @Field("email") email: String,
                        @Field("password") password: String) : Call<OAuthToken>

    @POST("/api/1/vehicles/48390726821509980/command/wake_up")
    fun postWakeUp(): Call<WakeUp>

    @POST("/api/1/vehicles/48390726821509980/command/door_lock")
    fun postDoorLock(): Call<Command>

    @POST("/api/1/vehicles/48390726821509980/command/door_unlock")
    fun postDoorUnlock(): Call<Command>

    @POST("/api/1/vehicles/48390726821509980/command/honk_horn")
    fun postHonkHorn(): Call<Command>
}