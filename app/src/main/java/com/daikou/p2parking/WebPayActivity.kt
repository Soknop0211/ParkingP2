package com.daikou.p2parking

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.webkit.*
import android.widget.ImageView
import android.widget.RelativeLayout
import com.daikou.p2parking.base.BaseActivity
import com.daikou.p2parking.base.Config
import com.daikou.p2parking.helper.AppLOGG
import com.daikou.p2parking.model.Constants
import com.daikou.p2parking.utility.RedirectClass
import java.io.UnsupportedEncodingException
import java.net.URLDecoder

class WebPayActivity : BaseActivity() {

    private var getLinkUrl: String? = null
    private var progressBar: RelativeLayout? = null
    private var webView: WebView? = null
    private var isOpenDeepLink = false
    private var isCanBack = true

    private var mIsAlreadySuccessBack : Boolean = false
    private var hasKessChatInstall: Boolean = false
    private var hasAcledaInstall: Boolean = false
    private var hasAbaInstall: Boolean = false
    private var hasSpnInstall: Boolean = false

    companion object {
        private const val JAVASCRIPT_OBJ = "javascript_obj"
        private val USER_AGENT_STRING = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/115.0.0.0 Safari/537.36"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_pay)

        initView()

        initData()

        initAction()
    }

    private fun initView() {
        webView = findViewById(R.id.webViewUrl)
        progressBar = findViewById(R.id.progressBar)
        val backBtn = findViewById<ImageView>(R.id.backWeb)

        backBtn.visibility = View.VISIBLE
        backBtn.setOnClickListener {
            if (!isCanBack) return@setOnClickListener

            if (webView?.canGoBack() == true) {
                webView?.goBack()
            } else {
                finish()
            }
        }

        hasKessChatInstall =
                /* isAppInstalled(Config.TypeDeepLink.kessChatDeepLink, packageManager) ==*/ true
        hasAbaInstall = isAppInstalled(Constants.Config.TypeDeepLink.abaDeepLink, packageManager) == true

        hasAcledaInstall =
            isAppInstalled(Constants.Config.TypeDeepLink.acledaDeepLink, packageManager) == true
        hasSpnInstall =
            isAppInstalled(Constants.Config.TypeDeepLink.sathapanaDeepLink, packageManager) == true

    }

    private fun backFromPaymentWebView() {
        val intent = Intent()
        intent.putExtra("is_back_payment", true)
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun initData() {
        if (intent != null && intent.hasExtra("linkUrl")) {
            getLinkUrl = intent.getStringExtra("linkUrl")
        }

    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initAction() {
        webView?.settings?.javaScriptEnabled = true
        webView?.settings?.domStorageEnabled = true
        webView?.settings?.userAgentString = USER_AGENT_STRING
        webView?.addJavascriptInterface(JavaScriptInterface(),
            JAVASCRIPT_OBJ
        )
        webView?.webViewClient = MyWebViewClient()
        webView?.webChromeClient =
            object : WebChromeClient() {
            override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                webView?.webChromeClient = object : WebChromeClient() {
                    override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                        AppLOGG.d("webViewUrl", "Console  : " + consoleMessage.message())
                        return true
                    }
                }
                return super.onConsoleMessage(consoleMessage)
            }
        }
        webView?.setNetworkAvailable(true)
//        if (intent.getStringExtra("toolBarTitle") == "card") {
//            val data = Base64.encodeToString(getLinkUrl!!.toByteArray(), Base64.NO_PADDING)
//            webView!!.loadData(data, "text/html", "base64")
//        } else {
//            webView!!.loadUrl(getLinkUrl!!)
//        }
        webView?.loadUrl(getLinkUrl ?: "")
    }

    inner class MyWebViewClient : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            AppLOGG.d("webViewUrl", "url start :       $url")
            isCanBack = false

            progressBar!!.visibility = View.VISIBLE
            view?.visibility = View.GONE

            if (!isOpenDeepLink) {
                super.onPageStarted(view, url, favicon)
            } else {
                view?.visibility = View.GONE
                isOpenDeepLink = false
                webView!!.goBack()
            }

            if (view?.visibility == View.GONE) {
                progressBar?.visibility = View.VISIBLE
            } else {
                progressBar?.visibility = View.GONE
            }

        }

        override fun onSafeBrowsingHit(view: WebView, request: WebResourceRequest,
                                       threatType: Int,
                                       callback: SafeBrowsingResponse) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                callback.backToSafety(false)
            }
            isCanBack = true
        }

