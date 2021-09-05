package com.example.eoyp_ii.calltocomposables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.example.eoyp_ii.composables.ResultLayout
import com.example.eoyp_ii.composables.mySet
import com.example.eoyp_ii.models.MyHelper

@Composable
fun CallResultLayout(
    navController: NavController,
    hashMap:HashMap<String,ArrayList<String>>
) {
    val isBtnSaveEnabled = remember {
        mutableStateOf(true)
    }

    val errorMsg = remember {
        mutableStateOf("")
    }

    var newHtml: String

    ResultLayout(
        hash = hashMap,
        isBtnSaveEnabled = isBtnSaveEnabled.value,
        displayErrorText = errorMsg.value
    ){
        val intent = FileIntent.intent

        if (intent != null){
            newHtml =  MyHelper.getHtmlDoc(intent).html().toString()

            mySet.forEach {
                if(it.suggestion != "Make No changes")
                    newHtml = newHtml.replace(it.wordWithTypo,it.suggestion)
            }

            isBtnSaveEnabled.value = false

            val result = MyHelper.saveFile(
                "newHtmlFile.html",
                newHtml
            )

            when(result){
                //Successfully wrote the file
                0->{
                    errorMsg.value = "File saved to Document as newHtmlFile"
                }
                //Did not successfully write the file
                1->{
                    errorMsg.value = "Failed to save file to Document!"
                }
            }
        }else
            println("Intent is null")
    }
}