package com.example.github.modules.details

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.github.R
import com.example.github.databinding.FragmentDetailsBinding
import com.example.github.models.response.RepositoryModel
import com.example.github.utils.CounterFormatter.formatCounterValue
import com.example.github.utils.DateFormatter
import com.example.github.utils.InternetConnectionChecker
import com.example.github.utils.SnackBarHelper
import com.example.github.viewmodels.RepositoriesViewModel
import com.example.github.viewmodels.RoomViewModel
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

class DetailsFragment : Fragment(), IDetailsView {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var repositoriesViewModel: RepositoriesViewModel
    private lateinit var roomViewModel: RoomViewModel
    private lateinit var presenter: IDetailsPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = DetailsPresenter()
        InternetConnectionChecker.checkInternetConnection(viewLifecycleOwner, { isConnected ->
            if (!isConnected) {
                showSnackBar()
            }
        })
        initViewModel()
        val repositoryModel =
            requireArguments().getSerializable("repositoryModel") as RepositoryModel
        presenter.isFromDatabase(roomViewModel, repositoryModel, repositoriesViewModel)
        binding.repoName.text = repositoryModel.name
        val repositoryLogin = "Автор: ${repositoryModel.owner?.login}"
        val targetPhoto = object : Target {
            override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                binding.avatar.setImageBitmap(bitmap)
            }

            override fun onBitmapFailed(p0: Exception?, p1: Drawable?) {
                binding.avatar.setImageResource(R.drawable.ic_photoholder)
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
            }
        }
        Picasso.get().load(repositoryModel.owner?.avatar).into(targetPhoto)
        binding.login.text = repositoryLogin
        val date = repositoryModel.createdAt?.substring(0, 10)
        val newDate = DateFormatter.formatCreatedDate(date ?: "")
        val creatingDate = "Дата создания: $newDate"
        binding.date.text = creatingDate
        binding.repoDescription.text = repositoryModel.description
        binding.countStar.text = formatCounterValue(repositoryModel.stars ?: 0)
        binding.forkCount.text = formatCounterValue(repositoryModel.forksCount ?: 0)
        repositoriesViewModel.getIsAuthorizedLiveData().observe(viewLifecycleOwner, {
            binding.save.visibility = if (it == 1) View.VISIBLE else View.GONE
        })
        repositoriesViewModel.getIsInsertingLiveData().observe(viewLifecycleOwner, {
            binding.save.setImageResource(if (it == 1) R.drawable.ic_star_saved else R.drawable.ic_not_saved)
        })
        binding.save.setOnClickListener {
            presenter.loadPostsFromDatabase(roomViewModel, repositoryModel, repositoriesViewModel)
        }
    }

    override fun handleIsInserting(value: Int) {
        repositoriesViewModel.handleIsInserting(value)
    }

    override fun showSnackBar() {
        requireActivity().runOnUiThread {
            SnackBarHelper.showSnackBar(
                binding.root,
                getString(R.string.no_internet_title),
                R.drawable.bg_rounded_error_snackbar,
                R.drawable.ic_exclamation
            )
        }
    }

    override fun initViewModel() {
        roomViewModel = ViewModelProvider(requireActivity()).get(RoomViewModel::class.java)
        repositoriesViewModel =
            ViewModelProvider(requireActivity())[RepositoriesViewModel::class.java]
    }

    override fun showErrorSnackBar() {
        requireActivity().runOnUiThread {
            SnackBarHelper.showSnackBar(
                binding.root,
                getString(R.string.db_error),
                R.drawable.bg_rounded_error_snackbar,
                R.drawable.ic_exclamation
            )
        }
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
}