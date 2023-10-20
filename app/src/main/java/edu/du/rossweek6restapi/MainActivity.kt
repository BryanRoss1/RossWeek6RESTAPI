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
    //private lateinit var service: JeepService
    private lateinit var service: SkiService
    private lateinit var gson: Gson
    private lateinit var requestText: TextView
    private lateinit var responseText: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = Retrofit.Builder()
            //.baseUrl("https://my-json-server.typicode.com/BryanRoss1/RossWeek6RESTAPI/")
            .baseUrl("https://my-json-server.typicode.com/Gtubridy/SkiResorts/")
            .build()

        //service = retrofit.create(JeepService::class.java)
        service = retrofit.create(SkiService::class.java)
        gson = GsonBuilder().setPrettyPrinting().create()

        requestText = findViewById(R.id.txt_request)
        responseText = findViewById(R.id.txt_response)

        findViewById<Button>(R.id.btn_get).setOnClickListener {
            makeCall {
                if (TextUtils.isEmpty(requestText.text)){
                    //service.getJeeps()
                    service.getSki()
                } else {
                    //service.getJeep(requestText.text.toString())
                    service.getSki(requestText.text.toString())
                }
            }
        }

        findViewById<Button>(R.id.btn_post).setOnClickListener {
            val jsonObject = JSONObject()
            jsonObject.put("id", "3")
//            jsonObject.put("model", "Wagoneer")
//            jsonObject.put("engine",
//                "{type: Straight 6, model: 258 4.2L, horsepower: 74.5 kW / 101 PS / 100 hp}"
//            )
            jsonObject.put("Name","Monarch")
            jsonObject.put("Elevation","10790")
            jsonObject.put("Vertical Feet", "1162")
            jsonObject.put("Average Annual Snowfall", "350in")
            jsonObject.put("Chairlift Count","7")
            jsonObject.put("Average Daily Personnel","375")
            makeCall {
                //service.createJeep(jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull()))
                service.createSki(jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull()))
            }
        }

        findViewById<Button>(R.id.btn_put).setOnClickListener {
            val jsonObject = JSONObject()
            jsonObject.put("id", "1")
            jsonObject.put("model", "DJ-6")
            makeCall {
                //service.updateJeep(jsonObject.getString("id"),jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull()))
                service.updateSki(jsonObject.getString("id"),jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull()))
            }
        }

        findViewById<Button>(R.id.btn_delete).setOnClickListener {
            makeCall {
                //service.deleteJeep(requestText.text.toString())
                service.deleteSki(requestText.text.toString())
            }
        }

    }

    private fun makeCall(action: suspend () -> Response<ResponseBody>) {
        CoroutineScope(Dispatchers.IO).launch {
            val response: Response<ResponseBody> = action()
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