//        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
//            super.shouldOverrideUrlLoading(view, request)
//            progressBar?.visibility = View.VISIBLE
//            isCanBack = true
//
//            AppLOGG.d("webViewUrl","url shouldOverrideUrlLoading :  " + request.url.toString() + "          " + request.url.authority)
//
//            val authority: String? = if (!TextUtils.isEmpty(request.url.toString()) && !TextUtils.isEmpty(request.url.authority)) {
//                request.url.authority
//            } else {
//                ""
//            }
//
//            val scheme =
//                if (!TextUtils.isEmpty(request.url.toString()) && !TextUtils.isEmpty(request.url.scheme)) request.url.scheme else ""
//
//            val kessChatScheme = "kesspay.io"
//            val url = request.url.toString()
//            if (authority == kessChatScheme) {
//                view.visibility = View.GONE
//                if (!url.contains(Constants.Config.BASE_URL + "/")) {
//                    if (url.contains(kessChatScheme) || !url.contains("https://") || url.contains(".apk")) {
//                        view.visibility = View.GONE
//                        try {
//                            isOpenDeepLink = true
//                            val intent = Intent(Intent.ACTION_VIEW)
//                            intent.data = request.url
//                            startActivity(intent)
//                        } catch (e: Exception) {
//                            isOpenDeepLink = true
//                            progressBar!!.visibility = View.GONE
//                            if (authority == Constants.Config.TypeScheme.kessChatScheme) {
//                                RedirectClass.gotoPlayStore(self(), Constants.Config.TypeDeepLink.kessChatDeepLink)
//                                return true
//                            }
//                            else if (scheme == Constants.Config.TypeScheme.abaScheme) {
//                                RedirectClass.gotoPlayStore(self(), Constants.Config.TypeDeepLink.abaDeepLink)
//                            }
//                            else if (scheme == Constants.Config.TypeScheme.acledaScheme) {
//                                RedirectClass.gotoPlayStore(self(), Constants.Config.TypeDeepLink.acledaDeepLink)
//                            }
//                            else if (scheme == Constants.Config.TypeScheme.spnScheme) {
//                                RedirectClass.gotoPlayStore(self(), Constants.Config.TypeDeepLink.sathapanaDeepLink)
//                            }
//                            else {
//                                val mUrl: String = request.url.toString()
//                                view.visibility = View.VISIBLE
//                                return !(mUrl.startsWith("http:") || mUrl.startsWith("https://"))
//                            }
//                        }
//                    }
//                }
//            } else {
//                if (!url.contains(Constants.Config.BASE_URL + "/")) {
//                    if (url.contains(Constants.WebPay.kess_url) || !url.contains("https://") || url.contains(
//                            ".apk"
//                        )
//                    ) {
//                        view.visibility = View.GONE
//                        try {
//                            isOpenDeepLink = true
////                            Utils.logDebug("webViewUrl", "uri :       " + Uri.parse(url))
//                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//                            startActivity(intent)
//                        } catch (e: Exception) {
//                            isOpenDeepLink = true
//                            progressBar!!.visibility = View.GONE
//                            if (authority == Constants.Config.TypeScheme.kessChatScheme) {
//                                RedirectClass.gotoPlayStore(self(), Constants.Config.TypeDeepLink.kessChatDeepLink)
//                                return true
//                            }
//                            else if (scheme == Constants.Config.TypeScheme.abaScheme) {
//                                RedirectClass.gotoPlayStore(self(), Constants.Config.TypeDeepLink.abaDeepLink)
//                            }
//                            else if (scheme == Constants.Config.TypeScheme.acledaScheme) {
//                                RedirectClass.gotoPlayStore(self(), Constants.Config.TypeDeepLink.acledaDeepLink)
//                            }
//                            else if (scheme == Constants.Config.TypeScheme.spnScheme) {
//                                RedirectClass.gotoPlayStore(self(), Constants.Config.TypeDeepLink.sathapanaDeepLink)
//                            }
//                            else {
//                                val mUrl: String = request.url.toString()
//                                view.visibility = View.VISIBLE
//                                return !(mUrl.startsWith("http:") || mUrl.startsWith("https://"))
//                            }
//                        }
//                    }
//                }
//            }
//
//            if (view.visibility == View.GONE) {
//                progressBar!!.visibility = View.VISIBLE
//            } else {
//                progressBar!!.visibility = View.GONE
//            }
//            return false
//        }

        override fun shouldOverrideUrlLoading(
            view: WebView,
            request: WebResourceRequest,
        ): Boolean {
            val authority =
                if (!TextUtils.isEmpty(request.url.toString()) && !TextUtils.isEmpty(request.url.authority)) request.url.authority else ""
            val scheme =
                if (!TextUtils.isEmpty(request.url.toString()) && !TextUtils.isEmpty(request.url.scheme)) request.url.scheme else ""
            if (authority == Constants.Config.TypeScheme.kessChatScheme) {
                if (hasKessChatInstall) {
                    RedirectClass.openDeepLink(self(), request.url)
                } else {
                    RedirectClass.gotoPlayStore(self(), Constants.Config.TypeDeepLink.kessChatDeepLink)
                }
                return true
            } else if (scheme == Constants.Config.TypeScheme.abaScheme) {
                if (hasAbaInstall) {
                    RedirectClass.openDeepLink(self(), request.url)
                } else {
                    try {
                        isOpenDeepLink = true
                        RedirectClass.openDeepLink(self(), request.url)
                    } catch (e: Exception) {
                        isOpenDeepLink = true
                        RedirectClass.gotoPlayStore(self(), Constants.Config.TypeDeepLink.abaDeepLink)
                    }
                }
            } else if (scheme == Constants.Config.TypeScheme.acledaScheme) {
                if (hasAcledaInstall) {
                    RedirectClass.openDeepLink(self(), request.url)
                } else {
                    try {
                        isOpenDeepLink = true
                        RedirectClass.openDeepLink(self(), request.url)
                    } catch (e: Exception) {
                        isOpenDeepLink = true
                        RedirectClass.gotoPlayStore(self(), Constants.Config.TypeDeepLink.acledaDeepLink)
                    }
                }
            } else if (scheme == Constants.Config.TypeScheme.spnScheme) {
                if (hasSpnInstall) {
                    RedirectClass.openDeepLink(self(), request.url)
                } else {
                    try {
                        isOpenDeepLink = true
                        RedirectClass.openDeepLink(self(), request.url)
                    } catch (e: Exception) {
                        isOpenDeepLink = true
                        RedirectClass.gotoPlayStore(self(), Constants.Config.TypeDeepLink.sathapanaDeepLink)
                    }
                }
            }
            val mUrl: String = request.url.toString()
            val isUrl = !(mUrl.startsWith("http:") || mUrl.startsWith("https://"))
            return !(mUrl.startsWith("http:") || mUrl.startsWith("https://"))
        }

        override fun onPageFinished(view: WebView, url: String) {
            AppLOGG.d("webViewUrl", "url finish :       $url")
            super.onPageFinished(view, url)
            isCanBack = true
            progressBar?.visibility = View.GONE
            view.visibility = View.VISIBLE

            // injectJavaScriptFunction()

            if (!TextUtils.isEmpty(url))     callBackResponseFinish(url, view)

        }
    }

    private fun callBackResponseFinish(url : String, view: WebView) {
        val urlFinish: String = try {
            URLDecoder.decode(url, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
            url
        }

        if (!urlFinish.contains(Config.BASE_URL + "/")) {
            if (urlFinish.contains(Constants.TypeDeepLink.kess_url) || !urlFinish.contains("https://") || urlFinish.contains(".apk")) {
                view.visibility = View.GONE
            } else if (urlFinish.contains("success=1")) {
                callBackFinishScreen("success=1")
            } else if (urlFinish.contains("success=0")) {
                callBackFinishScreen("success=0")
            } else {
                view.visibility = View.VISIBLE
            }
        } else if (urlFinish.contains("success=1")) {
            callBackFinishScreen("success=1")
        } else if (urlFinish.contains("success=0")) {
            callBackFinishScreen("success=0")
        } else {
            view.visibility = View.VISIBLE
        }

        if (view.visibility == View.GONE) {
            progressBar!!.visibility = View.VISIBLE
        } else {
            progressBar!!.visibility = View.GONE
        }

    }

    private fun injectJavaScriptFunction() {
        webView!!.loadUrl("javascript: window.WebPayJSBride.invoke = function(method, data) { $JAVASCRIPT_OBJ.onPaymentSuccess(method, data) }")
    }

    inner class JavaScriptInterface {
        @JavascriptInterface
        fun onPaymentSuccess(method: String?, data: String?) {
            if (!TextUtils.isEmpty(method) && method.equals("onPaymentSuccess")) {
                callBackFinishScreen("success=1")
            }
        }
    }

    private fun callBackFinishScreen(status : String) {
        if (!mIsAlreadySuccessBack){
            AppLOGG.d("webViewUrl", "WEBVIEW $status")
            mIsAlreadySuccessBack = true
            val intent = Intent()
            intent.putExtra("status", status)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    private fun isAppInstalled(packageName: String, packageManager: PackageManager): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    private fun startOpenDeepLink(url: Uri, packageName: String) {
        val isAppInstalled = isAppAvailable(this, packageName)
        val intent = Intent(Intent.ACTION_VIEW)
        if (isAppInstalled) {
            intent.data = url
        } else {
            intent.data =
                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
        }
        startActivity(intent)
    }

    private fun isAppAvailable(context: Context, appName: String): Boolean {
        val pm = context.packageManager
        return try {
            pm.getPackageInfo(appName, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

     override fun onResume() {
        super.onResume()
        webView!!.goBack()
        if (webView != null) {
            webView!!.onResume()
        }
    }

    override fun onBackPressed() {
        if (!isCanBack) return

        if (webView!!.canGoBack()) {
            webView!!.goBack()
        } else {
            finish()
        }
    }
}