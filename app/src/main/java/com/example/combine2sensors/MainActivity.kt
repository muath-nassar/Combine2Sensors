package com.example.combine2sensors

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.combine2sensors.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val questions = arrayListOf<CharSequence>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnAdd.setOnClickListener {
            if (binding.etQuestion.text.isNotEmpty()) {
                val q = binding.etQuestion.text.toString()
                questions.add(q)
                binding.etQuestion.text.clear()
            } else {
                Toast.makeText(this, "أضف نص السؤال", Toast.LENGTH_SHORT).show()
            }
        }


        binding.btnStart.setOnClickListener {
            if(questions.size >= 2){
                val intent = Intent(this,Game::class.java)
                intent.putExtra("questions",questions)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this, "Must have more than 2", Toast.LENGTH_SHORT).show()
            }

        }
    }


}