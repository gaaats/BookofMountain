package com.kongregate.mobile.burritob

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.kongregate.mobile.burritob.data.DataStoreRepo
import com.kongregate.mobile.burritob.data.DataStoreRepo.Companion.KEY_FOR_MAIN_ID_DATA
import com.onesignal.OneSignal
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltAndroidApp
class AppClass : Application() {

    @Inject
    lateinit var dataStoreRepo: DataStoreRepo

    companion object {

        const val AF_DEV_KEY = "dXbcrnFMv3aNP3fW83VipY"
        const val jsoupCheck = "1v3v"
        const val ONESIGNAL_APP_ID = "bcea6d33-c8f9-4e9f-8709-31e864e07351"

        val linkFilterPart1 = "http://book"
        val linkFilterPart2 = "ofmountain.xyz/go.php?to=1&"
        val linkAppsCheckPart1 = "http://book"
        val linkAppsCheckPart2 = "ofmountain.xyz/apps.txt"

        val odone = "sub_id_1="

//        var KEY_FOR_MAIN_ID: String = ""
//        var KEY_C1: String? = "c11"
//        var KEY_D1: String? = "d11"
//        var KEY_CH: String? = "check"
//
//        var KEY_FOR_MAIN_ID_DATA = stringPreferencesKey("")
//        var KEY_C1_DATA = stringPreferencesKey("c11")
//        var KEY_D1_DATA = stringPreferencesKey("d11")
//        var KEY_CH_DATA = stringPreferencesKey("check")
//
//        val KEY_ACTIVITY_PREF = stringPreferencesKey("ActivityPREF")
//        val KEY_ACTIVITY_EXEC = stringPreferencesKey("activity_exec")


    }

    override fun onCreate() {

        super.onCreate()




        GlobalScope.launch(Dispatchers.IO) {
            applyDeviceId(context = applicationContext)
        }
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
        // OneSignal Initialization
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)

        //TODO: delete dovn here
//        Hawk.init(this).build()
    }

    private suspend fun applyDeviceId(context: Context) {
        val advertisingInfo = Adv(context)
        val idInfo = advertisingInfo.getAdvertisingId()


        //TODO: delete dovn here
//        Hawk.put(KEY_FOR_MAIN_ID, idInfo)

        dataStoreRepo.saveToDataStore(KEY_FOR_MAIN_ID_DATA, idInfo)
    }


}

class Adv(context: Context) {
    private val adInfo = AdvertisingIdClient(context.applicationContext)

    suspend fun getAdvertisingId(): String =
        withContext(Dispatchers.IO) {
            adInfo.start()
            val adIdInfo = adInfo.info
            adIdInfo.id
        }
}

