package usmon.hamidulloh.randomcat.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import usmon.hamidulloh.randomcat.R
import usmon.hamidulloh.randomcat.databinding.ItemImageBinding
import usmon.hamidulloh.randomcat.model.History

class HistoryAdapter(
    val itemClickListener: ImageItemCallBack,
    val itemLongClickListener: ImageItemCallBack,
    val itemMoreClickListener: ImageItemCallBack
) : ListAdapter<History, HistoryAdapter.ViewHolder>(HistoryDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image = getItem(position)

        holder.binding.date.text = image.date
        holder.binding.size.text = "width = ${image.width}  -   height = ${image.height}"
        Glide.with(holder.binding.root)
            .load(image.url)
            .placeholder(R.drawable.img_loading)
            .error(R.drawable.img_error)
            .into(holder.binding.image)

        holder.binding.root.setOnClickListener {
            itemClickListener.onItemClick(image)
        }

        holder.binding.root.setOnLongClickListener {
            itemLongClickListener.onItemClick(image)
            return@setOnLongClickListener true
        }

        holder.binding.more.setOnClickListener {
            itemMoreClickListener.onItemClick(image)
        }
    }

    class ImageItemCallBack(val itemClickListener: (item: History) -> Unit) {
        fun onItemClick(item: History) = itemClickListener(item)
    }

    class ViewHolder(val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root)

    class HistoryDiffCallback : DiffUtil.ItemCallback<History>() {
        override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
            return oldItem == newItem
        }

    }
}