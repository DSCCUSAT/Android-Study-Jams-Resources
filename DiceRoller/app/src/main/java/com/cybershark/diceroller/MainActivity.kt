package com.cybershark.diceroller

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnRoll = findViewById<Button>(R.id.btnRoll)

        btnRoll.setOnClickListener {
            rollDice()
        }
    }

    private fun rollDice() {
        val ivDice = findViewById<ImageView>(R.id.ivDice)
        val etFavNumber = findViewById<EditText>(R.id.etFavNumber)

        val randomNumber = (1..6).random()
        when (randomNumber) {
            1 -> ivDice.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dice_1))
            2 -> ivDice.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dice_2))
            3 -> ivDice.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dice_3))
            4 -> ivDice.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dice_4))
            5 -> ivDice.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dice_5))
            6 -> ivDice.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dice_6))
        }

        val favNumberString = etFavNumber.text.toString()
        val favNumber = if (favNumberString.isBlank()) 0 else favNumberString.toInt()
        if (randomNumber == favNumber) {
            Toast.makeText(this, "It's your favorite number!", Toast.LENGTH_SHORT).show()
        }
    }
}