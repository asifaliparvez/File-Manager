package com.asifaliparvez.filemanager.helpers

import android.content.Context
import android.os.Build
import com.asifaliparvez.filemanager.models.FileModel
import java.io.File

class Files {
    companion object{

        fun getFiles(context: Context, path:String?):ArrayList<FileModel>{
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
                    files.add(file)
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
            array.add(
                FileModel("Back",
                    666,
                    true, "",
                    "", "",
                    false,5555
                )
            )
            directoriesOnly.forEach {
                array.add(
                    FileModel(it.name,
                    it.lastModified(),
                    it.isDirectory, it.absolutePath,
                    it.extension, it.path,
                    it.isHidden,it.length())
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

    }
}