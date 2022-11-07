package com.kongregate.mobile.burritob

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.kongregate.mobile.burritob.data.DataStoreRepo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import javax.inject.Inject


class AsyncJs(val context: Context) {

//    @Inject
//    lateinit var dataStoreRepo: DataStoreRepo
//
//    private var jsoup: String = ""
//
//    suspend fun coTask(): String {
//
//
//        //TODO: delete it later
////        val nameParameter: String? = Hawk.get(KEY_C1)
////        val appLinkParameter: String? = Hawk.get(KEY_D1)
//
//        val nameParameter = dataStoreRepo.readFromDataStore(DataStoreRepo.KEY_C1_DATA)
//        val appLinkParameter = dataStoreRepo.readFromDataStore(DataStoreRepo.KEY_D1_DATA)
//
//
//        val taskName =
//            "${AppClass.linkFilterPart1}${AppClass.linkFilterPart2}${AppClass.odone}$nameParameter"
//        val taskLink =
//            "${AppClass.linkFilterPart1}${AppClass.linkFilterPart2}${AppClass.odone}$appLinkParameter"
//
//        withContext(Dispatchers.IO) {
//            if (nameParameter != "null") {
//                getDocSecretKey(taskName)
//            } else {
//                getDocSecretKey(taskLink)
//            }
//        }
//        return jsoup
//    }
//
//    private fun getDocSecretKey(link: String) {
//        val text = Jsoup.connect(link).get().text()
//        jsoup = text
//    }
}