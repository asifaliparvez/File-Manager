package com.asifaliparvez.filemanager.helpers

import android.content.Context
import android.os.Build
import android.os.storage.StorageManager
import android.util.Log
import androidx.fragment.app.Fragment
import com.asifaliparvez.filemanager.adapters.FilesAdapter
import com.asifaliparvez.filemanager.fragments.FilesFragment
import com.asifaliparvez.filemanager.models.FileModel
import java.io.*

class Files {
    companion object{
         fun getExternalStorages(context: Context): ArrayList<FileModel> {
            var array = arrayListOf<FileModel>()
            val storageManager = context.getSystemService(StorageManager::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                var gg =  storageManager.storageVolumes
                gg.forEach {
                    var file = it.directory!!
                    array.add( FileModel(file.name, lastModified = file.lastModified(),
                        isDirectory = file.isDirectory, absolutePath = file.absolutePath,
                        extension = file.extension, path = file.path,
                        isHidden = file.isHidden, length = file.length()))
                }


            } else {
                context.getExternalFilesDirs(null).forEach {file ->

                    var index = file.absolutePath.indexOf("Android", ignoreCase = false)
                    val newFile = File(file.absolutePath.subSequence(0, index).toString())
                    Log.d("storagevolume", index.toString())

                    Log.d("storagevolume", file.absolutePath.subSequence(0, index).toString())
                    println(index)
                    array.add( FileModel(newFile.name, lastModified = newFile.lastModified(),
                        isDirectory = newFile.isDirectory, absolutePath = newFile.absolutePath,
                        extension = newFile.extension, path = newFile.path,
                        isHidden = newFile.isHidden, length = newFile.length()))
                }


            }
            return array
        }
         fun getFiles(context: Context, path:String = getExternalStorages(context)[0].absolutePath, isaDialog:Boolean = false):ArrayList<FileModel>{
            val arraysFile: Array<out File>
            val directoriesOnly = arrayListOf<File>()
            val  files = arrayListOf<File>()

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){

                val file = File(path!!)
                arraysFile = file.listFiles() as Array<out File>
                arraysFile.forEach { file ->
                    if (!file.isHidden){
                        if (file.isDirectory){
                            directoriesOnly.add(file)
                        }else{
                            files.add(file)
                        }
                    }

                }
            }else{
                val directory = File(path!!)
                arraysFile = directory.listFiles() as Array<out File>
                arraysFile.forEach {file ->
                    if (file.isDirectory){
                        directoriesOnly.add(file)
                    }else{
                        files.add(file)
                    }
                }


            }
            directoriesOnly.sortedWith(compareBy {
                it.name
            })
            files.sortedWith(compareBy {
                it.length()
            })
            directoriesOnly.addAll(files)
            val array = arrayListOf<FileModel>()
             if (!isaDialog){
                 array.add(
                     FileModel("Back",
                         666,
                         true, "",
                         "", "",
                         false,5555
                     )
                 )
             }

            directoriesOnly.forEach {
                array.add(
                    FileModel(it.name,
                    it.lastModified(),
                    it.isDirectory, it.absolutePath,
                    it.extension, it.path,
                    it.isHidden,it.length(), it.canRead())
                )
            }
            return array


        }


        fun createFile(path:String, fileName:String, context: Context):Boolean{
            val file = File(path,fileName )
            if (file.createNewFile()){
                return true
            }
            return false
        }
        fun createFolder(path:String, folderName:String, context: Context):Boolean{
            val file = File(path,folderName )
            if (file.mkdirs()){
                return true
            }
            return false
        }
        fun copyFile(srcFile: File, destinationFile:File):Boolean{
            val file = File("${destinationFile.absoluteFile}/${srcFile.name}")
            srcFile.copyTo(file, true)
            return file.exists()
        }
        fun moveFile(srcFile:File,  desFile:File, fragment: FilesFragment):Boolean{
            val file =File("${desFile.absoluteFile}/${srcFile.name}")
            srcFile.copyTo(file, true)
            if (file.exists()){
                srcFile.delete()
                val files = getFiles(fragment.requireContext(), desFile.absolutePath)
                val adapter =  FilesAdapter(fragment, files, )
                adapter.directoryOnClick(files)
                return true
            }else{
                return false
            }
        }

         fun deleteFile(path: String):Boolean{
            val file = File(path)
            return  file.delete()
        }

         fun renameFile(srcFile:File, desFile:File):Boolean{
             return srcFile.renameTo(desFile)
        }


    }
}