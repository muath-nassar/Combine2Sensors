package com.example.combine2sensors

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.combine2sensors.databinding.ActivityGameBinding
import java.lang.Exception

class Game : AppCompatActivity(), SensorEventListener {
    var sensorP: Sensor? = null
    var sensorAcc: Sensor? = null
    lateinit var sm: SensorManager
    private var isRunning = true
    private var windowIsActive = true
    private lateinit var showQuestionsThread: Thread
    lateinit var binding: ActivityGameBinding
    var list = arrayListOf<CharSequence>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sm = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorP = sm.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        sensorAcc = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        try {
            list = intent.getCharSequenceArrayListExtra("questions")!!
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
        }

        showQuestionsThread = Thread {
            var index = 0
            while (windowIsActive) {
                if (isRunning) {
                    if (index >= list.size) index = 0
                    runOnUiThread {
                        binding.tvQuestion.text = list[index++]
                    }
                }
                Thread.sleep(500)
                Log.d("ttt", index.toString())
            }

        }


    }

        override fun onResume() {
            super.onResume()
            sensorP?.let {
                sm.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
            }
            sensorAcc?.let {
                sm.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
            }
            showQuestionsThread.start()

        }

        override fun onPause() {
            super.onPause()
            sm.unregisterListener(this)
        }

        override fun onSensorChanged(event: SensorEvent) {
            if (event.sensor.type == Sensor.TYPE_PROXIMITY) {
                isRunning = event.values[0] > 0

            } else if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                val z = event.values[2]
                if (z < -9f) {
                    val i = Intent(this, MainActivity::class.java)
                    windowIsActive = false
                    startActivity(i)
                    finish()
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

        }
    }