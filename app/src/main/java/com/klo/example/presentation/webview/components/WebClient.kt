package com.klo.example.presentation.webview.components

import android.content.Context
import android.webkit.*
import com.klo.example.data.repository.SharedPrefDataRepository
import com.klo.example.domain.usecase.GetSharedPrefUseCase
import com.klo.example.domain.usecase.SaveSharedPrefUseCase

class WebClient(private val white: OrganicWV, private val context: Context) : WebViewClient() {

    // Сохранение ссылки
    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        if(GetSharedPrefUseCase(sharedPrefRepository = SharedPrefDataRepository(context = context)).execute().last_url == null) {
            SaveSharedPrefUseCase(sharedPrefRepository = SharedPrefDataRepository(context = context)).execute(url, 0, 1)
        }
    }
    // Статусы ошибок
    override fun onReceivedHttpError(view: WebView?, request: WebResourceRequest?, errorResponse: WebResourceResponse?) {
        super.onReceivedHttpError(view, request, errorResponse)
        when(errorResponse?.statusCode) {
            404 -> white.open()
        }
    }
}