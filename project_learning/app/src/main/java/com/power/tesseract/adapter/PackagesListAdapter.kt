package com.power.tesseract.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.power.tesseract.R
import com.power.tesseract.databinding.ListItemsAppBinding
import com.power.tesseract.viewmodels.AdapterListData
import com.power.tesseractapplistsdk.data.InstalledAppData


class PackagesListAdapter(var handleClickEvent: (String) -> Unit) :
    ListAdapter<InstalledAppData, PackagesListAdapter.MyViewHolder>(DifferenceCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val viewHolder = DataBindingUtil.inflate<ListItemsAppBinding>(
            LayoutInflater.from(parent.context), R.layout.list_items_app,
            parent, false
        )
        return MyViewHolder(viewHolder, handleClickEvent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindWith(getItem(position))
    }

    class MyViewHolder(
        val itemBinding: ListItemsAppBinding,
        handleCardClick: (String) -> Unit
    ) :
        RecyclerView.ViewHolder(itemBinding.root) {

        init {
            itemBinding.setClickListener {
                itemBinding.viewModel?.packageName?.let {
                    handleCardClick(it)
                }
            }
        }

        fun bindWith(installedItem: InstalledAppData) {
            with(itemBinding) {
                viewModel = AdapterListData(installedItem)
                executePendingBindings()
            }
        }
    }
}

private class DifferenceCallBack : DiffUtil.ItemCallback<InstalledAppData>() {

    override fun areItemsTheSame(oldItem: InstalledAppData, newItem: InstalledAppData): Boolean {
        return oldItem.packageName == newItem.packageName
    }

    override fun areContentsTheSame(oldItem: InstalledAppData, newItem: InstalledAppData): Boolean {
        return oldItem == newItem
    }
}
