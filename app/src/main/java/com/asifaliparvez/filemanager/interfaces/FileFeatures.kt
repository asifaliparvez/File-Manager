package com.asifaliparvez.filemanager.interfaces

interface FileFeatures {
    fun deleteFile(path:String):Boolean
    fun moveFile(path:String):Boolean
    fun copyFile(path:String):Boolean
    fun renameFile(path: String, ):Boolean
}