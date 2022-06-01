package com.asifaliparvez.filemanager.fragments


import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.storage.StorageManager
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.asifaliparvez.filemanager.BuildConfig
import com.asifaliparvez.filemanager.adapters.FilesAdapter
import com.asifaliparvez.filemanager.databinding.FragmentFilesBinding
import com.asifaliparvez.filemanager.dialogs.Dialog
import com.asifaliparvez.filemanager.helpers.Files
import com.asifaliparvez.filemanager.helpers.Permissions
import com.asifaliparvez.filemanager.models.FileModel

class FilesFragment : Fragment() {
    private var _binding:FragmentFilesBinding? = null
    private lateinit var callback: OnBackPressedCallback
    private val binding get() = _binding!!
    private var path = ""
    private val array = arrayListOf<FileModel>()
    lateinit var fileAdapter: FilesAdapter
    private lateinit var launcher: ActivityResultLauncher<String>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Handling OnBack Button Pressed Situation
        callback=
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    val newPath = path.subSequence(0, path.lastIndexOf("/")).toString()
                    path = newPath
                    fileAdapter.directoryOnClick(Files.getFiles(requireContext(), newPath))
                    Toast.makeText(requireContext(), "ggg", Toast.LENGTH_SHORT).show()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilesBinding.inflate(inflater, container, false)
        launcher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if (it){
                setUpRecyclerView()
            }

        }
        // Checking For Permission
        if(Permissions.isPermissionGranted(requireContext())){
            setUpRecyclerView()

        }else{
            // Asking For Manage_External_Storage Permission
            if (Permissions.isAndroidSdkGreaterThan10()){
                try {
                    val uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID)
                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri)
                    startActivity(intent)
                } catch (ex: Exception) {
                    val uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID)
                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri)
                    startActivity(intent)
                }
            }else{
                launcher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)

            }

        }

        binding.floatingActionButton.setOnClickListener {
            if(Dialog.createFileDialog(this, path)){
                setUpRecyclerView()
            }

        }
        fileAdapter.onItemClick = { path, position ->

            if (position == 0) {
                callback.handleOnBackPressed()

            }else{
                this.path = path
            }

        }
        return binding.root
    }
    // Getting Path
    private fun getPath(): String {
        val storageManager = requireContext().getSystemService(StorageManager::class.java)
        path = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            storageManager.primaryStorageVolume.directory?.path?:""

        } else {
            Environment.getExternalStorageDirectory().path.toString()

        }
        Toast.makeText(requireContext(), path, Toast.LENGTH_SHORT).show()
        return path
    }

    private fun setUpRecyclerView() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        array.addAll(Files.getFiles(requireContext(), getPath()))
        fileAdapter = FilesAdapter(requireContext(), array )
        binding.recyclerview.apply {
            this.adapter = fileAdapter
            this.layoutManager = layoutManager
        }
    }



}


