package com.example.eoyp_ii

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.eoyp_ii.composables.MainLayout
import com.example.eoyp_ii.models.MyHelper
import com.example.eoyp_ii.ui.theme.EOYP_IITheme
import kotlin.system.exitProcess

open class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyHelper.checkPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                STORAGE_PERMISSION_CODE,
                this@MainActivity
            )
            EOYP_IITheme {
                MainLayout(activity = this@MainActivity)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty()
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ){
                MyHelper.showToast(
                    applicationContext,
                    "Storage Permission Granted"
                )
            }else{
                finishAffinity()
                exitProcess(0)
            }
        }
    }

    companion object{
        private const val STORAGE_PERMISSION_CODE = 101
    }
}