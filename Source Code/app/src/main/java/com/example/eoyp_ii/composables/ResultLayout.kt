package com.example.eoyp_ii.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eoyp_ii.ui.theme.Brianne
import com.example.eoyp_ii.ui.theme.Complementary500
import com.example.eoyp_ii.ui.theme.Dark500

data class TypoAndSuggestion(
    val wordWithTypo:String,
    var suggestion: String
)


// Keeps tract of words together with their individual suggestion
val mySet = arrayListOf<TypoAndSuggestion>()

@Composable
fun ResultLayout(
    hash : HashMap<String,ArrayList<String>>,
    isBtnSaveEnabled:Boolean = true,
    displayErrorText:String,
    onSaveBtnClick:()->Unit
) {
    Card(
        modifier = Modifier
            .requiredWidth(380.dp)
            .requiredHeight(844.dp)
            .padding(16.dp),
        elevation = 8.dp,
        backgroundColor = if (isSystemInDarkTheme()) Dark500 else Color.White
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Table of Results",
                color = Complementary500,
                fontSize = 42.sp,
                fontFamily = Brianne,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp
            )
            Spacer(modifier = Modifier.padding(6.dp))
            Divider(
                Modifier.width(70.dp),
                thickness = 1.dp
            )
            Spacer(modifier = Modifier.padding(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ){
                Text(
                    text = "Word With Typo",
                    modifier = Modifier,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = Complementary500,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.padding(16.dp))
                Text(
                    text = "Suggestions",
                    modifier = Modifier,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = Complementary500,
                    fontFamily = FontFamily.Monospace
                )
            }
            Divider(
                modifier = Modifier.padding(4.dp)
            )
            LazyColumn(
                modifier = Modifier.requiredHeight(570.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                items(1){
                    hash.forEach { (t, u) ->
                        val new = u.plus("Make No changes") as ArrayList<String>
                        IndividualCell(
                            word = t,
                            suggestions = new,
                            isBtnSaveEnabled
                        )
                        Divider(
                            Modifier.width(160.dp),
                            thickness = 1.dp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.padding(4.dp))
            Divider(
                Modifier.width(320.dp),
                thickness = 1.dp
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Button(
                onClick = onSaveBtnClick,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Complementary500,
                    contentColor = Color.White
                ),
                enabled = isBtnSaveEnabled
            ) {
                Text(text = "Spell Check And Save File")
            }
            Spacer(modifier = Modifier.padding(6.dp))
            Text(
                text = displayErrorText,
                textAlign = TextAlign.Center,
                fontSize = 15.sp,
                color = Complementary500
            )
        }
    }
}


@Composable
fun IndividualCell(
    word:String,suggestions:ArrayList<String>,
    isRadioSaveEnabled: Boolean = true
) {
    val typoAndSuggestion = TypoAndSuggestion(word,suggestions[suggestions.size - 1])

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        val (selectedOption, onOptionSelected) = remember {
            mutableStateOf(suggestions[suggestions.size - 1])
        }

        Text(
            text = word,
            textAlign = TextAlign.Center,
            fontSize = 21.sp
        )

        Spacer(modifier = Modifier.padding(15.dp))
        Column {
            suggestions.forEachIndexed { _, item ->
                Row(
                    Modifier.padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (item == selectedOption),
                        onClick = {
                            onOptionSelected(item)
                            typoAndSuggestion.suggestion = item
                            if(typoAndSuggestion.suggestion != "Make No changes")
                                addToSet(typoAndSuggestion)
                        },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Complementary500
                        ),
                        enabled = isRadioSaveEnabled
                    )

                    val annotatedString = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle()
                        ){ append("  $item  ") }
                    }

                    ClickableText(
                        text = annotatedString,
                        onClick = {
                            //onOptionSelected(item)
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.padding(4.dp))
        }
    }
}

fun addToSet(typoAndSuggestion: TypoAndSuggestion){
    if (mySet.isEmpty()){
        mySet.add(typoAndSuggestion)
        return
    }
    else
        for(element in mySet){
            if (typoAndSuggestion == element){
                break
            }
            if(element != typoAndSuggestion){
                /*
                    checks to see if word entered exist then
                    remove it then insert word with it's new suggestion
                 */
                if (
                    (element.wordWithTypo == typoAndSuggestion.wordWithTypo) &&
                    (element.suggestion != typoAndSuggestion.suggestion)
                ){
                    val ans = mySet.remove(element)
                    if (ans)
                        mySet.add(typoAndSuggestion)
                    else
                        println("Did not remove\n")
                }else{
                    /*
                          In case the word doesn't exist insert it
                    */
                    if (
                        typoAndSuggestion != element
                    ){
                        mySet.add(typoAndSuggestion)
                        break
                    }
                }
            }
        }
}