package com.example.android.teslawear

import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import com.example.android.teslawear.Model.Command
import com.example.android.teslawear.Model.WakeUp
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : WearableActivity() {

    private lateinit var horn: Button
    private lateinit var wakeUp: Button

    var token: OAuthToken? = null
    var commandResponse: Command? = null
    var wakeUpResponse: WakeUp? = null

    private lateinit var teslaAPI: TeslaAPI

    private lateinit var progressCircle: ProgressBar

    private val credentials = Credentials.basic("bowen9284@gmail.com", "3sQ8O*Kad#8h")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        horn = findViewById(R.id.honk_horn)
        wakeUp = findViewById(R.id.wake_up)
        progressCircle = findViewById(R.id.indeterminateBar)

        createTeslaApi().let {
            teslaAPI.postCredentials(
                "password",
                "e4a9949fcfa04068f59abb5a658f2bac0a3428e4652315490b659d5ab3f35a9e",
                "c75f14bbadc8bee3a7594412c31416f8300256d7668ea7e6e7f06727bfb9d220",
                "bowen9284@gmail.com",
                "").enqueue(tokenCallback)
        }
    }

    private fun createTeslaApi() {
        val okHttpClient: OkHttpClient = OkHttpClient().newBuilder().addInterceptor(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                val originalRequest = chain.request()
                val builder = originalRequest.newBuilder().header("Authorization", "Bearer " +
                    if (token != null) token!!.accessToken else credentials
                )

                val newRequest = builder.build()
                return chain.proceed(newRequest)
            }
        }).build()

        val retroFit = Retrofit.Builder()
            .baseUrl(TeslaAPI.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        teslaAPI = retroFit.create(TeslaAPI::class.java)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.wake_up -> {
                teslaAPI.postWakeUp().enqueue(wakeUpCallback)
                progressCircle.visibility = View.VISIBLE
            }
            R.id.honk_horn -> teslaAPI.postHonkHorn().enqueue(commandCallback)
            R.id.door_unlock -> teslaAPI.postDoorUnlock().enqueue(commandCallback)
            R.id.door_lock -> teslaAPI.postDoorLock().enqueue(commandCallback)

            else -> Toast.makeText(this, "Nope", Toast.LENGTH_LONG).show()
        }
    }

    var tokenCallback: Callback<OAuthToken> = object : Callback<OAuthToken> {
        override fun onResponse(call: Call<OAuthToken>, response: Response<OAuthToken>) {
            if (response.isSuccessful) {
                token = response.body()
                Toast.makeText(this@MainActivity, "Connected", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this@MainActivity, "Failure while requesting token", Toast.LENGTH_LONG).show()
                Log.d("RequestTokenCallback", "Code: " + response.code() + "Message: " + response.message())
            }
            progressCircle.visibility = View.GONE
        }

        override fun onFailure(call: Call<OAuthToken>, t: Throwable) {
            t.printStackTrace()
        }
    }

    var commandCallback: Callback<Command> = object : Callback<Command> {
        override fun onResponse(call: Call<Command>, response: Response<Command>) {
            if (response.isSuccessful) {
                commandResponse = response.body()
                Toast.makeText(this@MainActivity, "Success", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this@MainActivity, "Failure sending command", Toast.LENGTH_LONG).show()
                Log.d("CommandCallback", "Code: " + response.code() + "Message: " + response.message())
            }
        }

        override fun onFailure(call: Call<Command>, t: Throwable) {
            t.printStackTrace()
        }
    }

    var wakeUpCallback: Callback<WakeUp> = object : Callback<WakeUp> {
        override fun onResponse(call: Call<WakeUp>, response: Response<WakeUp>) {
            if (response.isSuccessful) {
                wakeUpResponse = response.body()
                Toast.makeText(this@MainActivity, "Success", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this@MainActivity, "Failure sending command", Toast.LENGTH_LONG).show()
                Log.d("WakeUpCallback", "Code: " + response.code() + "Message: " + response.message())
            }
            progressCircle.visibility = View.GONE
        }

        override fun onFailure(call: Call<WakeUp>, t: Throwable) {
            t.printStackTrace()
        }
    }

}
