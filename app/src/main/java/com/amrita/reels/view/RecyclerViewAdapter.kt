package com.amrita.reels.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amrita.reels.databinding.ItemVideoBinding
import com.amrita.reels.model.DataItem
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class RecyclerViewAdapter(
    private val recyclerViewItemClick: (String) -> Unit,
): RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>(){

    private var dataList: List<DataItem>? = null

    fun setData(dataList: List<DataItem>?) {
        this.dataList = dataList
    }

    inner class ViewHolder(val binding: ItemVideoBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            (dataList?.get(position))?.let{
                binding.tvVideoTitle.text = it.snippet.title
                Glide.with(binding.ivVideoThumbNail)
                    .load(it.snippet.thumbnails.default.url)
                    .apply(RequestOptions.centerCropTransform())
                    .into(binding.ivVideoThumbNail)
                holder.itemView.setOnClickListener { _ -> recyclerViewItemClick(it.id.videoId) }
            }
        }
    }

    override fun getItemCount(): Int {
        dataList?.let {
            return it.size
        } ?: run{
            return 0
        }
    }
}