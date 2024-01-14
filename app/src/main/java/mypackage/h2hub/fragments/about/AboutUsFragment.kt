package mypackage.h2hub.fragments.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.fragment.app.DialogFragment
import dagger.hilt.android.AndroidEntryPoint
import mypackage.h2hub.R
import mypackage.h2hub.databinding.FragmentAboutUsBinding


@AndroidEntryPoint
class AboutUsFragment : DialogFragment() {
    private lateinit var binding: FragmentAboutUsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAboutUsBinding.inflate(inflater, container, false)
        val view = binding.root
        val webView:WebView = view.findViewById(R.id.webview)

        val video =
            "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/CiFoHm7HD94\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
        webView.loadData(video, "text/html", "utf-8")
        webView.settings.javaScriptEnabled = true
        webView.webChromeClient = WebChromeClient()


        return view
    }

}