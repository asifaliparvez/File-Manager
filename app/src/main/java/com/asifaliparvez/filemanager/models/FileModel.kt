package com.asifaliparvez.filemanager.models

data class FileModel(
    val name:String = "",
    val lastModified: Long,
    val isDirectory: Boolean,
    var absolutePath:String = "",
    val extension:String ="",
    val path:String = "",
    val isHidden:Boolean = false,
    val length: Long,
    val canRead:Boolean = true,
    val canWrite:Boolean = true
        )