package com.power.tesseract.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.power.tesseractapplistsdk.AppListBaseLayer
import com.power.tesseractapplistsdk.data.InstalledAppData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {

    private val mutablePackagesList = MutableLiveData<List<InstalledAppData>>()
    private val mutableSearchList = MutableLiveData<List<InstalledAppData>>()

    init {
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
     * Handle Search with delay 500 ms
     */
    fun handleRequest(searchValue: String, preList: List<InstalledAppData>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                delay(500)
                mutablePackagesList.postValue(AppListBaseLayer.getAppData())
            }
        }
    }


}