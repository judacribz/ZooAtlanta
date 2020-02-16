package ca.judacribz.zooatlanta.global.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

/**
 * Utility function to check if user is connected to internet.
 *
 * @param context - used to retrieve Connectivity Manager.
 */
@Suppress("DEPRECATION")
fun isNetworkActive(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    // API 23 or higher
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = connectivityManager.activeNetwork ?: return false
        with(connectivityManager.getNetworkCapabilities(network) ?: return false) {
            return hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                .or(hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
        }
    } else {
        // Deprecated after API 23
        val networkInfo = connectivityManager.activeNetworkInfo ?: return false
        return networkInfo.isConnected
    }
}