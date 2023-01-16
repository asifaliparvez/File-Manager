package com.asifaliparvez.filemanager.dialogs

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.asifaliparvez.filemanager.R
import com.asifaliparvez.filemanager.helpers.Files
import java.io.File

class RenameFileDialog {
    fun renameFileDialog(fragment: Fragment, srcPath:String, desPath:String): String {
        val builder = AlertDialog.Builder(fragment.requireContext())
        var path:String = ""
        val dialogView = fragment.layoutInflater.inflate(R.layout.rename_file_dialog, null)
        builder.setView(dialogView)
        val alertDialog = builder.create()
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


            } else {
              editText.error = "Please enter a valid name!"
            }
        }
        alertDialog.show()
        return path
    }


}