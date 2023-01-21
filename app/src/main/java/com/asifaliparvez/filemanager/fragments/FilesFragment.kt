package com.asifaliparvez.filemanager.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.asifaliparvez.filemanager.R
import com.asifaliparvez.filemanager.adapters.FilesAdapter
import com.asifaliparvez.filemanager.databinding.FragmentFilesBinding
import com.asifaliparvez.filemanager.dialogs.AskingExternalStoragePermissionDialog
import com.asifaliparvez.filemanager.dialogs.ChangeDirectoryDialog
import com.asifaliparvez.filemanager.dialogs.CreatingFileORFolderDialog
import com.asifaliparvez.filemanager.helpers.Files
import com.asifaliparvez.filemanager.helpers.Permissions
import com.asifaliparvez.filemanager.models.FileModel
import java.io.File

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
                    val file = File(path).parentFile
                    if (file == null || !file.canRead()){
                        activity?.finish()
                    }else{

                        fileAdapter!!.directoryOnClick(Files.getFiles(requireContext(), file.absolutePath))
                        path = file.absolutePath

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
        binding.toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.change_Directory ->{
                    ChangeDirectoryDialog().createDialog(this, fileAdapter, null)

                }

            }

            true

        }
        askForPermission()
        onClickFloatingBtn()

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

    private fun onClickFloatingBtn(){
        // Handling FloatingBtn onClick
        binding.floatingActionButton.setOnClickListener {
            (CreatingFileORFolderDialog().createFileDialog(this, path, fileAdapter))

        }
    }
    private fun askForPermission(){
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






