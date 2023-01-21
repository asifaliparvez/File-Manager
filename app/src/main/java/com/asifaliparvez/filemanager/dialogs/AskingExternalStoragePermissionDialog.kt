package com.asifaliparvez.filemanager.dialogs

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.provider.Settings
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import com.asifaliparvez.filemanager.BuildConfig
import com.asifaliparvez.filemanager.R
import com.asifaliparvez.filemanager.helpers.Permissions

// Asking Manage All Files Permission
class AskingExternalStoragePermissionDialog {

    fun createDialog(fragment: Fragment, launcher:ActivityResultLauncher<String>){
        val builder = AlertDialog.Builder(fragment.requireContext())
        val dialogView = fragment.layoutInflater.inflate(R.layout.asking_manage_files_permission_dialog, null)
        builder.setView(dialogView)
        val alertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val cancelBtn = dialogView.findViewById<Button>(R.id.cancel_Btn_Permission)
        val okBtn = dialogView.findViewById<Button>(R.id.ok_Btn_Permission)
        cancelBtn.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.show()
        okBtn.setOnClickListener {
            if (Permissions.isAndroidSdkGreaterThan10()){
                            try {
                                val uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID)
                                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri)
                                fragment.startActivity(intent)
                            } catch (ex: Exception) {
                                val uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID)
                                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri)
                                fragment.startActivity(intent)
                            }finally {
                                alertDialog.dismiss()
                            }

            }else{
                launcher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)

            }

        }

    }
}