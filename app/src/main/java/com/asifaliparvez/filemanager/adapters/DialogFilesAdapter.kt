package com.asifaliparvez.filemanager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.asifaliparvez.filemanager.R
import com.asifaliparvez.filemanager.databinding.ItemsBinding
import com.asifaliparvez.filemanager.extensions.checkFileSize
import com.asifaliparvez.filemanager.fragments.MyDialogFragment
import com.asifaliparvez.filemanager.helpers.Files
import com.asifaliparvez.filemanager.helpers.Utils
import com.asifaliparvez.filemanager.models.FileModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DialogFilesAdapter(var arrayList: ArrayList<FileModel>, val fragment: MyDialogFragment):RecyclerView.Adapter<DialogFilesAdapter.DialogViewHolder>() {
    var onClick:((path:String, file:FileModel)-> Unit)? = null
    inner class DialogViewHolder(val binding:ItemsBinding):RecyclerView.ViewHolder(binding.root){
        val fileNameTextView = binding.textViewFileName
        val imageView = binding.imageView
        val fileExtensionNameTextView = binding.fileExtensionNameTextView
        val lastModifiedTextView = binding.lastModifiedTextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DialogViewHolder {
        val view = ItemsBinding.inflate(LayoutInflater.from(parent.context),parent ,false)
        return DialogViewHolder(view)
    }

    override fun onBindViewHolder(holder: DialogViewHolder, position: Int) {
        val file = arrayList[position]
        val tempFile = File(file.absolutePath)
        val simpleDataFormat =  SimpleDateFormat.getDateInstance().format(Date(file.lastModified))
        holder.lastModifiedTextView.text = simpleDataFormat
        holder.fileNameTextView.text = file.name

        if (file.isDirectory){
            holder.imageView.setImageResource(R.drawable.ic_folder)
            holder.fileExtensionNameTextView.text = "<DIR>"
        }else{

            holder.imageView.setImageDrawable(Utils.getFileDrawable(tempFile, fragment.requireContext()))
            holder.fileExtensionNameTextView.text = tempFile.checkFileSize()
        }



        holder.itemView.setOnClickListener {
            if (file.isDirectory){

                if (file.canRead){
                    onClick?.invoke(file.absolutePath, file)
                    arrayList = Files.getFiles(fragment.requireContext(), file.absolutePath)
                    arrayList.removeAt(0)
                    onDirectoryClick(arrayList)
                    desFile = File(file.path)
                    return@setOnClickListener
                }else{
                    Toast.makeText(fragment.requireContext(),"Transforming file to this folder not allowed!", Toast.LENGTH_SHORT).show()
                }

            }
        }


    }
    fun onDirectoryClick(arrayList: ArrayList<FileModel>){
        this.arrayList = arrayList
        notifyDataSetChanged()


    }
    override fun getItemCount(): Int {
        return arrayList.size
    }

    companion object{
        var path = ""
        var desFile = File("")
    }

}