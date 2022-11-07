package com.kongregate.mobile.burritob.data

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.kongregate.mobile.burritob.AppClass
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import org.jsoup.Jsoup
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreRepo @Inject constructor(private val context: Application) {

    //TODO:change later
    private var jsoup: String = ""


    val Context.dataStoreMainSettings: DataStore<Preferences> by preferencesDataStore(name = "settings")

    companion object {

        const val PREFERENCE_NAME = "settings"

        var KEY_FOR_MAIN_ID_DATA = stringPreferencesKey("")
        var KEY_C1_DATA = stringPreferencesKey("c11")
        var KEY_D1_DATA = stringPreferencesKey("d11")
        var KEY_CH_DATA = stringPreferencesKey("check")

        val KEY_ACTIVITY_PREF = stringPreferencesKey("ActivityPREF")
        val KEY_ACTIVITY_EXEC = booleanPreferencesKey("activity_exec")
        val KEY_SP_WEBVIEW_PREFS = stringPreferencesKey("SP_WEBVIEW_PREFS")
        val KEY_SAVED_URL = stringPreferencesKey("SAVED_URL")

    }

    fun saveToDataStore(key: Preferences.Key<String>, value: String) {
        CoroutineScope(Dispatchers.IO).launch {
            context.dataStoreMainSettings.edit { preferences ->
                preferences[key] = value
            }
        }
    }

    fun saveBooleanToDataStore(key: Preferences.Key<Boolean>, value: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            context.dataStoreMainSettings.edit { preferences ->
                preferences[key] = value
            }
        }
    }


    fun readFromDataStore(key: Preferences.Key<String>): String? {
        var finishResult: String? = ""
        var result: Deferred<String?> = CoroutineScope(Dispatchers.IO).async {
            val preRes = context.dataStoreMainSettings.data.first()[key]
            preRes
        }
        CoroutineScope(Dispatchers.IO).launch {
            finishResult = result.await()
        }
        return finishResult
    }

    suspend fun readBooleanFromDataStore(key: Preferences.Key<Boolean>): Boolean {
        var result: Deferred<Boolean> = CoroutineScope(Dispatchers.IO).async {
            val preRes = context.dataStoreMainSettings.data.first()[key]
            preRes ?: false
        }
        val finishResult = result.await()
        return finishResult
    }


    suspend fun coTask(): String {

        //TODO: delete it later
//        val nameParameter: String? = Hawk.get(KEY_C1)
//        val appLinkParameter: String? = Hawk.get(KEY_D1)

        val nameParameter = readFromDataStore(DataStoreRepo.KEY_C1_DATA)
        val appLinkParameter = readFromDataStore(DataStoreRepo.KEY_D1_DATA)


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