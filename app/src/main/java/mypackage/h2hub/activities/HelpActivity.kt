package mypackage.h2hub.activities

import FlipbookAdapter
import android.os.Bundle
import android.view.View
import android.view.ViewStub
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import dagger.hilt.android.AndroidEntryPoint
import mypackage.h2hub.R


@AndroidEntryPoint
class HelpActivity : AppCompatActivity() {
    //    private lateinit var binding: ActivityHelpBinding
//    private lateinit var viewFlipper: ViewFlipper
    private lateinit var viewPager: ViewPager
    private lateinit var viewStubPage1: ViewStub
    private lateinit var viewStubPage2: ViewStub
    //private val frameCount = 2 // Number of frames in the flipbook

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_help)
        setupToolbar()

        viewPager = findViewById(R.id.viewPager)
        viewStubPage1 = findViewById(R.id.viewStubPage1)
        viewStubPage2 = findViewById(R.id.viewStubPage2)

        val layouts = listOf(
            R.layout.page1_content,
            R.layout.page2_content,
            // Add more pages as needed
        )

        val adapter = FlipbookAdapter(layouts)
        viewPager.adapter = adapter

        // Set up ViewStub visibility based on ViewPager page change
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        viewStubPage1.visibility = View.VISIBLE
                        viewStubPage2.visibility = View.GONE
                        // Add more cases for additional pages
                    }
                    1 -> {
                        viewStubPage1.visibility = View.GONE
                        viewStubPage2.visibility = View.VISIBLE
                        // Add more cases for additional pages
                    }
                    // Add more cases for additional pages
                    else -> {
                        // Handle other pages if needed
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        viewPager.setPageTransformer(true, FlipBookPageTransformer())
    }
    private fun setupToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        }

        toolbar.setNavigationOnClickListener { onBackPressed() }
    }
    inner class FlipBookPageTransformer : ViewPager.PageTransformer {
        override fun transformPage(view: View, position: Float) {
            //val normalizedPosition = Math.abs(Math.abs(position) - 1)
            //page.scaleY = normalizedPosition / 2 + 0.5f
            val rotation = -90 * position
            view.apply {
                rotationY = rotation
            }
        }
    }
}