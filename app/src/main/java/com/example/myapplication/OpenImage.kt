package com.example.myapplication

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat.requestPermissions
import android.support.v4.content.ContextCompat.checkSelfPermission
import android.support.v7.app.AppCompatActivity


class OpenImage{
    private val permissionCode = 1000
    private val imagePickCode = 1000

    fun checkPermissionsForGallery(context : Context){
        // FOR GALLERY REQ CODE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED){
                //permission denied
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                //show popup to request runtime permission
                requestPermissions(MainActivity(), permissions, permissionCode)
            }
            else{
                //permission already granted
                pickImageFromGallery();
            }
        }
        else{
            //system OS is < Marshmallow
            pickImageFromGallery();
        }
    }
    fun pickImageFromGallery() {
        //Intent to pick image

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
//        val bundleOptions = Bundle()
        AppCompatActivity().startActivityForResult(intent, imagePickCode)
    }

}