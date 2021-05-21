package com.power.tesseract.ui.main

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.power.tesseract.adapter.PackagesListAdapter
import com.power.tesseract.databinding.MainFragmentBinding
import com.power.tesseract.viewmodels.MainViewModel


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
            listOfInstalledPacks.forEach {
                val packageName = it.packageName
                if (packageName != null) {
                    val icon: Drawable =
                        requireContext().packageManager.getApplicationIcon(packageName)
                    it.icon = icon
                }
            }

            adapter.submitList(listOfInstalledPacks)
        })
    }


//    fun runBackgroundService() {
//        val serviceComponent = ComponentName(requireActivity(), MyJobService::class.java)
//        val builder = JobInfo.Builder(0, serviceComponent)
//        builder.setMinimumLatency((30 * 1000).toLong()) // Wait at least 30s
//
//        val jobScheduler = requireActivity().getSystemService(context.JOb) as JobScheduler
//        jobScheduler.schedule(builder.build())
//
//    }

}