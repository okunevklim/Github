package com.example.github

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.github.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navigateToFragment()
    }

    private fun navigateToFragment() {
        (supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment).navController.setGraph(
            R.navigation.nav_graph
        )
        val navController = findNavController(R.id.navHostFragment)
        navController.navigate(R.id.loginFragment)
    }

    override fun onBackPressed() {
        if (findNavController(R.id.navHostFragment).currentDestination?.id != R.id.listFragment) {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}