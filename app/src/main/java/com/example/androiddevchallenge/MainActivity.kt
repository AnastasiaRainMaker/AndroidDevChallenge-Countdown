/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import android.os.CountDownTimer
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.animateColorAsState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.androiddevchallenge.ui.theme.MyTheme
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.ui.theme.lightBlue
import com.example.androiddevchallenge.ui.theme.lightPink
import com.example.androiddevchallenge.ui.theme.lightPurple
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = android.R.color.transparent
        setContent {
            MyTheme() {
                MyApp()
            }
        }
    }

    private fun setWindowFlag(bits: Int, on: Boolean) {
        val win = window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }
}

// Start building your app here!
@Composable
fun MyApp() {
    var progress by remember { mutableStateOf(0.0f) }
    var buttonLabel by remember { mutableStateOf("Start") }
    var timeElapsed by remember { mutableStateOf("00h 00m 00s") }
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )

    val animatedColor by animateColorAsState(
        targetValue = when {
            progress < 0.3f -> lightBlue
            progress < 0.6f -> lightPurple
            else -> lightPink
        }
    )

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(height = 600.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = timeElapsed,
            color = Color.White,
            style = TextStyle(fontSize = 30.sp)
        )

    }
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(height = 600.dp)
            .fillMaxWidth()
    ) {
        CircularProgressIndicator(
            strokeWidth = 8.dp,
            progress = animatedProgress, color = animatedColor,
            modifier = Modifier.size(300.dp)
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(Color.Transparent, animatedColor),
                    1700f,
                    2700f
                )
            )
    ) {
        Spacer(modifier = Modifier.height(300.dp).fillMaxWidth())
        StartButton(
            buttonLabel,
            onClick = {
                progress = 0.0f
                buttonLabel = "Stop"
                val timerTime: Long = 10_000
                val timer = object : CountDownTimer(timerTime, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        timeElapsed = "00h 00m ${
                            TimeUnit.MILLISECONDS
                                .toSeconds(10_000 - millisUntilFinished)
                                .toString()
                                .padStart(2, '0')

                        }s"
                        val percentageCompleted = 100 - millisUntilFinished / (timerTime / 100)
                        progress = percentageCompleted / 100f
                    }

                    override fun onFinish() {
                        progress = 1f
                        buttonLabel = "Reset"
                        timeElapsed = "00h 00m ${
                            TimeUnit.MILLISECONDS
                                .toSeconds(timerTime)
                                .toString()
                                .padStart(2, '0')

                        }s"
                    }
                }
                timer.start()
            })
    }

}

@Composable
private fun StartButton(text: String, onClick: () -> Unit) {
    Button(
        shape = RoundedCornerShape(25.dp),
        modifier = Modifier.padding(8.dp).width(120.dp).height(50.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.White,
            contentColor = MaterialTheme.colors.primaryVariant
        ),
        onClick = onClick
    ) {
        Text(text = text, style = MaterialTheme.typography.h6)
    }
}


@Preview("Preview", widthDp = 660, heightDp = 1240)
@Composable
fun LightPreview() {
    MyTheme() {
        MyApp()
    }
}

