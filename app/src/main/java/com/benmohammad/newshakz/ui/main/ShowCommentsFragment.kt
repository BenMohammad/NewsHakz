package com.benmohammad.newshakz.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.atomicrobot.marsrover.ui.main.ShowCommentsFragmentBinding
import com.benmohammad.newshakz.R
import com.benmohammad.newshakz.StoryItemBinding
import com.benmohammad.newshakz.data.model.Item
import com.benmohammad.newshakz.util.recyclerview.ArrayAdapter
import retrofit2.http.POST
import java.lang.reflect.Array.get

class ShowCommentsFragment: BaseDialog() {

    companion object {
        fun newInstance() = ShowCommentsFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ShowCommentsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_show_comments, container, false)
        binding.vm = viewModel
        binding.comments.layoutManager = LinearLayoutManager(activity)
        binding.comments.adapter = CommentAdapter()
        viewModel.fetchComments()
        return binding.root
    }


    private class CommentAdapter : ArrayAdapter<Item, ItemViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding : StoryItemBinding = DataBindingUtil.inflate(
                layoutInflater,
                R.layout.item_row_story_summary,
                parent,
                false
            )
            return ItemViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            val story = getItemAtPosition(position)
            holder.bind(story)
        }
    }



    private class ItemViewHolder(private val binding: StoryItemBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Item) {
        binding.item = item
        binding.executePendingBindings()
    }
}
}