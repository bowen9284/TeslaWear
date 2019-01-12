package com.example.android.teslawear

import com.google.gson.annotations.SerializedName

data class OAuthToken (
    @SerializedName("access_token")
    val accessToken: String,

    @SerializedName("token_type")
    private val tokenType: String,

    @SerializedName("refresh_token")
    private val refreshToken: String,

    @SerializedName("expires_in")
    private val expiresIn: Int,

    @SerializedName("created_at")
    private val createdAt: Int,

    val authString: String = "Bearer $accessToken"

)