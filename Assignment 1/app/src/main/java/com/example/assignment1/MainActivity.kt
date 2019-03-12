package com.example.assignment1

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

class MainActivity : AppCompatActivity() {

    lateinit var question : TextView
    lateinit var nextBtn : Button
    lateinit var startBtn : Button
    lateinit var radioGrp : RadioGroup
    lateinit var radioBtn : RadioButton
    lateinit var radioBtn_2 : RadioButton
    lateinit var radioBtn_3 : RadioButton

    var question_array = arrayOf<String>()
    var radioQuestion_1 = arrayOf<String>()
    var radioQuestion_2 = arrayOf<String>()
    var radioQuestion_3 = arrayOf<String>()
    var answerArray = arrayOf<String>()

    var currentQuestion : Int = 0
    var currentRadioQuestion : Int = 0
    var totalPoint : Int = 0
    var radioGrpCheck : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        question = findViewById(R.id.displayQuestion)
        radioGrp = findViewById(R.id.radioGroup)
        nextBtn = findViewById(R.id.nextBtn)
        startBtn = findViewById(R.id.startBtn)
        radioBtn = findViewById(R.id.questionBtn)
        radioBtn_2 = findViewById(R.id.questionBtn2)
        radioBtn_3 = findViewById(R.id.questionBtn3)
        question_array = resources.getStringArray(R.array.Questions)
        radioQuestion_1 = resources.getStringArray(R.array.Question1)
        radioQuestion_2 = resources.getStringArray(R.array.Question2)
        radioQuestion_3 = resources.getStringArray(R.array.Question3)
        answerArray = resources.getStringArray(R.array.Answer)
        this.question.text = resources.getString(R.string.welcome)
        radioGrp.visibility = View.INVISIBLE
        nextBtn.visibility = View.INVISIBLE
    }


    fun nextAction(v:View){
        radioButtonCheck()

        if(radioGrpCheck){

            currentQuestion++
            currentRadioQuestion++
            if(currentRadioQuestion < radioQuestion_1.size && currentRadioQuestion < radioQuestion_2.size
                && currentRadioQuestion < radioQuestion_3.size && currentQuestion < question_array.size){
                this.radioBtn.text = radioQuestion_1[currentRadioQuestion]
                this.radioBtn_2.text = radioQuestion_2[currentRadioQuestion]
                this.radioBtn_3.text = radioQuestion_3[currentRadioQuestion]
                this.question.text = question_array[currentQuestion]
            }else{
                val basicIntent = Intent(this, SecondActivity::class.java)
                basicIntent.putExtra("Msg", totalPoint.toString())
                startActivity(basicIntent)
            }
        }
        radioGrp.clearCheck()
    }

    fun radioButtonCheck(){
        radioGrp = findViewById(R.id.radioGroup)
        var id = radioGrp.checkedRadioButtonId
        val radioBtnCheck = findViewById<RadioButton>(id)
        if(id == -1){
            Toast.makeText(this, R.string.radio_btn_check_alter, Toast.LENGTH_LONG).show()
            radioGrpCheck = false
        }else{
            if(radioBtnCheck?.text == answerArray[currentQuestion]){
                totalPoint++
                radioGrpCheck = true
            }
            radioGrpCheck = true
        }
    }

    fun startAction(v:View){
        this.question.text = question_array[currentQuestion]
        this.radioBtn.text = radioQuestion_1[currentRadioQuestion]
        this.radioBtn_2.text = radioQuestion_2[currentRadioQuestion]
        this.radioBtn_3.text = radioQuestion_3[currentRadioQuestion]

        radioGrp.visibility = View.VISIBLE
        nextBtn.visibility = View.VISIBLE
        startBtn.visibility = View.INVISIBLE
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState!!.putCharSequence(getString(R.string.text), question.text)
        outState!!.putCharSequence(getString(R.string.question_1), radioBtn.text)
        outState!!.putCharSequence(getString(R.string.question_2),radioBtn_2.text)
        outState!!.putCharSequence(getString(R.string.question_3), radioBtn_3.text)
        outState!!.putInt("Score", totalPoint)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)

        question.text = savedInstanceState!!.getCharSequence(getString(R.string.text))
        radioBtn.text = savedInstanceState!!.getCharSequence(getString(R.string.question_1))
        radioBtn_2.text = savedInstanceState!!.getCharSequence(getString(R.string.question_2))
        radioBtn_3.text = savedInstanceState!!.getCharSequence(getString(R.string.question_3))
        totalPoint = savedInstanceState!!.getInt("Score")
    }

}
