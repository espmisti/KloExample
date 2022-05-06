package com.klo.example.activities.splash

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.os.PersistableBundle
import android.telephony.TelephonyManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.klo.example.R
import com.klo.example.activities.webview.WebViewActivity
import com.klo.example.activities.white.MainActivity
import com.klo.example.components.appsflyer.AppsflyerUtils
import com.klo.example.components.settings.ScreenUtils
import com.klo.example.components.url.ParamUtils
import kotlinx.coroutines.*

class SplashActivity : AppCompatActivity() {
    private val model: SplashViewModel by lazy { ViewModelProvider(this)[SplashViewModel::class.java] }
    //
    private lateinit var sPrefs: SharedPreferences
    private lateinit var job: Job
    private lateinit var observer: Observer<Boolean>
    //
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val masterKey = MasterKey.Builder(this).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
        sPrefs = EncryptedSharedPreferences.create(this, "setting", masterKey, EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)
        ScreenUtils().setFull(window)
        if(isInternetConnection(applicationContext)){
            if(sPrefs.getString("last_url", null) != null){
                startActivity(Intent(this, WebViewActivity::class.java).putExtra("organic", false))
                finish()
            } else {
                model.initViewModel()
                observer = observer()
                model.finished.observe(this, observer)
            }
        } else {
            startActivity(Intent(this, MainActivity::class.java).putExtra("organic", true))
            finish()
        }

    }
    private fun observer() = Observer<Boolean> {
        if(it){
            job = CoroutineScope(Dispatchers.IO).launch {
                val url = createURL(
                    model.campaign.value!!,
                    model.campaign_id.value.toString(),
                    model.adset_id.value.toString()
                )
                with(sPrefs.edit()){
                    putString("last_url", url)
                    apply()
                }
                withContext(coroutineContext){
                    startActivity(Intent(applicationContext, WebViewActivity::class.java)
                        .putExtra("url", url)
                        .putExtra("organic", false))
                    finish()
                    Log.i("APP_CHECK", "DONBASS: ${model.campaign.value} + ${model.campaign_id.value} + ${model.adset_id.value} + $url")
                }
            }
        } else {
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }
    }

    private suspend fun createURL(sub: HashMap<Int, String>, campaign_id: String, adset_id: String) : String = withContext(Dispatchers.IO){
        val tM = applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return@withContext ParamUtils().replace_param(
            sub,
            AppsflyerUtils().getAdvertisingId(applicationContext),
            AppsflyerUtils().getAppsflyerId(applicationContext),
            campaign_id,
            adset_id,
            applicationContext.packageName,
            tM.networkCountryIso
        )
    }
    private fun isInternetConnection(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) return true
            else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) return true
            else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) return true
        }
        return false
    }
    override fun onStop() {
        super.onStop()
        if(model.finished.hasActiveObservers()) model.finished.removeObserver(observer)
    }
}