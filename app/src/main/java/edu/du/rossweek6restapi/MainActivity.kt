package edu.du.rossweek6restapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {
    lateinit var service: JeepService
    lateinit var gson: Gson
    lateinit var requestText: TextView
    lateinit var responseText: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://my-json-server.typicode.com/BryanRoss1/RossWeek6RESTAPI/")
            .build()

        service = retrofit.create(JeepService::class.java)
        gson = GsonBuilder().setPrettyPrinting().create()

        requestText = findViewById(R.id.txt_request)
        responseText = findViewById(R.id.txt_response)

        findViewById<Button>(R.id.btn_get).setOnClickListener {
            makeCall {
                if (TextUtils.isEmpty(requestText.text)){
                    service.getJeeps()
                } else {
                    service.getJeep(requestText.text.toString())
                }
            }
        }

        findViewById<Button>(R.id.btn_post).setOnClickListener {
            val jsonObject = JSONObject()
            jsonObject.put("id", "3")
            jsonObject.put("model", "Wagoneer")
            jsonObject.put("engine", "{\"type\": \"Straight 6\",\n" +
                    "\"model\": \"258 4.2L\",\n" +
                    "\"horsepower\": \"74.5 kW / 101 PS / 100 hp\"}")
            makeCall {
                service.createJeep(jsonObject.toString().toRequestBody("applcation/json".toMediaTypeOrNull()))
            }
        }

        findViewById<Button>(R.id.btn_put).setOnClickListener {
            val jsonObject = JSONObject()
            jsonObject.put("id", "3")
            jsonObject.put("model", "Wagoneer")
            makeCall {
                service.createJeep(jsonObject.toString().toRequestBody("applcation/json".toMediaTypeOrNull()))
            }
        }

        findViewById<Button>(R.id.btn_delete).setOnClickListener {
            makeCall {
                service.deleteJeep(requestText.text.toString())
            }
        }

    }

    private fun makeCall(action: suspend () -> Response<ResponseBody>) {
        CoroutineScope(Dispatchers.IO).launch {
            var response: Response<ResponseBody> = action()
            withContext(Dispatchers.Main){
                if(response.isSuccessful){
                    responseText.text = formatJson(response.body()?.string())
                } else {
                    responseText.text = response.code().toString()
                }
            }
        }
    }

    private fun formatJson(text: String?): String {
        return gson.toJson(JsonParser.parseString(text))
    }
}