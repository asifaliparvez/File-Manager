package com.asifaliparvez.filemanager.helpers

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.core.app.ActivityCompat
import com.asifaliparvez.filemanager.R
import com.asifaliparvez.filemanager.models.FileModel
import java.io.File

class Utils {
    companion object{
        fun getFileDrawable(file: File, context: Context): Drawable {
            return when(file.extension.lowercase()){
                "pdf" ->{
                    ActivityCompat.getDrawable(context, R.drawable.ic_bxs_file_pdf)!!
                }  "jpg", "png", "webp", "jpeg", "svg", "psd" ,"gif"  ->{
                    ActivityCompat.getDrawable(context, R.drawable.ic_bxs_file_image)!!
                }   "mp4", "mkv" ->{
                    ActivityCompat.getDrawable(context, R.drawable.ic_bi_file_earmark_play)!!
                }
                "mp3", "aac", "wav", "aiff", "dsd", "pcm"->{
                    ActivityCompat.getDrawable(context, R.drawable.ic_bi_file_earmark_music)!!
                }

                "json" ->{
                    ActivityCompat.getDrawable(context, R.drawable.ic_bxs_file_json)!!
                }
                "html" ->{
                    ActivityCompat.getDrawable(context, R.drawable.ic_bxs_file_html)!!
                }
                "txt" ->{
                    ActivityCompat.getDrawable(context, R.drawable.ic_bxs_file_txt)!!
                }
                "doc" ->{
                    ActivityCompat.getDrawable(context, R.drawable.ic_bxs_file_doc)!!
                }

                else -> {
                    ActivityCompat.getDrawable(context, R.drawable.ic__file_blank_outline)!!
                }
            }
        }





    }

}