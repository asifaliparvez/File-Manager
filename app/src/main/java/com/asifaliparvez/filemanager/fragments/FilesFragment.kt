package com.asifaliparvez.filemanager.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.asifaliparvez.filemanager.adapters.FilesAdapter
import com.asifaliparvez.filemanager.databinding.FragmentFilesBinding
import com.asifaliparvez.filemanager.dialogs.AskingExternalStoragePermissionDialog
import com.asifaliparvez.filemanager.dialogs.CreatingFileORFolderDialog
import com.asifaliparvez.filemanager.helpers.Files
import com.asifaliparvez.filemanager.helpers.Permissions
import com.asifaliparvez.filemanager.models.FileModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FilesFragment : Fragment() {


    private var _binding: FragmentFilesBinding? = null
    private lateinit var callback: OnBackPressedCallback
    private val binding get() = _binding!!
    private lateinit var path:String
    var fileAdapter: FilesAdapter? = null
    private lateinit var launcher: ActivityResultLauncher<String>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Handling OnBack Button Pressed Situation
        callback=
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    val storageVolumes = Files.getExternalStorages(requireContext()).filter {
                        it.path == path
                    }
                    if (storageVolumes.isNotEmpty()){

                        callback.isEnabled = false
                        activity?.onBackPressed()

                    }else{
                        val newPath = path.subSequence(0, path.lastIndexOf("/")).toString()
                        path = newPath
                        fileAdapter!!.directoryOnClick(Files.getFiles(requireContext(), newPath))
                    }

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
                setUpRv()
            }

        }

        // Checking For Permission
        if(Permissions.isPermissionGranted(requireContext())){
            setUpRv()

        }else{
            AskingExternalStoragePermissionDialog().createDialog(this, launcher)




        }
        if(Permissions.isPermissionGranted(requireContext())){
            setUpRv()
        }
        // Handling FloatingBtn onClick
        binding.floatingActionButton.setOnClickListener {
            if(CreatingFileORFolderDialog().createFileDialog(this, path)){
                Files.getFiles(requireContext(), path).forEach {
                    Log.d("TAG", it.name)
                }
                Toast.makeText(requireContext(), "File Created", Toast.LENGTH_SHORT).show()
                setUpRv(Files.getFiles(requireContext(), path))
                fileAdapter?.notifyDataSetChanged()
            }

        }
        fileAdapter?.onItemClick = { path, position ->

            if (position == 0) {
                callback.handleOnBackPressed()

            }else{
                this.path = path
            }

        }
        path = Files.getExternalStorages(requireContext())[0].absolutePath
        return binding.root
    }
    /// Setup RecyclerView
    private fun setUpRv(array:ArrayList<FileModel> = Files.getFiles(requireContext())) {
        binding.floatingActionButton.visibility = View.VISIBLE
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        //array.addAll(Files.getFiles(requireContext(), Files.getExternalStorages(requireContext())[0].absolutePath))
        fileAdapter = FilesAdapter(this, array)
        binding.recyclerview.apply {
            this.adapter = fileAdapter
            this.layoutManager = layoutManager
        }
    }

}






