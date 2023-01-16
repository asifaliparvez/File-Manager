package com.asifaliparvez.filemanager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.asifaliparvez.filemanager.R
import com.asifaliparvez.filemanager.databinding.ItemsBinding
import com.asifaliparvez.filemanager.extensions.checkFileSize
import com.asifaliparvez.filemanager.fragments.MyDialogFragment
import com.asifaliparvez.filemanager.helpers.Files
import com.asifaliparvez.filemanager.helpers.Utils
import com.asifaliparvez.filemanager.models.FileModel
import java.io.File

class DialogFilesAdapter(var arrayList: ArrayList<FileModel>, val fragment: MyDialogFragment):RecyclerView.Adapter<DialogFilesAdapter.DialogViewHolder>() {
    var onClick:((path:String)-> Unit)? = null
    inner class DialogViewHolder(val binding:ItemsBinding):RecyclerView.ViewHolder(binding.root){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DialogViewHolder {
       val view = ItemsBinding.inflate(LayoutInflater.from(parent.context),parent ,false)
        return DialogViewHolder(view)
    }

    override fun onBindViewHolder(holder: DialogViewHolder, position: Int) {
        val file = arrayList[position]
        onClick?.invoke(file.path)
        val tempFile = File(file.absolutePath)
        if (file.isDirectory){
            holder.binding.imageView.setImageResource(R.drawable.ic_folder)
            holder.binding.fileExtensionNameTextView.text = "<DIR>"
        }else{

            holder.binding.imageView.setImageDrawable(Utils.getFileDrawable(tempFile, fragment.requireContext()))
            holder.binding.fileExtensionNameTextView.text = tempFile.checkFileSize()
        }

        holder.binding.textViewFileName.text = file.name
        holder.binding.lastModifiedTextView.text = file.lastModified.toString()

        holder.itemView.setOnClickListener {
            if (file.isDirectory){
                onClick?.invoke(file.path)
                onDirectoryClick(file.path)
                desFile = File(file.path)


            }
        }


    }
     fun onDirectoryClick(path: String){
        arrayList = Files.getFiles(fragment.requireContext(), path, true)
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