package com.praktikum.bomoapp

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent


class ChromeTab {
    companion object {
        fun ShowFilesInBrowser(context: Context) {
            val builder = CustomTabsIntent.Builder()
            builder.setShareState(CustomTabsIntent.SHARE_STATE_OFF)
            val customBuilder = builder.build()
            customBuilder.intent.setPackage("com.android.chrome")
            customBuilder.launchUrl(context,Uri.parse("http://185.239.238.141:8080/"))

        }

    }
}

