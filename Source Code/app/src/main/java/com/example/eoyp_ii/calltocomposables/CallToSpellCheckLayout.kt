package com.example.eoyp_ii.calltocomposables

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.eoyp_ii.MainActivity
import com.example.eoyp_ii.composables.SpellCheckLayout
import com.example.eoyp_ii.models.MyHelper
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FileIntent{
    companion object{
        var intent:Intent? = null
    }
}

@Composable
fun CallSpellCheckerLayout(
    activity: MainActivity,
    navController: NavController
) {
    val STORAGE_PERMISSION_CODE = 101

    /* STATE VARIABLES */
    val url = remember { mutableStateOf("") }
    val urlBtnText = remember { mutableStateOf("USE URL") }
    val fileBtnText = remember { mutableStateOf("USE A FILE") }
    val isFileBtnEnabled = remember { mutableStateOf(true) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            FileIntent.intent = result.data

            if (MyHelper.isSelectedFileHtml(data?.data)){
                isFileBtnEnabled.value = false

                var hashMap : HashMap<String,ArrayList<String>>?

                CoroutineScope(IO).launch {

                    fileBtnText.value = "Please Wait........."

                    hashMap = MyHelper.getSuggestions(data)

                    if (hashMap.isNullOrEmpty()) {
                        withContext(Dispatchers.Main) {
                            MyHelper.showToast(
                                activity.applicationContext,
                                "An error occurred please check if you have internet" +
                                        " connection.\nIf yes please try again"

                            )
                            isFileBtnEnabled.value = true
                            fileBtnText.value = "USE A FILE"
                        }
                    }else{
                        withContext(Dispatchers.Main) {
                            val hashMapToJson = Gson().toJson(
                                hashMap as HashMap<String,ArrayList<String>>
                            )

                            navController.navigate("Result/$hashMapToJson")
                        }
                    }
                }
            }
            else{
                fileBtnText.value = "USE A FILE"
                MyHelper.showToast(
                    activity.applicationContext,
                    "Please select an html document"
                )
            }
        }
    }

    SpellCheckLayout(
        url = url.value,
        onURLChange = { url.value = it },
        urlBtnTxt = urlBtnText.value,
        isBtnFileEnabled = isFileBtnEnabled.value,
        fileBtnTxt = fileBtnText.value,
        workWithFile = {
            if (MyHelper.checkPermission(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    STORAGE_PERMISSION_CODE,
                    activity
                )
            ) {
               launcher.launch(MyHelper.pickFile())
            }
        }
    ) {
        Toast.makeText(
            activity,"Feature not yet implemented\nWill be implemented in future versions\n" +
                    "Use button below to use a file instead",
            Toast.LENGTH_LONG
        ).show()
    }
}