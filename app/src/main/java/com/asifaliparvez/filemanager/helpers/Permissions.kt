package com.asifaliparvez.filemanager.helpers

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import androidx.core.content.ContextCompat
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
        fun getExternalStorages(context: Context):ArrayList<File>{
            val arrayList = arrayListOf<File>()
            return if (isExternalStorageReadable()){
                val externalStorageVolumes:Array<out File> = ContextCompat.getExternalFilesDirs(context, null)
                val primaryVolume = externalStorageVolumes[0]
                externalStorageVolumes.forEach {
                    arrayList.add(it)
                }
                arrayList
            }else{
                arrayList
            }
        }


    }
}