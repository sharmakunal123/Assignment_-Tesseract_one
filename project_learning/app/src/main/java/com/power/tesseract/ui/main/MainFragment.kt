package com.power.tesseract.ui.main

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.google.gson.Gson
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
            startActivity(launchIntent)
        })

        binding.rvPlantList.adapter = adapter
        fetchDataObserver(adapter)
        return binding.root
    }

    private fun fetchDataObserver(adapter: PackagesListAdapter) {
        viewModel.getListOfPackages().observe(requireActivity(), Observer { listOfInstalledPacks ->
            // Filter and Adding icon of Installed Applications
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
            val gson = Gson()
            val packJsonList = gson.toJson(listOfPackages)
            activity?.let {
                Preferences.setListOfPackages(it, packJsonList)
            }

            adapter.submitList(listOfInstalledPacks)
        })
    }

    fun runBackgroundService() {
        val periodicWorkRequest = PeriodicWorkRequest.Builder(
            MyJobService::class.java, 15, TimeUnit.MINUTES
        ).build()
        WorkManager.getInstance().enqueue(periodicWorkRequest)
    }

}