package com.asifaliparvez.filemanager.helpers

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.util.Log
import androidx.core.content.ContextCompat
import com.asifaliparvez.filemanager.BuildConfig
import java.io.File

class Permissions {

    companion object{
        fun isAndroidSdkGreaterThan10():Boolean{
            return Build.VERSION.SDK_INT > 29
        }
        fun isPermissionGranted(context: Context):Boolean{
            if (isAndroidSdkGreaterThan10()){
                return  Environment.isExternalStorageManager()
            }
            return (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        }
        private fun isExternalStorageReadable():Boolean{
            return Environment.getExternalStorageState() in setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)
        }



    }
}