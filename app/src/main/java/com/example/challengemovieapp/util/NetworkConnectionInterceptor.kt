package com.example.challengemovieapp.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import javax.inject.Inject

class NetworkConnectionInterceptor@Inject constructor(
    context: Context
) : Interceptor {
    private val applicationContext = context.applicationContext
    override fun intercept(chain: Interceptor.Chain): Response {
        if(!isInternetConnection()){
            throw NoInternetException("Lütfen Internet bağlantınızı açınız")
        }else if (!isInternetAvailable()){
            throw NoInternetException("Lütfen internet bağlantınızı kontrol edin")
        }
        return chain.proceed(chain.request())
    }

    private fun isInternetConnection(): Boolean {
        var result = false
        val connetctivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as
                ConnectivityManager?
        connetctivityManager?.let {
            it.getNetworkCapabilities(connetctivityManager.activeNetwork)?.apply {
                result = when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    else -> false
                }
            }
        }
        return result
    }

    private fun isInternetAvailable(): Boolean {
        return try {
            val timeoutMs = 15000
            val sock = Socket()
            val sockaddr = InetSocketAddress("8.8.8.8", 53)

            sock.connect(sockaddr, timeoutMs)
            sock.close()

            true
        } catch (e: IOException) {
            false
        }
    }
}