package com.example.eoyp_ii.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eoyp_ii.R
import com.example.eoyp_ii.ui.theme.Brianne
import com.example.eoyp_ii.ui.theme.Complementary500

@Composable
fun SpellCheckLayout(
    url:String,
    onURLChange:(String)->Unit,
    isBtnUrlEnabled:Boolean = true,
    isBtnFileEnabled:Boolean = true,
    urlBtnTxt:String,
    fileBtnTxt:String,
    workWithFile:()->Unit,
    onBtnClickScrapeUrl:(String)->Unit
) {
    Card(
        modifier = Modifier
            .requiredWidth(320.dp)
            .requiredHeight(680.dp),
        elevation = 12.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(18.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "SPELL CHECKER",
                fontFamily = Brianne,
                fontSize = 54.sp,
                color = Complementary500
            )
            Spacer(modifier = Modifier.padding(14.dp))
            Image(
                painter = painterResource(id = R.drawable.noted),
                contentDescription = null
            )
            Spacer(modifier = Modifier.padding(12.dp))
            OutlinedTextField(
                value = url,
                onValueChange = onURLChange,
                placeholder = {
                    Text(
                        text = "Paste web site url here"
                    )
                },
                label = {
                    Text(
                        text = "URL",
                        color = Complementary500
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Complementary500,
                    unfocusedBorderColor = Complementary500,
                    cursorColor = Complementary500
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(10.dp))
            Button(
                onClick = { onBtnClickScrapeUrl(url) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Complementary500,
                    contentColor = Color.White
                ),
                enabled = isBtnUrlEnabled
            ) {
                Text(
                    text = urlBtnTxt
                )
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "OR",
                    color = Complementary500,
                    fontSize = 11.sp
                )
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Button(
                onClick = workWithFile,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Complementary500,
                    contentColor = Color.White
                ),
                enabled = isBtnFileEnabled
            ) {
                Text(
                    text = fileBtnTxt
                )
            }
        }
    }
}

@Preview
@Composable
fun S() {
    SpellCheckLayout(
        url = "",
        onURLChange = { /*TODO*/ },
        urlBtnTxt = "",
        fileBtnTxt = "USE FILE",
        workWithFile = { /*TODO*/ }) {

    }
}
