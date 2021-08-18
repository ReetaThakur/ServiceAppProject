package com.example.timer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.timer.databinding.ActivityMainBinding
import java.util.*
import kotlin.math.min
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var timerStarted=true
    private lateinit var serviceIntent:Intent
    private var time=0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnReset.setOnClickListener{
            resetTime()
        }
        binding.btnStart.setOnClickListener{
            startTimer()
        }
        serviceIntent=Intent(applicationContext,TimerService::class.java)
        registerReceiver(updateTime, IntentFilter (TimerService.TIMER_UPDATED))
    }
    private val updateTime:BroadcastReceiver=object :BroadcastReceiver(){
        override fun onReceive(context: Context, intent: Intent) {
            time=intent.getDoubleExtra(TimerService.TIME_EXTA,0.0)
            binding.txtTime.text=getTimeStringFromDouble(time)
        }

    }

    private fun getTimeStringFromDouble(time: Double): String {
        val resultInt=time.roundToInt()
        val hour=resultInt%86400/3600
        val minutes=resultInt % 86400 /60
        val seconds=resultInt % 3600 %60
        return makeTimeString(hour, minutes,seconds)

    }

    private fun makeTimeString(hour: Int, minutes: Int, seconds: Int): String = String.format("%02d:%02d:%02d",hour,minutes,seconds)

    private fun resetTime() {
        stoptime()
        time=0.0
        binding.txtTime.text=getTimeStringFromDouble(time)
    }

    private fun startTimer() {
        if (timerStarted)
            startTime()
        else
            stoptime()
    }

    private fun stoptime() {
        stopService(serviceIntent)
        binding.btnStart.text="start"
        binding.btnStart.icon=getDrawable(R.drawable.ic_baseline_play_arrow_24)
        timerStarted=false
    }

    private fun startTime() {
        serviceIntent.putExtra(TimerService.TIME_EXTA,time)
        startService(serviceIntent)
        binding.btnStart.text="stop"
        binding.btnStart.icon=getDrawable(R.drawable.ic_baseline_pause_24)
        timerStarted=true
    }
}