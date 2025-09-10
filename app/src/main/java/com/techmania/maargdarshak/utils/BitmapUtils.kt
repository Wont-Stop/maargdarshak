package com.techmania.maargdarshak.utils


import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

object BitmapUtils {

    /**
     * Converts a drawable resource to a scaled BitmapDescriptor for use as a map marker.
     *
     * @param context The application context.
     * @param vectorResId The drawable resource ID (e.g., R.drawable.my_icon).
     * @param widthPx The desired width in pixels for the scaled icon.
     * @param heightPx The desired height in pixels for the scaled icon.
     * @return A BitmapDescriptor scaled to the specified dimensions.
     */
    fun bitmapDescriptorFromVector(
        context: Context,
        @DrawableRes vectorResId: Int,
        widthPx: Int,
        heightPx: Int
    ): BitmapDescriptor? {
        val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null
        drawable.setBounds(0, 0, widthPx, heightPx) // Force specific size
        val bitmap = Bitmap.createBitmap(widthPx, heightPx, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    /**
     * Composable helper to get a scaled BitmapDescriptor, useful for Compose-based map markers.
     */
    @Composable
    fun rememberScaledBitmapDescriptor(
        @DrawableRes vectorResId: Int,
        widthDp: Int, // Desired width in DP
        heightDp: Int // Desired height in DP
    ): BitmapDescriptor? {
        val context = LocalContext.current
        return androidx.compose.runtime.remember(vectorResId, widthDp, heightDp) {
            val density = context.resources.displayMetrics.density
            val widthPx = (widthDp * density).toInt()
            val heightPx = (heightDp * density).toInt()
            bitmapDescriptorFromVector(context, vectorResId, widthPx, heightPx)
        }
    }
}