package com.example.geoquiz

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

const val EXTRA_ANSWER_SHOWN = "com.example.android.geoquiz.answer_shown"
private const val EXTRA_ANSWER_IS_TRUE =
    "com.example.android.geoquiz.answer_is_true"

private const val ANSWER_TEXT = "answer_text"
private const val IS_ANSWER_SHOWN = "is_answer_shown"

class CheatActivity : AppCompatActivity() {

    private var answerIsTrue : Boolean = false
    private lateinit var answerTextView: TextView
    private lateinit var apiTextView: TextView
    private lateinit var showAnswerButton : Button

    private var answerText : Int? = null
    private var isAnswerShown : Boolean? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)
        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        answerTextView = findViewById(R.id.answer_text_view)
        showAnswerButton = findViewById(R.id.show_answer_button)
        apiTextView = findViewById(R.id.api_text_view)

        answerText = savedInstanceState?.getInt(ANSWER_TEXT)
        isAnswerShown = savedInstanceState?.getBoolean(IS_ANSWER_SHOWN, false)


        answerText?.let {
            answerTextView.setText(it)
        }

        setAnswerShownResult()

        showAnswerButton.setOnClickListener {
            answerText = when {
                answerIsTrue -> R.string.true_button
                else -> R.string.false_button
            }
            answerText?.let {
                answerTextView.setText(it)
            }
            isAnswerShown = true
            setAnswerShownResult()
        }

        setAPILevel()

    }

    private fun setAnswerShownResult() {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        }
        setResult(Activity.RESULT_OK, data)
    }

    private fun setAPILevel() {
        apiTextView.setText(R.string.api_level)
        apiTextView.append(Build.VERSION.SDK_INT.toString())
    }


    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        answerText?.let { savedInstanceState.putInt(ANSWER_TEXT, it) }
        isAnswerShown?.let { savedInstanceState.putBoolean(IS_ANSWER_SHOWN, it) }
    }

    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    }

}