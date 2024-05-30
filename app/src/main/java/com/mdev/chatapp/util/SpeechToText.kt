package com.mdev.chatapp.util

import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import com.mdev.chatapp.ui.chat.ChatUIEvent
import com.mdev.chatapp.ui.chat.ChatViewModel


class SpeechListener(private val chatViewModel: ChatViewModel) : RecognitionListener {
    /**
     * Called when the endpointer is ready for the user to start speaking.
     *
     * @param params parameters set by the recognition service. Reserved for future use.
     */
    override fun onReadyForSpeech(params: Bundle?) {
        chatViewModel.onEvent(ChatUIEvent.IsListening(true))
    }


    /**
     * The user has started to speak.
     */
    override fun onBeginningOfSpeech() = Unit

    /**
     * The sound level in the audio stream has changed. There is no guarantee that this method will
     * be called.
     *
     * @param rmsdB the new RMS dB value
     */
    override fun onRmsChanged(rmsdB: Float) = Unit

    /**
     * More sound has been received. The purpose of this function is to allow giving feedback to the
     * user regarding the captured audio. There is no guarantee that this method will be called.
     *
     * @param buffer a buffer containing a sequence of big-endian 16-bit integers representing a
     * single channel audio stream. The sample rate is implementation dependent.
     */
    override fun onBufferReceived(buffer: ByteArray?) = Unit

    /**
     * Called after the user stops speaking.
     */
    override fun onEndOfSpeech() {
        chatViewModel.onEvent(ChatUIEvent.IsListening(false))
    }

    override fun onError(error: Int) {
        chatViewModel.onEvent(ChatUIEvent.IsListening(false))
        if(error == SpeechRecognizer.ERROR_CLIENT){
            return
        }
        chatViewModel.state = chatViewModel.state.copy(speechToTextError = error)
    }


    /**
     * Called when recognition results are ready.
     *
     *
     *
     * Called with the results for the full speech since [.onReadyForSpeech].
     * To get recognition results in segments rather than for the full session see
     * [RecognizerIntent.EXTRA_SEGMENTED_SESSION].
     *
     *
     * @param results the recognition results. To retrieve the results in `ArrayList<String>` format use [Bundle.getStringArrayList] with
     * [SpeechRecognizer.RESULTS_RECOGNITION] as a parameter. A float array of
     * confidence values might also be given in [SpeechRecognizer.CONFIDENCE_SCORES].
     */
    override fun onResults(results: Bundle?) {
        results
            ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            ?.getOrNull(0)
            ?.let {
                chatViewModel.onEvent(ChatUIEvent.IsListening(false))
                val matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    chatViewModel.onEvent(ChatUIEvent.OnInputMessageChangedByListening(matches[0]))
                }
            }
    }

    /**
     * Called when partial recognition results are available. The callback might be called at any
     * time between [.onBeginningOfSpeech] and [.onResults] when partial
     * results are ready. This method may be called zero, one or multiple times for each call to
     * [SpeechRecognizer.startListening], depending on the speech recognition
     * service implementation.  To request partial results, use
     * [RecognizerIntent.EXTRA_PARTIAL_RESULTS]
     *
     * @param partialResults the returned results. To retrieve the results in
     * ArrayList&lt;String&gt; format use [Bundle.getStringArrayList] with
     * [SpeechRecognizer.RESULTS_RECOGNITION] as a parameter
     */

    override fun onPartialResults(partialResults: Bundle?) = Unit

    /**
     * Reserved for adding future events.
     *
     * @param eventType the type of the occurred event
     * @param params a Bundle containing the passed parameters
     */
    override fun onEvent(eventType: Int, params: Bundle?) = Unit
}
