package com.example.eoyp_ii.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.example.eoyp_ii.MainActivity
import com.example.eoyp_ii.calltocomposables.CallResultLayout
import com.example.eoyp_ii.calltocomposables.CallSpellCheckerLayout
import com.google.gson.Gson

@Composable
fun MainLayout(
    activity: MainActivity
) {
    Scaffold{
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(9.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = "SpellChecker"
            ) {
                composable("SpellChecker"){
                    CallSpellCheckerLayout(activity,navController)
                }

                composable(
                    "Result/{hashMap}",
                    arguments = listOf(
                        navArgument("hashMap")
                        {
                            type = NavType.StringType
                        }
                    )
                ){backStackEntry->
                    val json = backStackEntry.arguments?.getString("hashMap")
                    val myHashMap = Gson().fromJson(json,HashMap::class.java)

                    CallResultLayout(
                        navController = navController,
                        hashMap = myHashMap as HashMap<String,ArrayList<String>>
                    )
                }
            }

        }
    }
}
