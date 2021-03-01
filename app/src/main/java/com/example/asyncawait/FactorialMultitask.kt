package com.example.asyncawait

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.asyncawait.databinding.ActivityFactorialMultitaskBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import java.math.BigInteger
import kotlin.random.Random
import kotlin.system.measureTimeMillis

class FactorialMultitask : AppCompatActivity() {

    private lateinit var binding: ActivityFactorialMultitaskBinding
    private val data = (0..50).map { Random.nextInt(2_000, 3_000) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFactorialMultitaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnStart.setOnClickListener { startMultiTaskCalculate() }
    }

    private fun startMultiTaskCalculate() {
        val time = measureTimeMillis {
            GlobalScope.launch {
                Log.d("insScope", Thread.currentThread().name)
                val sum = data.map {
                    async { calculateFactorial(it) }
                }.awaitAll().sum()
                binding.textResult.text = sum.toString()
                Log.d("result", sum.toString())
            }
        }
        binding.textTime.text = time.toString()
        Log.d("outScope", Thread.currentThread().name)
    }

    private fun calculateFactorial(number: Int): Int {
        Log.d("inFactorial", Thread.currentThread().name)
        var factorial = BigInteger.ONE
        for (i in 1..number) {
            factorial = factorial.multiply(BigInteger.valueOf(i.toLong()))
        }
        return factorial.toInt()
    }
}