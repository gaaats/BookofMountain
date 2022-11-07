package com.kongregate.mobile.burritob

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kongregate.mobile.burritob.AppClass.Companion.jsoupCheck
import com.kongregate.mobile.burritob.data.DataStoreRepo
import com.kongregate.mobile.burritob.game.JustAGameActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton


@AndroidEntryPoint
class MakeFiltrationActivity : AppCompatActivity() {

    @Inject
    lateinit var dataStoreRepo: DataStoreRepo

    lateinit var jsoup: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_filtration)
        jsoup = ""


//        val asyncJs = AsyncJs(applicationContext)

        val job = GlobalScope.launch(Dispatchers.IO) {
            jsoup = dataStoreRepo.coTask()
        }

        runBlocking {
            job.join()
            if (jsoup == jsoupCheck) {
                Intent(applicationContext, JustAGameActivity::class.java).also { startActivity(it) }
            } else {

                Log.d("lolo", "i am in MakeFiltrationActivity -- else")
                Intent(applicationContext, OpenVebVievActivity::class.java).also { startActivity(it) }
            }
            finish()
        }
    }
}