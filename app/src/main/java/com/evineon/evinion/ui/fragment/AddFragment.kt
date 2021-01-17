package com.evineon.evinion.ui.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.evineon.evinion.R
import com.evineon.evinion.model.User
import com.evineon.evinion.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream

class AddFragment : Fragment() {
    private lateinit var mView: View
    lateinit var userViewModel: UserViewModel
    private val RESULT_LOAD_IMAGE = 1
    private var image: ByteArray? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_add, container, false)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        mView.btnAddUser.setOnClickListener {
            insertIntoDatabase()
        }
        mView.pickImage.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(
                photoPickerIntent,
                RESULT_LOAD_IMAGE
            )
        }
        return mView
    }

    private fun insertIntoDatabase() {
        val fName = edtFirstName.text.toString()
        val lName = edtLastName.text.toString()
        val introduction = edtIntroduction.text.toString()

        if (isCheck(fName, lName, introduction, image)) {
            val user = User(
                0,
                fName, lName, introduction, image!!
            )
            userViewModel.addUser(user)
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "Please Check the fields.", Toast.LENGTH_SHORT).show()
        }
    }

    fun isCheck(firstName: String, LastName: String, introduction: String, image:ByteArray?): Boolean {
        return !(TextUtils.isEmpty(firstName) && TextUtils.isEmpty(LastName) && TextUtils.isEmpty(
            introduction) && image == null)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            try {
                val imageUri = data!!.data
                val imageStream: InputStream? =
                    requireContext().getContentResolver().openInputStream(
                        imageUri!!
                    )
                val selectedImage = BitmapFactory.decodeStream(imageStream)
                val sp = getResizedBitmap(selectedImage, 500, 500)!!
                mView.pickImage.setImageBitmap(sp)
                image = getBitmapAsByteArray(sp)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Something wrong", Toast.LENGTH_LONG).show()
            } catch (e: IOException) {
                e.printStackTrace()
                e.message
            }
        } else {
            Toast.makeText(requireContext(), "You haven't picked Image", Toast.LENGTH_LONG).show()
        }
    }

    fun getBitmapAsByteArray(bitmap: Bitmap): ByteArray? {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream)
        return outputStream.toByteArray()
    }

    fun getResizedBitmap(bm: Bitmap, newWidth: Int, newHeight: Int): Bitmap? {
        val width = bm.width
        val height = bm.height
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        // CREATE A MATRIX FOR THE MANIPULATION
        val matrix = Matrix()
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight)

        // "RECREATE" THE NEW BITMAP
        return Bitmap.createBitmap(
            bm, 0, 0, width, height, matrix, false
        )
    }
}