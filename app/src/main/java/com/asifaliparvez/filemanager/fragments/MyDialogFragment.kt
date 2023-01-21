package com.asifaliparvez.filemanager.fragments

import android.app.AlertDialog.Builder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.asifaliparvez.filemanager.R
import com.asifaliparvez.filemanager.adapters.DialogFilesAdapter
import com.asifaliparvez.filemanager.adapters.FilesAdapter
import com.asifaliparvez.filemanager.databinding.FragmentDialogBinding
import com.asifaliparvez.filemanager.dialogs.ChangeDirectoryDialog
import com.asifaliparvez.filemanager.helpers.Files
import com.asifaliparvez.filemanager.models.FileModel
import java.io.File


class MyDialogFragment : DialogFragment() {
    private var _binding:FragmentDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var storageVolumes:ArrayList<FileModel>
    private  lateinit var adapter: DialogFilesAdapter
    private var destinationPath = ""
    private var  filesAdapter:FilesAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogBinding.inflate(inflater,container, false)
        storageVolumes = Files.getExternalStorages(requireContext())
        setUpRecyclerView()
        onClickListeners()
        if (FilesAdapter.isCopy){
            binding.tvStorageTitle.text = "Copy File too ->"
        }else{
            binding.tvStorageTitle.text = "Move File too ->"
        }
        adapter.onClick = {path , file ->
            destinationPath = path
        }


        return binding.root
    }
    private fun onClickListeners(){

        binding.btnOk.setOnClickListener {
            if (FilesAdapter.isCopy){
                copyFileListener()
            }else{
                moveFileListener()
            }

        }
        binding.btnCancel.setOnClickListener {
            dialog?.dismiss()
        }
        binding.imgViewChangeDir.setOnClickListener {
            ChangeDirectoryDialog().createDialog(this, null, adapter)
        }
        binding.imgBtnBack.setOnClickListener {
            val parentFile = File(destinationPath).parentFile
            if (parentFile!= null && parentFile.canRead()){
                destinationPath = parentFile.absolutePath
                adapter.onDirectoryClick(Files.getFiles(requireContext(), parentFile.absolutePath))
            }else{
                Toast.makeText(requireContext(), "Can't go Back!!", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun copyFileListener(){
            if (Files.copyFile(FilesAdapter.srcFile!!, DialogFilesAdapter.desFile)){
                Toast.makeText(requireContext(), "File Copied Successfully ", Toast.LENGTH_SHORT).show()
                dismiss()
                isSuccessfully = true
            }else{
                Toast.makeText(requireContext(), "Error! Could Not Able To Copy The File", Toast.LENGTH_LONG).show()
                isSuccessfully = false
            }


    }

    private fun moveFileListener(){
            if (Files.moveFile(FilesAdapter.srcFile!!, DialogFilesAdapter.desFile, this)){
                Toast.makeText(requireContext(), "File Moved Successfully ", Toast.LENGTH_SHORT).show()
                dismiss()
                isSuccessfully = true
            }else{
                Toast.makeText(requireContext(), "Error! Could Not Able To Move The File", Toast.LENGTH_LONG).show()
                isSuccessfully = false
            }

    }




    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
    }

    private fun setUpRecyclerView(){
        adapter = DialogFilesAdapter(Files.getFiles(requireContext(),storageVolumes[0].path, isaDialog = true), this)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = adapter
    }
    companion object{
        var isSuccessfully = false
    }
}