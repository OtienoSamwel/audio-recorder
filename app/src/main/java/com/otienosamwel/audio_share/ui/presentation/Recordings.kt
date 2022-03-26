package com.otienosamwel.audio_share.ui.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.otienosamwel.audio_share.R
import com.otienosamwel.audio_share.ui.presentation.state.Recording
import com.otienosamwel.audio_share.ui.presentation.state.RecordingState
import com.otienosamwel.audio_share.ui.presentation.state.Screens

@Composable
fun Recordings(onRecordingClicked: (Recording) -> Unit, shareFile: (Recording) -> Unit) {
    val state = RecordingState
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Recordings",
            style = MaterialTheme.typography.h3,
            modifier = Modifier.padding(10.dp)
        )
        if (state.isAudioPlaying) PlayingAnimation()
        LazyColumn {
            items(state.audioRecordingList) { item ->
                AudioRecording(
                    recording = item,
                    onRecordingClicked = onRecordingClicked,
                    shareFile = shareFile
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        OutlinedButton(
            onClick = { state.currentScreen = Screens.RECORDING },
            shape = CircleShape
        ) {
            Text(text = "Record more", modifier = Modifier.padding(16.dp))
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun AudioRecording(
    recording: Recording,
    onRecordingClicked: (Recording) -> Unit,
    shareFile: (Recording) -> Unit
) {
    Card(
        elevation = 8.dp, modifier = Modifier
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onRecordingClicked(recording) }) {
                Icon(Icons.Rounded.PlayArrow, contentDescription = "play button")
            }

            Text(text = recording.name)

            IconButton(onClick = { shareFile(recording) }) {
                Icon(Icons.Rounded.Share, contentDescription = "share button")
            }
        }
    }
}

@Composable
fun PlayingAnimation() {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.playing_animation))
    LottieAnimation(
        composition = composition,
        iterations = Int.MAX_VALUE,
        modifier = Modifier.size(100.dp)
    )
}