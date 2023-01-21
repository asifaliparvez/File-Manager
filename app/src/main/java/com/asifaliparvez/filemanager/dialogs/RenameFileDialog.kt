package com.asifaliparvez.filemanager.dialogs

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.asifaliparvez.filemanager.R
import com.asifaliparvez.filemanager.adapters.FilesAdapter
import com.asifaliparvez.filemanager.helpers.Files
import java.io.File

class RenameFileDialog {
    fun renameFileDialog(
        fragment: Fragment,
        srcPath: String,
        desPath: String,
        filesAdapter: FilesAdapter,
        position: Int
    ) {
        val builder = AlertDialog.Builder(fragment.requireContext())
        var path:String = ""
        val dialogView = fragment.layoutInflater.inflate(R.layout.rename_file_dialog, null)
        builder.setView(dialogView)
        val alertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val cancelBtn = dialogView.findViewById<Button>(R.id.cancelBtnRenameFile)
        val renameBtn = dialogView.findViewById<Button>(R.id.renameBtnRenameFile)
        val editText = dialogView.findViewById<EditText>(R.id.fileNameEditText_RenameFileDialog)
        editText.setText(File(srcPath).name)

        cancelBtn.setOnClickListener {
            alertDialog.dismiss()
        }


        renameBtn.setOnClickListener {

            if (editText.text.trim().isNotEmpty()) {
                val text = editText.text.trim().toString()
                path = "$desPath/$text"
                val newFile = Files.renameFile(fragment.requireContext(), File(srcPath), File(path))
                if (newFile != null) {
                    filesAdapter.arrayList[position] = newFile
                    filesAdapter.notifyItemChanged(position)
                    alertDialog.dismiss()
                }

                return@setOnClickListener


            } else {
              editText.error = "Please enter a valid name!"
            }
        }
        alertDialog.show()
    }


}