package com.example.webview_interface_test

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textInputLayout = findViewById<TextInputLayout>(R.id.textInputLayout)
        val textInputEditText = findViewById<TextInputEditText>(R.id.textInputEditText)
        val button = findViewById<MaterialButton>(R.id.button)
        val webView = findViewById<WebView>(R.id.webView)

        button.setOnClickListener {
            setupWebView(textInputEditText.text.toString())
            textInputLayout.isVisible = false
            button.isVisible = false
            webView.isVisible = true
        }
    }

    @SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
    private fun setupWebView(streamUrl: String) {
        val webView = findViewById<WebView>(R.id.webView)
        webView.apply {
            setLayerType(View.LAYER_TYPE_HARDWARE, null)

            with(settings) {
                javaScriptEnabled = true
                domStorageEnabled = true
                javaScriptCanOpenWindowsAutomatically = true
                loadWithOverviewMode = true
                mediaPlaybackRequiresUserGesture = false
            }

            addJavascriptInterface(
                object {
                    @JavascriptInterface
                    fun copyToClipboard(text: String?) {
                        showToast("copy", text)
                    }

                    @JavascriptInterface
                    fun share(url: String?) {
                        showToast("share", url)
                    }

                    @JavascriptInterface
                    fun addProductToCart(itemId: String) {
                        showToast("addProductToCart", itemId)
                    }

                    @JavascriptInterface
                    fun trackEvent(args: String?) {
                        showToast("trackEvent", args)
                    }
                },
                "NativeAndroid"
            )

            loadUrl(streamUrl)
        }
    }

    private fun showToast(vararg params: String?) {
        Toast.makeText(this, params.joinToString(", "), Toast.LENGTH_SHORT).show()
    }
}