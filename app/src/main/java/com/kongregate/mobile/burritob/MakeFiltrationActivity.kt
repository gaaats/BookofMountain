package com.kongregate.mobile.burritob

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kongregate.mobile.burritob.AppClass.Companion.jsoupCheck
import com.kongregate.mobile.burritob.game.JustAGameActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MakeFiltrationActivity : AppCompatActivity() {
    lateinit var jsoup: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_filtration)
        jsoup = ""
        val asyncJs = AsyncJs(applicationContext)

        val job = GlobalScope.launch(Dispatchers.IO) {
            jsoup = asyncJs.coTask()
        }

        runBlocking {
            job.join()
            if (jsoup == jsoupCheck) {
                Intent(applicationContext, JustAGameActivity::class.java).also { startActivity(it) }
            } else {
                Intent(applicationContext, OpenVebVievActivity::class.java).also { startActivity(it) }
            }
            finish()
        }
    }
}