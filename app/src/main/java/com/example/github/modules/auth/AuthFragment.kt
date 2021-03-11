package com.example.github.modules.auth

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.github.R
import com.example.github.databinding.FragmentAuthBinding
import com.example.github.utils.Constants.ALLOW_SIGNUP
import com.example.github.utils.Constants.CLIENT_ID
import com.example.github.utils.Constants.GITHUB
import com.example.github.utils.Constants.NOT_AUTHORIZED
import com.example.github.utils.Constants.REDIRECT_URI
import com.example.github.viewmodels.RepositoriesViewModel

class AuthFragment : Fragment(), IAuthView {
    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter: IAuthPresenter
    private lateinit var repositoriesViewModel: RepositoriesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = AuthPresenter()
        repositoriesViewModel =
            ViewModelProvider(requireActivity())[RepositoriesViewModel::class.java]
        binding.webView.apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            webViewClient = object : WebViewClient() {

                override fun shouldOverrideUrlLoading(
                    view: WebView,
                    request: WebResourceRequest
                ): Boolean {
                    return false
                }

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    if (url != null) {
                        if (url.contains("?code=")) {
                            val uri = Uri.parse(url)
                            val authCode = uri.getQueryParameter("code")
                            if (!authCode.isNullOrEmpty()) {
                                presenter.getAccessToken(authCode, repositoriesViewModel)
                            } else {
                                repositoriesViewModel.handleIsAuthorized(NOT_AUTHORIZED)
                                navigateToListFragment()
                            }

                        } else if (url.contains("error")) {
                            Log.i("Github", "ACCESS_DENIED_HERE")
                            Toast.makeText(context, "Error Occured", Toast.LENGTH_SHORT)
                                .show()
                            repositoriesViewModel.handleIsAuthorized(NOT_AUTHORIZED)
                            navigateToListFragment()
                        }
                    }
                }
            }
        }
        binding.webView.loadUrl("$GITHUB/login/oauth/authorize?client_id=$CLIENT_ID&redirect_uri=$REDIRECT_URI&scope=&allow_signup=$ALLOW_SIGNUP")
    }

    override fun onStop() {
        super.onStop()
        presenter.detachView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        presenter.attachView(this)
    }

    override fun navigateToListFragment() {
        val navController = requireActivity().findNavController(R.id.navHostFragment)
        navController.navigate(R.id.action_list)
    }
}