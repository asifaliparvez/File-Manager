package com.asifaliparvez.filemanager.fragments

import android.app.AlertDialog.Builder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.asifaliparvez.filemanager.R
import com.asifaliparvez.filemanager.adapters.DialogFilesAdapter
import com.asifaliparvez.filemanager.adapters.FilesAdapter
import com.asifaliparvez.filemanager.databinding.FragmentDialogBinding
import com.asifaliparvez.filemanager.helpers.Files
import com.asifaliparvez.filemanager.models.FileModel


class MyDialogFragment : DialogFragment() {
    private var _binding:FragmentDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var storagVolumes:ArrayList<FileModel>
    private  var adapter: DialogFilesAdapter? = null
    private var destinationPath = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogBinding.inflate(inflater,container, false)
        storagVolumes = Files.Companion.getExternalStorages(requireContext())
        binding.storageTitleTextView.setOnClickListener {
            changeStorageDialog()

        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.storageTitleTextView.text = storagVolumes[0].path
        setUpRecyclerView()
        adapter?.onClick = {path ->
            destinationPath = path
            binding.storageTitleTextView.text = destinationPath
            Toast.makeText(requireContext(), "OnCl;ickl", Toast.LENGTH_SHORT).show()
        }
        binding.storageTitleTextView.text = destinationPath
        binding.okBtn.setOnClickListener {
            if (FilesAdapter.isCopy){
                copyFileListener()
            }else{
                moveFileListener()
            }


        }
        binding.cancelBtn.setOnClickListener {
            dialog?.dismiss()
        }
        binding.backImageBtn.setOnClickListener {
            val newPath = destinationPath.subSequence(0, destinationPath.lastIndexOf("/")).toString()
            destinationPath = newPath
            adapter!!.onDirectoryClick(destinationPath)
        }
    }
    private fun changeStorageDialog(){
        val builder = Builder(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.change_directory_alert_dialog, null)
        builder.setView(dialogView)
        val alertDialog = builder.create()
        alertDialog.show()

    }


    private fun copyFileListener(){
            if (Files.copyFile(FilesAdapter.srcFile!!, DialogFilesAdapter.desFile)){
                Toast.makeText(requireContext(), "File Copied Successfully ", Toast.LENGTH_SHORT).show()
                dismiss()
                isSuccessfull = true
            }else{
                Toast.makeText(requireContext(), "Error! Could Not Able To Copy The File", Toast.LENGTH_LONG).show()
                isSuccessfull = false
            }


    }

    private fun moveFileListener(){
            if (Files.moveFile(FilesAdapter.srcFile!!, DialogFilesAdapter.desFile!!, FilesFragment())){
                Toast.makeText(requireContext(), "File Moved Successfully ", Toast.LENGTH_SHORT).show()
                dismiss()
                isSuccessfull = true
            }else{
                Toast.makeText(requireContext(), "Error! Could Not Able To Move The File", Toast.LENGTH_LONG).show()
                isSuccessfull = false
            }



    }


    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
    }

    private fun setUpRecyclerView(){
        val adapter = DialogFilesAdapter(Files.getFiles(requireContext(),storagVolumes[0].path, isaDialog = true), this)
        binding.listFilesRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.listFilesRecyclerView.adapter = adapter
    }
    companion object{
        var isSuccessfull = false
    }
}