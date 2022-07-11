package com.example.geoquiz

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
private const val EXTRA_ANSWER_IS_TRUE =
    "com.example.android.geoquiz.answer_is_true"
private const val REQUEST_CODE_CHEAT = 0
private const val CHEATS_LIMIT = 3

class MainActivity : AppCompatActivity() {

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }

    private lateinit var trueButton : Button
    private lateinit var falseButton : Button
    private lateinit var nextButton : Button
    private lateinit var cheatButton: Button
    private lateinit var questionTextView: TextView
    private lateinit var numberCheatsTextView : TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex

        // views
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        cheatButton = findViewById(R.id.cheat_button)
        questionTextView = findViewById(R.id.question_text_view)
        numberCheatsTextView = findViewById(R.id.number_cheats_text_view)

        // listeners
        trueButton.setOnClickListener {view : View ->
            checkAnswer(true)
        }

        falseButton.setOnClickListener {view : View ->
            checkAnswer(false)


        }

        nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()

            quizViewModel.isCheater = false

        }

        cheatButton.setOnClickListener {view ->

            if (quizViewModel.numberCheats >= CHEATS_LIMIT) {
                return@setOnClickListener
            }

            val answerIsTrue : Boolean = quizViewModel.currentQuestionAnswer
            val intent : Intent = CheatActivity.newIntent(
                this@MainActivity, answerIsTrue)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val options = ActivityOptions
                    .makeClipRevealAnimation(view, 0, 0, view.width, view.height)
                startActivityForResult(intent, REQUEST_CODE_CHEAT, options.toBundle())

            }
            else {
                startActivityForResult(intent, REQUEST_CODE_CHEAT)
            }
        }



        updateQuestion()
        updateNumberCheats()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            quizViewModel.isCheater = data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
            if (quizViewModel.isCheater) {
                quizViewModel.increaseNumberCheats()
                updateNumberCheats()
            }
        }

    }



    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.d(TAG, "onSaveInstanceState(Bundle) called")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
    }



    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
    }

    private fun updateNumberCheats() {
        val numberCheats = quizViewModel.numberCheats
       // val text = "${R.string.number_cheats.()} $numberCheats/$CHEATS_LIMIT "
        numberCheatsTextView.setText(R.string.number_cheats)
        numberCheatsTextView.append(" $numberCheats/$CHEATS_LIMIT")
    }

    private fun checkAnswer(userAnswer : Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer
        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }

}



/*
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }
    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

*/