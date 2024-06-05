package com.mdev.chatapp.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities


class CheckInternConnection {
    companion object{
        fun isInternetConnected(context: Context): Boolean {
            return CheckInternConnection().internetIsConnected() && CheckInternConnection().networkIsConnected(context)
        }
    }

    private fun internetIsConnected(): Boolean {
        try {
            val command = "ping -c 1 google.com"
            return (Runtime.getRuntime().exec(command).waitFor() == 0)
        } catch (e: Exception) {
            return false
        }
    }
    private fun networkIsConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val activeNetwork = cm.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    }
}
