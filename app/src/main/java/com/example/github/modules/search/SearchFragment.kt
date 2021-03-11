package com.example.github.modules.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.github.R
import com.example.github.adapters.SearchAdapter
import com.example.github.databinding.FragmentSearchBinding
import com.example.github.interfaces.OnRepositoryClickListener
import com.example.github.models.response.RepositoryModel
import com.example.github.viewmodels.RepositoriesViewModel
import com.example.github.viewmodels.RoomViewModel
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit


class SearchFragment : Fragment(), ISearchView, OnRepositoryClickListener {

    private lateinit var presenter: ISearchPresenter
    private lateinit var inputDisposable: Disposable
    private lateinit var repositoriesViewModel: RepositoriesViewModel
    private lateinit var roomViewModel: RoomViewModel

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val adapter = SearchAdapter(this)
    private var isScrolling = false
    private var pageNumber = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = SearchPresenter()
        val manager = LinearLayoutManager(requireContext())
        roomViewModel = ViewModelProvider(requireActivity()).get(RoomViewModel::class.java)
        binding.searchRecycler.layoutManager = manager
        binding.searchRecycler.adapter = adapter
        binding.clearDb.visibility = View.GONE
        binding.searchRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val currentItems = manager.childCount
                val totalItems = manager.itemCount
                val scrollOutItems = manager.findFirstVisibleItemPosition()
                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                    isScrolling = false
                    pageNumber += 1
                    val newNumber = pageNumber
                    presenter.searchRepositories(
                        binding.searchText.text.toString(),
                        newNumber,
                        false
                    )
                }
            }
        })
        repositoriesViewModel =
            ViewModelProvider(requireActivity())[RepositoriesViewModel::class.java]
        inputDisposable = binding.searchText.textChanges()
            .debounce(300, TimeUnit.MILLISECONDS)
            .map { it.toString() }
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe { performSearch(it, true) }
    }


    override fun performSearch(text: String, isRefresh: Boolean) {
        if (text.isNotEmpty()) {
            presenter.searchRepositories(text, if (isRefresh) 1 else pageNumber++, isRefresh)
        } else {
            handleEmptyResult()
            updateEmptyResult(false)
        }
    }

    override fun handleEmptyResult() {
        binding.searchRecycler.visibility = View.GONE
        binding.errorText.visibility = View.VISIBLE
    }

    override fun updateEmptyResult(isVisible: Boolean) {
        binding.errorText.text = getString(R.string.empty_result)
        binding.errorText.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    override fun handleResults(results: ArrayList<RepositoryModel>, isRefresh: Boolean) {
        binding.searchRecycler.visibility = View.VISIBLE
        binding.errorText.visibility = View.GONE
        adapter.handleResults(results, isRefresh)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!inputDisposable.isDisposed) {
            inputDisposable.dispose()
        }
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

    override fun showRateLimitErrorToast() {
        Toast.makeText(
            requireContext(), getString(R.string.rate_limit_error),
            Toast.LENGTH_SHORT
        ).show()
    }


    override fun updateProgressBar(isVisible: Boolean) {
        binding.progressBar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    override fun onRepositoryClick(repositoryModel: RepositoryModel) {
        val bundle = Bundle()
        bundle.putSerializable("repositoryModel", repositoryModel)
        val navController = requireActivity().findNavController(R.id.navHostFragment)
        navController.navigate(R.id.action_see_details, bundle)
    }
}