package com.example.gitobserverapp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gitobserverapp.App
import com.example.gitobserverapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding?= null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}