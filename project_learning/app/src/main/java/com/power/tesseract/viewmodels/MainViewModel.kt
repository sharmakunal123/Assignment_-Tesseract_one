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
        mSearchJob = viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mutablePackagesList.value?.let { listApp ->
                    val filteredNot = listApp.filter {
                        it.appName.contains(appName)
                    }
                    if (filteredNot.isEmpty()) {
                        mutablePackagesList.postValue(emptyList())
                        mutablePackagesList.postValue(mutablePackagesList.value)
                    } else {
                        mutablePackagesList.postValue(filteredNot)
                    }
                }
            }
        }
    }


}