package com.example.github.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.github.R
import com.example.github.adapters.CategoryAdapter
import com.example.github.databinding.FragmentListBinding
import com.example.github.utils.Constants.AUTHORIZED
import com.example.github.utils.Constants.NOT_AUTHORIZED
import com.example.github.utils.InternetConnectionChecker
import com.example.github.utils.PreferencesHelper
import com.example.github.utils.SnackBarHelper
import com.example.github.viewmodels.RepositoriesViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private lateinit var repositoriesViewModel: RepositoriesViewModel
    private var isAuthorized = false

    private val tabIcons = intArrayOf(
        R.drawable.ic_search,
        R.drawable.ic_saved
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout ->
                repositoriesViewModel.getIsAuthorizedLiveData().observe(viewLifecycleOwner, {
                    if (it == 1) {
                        logout(false)
                    } else logout(true)
                })
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.user_info_menu, menu)
    }

    private fun logout(guest: Boolean) {
        if (!guest) {
            PreferencesHelper.clear()
        }
        val navController = requireActivity().findNavController(R.id.navHostFragment)
        navController.navigate(R.id.action_back_to_login)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        binding.toolbar.inflateMenu(R.menu.user_info_menu)
        setHasOptionsMenu(true)
        checkInternetConnection()
        initViewModel()
        val sections = arrayOf(
            getString(R.string.search),
            getString(R.string.saved)
        )
        setupViewPager()
        TabLayoutMediator(
            binding.tabLayout, binding.viewPager2, true, false
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text = sections[position]
        }.attach()
        repositoriesViewModel.getIsAuthorizedLiveData().observe(viewLifecycleOwner, {
            binding.tabLayout.getTabAt(1)?.view?.visibility =
                if (it == AUTHORIZED) View.VISIBLE else View.GONE
            if (it == NOT_AUTHORIZED) {
                if (!isAuthorized) {
                    showGuestSnackBar()
                    isAuthorized = true
                }

            }
        })
        repositoriesViewModel.getIsClearedLiveData().observe(viewLifecycleOwner, {
            if (it == 1) {
                binding.tabLayout.getTabAt(0)?.select()
            }
        })
        setupTabIcons()
    }

    private fun setupTabIcons() {
        binding.tabLayout.getTabAt(0)?.setIcon(tabIcons[0])
        binding.tabLayout.getTabAt(1)?.setIcon(tabIcons[1])
    }

    private fun setupViewPager() {
        binding.viewPager2.offscreenPageLimit = 2
        binding.viewPager2.isUserInputEnabled = false
        binding.viewPager2.adapter = CategoryAdapter(requireActivity())
    }

    private fun checkInternetConnection() {
        InternetConnectionChecker.checkInternetConnection(viewLifecycleOwner, { isConnected ->
            if (!isConnected) {
                showErrorNetworkSnackBar()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showErrorNetworkSnackBar() {
        requireActivity().runOnUiThread {
            SnackBarHelper.showSnackBar(
                binding.coordinator,
                getString(R.string.no_internet_title),
                R.drawable.bg_rounded_error_snackbar,
                R.drawable.ic_exclamation
            )
        }
    }

    private fun initViewModel() {
        repositoriesViewModel =
            ViewModelProvider(requireActivity())[RepositoriesViewModel::class.java]
    }

    private fun showGuestSnackBar() {
        requireActivity().runOnUiThread {
            SnackBarHelper.showSnackBar(
                binding.coordinator,
                getString(R.string.guest_auth),
                R.drawable.bg_rounded_warning_snackbar,
                R.drawable.ic_exclamation
            )
        }
    }
}