package com.example.assignment1

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast

class SecondActivity : AppCompatActivity() {

    lateinit var congratz : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second2)

        if(intent != null){
            val msg = intent.getStringExtra("Msg")
            congratz = findViewById(R.id.CongratzView)
            congratz.text = String.format(getString(R.string.Congratz) + " %s", msg)
        }
    }

    fun startAgain(v: View){
        val mainIntent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)
    }

    fun shareBtn(v:View){
        val message : String = congratz.text.toString()
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, message)
        intent.type = getString(R.string.textPlain)

        startActivity(Intent.createChooser(intent, getString(R.string.share)))
    }
}
