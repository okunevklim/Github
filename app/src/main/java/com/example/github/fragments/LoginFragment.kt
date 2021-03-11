package com.example.github.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.github.R
import com.example.github.databinding.FragmentLoginBinding
import com.example.github.utils.Constants.NOT_AUTHORIZED
import com.example.github.utils.InternetConnectionChecker
import com.example.github.utils.PreferencesHelper
import com.example.github.viewmodels.RepositoriesViewModel

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var repositoriesViewModel: RepositoriesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        checkAccessToken()
    }

    private fun checkAccessToken() {
        if (PreferencesHelper.getAccessToken().isNotBlank()) {
            navigateToListFragment()
        } else {
            InternetConnectionChecker.checkInternetConnection(viewLifecycleOwner, { isConnected ->
                if (isConnected) {
                    handleErrorLayout(false)
                    binding.loginButton.setOnClickListener {
                        navigateToAuthFragment()
                    }
                    binding.authText.setOnClickListener {
                        repositoriesViewModel.handleIsAuthorized(NOT_AUTHORIZED)
                        navigateToListFragment()
                    }
                } else {
                    handleErrorLayout(true)
                }
            })

        }
    }

    private fun initViewModel() {
        repositoriesViewModel =
            ViewModelProvider(requireActivity())[RepositoriesViewModel::class.java]
    }

    private fun handleErrorLayout(isShow: Boolean) {
        binding.disconnectedImage.visibility = if (isShow) View.VISIBLE else View.GONE
        binding.disconnectedText.visibility = if (isShow) View.VISIBLE else View.GONE
        binding.authText.visibility = if (isShow) View.GONE else View.VISIBLE
        binding.loginButton.visibility = if (isShow) View.GONE else View.VISIBLE
    }

    private fun navigateToAuthFragment() {
        val navController = requireActivity().findNavController(R.id.navHostFragment)
        navController.navigate(R.id.action_auth)
    }

    private fun navigateToListFragment() {
        val navController = requireActivity().findNavController(R.id.navHostFragment)
        navController.navigate(R.id.action_list)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}