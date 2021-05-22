package com.power.tesseract.ui.main

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.work.Constraints
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.gson.Gson
import com.power.tesseract.R
import com.power.tesseract.adapter.PackagesListAdapter
import com.power.tesseract.databinding.MainFragmentBinding
import com.power.tesseract.services.MyJobService
import com.power.tesseract.utils.Preferences
import com.power.tesseract.viewmodels.MainViewModel
import java.util.concurrent.TimeUnit


class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val binding = MainFragmentBinding.inflate(inflater, container, false)
        val adapter = PackagesListAdapter(handleClickEvent = {
            val launchIntent = requireActivity().packageManager.getLaunchIntentForPackage(it)
            if (launchIntent != null) {
                startActivity(launchIntent)
            } else {
                requireActivity().showShortMessage(requireActivity().getString(R.string.unabe_to_open_app))
            }
        })

        binding.rvPlantList.adapter = adapter
        fetchDataObserver(adapter)
        handleNavigation(binding)
        handleSearchFun(binding)
        return binding.root
    }

    private fun handleSearchFun(binding: MainFragmentBinding) {
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(str: String?): Boolean {
                str?.let {
                    viewModel.handleSearchFunctionality(it)
                }
                return true
            }
        })

        binding.search.setOnCloseListener {
            viewModel.requestAllPackages()
            false
        }
    }

    /**
     * Navigate to AIDL Screen.
     */
    private fun handleNavigation(binding: MainFragmentBinding) {
        binding.btnGetAidlData.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, AidlBindingFragment.newInstance())
                .addToBackStack(MainFragment.newInstance().tag)
                .commit()
        }
    }

    /**
     * Fetch Installed packages and update to Recycler View.
     */
    private fun fetchDataObserver(adapter: PackagesListAdapter) {
        viewModel.getListOfPackages().observe(requireActivity(), Observer { listOfInstalledPacks ->
            val listOfPackages = arrayListOf<String>()
            listOfInstalledPacks.forEach {
                val packageName = it.packageName
                if (packageName != null) {
                    val icon: Drawable =
                        requireContext().packageManager.getApplicationIcon(packageName)
                    it.icon = icon
                    listOfPackages.add(packageName)
                }

            }
            activity?.let {
                runBackgroundService()
                val gson = Gson()
                val packJsonList = gson.toJson(listOfPackages)
                Preferences.setListOfPackages(it, packJsonList)
            }
//            adapter.
            adapter.submitList(listOfInstalledPacks)
        })
    }

    /**
     * Run Work Manager Periodic Request after every 15 seconds.
     */
    private fun runBackgroundService() {
        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .build()
        val work = PeriodicWorkRequestBuilder<MyJobService>(
            15,
            TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .build()
        val workManager = WorkManager.getInstance(requireActivity())
        workManager.enqueue(work)

    }

    /**
     * Show Toast Message
     */
    private fun Context.showShortMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}