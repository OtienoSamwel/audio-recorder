package com.otienosamwel.audio_share

import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.*
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.content.FileProvider
import com.otienosamwel.audio_share.ui.presentation.Record
import com.otienosamwel.audio_share.ui.presentation.Recordings
import com.otienosamwel.audio_share.ui.presentation.state.Recording
import com.otienosamwel.audio_share.ui.presentation.state.RecordingState
import com.otienosamwel.audio_share.ui.presentation.state.Screens
import com.otienosamwel.audio_share.ui.presentation.toast
import com.otienosamwel.audio_share.ui.theme.AudioshareTheme
import java.io.File
import java.util.*

class MainActivity : ComponentActivity() {

    private var recorder: MediaRecorder = MediaRecorder()
    private var player: MediaPlayer = MediaPlayer()
    private val permissionContract =
        registerForActivityResult(RequestMultiplePermissions()) { permissionResult ->
            if (permissionResult.all { it.value == true }) {
                Log.i(TAG, "permissions: granted")
            } else {
                toast("Please grant permissions to use the app.")
                closeApp()
            }
        }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AudioshareTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = colors.background
                ) { Content(this::onRecordClicked, this::onRecordingClicked, this::shareAudioFile) }
            }
        }
        requestPermissions()

        player.setOnCompletionListener { RecordingState.isAudioPlaying = false }
    }

    private fun initMediaRecorder() {
        val filepath =
            "${getExternalFilesDir(Environment.DIRECTORY_MUSIC)}/${UUID.randomUUID()}.mp3"
        with(recorder) {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(filepath)
            prepare()
        }
    }

    private fun requestPermissions() {
        val permissions = arrayOf(
            android.Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        permissionContract.launch(permissions)
    }

    private fun onRecordClicked() {
        if (RecordingState.isRecording) {
            recorder.stop()
            recorder.reset()
            RecordingState.isRecording = false
        } else {
            initMediaRecorder()
            recorder.start()
            RecordingState.isRecording = true
        }
    }

    private fun closeApp() = Handler(Looper.getMainLooper()).postDelayed({ finish() }, 3000)

    private fun onRecordingClicked(recording: Recording) {
        RecordingState.isAudioPlaying = player.isPlaying
        if (player.isPlaying) {
            player.stop()
            RecordingState.isAudioPlaying = false
        } else {
            player.reset()
            player.setDataSource(recording.path)
            player.prepare()
            player.start()
            RecordingState.isAudioPlaying = true
        }
    }

    private fun shareAudioFile(recording: Recording) {
        val file = File(recording.path)
        val uri = FileProvider.getUriForFile(
            this,
            "${this.applicationContext.packageName}.provider",
            file
        )
        val intent = Intent(Intent.ACTION_SEND).also {
            it.type = "audio/*"
            it.putExtra(Intent.EXTRA_STREAM, uri)
            it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(Intent.createChooser(intent, "Share voice recording"))
    }

    companion object {
        private const val TAG = "MAIN ACTIVITY"
    }

    @Composable
    fun Content(
        onRecordButtonClicked: () -> Unit,
        onRecordingClicked: (Recording) -> Unit,
        shareAudioFile: (Recording) -> Unit
    ) {
        when (RecordingState.currentScreen) {
            Screens.RECORDING -> Record(onRecordButtonClicked = onRecordButtonClicked)
            Screens.RECORDINGS -> {
                Util.getAllAppFiles(getExternalFilesDir(Environment.DIRECTORY_MUSIC)!!.absolutePath)
                Recordings(onRecordingClicked = onRecordingClicked, shareFile = shareAudioFile)
            }
        }
    }
}