package com.jaganrout.voicetranslate

import android.inputmethodservice.InputMethodService
import android.speech.RecognizerIntent
import android.view.View
import android.view.inputmethod.InputConnection
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import android.app.Activity
import android.content.Intent

class VoiceTranslateImeService : InputMethodService() {
    override fun onCreateInputView(): View {
        val root = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; setPadding(8,8,8,8) }
        fun key(label: String, action: () -> Unit) = Button(this).apply { text = label; setOnClickListener { action() } }
        root.addView(key("🎙 Voice") { startVoiceInput() })
        root.addView(key("Copy") { currentInputConnection?.let { Toast.makeText(this, "Use the selected text's copy action", Toast.LENGTH_SHORT).show() } })
        root.addView(key(",") { commitText(",") })
        root.addView(key(".") { commitText(".") })
        root.addView(key("Space") { commitText(" ") })
        root.addView(key("⌫") { currentInputConnection?.deleteSurroundingText(1,0) })
        root.addView(key("Enter") { currentInputConnection?.sendKeyEvent(android.view.KeyEvent(android.view.KeyEvent.ACTION_DOWN, android.view.KeyEvent.KEYCODE_ENTER)) })
        return root
    }
    private fun commitText(text: String) { currentInputConnection?.commitText(text, 1) }
    private fun startVoiceInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now")
        }
        try { startActivity(intent) } catch (_: Exception) { Toast.makeText(this, "Voice input unavailable", Toast.LENGTH_SHORT).show() }
    }
}
