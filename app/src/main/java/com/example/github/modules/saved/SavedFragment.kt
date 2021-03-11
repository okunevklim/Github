package com.example.github.modules.saved

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.github.R
import com.example.github.adapters.SavedAdapter
import com.example.github.databinding.FragmentSearchBinding
import com.example.github.interfaces.OnRepositoryClickListener
import com.example.github.models.response.RepositoryModel
import com.example.github.utils.SnackBarHelper
import com.example.github.viewmodels.RepositoriesViewModel
import com.example.github.viewmodels.RoomViewModel
import java.util.*

class SavedFragment : Fragment(), ISavedView, OnRepositoryClickListener {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter: ISavedPresenter
    private lateinit var repositoriesViewModel: RepositoriesViewModel
    private lateinit var roomViewModel: RoomViewModel

    private val adapter = SavedAdapter(this)
    private var savedRepositories = arrayListOf<RepositoryModel>()
    private val searchArray = arrayListOf<RepositoryModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = SavedPresenter()
        setupRecycler()
        initViewModels()
        presenter.checkDatabase(roomViewModel)
        binding.errorText.text = getString(R.string.empty_saved)
        binding.clearDb.setOnClickListener {
            presenter.clearDatabase(roomViewModel, repositoriesViewModel)
        }
        binding.searchText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                performSearch(s?.trim())
            }
        })
    }

    override fun performSearch(text: CharSequence?) {
        searchArray.clear()
        if (!text.isNullOrEmpty()) {
            presenter.loadPostsFromDatabase(roomViewModel, true)
            savedRepositories.forEach {
                if (it.name?.toLowerCase(Locale.ROOT)
                        ?.contains(text.toString().trim().toLowerCase(Locale.ROOT)) == true
                ) {
                    handleEmptyResult(false)
                    searchArray.add(it)
                }
            }
            handleRepositories(searchArray)
        } else {
            handleEmptyResult(false)
            presenter.loadPostsFromDatabase(roomViewModel, false)
            handleRepositories(savedRepositories)
        }

    }

    override fun handleEmptyResult(isVisible: Boolean) {
        if (isVisible) {
            binding.errorText.visibility = View.VISIBLE
            binding.errorText.text = getString(R.string.empty_result)
        } else {
            binding.errorText.visibility = View.GONE
            binding.errorText.text = getString(R.string.empty_saved)
        }

    }

    override fun updateSavedRepositories(repositories: ArrayList<RepositoryModel>) {
        savedRepositories.clear()
        savedRepositories.addAll(repositories)
    }

    override fun updateErrorText(isShow: Boolean) {
        binding.errorText.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun updateSearchField(isShow: Boolean) {
        binding.searchText.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun hideRecycler() {
        binding.searchRecycler.visibility = View.GONE
    }

    override fun showRecycler() {
        binding.searchRecycler.visibility = View.VISIBLE
    }

    override fun setupRecycler() {
        binding.searchRecycler.visibility = View.VISIBLE
        binding.searchRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.searchRecycler.adapter = adapter
    }

    override fun handleClearButton(isShow: Boolean) {
        binding.clearDb.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun handleRepositories(repositories: ArrayList<RepositoryModel>) {
        savedRepositories.clear()
        savedRepositories.addAll(repositories)
        adapter.handleRepositories(repositories)
    }

    override fun onStop() {
        super.onStop()
        presenter.detachView()
    }

    override fun onStart() {
        super.onStart()
        presenter.attachView(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        presenter.checkDatabase(roomViewModel)
    }

    override fun showSnackBar() {
        requireActivity().runOnUiThread {
            SnackBarHelper.showSnackBar(
                binding.root,
                getString(R.string.db_failed),
                R.drawable.bg_rounded_error_snackbar,
                R.drawable.ic_exclamation
            )
        }
    }

    override fun showSuccessfulSnackBar() {
        requireActivity().runOnUiThread {
            SnackBarHelper.showSnackBar(
                binding.root,
                getString(R.string.db_cleared),
                R.drawable.bg_rounded_successful_snackbar,
                R.drawable.ic_successful_tick
            )
        }
    }

    override fun initViewModels() {
        repositoriesViewModel =
            ViewModelProvider(requireActivity())[RepositoriesViewModel::class.java]
        roomViewModel = ViewModelProvider(requireActivity()).get(RoomViewModel::class.java)
    }

    override fun onRepositoryClick(repositoryModel: RepositoryModel) {
        val bundle = Bundle()
        bundle.putSerializable("repositoryModel", repositoryModel)
        val navController = requireActivity().findNavController(R.id.navHostFragment)
        navController.navigate(R.id.action_see_details, bundle)
    }
}