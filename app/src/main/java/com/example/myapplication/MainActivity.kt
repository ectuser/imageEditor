package com.example.myapplication


import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream
import java.text.DateFormat.getDateTimeInstance
import java.util.*
import kotlin.math.min



class MainActivity : AppCompatActivity() {

    private val PERMISSION_CODE = 1000
    private val IMAGE_PICK_CODE = 1000
    private val IMAGE_CAPTURE_CODE = 1001
    private var image_uri: Uri? = null
    private var REQ_CODE_FOR_ACTION: Int = 0
    private var initialHeight = 0
    var RETURN_BITMAP = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
    var BACK_BITMAP = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
    private val edit = EditImage(BACK_BITMAP)
    private val actionBut = ActionsWithButtons()
    private var MAIN_COUNTER = 0

//    private val image = OpenImage()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //button click
        imageButton.setOnClickListener { checkPermissionsForGallery() }
        cameraButton.setOnClickListener { checkPermissionsForCamera() }

        @Suppress("DEPRECATION")
        mainImage.layoutParams.width = windowManager.defaultDisplay.width / 2
        @Suppress("DEPRECATION")
        mainImage.layoutParams.height = windowManager.defaultDisplay.height / 2

        initialHeight = mainImage.layoutParams.height

        // 100% enlarge by default
        enlargeSpinner.setSelection(2)

        // SHOW FILTERS BUTTONS
        filtersButton.setOnClickListener{ actionBut.filterClick(firstFilter, secondFilter, thirdFilter, cameraButton, imageButton, backButton, returnButton) }
        // NEGATIVE FILTER
        firstFilter.setOnClickListener { edit.filter(mainImage, 1) }
        secondFilter.setOnClickListener { edit.filter(mainImage, 2) }
        thirdFilter.setOnClickListener { edit.filter(mainImage, 3) }
//        button3.setOnClickListener { edit.rotateImage(mainImage) }
<<<<<<< HEAD
        button3.setOnClickListener { edit.blur(mainImage, coordinates) }
        unsharpMaskButton.setOnClickListener { edit.unsharpMask(this, mainImage) }
        returnButton.setOnClickListener { edit.returnImage(mainImage, RETURN_BITMAP) }
        backButton.setOnClickListener {
            BACK_BITMAP = edit.returnBackBitmap()
            edit.returnImage(mainImage, BACK_BITMAP)
        }

=======
        button3.setOnClickListener { edit.blur(mainImage) }
>>>>>>> a017cd3 (Nrls 2: x2, x3)
    }

    // SO NIGGAS THAT'S MY FUCKING CHECK FOR PERMISSIONS OK?
    private fun checkPermissionsForGallery(){
        // FOR GALLERY REQ CODE
        REQ_CODE_FOR_ACTION = 2002

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED){
                //permission denied
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                //show popup to request runtime permission
                requestPermissions(permissions, PERMISSION_CODE)
            }
            else{
                //permission already granted
                pickImageFromGallery()
            }
        }
        else{
            //system OS is < Marshmallow
            pickImageFromGallery()
        }
    }

    private fun checkPermissionsForCamera(){
        // FOR CAMERA REQ CODE
        REQ_CODE_FOR_ACTION = 2001

        //if system os is Marshmallow or Above, we need to request runtime permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED ||
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED){
                //permission was not enabled
                val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                //show popup to request permission
                requestPermissions(permission, PERMISSION_CODE)
            }
            else{
                //permission already granted
                openCamera()
            }
        }
        else{
            //system os is < marshmallow
            openCamera()
        }
    }

    // SOME KIND OF PERMISSIONS CODE SHIT
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        //called when user presses ALLOW or DENY from Permission Request Popup
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && REQ_CODE_FOR_ACTION == 2001) {
                    //permission from popup was granted
                    openCamera()
                }
                else if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && REQ_CODE_FOR_ACTION == 2002) {
                    //permission from popup was granted
                    pickImageFromGallery()
                }
                else{
                    //permission from popup was denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // OPEN YOUR CAMERA NIGGA
    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

    // OPEN YOUR GALLERY NIGGA
    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    // SAVE YOUR ASS IMAGE NIGGA
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //called when image was captured from camera intent
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            mainImage.setImageURI(data?.data)
        }
        else if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_CAPTURE_CODE){
            //set image captured to image view
            mainImage.setImageURI(image_uri)
        }

<<<<<<< HEAD
        RETURN_BITMAP = (mainImage.drawable as BitmapDrawable).bitmap

//        compressImage()
=======
            //compressImage()
>>>>>>> 7d72210 (Fix 6: splines without gaps (better interpolation))


        // SHOW BUTTON "FILTERS":
        filtersButton.visibility = View.VISIBLE
    }


    // IMAGE COMPRESSION
    private fun compressImage() {
        val reqHeight = 2000
        val reqWidth = 2000
        val compressCoefficient: Double
        var bitmap = (mainImage.drawable as BitmapDrawable).bitmap

        if (bitmap.width > reqWidth || bitmap.height > reqHeight) {
            compressCoefficient = min(bitmap.width.toDouble() / reqWidth.toDouble(),
                bitmap.height.toDouble() / reqHeight.toDouble())

            if (compressCoefficient > 1) {
                bitmap = Bitmap.createScaledBitmap(bitmap, (bitmap.width.toDouble() / compressCoefficient).toInt(),
                    (bitmap.height.toDouble() / compressCoefficient).toInt(), false)
                mainImage.setImageBitmap(bitmap)
            }
        }
    }

    // SAVING YOUR EDITED IMAGE TO A SPECIFIC FOLDER
    fun saveImage(@Suppress("UNUSED_PARAMETER") view: View) {
        // Creating new folder
        val folder = File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)}" +
                "${File.separator}imageEditor${File.separator}")
        folder.mkdir()

        val dateFormat = getDateTimeInstance()
        val currentDate = dateFormat.format(Date())
        val file = File(folder, "Img[$currentDate].jpg")
        val outputStream = FileOutputStream(file)
        val bitmap = (mainImage.drawable as BitmapDrawable).bitmap

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()

        // Forcing the system to see your new image
        val folderArray = arrayOf(folder.path)
        val fileArray = arrayOf(file.path)
        MediaScannerConnection.scanFile(this, folderArray, fileArray, null)
    }

    // JUST A WRAPPER FUNCTION FOR ENLARGING
    fun doEnlarging(@Suppress("UNUSED_PARAMETER") view: View) {
        edit.enlarge(mainImage, enlargeSpinner.selectedItem.toString(), initialHeight)
    }

    // JUST A WRAPPER FUNCTION FOR ENLARGING
    fun doScaling(@Suppress("UNUSED_PARAMETER") view: View) {
        when {
            scaleSpinner.selectedItemPosition == 0 -> edit.scale05x(this, mainImage)
            scaleSpinner.selectedItemPosition == 1 -> edit.scale2x(this, mainImage)
            scaleSpinner.selectedItemPosition == 2 -> edit.scale3x(this, mainImage)
        }
    }

    // DMITRY'S FUNCTION TO FUCK SOME BITCHES USING A*
    fun transitToAStar(@Suppress("UNUSED_PARAMETER") view: View) {
        val intent = Intent(this, AStarMainActivity::class.java)
        startActivity(intent)
    }

    fun transitToInterpolation(@Suppress("UNUSED_PARAMETER") view: View) {
        val intent = Intent(this, InterpolationActivity::class.java)
        startActivity(intent)
    }
}