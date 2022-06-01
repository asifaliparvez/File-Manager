package com.asifaliparvez.filemanager.adapters

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.asifaliparvez.filemanager.R
import com.asifaliparvez.filemanager.databinding.ItemsBinding
import com.asifaliparvez.filemanager.extensions.checkFileSize
import com.asifaliparvez.filemanager.helpers.Files
import com.asifaliparvez.filemanager.models.FileModel
import com.asifaliparvez.filemanager.helpers.Utils
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class FilesAdapter(private var context: Context, var arrayList: ArrayList<FileModel>):RecyclerView.Adapter<FilesAdapter.MyViewHolder>() {
    var onItemClick:((path:String, position:Int) -> Unit)? = null

    inner class MyViewHolder(binding: ItemsBinding):RecyclerView.ViewHolder(binding.root){
        val fileNameTextView = binding.textViewFileName
        val imageView = binding.imageView
        val fileExtensionNameTextView = binding.fileExtensionNameTextView
        val lastModifiedTextView = binding.lastModifiedTextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =ItemsBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return MyViewHolder(view)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val file = arrayList[position]
        holder.fileNameTextView.text = file.name
        val simpleDataFormat =  SimpleDateFormat.getDateInstance().format(Date(file.lastModified))
        holder.lastModifiedTextView.text = simpleDataFormat


        if (file.isDirectory){
            if (position == 0){
                holder.fileExtensionNameTextView.text= ""
                holder.lastModifiedTextView.text= "Previous Folder"
                holder.imageView.setImageDrawable(ActivityCompat.getDrawable(context,
                    R.drawable.ic_back_folder
                ))
            }else{
                holder.imageView.setImageDrawable(ActivityCompat.getDrawable(context,
                    R.drawable.ic_folder
                ))
                holder.fileExtensionNameTextView.text = context.getString(R.string.folder)
            }

        }else{

            holder.fileExtensionNameTextView.text = File(file.absolutePath).checkFileSize()
            holder.imageView.setImageDrawable(Utils.getFileDrawable(File(file.absolutePath), context))

        }
        holder.itemView.setOnClickListener {
            if (position == 0){
                Toast.makeText(context, "postion on clicked", Toast.LENGTH_SHORT).show()
                onItemClick?.invoke("", 0)
                return@setOnClickListener
            }
            if (file.isDirectory){
                val path = file.absolutePath
                onItemClick?.invoke(path,position)
                directoryOnClick(Files.getFiles(context, path))

            }else{
                try {
                    val apkUri = FileProvider.getUriForFile(context, context.applicationContext.packageName + ".providers.MyFileProvider", File(file.absolutePath))
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        setDataAndType(apkUri, null)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                    }
                    val chooser = Intent.createChooser(intent, "Choose a App")

                    context.startActivity(chooser)
                }catch (e:ActivityNotFoundException){
                    Log.d("hello", e.printStackTrace().toString())
                }


            }
        }
        holder.itemView.setOnLongClickListener {
            true
        }
    }
    fun directoryOnClick(arrayList: ArrayList<FileModel>){
        this.arrayList = arrayList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}