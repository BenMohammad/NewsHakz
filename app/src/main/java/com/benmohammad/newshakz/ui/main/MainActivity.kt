package com.benmohammad.newshakz.ui.main

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.atomicrobot.marsrover.ui.main.MainActivityBinding
import com.benmohammad.newshakz.R
import com.benmohammad.newshakz.ui.NavigationEvent

class MainActivity : BaseActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        appComponent.inject(this)
        super.onCreate(savedInstanceState)

        viewModel = getViewModel(MainViewModel::class)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.vm = viewModel
        binding.executePendingBindings()

        viewModel.navigationEvent.observe(
            this,
            object : NavigationEvent.NavigationObserver<MainViewModel.StoryNavigation> {
                override fun onNavigationEvent(event: MainViewModel.StoryNavigation) {
                    when(event) {
                        is MainViewModel.StoryNavigation.OpenWebPage -> openWebPage(event.url)
                        MainViewModel.StoryNavigation.OpenStory -> openStory()
                        MainViewModel.StoryNavigation.OpenComments -> openComments()
                    }
                }
            }
        )
    }

    private fun openWebPage(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)

    }

    private fun openStory() {
        ShowStoryFragment.newInstance().show(supportFragmentManager, SHOW_STORY_TAG)
    }


    private fun openComments() {
        ShowCommentsFragment.newInstance().show(supportFragmentManager, SHOW_COMMENTS_TAG)

    }

    companion object {
        private const val SHOW_COMMENTS_TAG = "showCommentsFragment"
        private const val SHOW_STORY_TAG = "showStoryFragment"

    }}
