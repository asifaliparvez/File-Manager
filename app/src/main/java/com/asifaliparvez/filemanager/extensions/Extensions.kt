package com.asifaliparvez.filemanager.extensions

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

