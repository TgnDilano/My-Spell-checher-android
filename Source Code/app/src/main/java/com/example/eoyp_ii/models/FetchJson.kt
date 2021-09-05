package com.example.eoyp_ii.models

import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class FetchJson(word: String){
    private lateinit var connection:HttpURLConnection

    private val myURL = "https://www.stands4.com/services/v2/grammar.php?" +
            "uid=8953&tokenid=8RGhxglThaK64k5V&text=$word&format=json"

    fun getSuggestions() : ArrayList<String>{
        var suggestions = ArrayList<String>()
        var jsonData = ""
        try {
            connection = URL(myURL).openConnection() as HttpURLConnection
            val inputStream = connection.inputStream
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            var line = bufferedReader.readLine()

            while (line != null){
                jsonData += line
                line = bufferedReader.readLine()
            }

            if (jsonData.isNotEmpty()){
                val jsonObject = JSONObject(jsonData)
                val matchesFound = jsonObject.getJSONArray("matches")
                if (matchesFound.length() == 0){
                    suggestions = ArrayList()
                    println("No errors found in the word")
                }else{
                    suggestions = ArrayList()
                    val replacements = matchesFound
                        .getJSONObject(0)
                        .getJSONArray("replacements")
                    var i =0
                    while (i < replacements.length()){
                        suggestions.add(replacements.getJSONObject(i).getString("value"))
                        i++
                    }
                }
            }

        }catch (e: IllegalArgumentException){
            val msg = "URL supplied is not valid"
            //showToast(context,msg)
            println(msg)
        }catch (e: IOException){
            val msg = "Network issues occurred"
            //showToast(context,msg)
            println(msg)
        }catch (e: JSONException){
            val msg = "Unable to get the json"
            //showToast(context,msg)
            println(msg)
            e.printStackTrace()
        }catch (e:Exception){
            val msg = "Unknown Error Occurred"
            //showToast(context,msg)
            println(msg)
            e.printStackTrace()
        }finally {
            connection.disconnect()
        }
        return suggestions
    }
}