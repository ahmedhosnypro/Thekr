package com.thekr.util

import android.content.Context
import android.speech.tts.TextToSpeech
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Locale


class TTS(private var context: Context, var text: String) {
    var textToSpeech: TextToSpeech? = null

    init {
        textToSpeech()
    }

    private fun textToSpeech() {
        textToSpeech = TextToSpeech(
            context
        ) {
            if (it == TextToSpeech.SUCCESS) {
                textToSpeech?.let { txtToSpeech ->
                    txtToSpeech.language = Locale.US
                    txtToSpeech.setSpeechRate(1.0f)
                    txtToSpeech.speak(
                        text,
                        TextToSpeech.QUEUE_ADD,
                        null,
                        null
                    )
                }
            }
        }
    }
}


object TTSSpeaker {
    private var lastTextToSpeech: TextToSpeech? = null
    private var job: Job? = null
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val lock = Any()

    fun textToSpeech(context: Context, text: String) {
        synchronized(lock) {
            // Every TTS() below constructs a new engine; release the previous
            // one or its service connection leaks.
            lastTextToSpeech?.let {
                it.stop()
                it.shutdown()
            }
            lastTextToSpeech = null

            job?.cancel()

            job = coroutineScope.launch {
                delay(200)
                val tts = TTS(context, text)

                synchronized(lock) {
                    if (!isActive) {
                        // Superseded while delayed: this call's release ran
                        // before the engine existed, so dispose it here or its
                        // service connection leaks.
                        tts.textToSpeech?.let {
                            it.stop()
                            it.shutdown()
                        }
                    } else {
                        lastTextToSpeech = tts.textToSpeech
                    }
                }
            }
        }
    }
}