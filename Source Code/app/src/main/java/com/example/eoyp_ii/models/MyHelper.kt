package com.example.eoyp_ii.models

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.eoyp_ii.MainActivity
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MyHelper{
    companion object{

        /* PICK AN HTML FILE USING FILE CHOOSER */
        fun pickFile():Intent{
            val intent: Intent

            if(Build.MANUFACTURER != "samsung"){
                intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = "*/*"
            }else{
                intent = Intent("com.sec.android.app.myfiles.PICK_DATA")
                intent.putExtra("CONTENT_TYPE", "*/*")
                intent.addCategory(Intent.CATEGORY_DEFAULT)
            }
            return intent
        }

        /* CHECKS IF SELECTED FILE IS AN HTML FILE FROM ITS URI*/
        fun isSelectedFileHtml(uri: Uri?): Boolean {
            return if (uri?.lastPathSegment?.endsWith("html") == true) {
                println("HTML DOC")
                true
            } else
                false
        }

        /* CHECKS AND ASK FOR STORAGE PERMISSION */
        fun checkPermission(
            permission: String,
            requestCode: Int,
            activity: MainActivity
        ) : Boolean{

            var isPermissionGranted = false

            if (ContextCompat.checkSelfPermission(
                    activity, permission
                ) == PackageManager.PERMISSION_DENIED
            ) {
                // Requesting the permission if not granted
                ActivityCompat.requestPermissions(
                    activity, arrayOf(permission), requestCode
                )
            } else
                isPermissionGranted = true

            return isPermissionGranted
        }

        fun showToast(
            context:Context,
            msg:String
        ){
            Toast.makeText(context,msg,Toast.LENGTH_LONG).show()
        }

        private fun isAlphaNumChar(str: String):Boolean{
            return  str == "." || str == ":" || str == ";" ||
                    str == "-" || str == "_" || str == "?" ||
                    str == "!" || str == "%" || str == "$" ||
                    str == "@" || str == "#" || str == "^" ||
                    str == "&" || str == "*" || str == "(" ||
                    str == ")" || str == "=" || str == "`" ||
                    str == "~" || str == "/" || str == "|" ||
                    str == " " || str == "," || str  == "\""
        }

        private fun dropAlphaCharInWord(str:String):String{
            var myStr = str
            return if (
                str.endsWith(".") || str.endsWith(":") || str.endsWith(";") ||
                str.endsWith("-") || str.endsWith("_") || str.endsWith("?") ||
                str.endsWith("!") || str.endsWith("%") || str.endsWith("$") ||
                str.endsWith("@") || str.endsWith("#") || str.endsWith("^") ||
                str.endsWith("&") || str.endsWith("*") || str.endsWith("(") ||
                str.endsWith(")") || str.endsWith("=") || str.endsWith("`") ||
                str.endsWith("~") || str.endsWith("/") || str.endsWith("|")  ||
                str.endsWith("\"") || str.endsWith(",")
            ){
                myStr = myStr.dropLast(1)
                myStr
            }else
                str
        }

        private fun trimWords(list:List<String>): ArrayList<String> {
            val newList = ArrayList<String>()
            list.forEach {
                if (it != " " && !isAlphaNumChar(it))
                    newList.add(
                        dropAlphaCharInWord(it)
                    )
            }
            return newList
        }

        //Returns the source code of a an html from an implicit intent
        fun getHtmlDoc(intent: Intent?):Document{
            var doc : Document? = null

            try {
                doc = Jsoup.parse(File(intent?.data?.path!!),"UTF-8")

            }catch (e:Exception){
                println("*******-- Error Occurred --******")
                e.printStackTrace()
            }
            return doc!!
        }


        fun getSuggestions(intent: Intent?) : HashMap<String,ArrayList<String>>?{
            var hashMap : HashMap<String,ArrayList<String>>? = null
            try {
                val doc = Jsoup.parse(File(intent?.data?.path!!),"UTF-8")

                var listOfAllWords = doc.body().text().toString().split(" ")
                listOfAllWords = trimWords(listOfAllWords)

                hashMap = HashMap()
                run outside@{
                    listOfAllWords.forEachIndexed { index, str ->
                        if (index == 3)
                            return@outside
                        else{
                            val suggestions = FetchJson(str).getSuggestions()
                            if(suggestions.isNotEmpty()){
                                hashMap[str] = suggestions
                            }
                        }
                    }
                }

            }catch (e:Exception){
                println("*******-- Error Occurred --******")
                e.printStackTrace()
            }

            return hashMap
        }

        fun saveFile(
            filename:String,
            dataToStore:String
        ):Int{
            val folder :File = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS
                )
            }else{
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) // Folder Name
            }
            val myFile = File(folder, filename) // Filename
            return writeData(myFile, dataToStore)
        }

        private fun writeData(myFile: File, data: String):Int{
            var errorCode: Int
            val fileOutputStream: FileOutputStream? = null
            try {
                FileOutputStream(myFile).write(data.toByteArray())
                println("Wrote file")
                errorCode = 0
                return errorCode
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                errorCode = 1
                return errorCode
            } finally {
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }

    }
}