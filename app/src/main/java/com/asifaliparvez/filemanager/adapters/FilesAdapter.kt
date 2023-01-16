package com.asifaliparvez.filemanager.adapters

import android.content.ActivityNotFoundException
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.asifaliparvez.filemanager.R
import com.asifaliparvez.filemanager.databinding.ItemsBinding
import com.asifaliparvez.filemanager.dialogs.RenameFileDialog
import com.asifaliparvez.filemanager.extensions.checkFileSize
import com.asifaliparvez.filemanager.fragments.FilesFragment
import com.asifaliparvez.filemanager.fragments.MyDialogFragment
import com.asifaliparvez.filemanager.helpers.Files
import com.asifaliparvez.filemanager.helpers.Utils
import com.asifaliparvez.filemanager.models.FileModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class FilesAdapter(private var fragment: FilesFragment, var arrayList: ArrayList<FileModel>):RecyclerView.Adapter<FilesAdapter.MyViewHolder>() {

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
                holder.imageView.setImageDrawable(ActivityCompat.getDrawable(fragment.requireContext(),
                    R.drawable.ic_back_folder
                ))
            }else{
                holder.imageView.setImageDrawable(ActivityCompat.getDrawable(fragment.requireContext(),
                    R.drawable.ic_folder
                ))
                holder.fileExtensionNameTextView.text = fragment.getString(R.string.folder)
            }

        }else{

            holder.fileExtensionNameTextView.text = File(file.absolutePath).checkFileSize()
            holder.imageView.setImageDrawable(Utils.getFileDrawable(File(file.absolutePath), fragment.requireContext()))

        }
        holder.itemView.setOnClickListener {
            if(!file.canRead){
                Toast.makeText(fragment.requireContext(), "Can't read this Folder/File due to new android restrictions.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (position == 0){
                onItemClick?.invoke(file.absolutePath, 0)
                return@setOnClickListener
            }
            if (file.isDirectory){
                onItemClick?.invoke(file.absolutePath, position)
                directoryOnClick(Files.getFiles(fragment.requireContext(), file.absolutePath))

            }else{
                try {
                    val apkUri = FileProvider.getUriForFile(fragment.requireContext(), fragment.requireContext().packageName + ".providers.MyFileProvider", File(file.absolutePath))
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        setDataAndType(apkUri, null)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                    }
                    val chooser = Intent.createChooser(intent, "Choose a App")

                    fragment.startActivity(chooser)
                }catch (e:ActivityNotFoundException){
                    Log.d("hello", e.printStackTrace().toString())
                }


            }
        }
        holder.itemView.setOnLongClickListener {
            srcFile = File(file.absolutePath)
            bottomSheet(file.absolutePath, position)
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
    private fun bottomSheet(path:String, position: Int){
        val file = File(path)
        val dialog = BottomSheetDialog(fragment.requireContext())
        val view = fragment.layoutInflater.inflate(R.layout.files_model_bottom_sheet, null)
        dialog.apply {
            setCancelable(true)
        }
        val fileNameTextView = view.findViewById<TextView>(R.id.fileName)
        fileNameTextView.text = file.name

        val moveLinerLayout = view.findViewById<LinearLayout>(R.id.llMove)
        val copyLinerLayout = view.findViewById<LinearLayout>(R.id.llCopy)
        val renameLinerLayout = view.findViewById<LinearLayout>(R.id.llRename)
        val deleteLinerLayout = view.findViewById<LinearLayout>(R.id.llDelete)
        moveLinerLayout.setOnClickListener {
            isCopy = false
            if (showDialogFragment()){
                removeItemFromList(position)


            }else{
                Toast.makeText(fragment.requireContext(), "Not able to move the file", Toast.LENGTH_SHORT).show()
            }

        }
        copyLinerLayout.setOnClickListener {
            isCopy = true
            showDialogFragment()
        }
        renameLinerLayout.setOnClickListener {
            if (file.canWrite()){
                val desPath = RenameFileDialog().renameFileDialog(fragment, file.absolutePath, file.parent!!)
                if (desPath.isNotEmpty()){
                    if (Files.renameFile(File(file.absolutePath), File(desPath))){
                        Toast.makeText(fragment.requireContext(), "File Renamed Successfully ", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(fragment.requireContext(), "Could Not Able Rename The File", Toast.LENGTH_SHORT).show()
                    }

                }else{
                    Toast.makeText(fragment.requireContext(), "", Toast.LENGTH_SHORT).show()
                }


            }

        }
        deleteLinerLayout.setOnClickListener {
            if (Files.deleteFile(path)){
                removeItemFromList(position)
                Toast.makeText(fragment.requireContext(), "File Deleted Successfully ", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }else{
                dialog.dismiss()
                Toast.makeText(fragment.requireContext(), "Error!", Toast.LENGTH_SHORT).show()
            }

        }
        dialog.setContentView(view)
        dialog.show()

    }
    private fun showDialogFragment():Boolean{
        val dialog = MyDialogFragment()
        dialog.show(fragment.parentFragmentManager, "Tittle")
        return MyDialogFragment.isSuccessfull

    }

    private fun removeItemFromList(position:Int){
        notifyItemRemoved(position)
        arrayList.removeAt(position)
        notifyItemRangeChanged(position, arrayList.size)
    }

    companion object{
        var srcFile:File? = null
        var isCopy = false

    }


}