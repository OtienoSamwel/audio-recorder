package com.otienosamwel.audio_share.ui.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.otienosamwel.audio_share.R
import com.otienosamwel.audio_share.ui.presentation.state.RecordingState
import com.otienosamwel.audio_share.ui.presentation.state.Screens

@Composable
fun Record(onRecordButtonClicked: () -> Unit) {
    val state = RecordingState
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(250.dp))

        if (state.isRecording) RecordingAnimation()

        Spacer(modifier = Modifier.weight(1f))

        RecordingButton(recordingState = state, onRecordButtonClicked = onRecordButtonClicked)

        OutlinedButton(
            onClick = { state.currentScreen = Screens.RECORDINGS },
            shape = CircleShape
        ) {
            Text(text = "See My Recordings", modifier = Modifier.padding(16.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun RecordingAnimation() {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.recording_animation))
    LottieAnimation(
        composition = composition,
        iterations = Int.MAX_VALUE,
        modifier = Modifier.size(300.dp)
    )
}

@Composable
fun RecordingButton(recordingState: RecordingState, onRecordButtonClicked: () -> Unit) {
    val resource = if (recordingState.isRecording) R.drawable.recording else R.drawable.rec_button
    Image(
        painter = painterResource(resource),
        modifier = Modifier
            .padding(16.dp)
            .size(100.dp)
            .clip(CircleShape)
            .clickable(enabled = true, onClick = onRecordButtonClicked),
        contentDescription = "Play pause button"
    )
}