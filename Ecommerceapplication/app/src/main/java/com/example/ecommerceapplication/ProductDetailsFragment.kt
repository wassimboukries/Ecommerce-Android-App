package com.example.ecommerceapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs


class ProductDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = ProductDetailsFragment()
    }

    private lateinit var viewModel: ProductDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.product_details_fragment, container, false)

        val args: ProductDetailsFragmentArgs by navArgs()
        val webView: WebView = view.findViewById(R.id.webView)
        webView.webViewClient = WebViewClient()
        webView.loadUrl(args.url)

        return view
    }

}