package com.kongregate.mobile.burritob

import android.content.Context
import com.kongregate.mobile.burritob.AppClass.Companion.C1
import com.kongregate.mobile.burritob.AppClass.Companion.D1
import com.orhanobut.hawk.Hawk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup


class AsyncJs(val context: Context) {
    private var jsoup: String = ""

    suspend fun coTask(): String {

        val nameParameter: String? = Hawk.get(C1)
        val appLinkParameter: String? = Hawk.get(D1)


        val taskName =
            "${AppClass.linkFilterPart1}${AppClass.linkFilterPart2}${AppClass.odone}$nameParameter"
        val taskLink =
            "${AppClass.linkFilterPart1}${AppClass.linkFilterPart2}${AppClass.odone}$appLinkParameter"

        withContext(Dispatchers.IO) {
            if (nameParameter != "null") {
                getDocSecretKey(taskName)
            } else {
                getDocSecretKey(taskLink)
            }
        }
        return jsoup
    }

    private fun getDocSecretKey(link: String) {
        val text = Jsoup.connect(link).get().text()
        jsoup = text
    }
}