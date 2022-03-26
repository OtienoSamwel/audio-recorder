package com.otienosamwel.audio_share.ui.presentation.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object RecordingState {
    var isRecording by mutableStateOf(false)
    var currentScreen by mutableStateOf(Screens.RECORDING)

    val audioRecordingList = mutableStateListOf<Recording>()

    var isAudioPlaying by mutableStateOf(false)
}


enum class Screens { RECORDING, RECORDINGS }

data class Recording(val path: String, val name: String)