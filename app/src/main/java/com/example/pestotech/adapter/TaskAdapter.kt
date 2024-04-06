package com.example.pestotech.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pestotech.R
import com.example.pestotech.callbacks.ItemClickListener
import com.example.pestotech.databinding.ItemTaskBinding
import com.example.pestotech.model.Task
import com.example.pestotech.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale
import javax.inject.Inject

/**
 * Author: Aquib khan (khanaquib384@gmail.com)
 * Created On:  06/04/2024
 * Description: Adapter to view task
 */
class MainAdapter(
    private val context: Context,
    private val callback: ItemClickListener<Task>,
) : ListAdapter<Task, MainAdapter.ViewHolder>(DifferenceCallback()), Filterable {

    private var list = mutableListOf<Task>()

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): MainAdapter.ViewHolder {
        return ViewHolder(
            ItemTaskBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MainAdapter.ViewHolder, position: Int) {
        val results = getItem(position)

        holder.apply {
            bind(results)
            itemView.tag = results
        }
    }

    fun setData(list: MutableList<Task>) {
        this.list = list
        submitList(list)
    }


    inner class ViewHolder(
        private val binding: ItemTaskBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Task) {

            binding.apply {
                    tvTitle.text = context.getString(R.string.tast_title, data.title)
                    tvDescription.text = context.getString(R.string.task_description, data.description)
                    tvStatus.text = context.getString(R.string.task_status, data.status)
                    val status = data.status
                    val image = if (status.equals(context.getString(R.string.todo), true)) {
                        ContextCompat.getDrawable(context, R.drawable.todo)
                    } else if (status.equals(context.getString(R.string.in_progress), true)) {
                        ContextCompat.getDrawable(context, R.drawable.progress)
                    } else {
                        ContextCompat.getDrawable(context, R.drawable.done)
                    }
                    Glide.with(context).load(image).into(imageView)

                    root.setOnClickListener {

                        callback.onRecyclerItemClicked(adapterPosition, it, data)

                    }

            }

        }
    }


    private class DifferenceCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(
            oldItem: Task, newItem: Task
        ): Boolean {
            return oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: Task, newItem: Task
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun getFilter(): Filter {
        return customFilter
    }

    private val customFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = mutableListOf<Task>()
            if (constraint.isNullOrEmpty() || constraint.equals(context.getString(R.string.all))) {
                filteredList.addAll(list)
            } else {
                for (item in list) {
                    if (item.status.lowercase(Locale.ROOT).startsWith(
                            constraint.toString().lowercase(Locale.ROOT)
                        )
                    ) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, filterResults: FilterResults?) {
            submitList(filterResults?.values as MutableList<Task>)
        }

    }
}