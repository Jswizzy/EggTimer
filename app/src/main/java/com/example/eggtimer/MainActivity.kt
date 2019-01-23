package com.example.eggtimer

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    private var counterIsActive = false
    private var countDownTimer: CountDownTimer? = null

    @SuppressLint("SetTextI18n")
    private fun updateTimer(totalSeconds: Int) {
        val minutes = totalSeconds / 60
        val seconds = totalSeconds - minutes * 60

        val df = DecimalFormat("00")

        timeTextView.text = "$minutes:${df.format(seconds)}"
        timerSeekBar.progress = totalSeconds
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timerSeekBar.apply {
            max = 600
            progress = 30
        }

        updateTimer(timerSeekBar.progress)

        timerSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateTimer(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
    }

    private fun resetTimer() {
        timerSeekBar.progress = START_TIME_IN_SECS
        timerSeekBar.isEnabled = true
        updateTimer(START_TIME_IN_SECS)
        countDownTimer?.cancel()
        startButton.text = getString(R.string.start)
        counterIsActive = false
    }

    fun onStart(view: View) {
        when (counterIsActive) {
            true -> {
                resetTimer()
            }
            false -> {
                counterIsActive = true
                timerSeekBar.isEnabled = false
                startButton.text = getString(R.string.stop)
                val time = (timerSeekBar.progress * SECONDS_TO_MILLIS)

                countDownTimer = object : CountDownTimer(time, SECONDS_TO_MILLIS) {
                    override fun onFinish() {
                        MediaPlayer
                            .create(applicationContext, R.raw.rooster)
                            .start()

                        toast("DONE!")

                        resetTimer()
                    }

                    override fun onTick(millisUntilFinished: Long) {
                        updateTimer((millisUntilFinished / SECONDS_TO_MILLIS).toInt())
                    }
                }.start()
            }
        }
    }
}

private const val SECONDS_TO_MILLIS = 1000L
private const val START_TIME_IN_SECS = 30

fun Context.toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()