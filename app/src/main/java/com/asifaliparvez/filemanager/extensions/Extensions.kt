package com.asifaliparvez.filemanager.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.app.ActivityCompat
import com.asifaliparvez.filemanager.R
import java.io.File
import kotlin.math.roundToInt


fun File.sizeInKb():Float{
    return (length() / 1024).toFloat()
        }
fun File.sizeInMb():Int{
    return (sizeInKb() / 1024).roundToInt()
}
fun File.checkFileSize():String{
    return  if(sizeInKb() > 1000){
        "${sizeInMb()}MB"
    }else{
        "${sizeInKb()}KB"
    }
}

