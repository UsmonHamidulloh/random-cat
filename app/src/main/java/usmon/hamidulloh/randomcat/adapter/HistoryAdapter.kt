package usmon.hamidulloh.randomcat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import usmon.hamidulloh.randomcat.R
import usmon.hamidulloh.randomcat.databinding.ItemImageBinding
import usmon.hamidulloh.randomcat.model.History

class HistoryAdapter(val itemClickListener: ImageItemCallBack)
    : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    var images: List<History> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image = images[position]

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
    }

    override fun getItemCount(): Int = images.size

    class ImageItemCallBack(val itemClickListener: (item: History) -> Unit) {
        fun onItemClick(item: History) = itemClickListener(item)
    }

    class ViewHolder(val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root)
}