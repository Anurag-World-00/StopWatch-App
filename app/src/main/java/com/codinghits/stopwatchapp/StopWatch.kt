package com.codinghits.stopwatchapp

import android.app.Dialog
import android.os.Bundle
import android.os.SystemClock
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Chronometer
import android.widget.NumberPicker
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.codinghits.stopwatchapp.databinding.ActivityStopWatchBinding

class StopWatch : AppCompatActivity() {
    private lateinit var binding: ActivityStopWatchBinding
    private var isRunning = false
    private var minutes: Int = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStopWatchBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        var lapsList = ArrayList<String>()
        var adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,lapsList)
        binding.listView.adapter = adapter
        binding.lap.setOnClickListener {
            if(isRunning){
                lapsList.add(binding.chronometer.text.toString())
                adapter.notifyDataSetChanged()
            }
        }
        binding.clock.setOnClickListener {
            // Open the dialog to set time
            val dialogue = Dialog(this)
            dialogue.setContentView(R.layout.dialogue)
            val numberPicker = dialogue.findViewById<NumberPicker>(R.id.numberPicker)
            dialogue.findViewById<Button>(R.id.set_time).setOnClickListener {
                minutes = numberPicker.value  // Get selected minutes from NumberPicker
                binding.clockTime.text = "$minutes mins"  // Update the UI with selected time
                dialogue.dismiss()  // Close the dialog
            }
            numberPicker.minValue = 0
            numberPicker.maxValue = 5
            dialogue.show()
        }

        binding.run.setOnClickListener {
            if (!isRunning) {
                isRunning = true
                if (minutes > 0) {
                    val totalMins = (minutes * 60 * 1000).toLong()  // Convert minutes to milliseconds
                    binding.chronometer.base = SystemClock.elapsedRealtime() + totalMins
                    binding.chronometer.format = "%02d:%02d:%02d" // Format for hours, minutes, and seconds
                    binding.chronometer.start()

                    binding.chronometer.setOnChronometerTickListener { chronometer ->
                        val elapsedTime = SystemClock.elapsedRealtime() - chronometer.base
                        if (elapsedTime >= totalMins) {
                            chronometer.stop()
                            isRunning = false
                            binding.run.text = "Run" // Reset the button text
                        }
                    }
                    binding.run.text = "Stop" // Update the button text when stopwatch is running
                } else {
                    // No time set, start the chronometer from 0
                    binding.chronometer.base = SystemClock.elapsedRealtime()
                    binding.chronometer.start()
                    binding.run.text = "Stop"  // Update button text
                }
            } else {
                binding.chronometer.stop()  // Stop the chronometer
                isRunning = false
                binding.run.text = "Run"  // Reset the button text
            }
        }
    }
}
