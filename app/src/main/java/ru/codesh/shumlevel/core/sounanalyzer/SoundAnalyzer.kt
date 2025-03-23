package ru.codesh.shumlevel.core.sounanalyzer

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.sqrt

class SoundAnalyzer(
    private val context: Context,
    private val sampleRate: Int = 44100,
    private val intervalMs: Long = 100L // как часто считывать громкость
) {

    private val bufferSize: Int = AudioRecord.getMinBufferSize(
        sampleRate,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT
    )

    private var audioRecord: AudioRecord? = null
    private var job: Job? = null

    private val _decibelFlow = MutableStateFlow(0f)
    val decibelFlow: StateFlow<Float> = _decibelFlow.asStateFlow()

    fun start() {
        if (audioRecord == null) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.RECORD_AUDIO
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            audioRecord = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize
            )
        }

        if (audioRecord?.state != AudioRecord.STATE_INITIALIZED) return

        audioRecord?.startRecording()

        job = CoroutineScope(Dispatchers.IO).launch {
            val buffer = ShortArray(bufferSize)
            while (isActive) {
                val read = audioRecord?.read(buffer, 0, buffer.size) ?: 0
                if (read > 0) {
                    val amplitude = calculateRMS(buffer, read)
                    val decibel = amplitudeToDb(amplitude)
                    _decibelFlow.emit(decibel)
                }
                delay(intervalMs)
            }
        }
    }

    fun stop() {
        job?.cancel()
        audioRecord?.stop()
        audioRecord?.release()
        audioRecord = null
    }

    private fun calculateRMS(buffer: ShortArray, readSize: Int): Double {
        val sum = buffer
            .take(readSize)
            .sumOf { it.toDouble().pow(2.0) }
        return sqrt(sum / readSize)
    }

    private fun amplitudeToDb(amplitude: Double): Float {
        return if (amplitude > 0) {
            (20 * log10(amplitude)).toFloat()
        } else {
            0f
        }
    }
}