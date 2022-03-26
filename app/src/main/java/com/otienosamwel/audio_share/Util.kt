package com.otienosamwel.audio_share

import com.otienosamwel.audio_share.ui.presentation.state.Recording
import com.otienosamwel.audio_share.ui.presentation.state.RecordingState
import java.io.File

object Util {
    fun getAllAppFiles(path: String) {
        val files = File(path).listFiles()
        RecordingState.audioRecordingList.clear()
        files?.forEach {
            RecordingState.audioRecordingList.add(
                Recording(
                    path = it.absolutePath,
                    name = it.name
                )
            )
        }
    }
}