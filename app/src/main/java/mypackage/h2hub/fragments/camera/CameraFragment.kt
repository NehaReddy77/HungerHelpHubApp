package mypackage.h2hub.fragments.camera

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import mypackage.h2hub.databinding.FragmentCameraBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CameraFragment : Fragment() {

    /*private val CAMERA_PERMISSION_REQUEST = 1001
    private val REQUEST_STORAGE_PERMISSION_CODE = 1002
    private val PICK_IMAGE_REQUEST = 101*/
    private val CAPTURE_IMAGE_REQUEST = 102
    private lateinit var binding: FragmentCameraBinding

    private val CAMERA_PERMISSION_REQUEST = 1003
    private val PICK_IMAGE_REQUEST = 1002
    private val REQUEST_STORAGE_PERMISSION_CODE = 1001
    private var currentPhotoPath: String = ""
    private lateinit var selectedImageUri: Uri // Assume this is the URI of the selected image

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //super.onCreate(savedInstanceState)
        //setContentView(R.layout.your_layout)
        binding = FragmentCameraBinding.inflate(inflater, container, false)
        val view = binding.root
        // Check and request camera permissions if not granted
        if (!checkCameraPermissions()) {
            requestCameraPermissions()
        }
        if (!checkAccessDownloadsPermissions()){
            requestAccessDownloadsPermissions()
        }

        // Add your button click listener
        //navigateToCameraButton.setOnClickListener {
            // Call the function to open the dialog for selecting camera/gallery

        //checkStoragePermission()
        //}

        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showImagePickerDialog()
        //checkStoragePermission()
        // Now that the view is created, you can safely access the imageView
        //loadImageIntoImageView(selectedImageUri)
    }
    private fun checkCameraPermissions(): Boolean {
        return (ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED)
    }
    private fun requestCameraPermissions() {
        requestPermissions(
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST
        )
    }
    private fun checkAccessDownloadsPermissions(): Boolean {
        return (ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED)
    }
    private fun requestAccessDownloadsPermissions(){
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_STORAGE_PERMISSION_CODE
        )
    }
    fun showImagePickerDialog() {
        val options = arrayOf("Take Photo", "Choose from Gallery", "Cancel")

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Choose an option")

        builder.setItems(options) { dialog, item ->
            when {
                options[item] == "Take Photo" -> {
                    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    var photoFile: File? = null
                    try {
                        photoFile = createImageFile()
                    } catch (ex: IOException) {
                        // Handle file creation error
                    }
                    photoFile?.also {
                        val photoURI: Uri = FileProvider.getUriForFile(
                            requireContext(),
                            "com.your.package.name.fileprovider",
                            it
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST)
                    }
                }
                options[item] == "Choose from Gallery" -> {
                    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                    intent.addCategory(Intent.CATEGORY_OPENABLE)
                    intent.type = "image/*" // Limit the selection to image files
                    startActivityForResult(intent, PICK_IMAGE_REQUEST)
                }

                options[item] == "Cancel" -> {
                    dialog.dismiss()
                }
            }
        }
        builder.show()
    }
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            // Save a file path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    // Handle the image picked from the gallery
                    val photoURI = data?.data
                    val result = Bundle()
                    result.putString("photoUri", photoURI.toString())
                    parentFragmentManager.setFragmentResult("photoUri", result)
                    // Navigate back to the parent fragment
                    parentFragmentManager.popBackStack()
                }
                CAPTURE_IMAGE_REQUEST -> {
                    val photoFile = File(currentPhotoPath)
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        "com.your.package.name.fileprovider",
                        photoFile
                    )
                    val result = Bundle()
                    result.putString("photoUri", photoURI.toString())
                    parentFragmentManager.setFragmentResult("photoUri", result)
                    // Navigate back to the parent fragment
                    parentFragmentManager.popBackStack()
                }
            }
        }
    }
    // Check and request storage permission
    /*private fun checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_STORAGE_PERMISSION_CODE
            )
        } else {
            openFilePicker()
            //openGallery()
            //val jpgFileUri = getJpgFileFromDownloads()
            /*if (jpgFileUri != null) {
                loadImageWithGlide(jpgFileUri)
            }*/
        }
    }*/



    // Handle permission result
    /*override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery()
            } else {
                // Handle permission denied case
            }
        }
    }*/
    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*" // Limit the selection to image files
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    /*private fun loadImageWithGlide(imageUri: Uri) {
        val imageView: ImageView = requireView().findViewById(R.id.btnCapture) // Replace with your ImageView ID

        Glide.with(this)
            .load(imageUri)
            .placeholder(R.drawable.info) // Placeholder image while loading
            .error(R.drawable.info) // Image to show if loading fails
            .into(imageView)
    }*/
}
