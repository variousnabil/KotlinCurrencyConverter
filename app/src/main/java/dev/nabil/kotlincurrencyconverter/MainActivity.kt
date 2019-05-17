package dev.nabil.kotlincurrencyconverter

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun get(view: View) {
        val downloadData = Download()

        try {
            val url = "https://api.exchangeratesapi.io/latest?base="
            val chosenBase = editText.text.toString()
            downloadData.execute(url+chosenBase)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    inner class Download : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String?): String {
            var result = ""
            var url: URL
            val httpURLConnection: HttpURLConnection

            try {
                url = URL(params[0])
                httpURLConnection = url.openConnection() as HttpURLConnection
                val inputStream = httpURLConnection.inputStream
                val inputStreamReader = InputStreamReader(inputStream)

                var data = inputStreamReader.read()

                while (data > 0) {
                    val character = data.toChar()
                    result += character

                    data = inputStreamReader.read()
                }

                return result

            } catch (e: Exception) {
                e.printStackTrace()
                return result
            }
        }

        override fun onPostExecute(result: String?) {
            try {

                val jsonObject = JSONObject(result)
                println(jsonObject)
                val base = jsonObject.getString("base")
                println(base)
                val date = jsonObject.getString("date")
                println(date)
                val rates = jsonObject.getString("rates")
                println(rates)

                val newJSONObject = JSONObject(rates)
                val IDR = newJSONObject.getString("IDR")
                println("USD to IDR = $IDR")
                val TRY = newJSONObject.getString("TRY")
                println("USD to TRY = $TRY")
                val JPY = newJSONObject.getString("JPY")
                println("USD to JPY = $JPY")

                tvIDR.text = "IDR: $IDR"
                tvTRY.text = "TRY: $TRY"
                tvJPY.text = "JPY: $JPY"

            } catch (e: Exception) {
                e.printStackTrace()
            }
            super.onPostExecute(result)
        }

    }

}
