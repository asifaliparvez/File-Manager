package com.asifaliparvez.filemanager.dialogs

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.asifaliparvez.filemanager.R
import com.asifaliparvez.filemanager.adapters.FilesAdapter
import com.asifaliparvez.filemanager.fragments.FilesFragment
import com.asifaliparvez.filemanager.helpers.Files
import java.io.File

class CreatingFileORFolderDialog {
        fun createFileDialog(fragment: Fragment, path: String, filesAdapter: FilesAdapter?) {
            val builder = AlertDialog.Builder(fragment.requireContext())
            val dialogView = fragment.layoutInflater.inflate(R.layout.create_new_file_dialog, null)
            builder.setView(dialogView)
            val alertDialog = builder.create()
            alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val cancelBtn = dialogView.findViewById<Button>(R.id.cancelBtn)
            val createBtn = dialogView.findViewById<Button>(R.id.createBtn)
            val fileRadioBtn = dialogView.findViewById<RadioButton>(R.id.fileRadioBtn)
            val folderRadioBtn = dialogView.findViewById<RadioButton>(R.id.folderRadioBtn)
            fileRadioBtn.isChecked = true
            val editText = dialogView.findViewById<EditText>(R.id.editTextTextFileName)
            cancelBtn.setOnClickListener {
                alertDialog.dismiss()
            }
            alertDialog.show()
            createBtn.setOnClickListener {
                if (folderRadioBtn.isChecked) {
                    val file = File(path, editText.text.toString())
                    if (file.exists()) {
                        editText.error = "Folder Already Exists!"
                        return@setOnClickListener

                    } else {
                        val createFile = Files.createFolder(path, editText.text.toString(), fragment.requireContext())
                        if(createFile != null){
                            filesAdapter?.insertItem(createFile)

                            alertDialog.dismiss()
                        }
                        return@setOnClickListener

                    }
                }

                else if (fileRadioBtn.isChecked) {
                    val file = File(path, editText.text.toString())
                    if (file.exists()) {
                        editText.error = "File Already Exists!"
                        return@setOnClickListener
                    } else {
                        val newFile = Files.createFile(path,editText.text.toString(), fragment.requireContext() )
                        if (newFile != null){
                            filesAdapter?.insertItem(newFile)
                            alertDialog.dismiss()
                        }
                        return@setOnClickListener


                    }

                } else {
                    Toast.makeText(
                        fragment.requireContext(),
                        "Please Select A Type",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
            fileRadioBtn.setOnClickListener {
                folderRadioBtn.isChecked = false
            }
            folderRadioBtn.setOnClickListener {
                fileRadioBtn.isChecked = false

            }

        }

    }

