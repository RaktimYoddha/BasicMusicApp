package com.raktimyoddha.beginnerlevelmusicapp

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var seekBar: SeekBar
    private var mediaPlayer: MediaPlayer? = null //this ? is safe call operator used to declare null values
    private lateinit var runnable: Runnable
    private  lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seekBar = findViewById(R.id.seekBar)
        handler = Handler(Looper.getMainLooper())

        val playButton = findViewById<FloatingActionButton>(R.id.fabPlay)
        playButton.setOnClickListener{
            if(mediaPlayer==null){
                mediaPlayer = MediaPlayer.create(this,R.raw.faded)
                initializeSeekBar() //when we initialise the media player we should also initialise its seek bar
            }
            mediaPlayer?.start()  //mediaPlayer? is used because we need to add the ? everytime as we added it at time of declaration
        }
        val pauseButton = findViewById<FloatingActionButton>(R.id.fabPause)
        pauseButton.setOnClickListener{
            mediaPlayer?.pause()
        }
        val stopButton = findViewById<FloatingActionButton>(R.id.fabstop)
        stopButton.setOnClickListener{
            mediaPlayer?.stop()
            mediaPlayer?.reset()  //do reset and release to avoid unnecessary memory consumption
            mediaPlayer?.release()
            mediaPlayer = null
            handler.removeCallbacks(runnable)
            seekBar.progress = 0 //this will make seek bar go to starting again i.e. 0 progress position
        }



    }
    private fun initializeSeekBar(){
        seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) mediaPlayer?.seekTo(progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })
        //for time played show
        val tvPlayed = findViewById<TextView>(R.id.tvPlayed)
        seekBar.max= mediaPlayer!!.duration //!! is not null assertion operator
        runnable = Runnable {
            seekBar.progress = mediaPlayer!!.currentPosition //this currentPosition property helps to find position and implement it easily

            val playedTime = mediaPlayer!!.currentPosition/1000   // because its in miliseconds and dividing with 1000 will give second
            tvPlayed.text = "0:0$playedTime"

            handler.postDelayed(runnable,1000)
        }
        handler.postDelayed(runnable,1000)
    }
}