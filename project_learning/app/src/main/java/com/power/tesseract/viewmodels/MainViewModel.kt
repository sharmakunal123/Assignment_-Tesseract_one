package com.power.tesseract.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.power.tesseractapplistsdk.AppListBaseLayer
import com.power.tesseractapplistsdk.data.InstalledAppData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MainViewModel : ViewModel() {

    private val mutablePackagesList = MutableLiveData<List<InstalledAppData>>()

    var mSearchJob: Job? = null

    init {
        requestAllPackages()
    }

    /**
     * Get List of Packages
     */
    fun requestAllPackages() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mutablePackagesList.postValue(AppListBaseLayer.getAppData())
            }
        }
    }

    /**
     * Get List of installed Applications
     */
    fun getListOfPackages(): LiveData<List<InstalledAppData>> {
        return mutablePackagesList
    }

    /**
     * Search Implementation
     */
    fun handleSearchFunctionality(appName: String) {
        mSearchJob?.cancel()
        if (appName.isEmpty()) {
            requestAllPackages()
            return
        }
        mSearchJob = viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mutablePackagesList.value?.let { listApp ->
                    val filteredNot = listApp.filter {
                        it.appName.toLowerCase(Locale.getDefault())
                            .startsWith(appName.toLowerCase(Locale.getDefault()))
                    }
                    if (filteredNot.isEmpty()) {
                        requestAllPackages()
                    } else {
                        mutablePackagesList.postValue(filteredNot)
                    }
                }
            }
        }
    }


}