package com.evineon.evinion.ui.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.evineon.evinion.R
import com.evineon.evinion.model.User
import com.evineon.evinion.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.fragment_update.*
import kotlinx.android.synthetic.main.fragment_update.view.*
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream

class UpdateFragment : Fragment() {
    private val args by navArgs<UpdateFragmentArgs>()
    lateinit var userViewModel: UserViewModel
    lateinit var mView: View
    private var image:ByteArray? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_update, container, false)
        mView.edtFirstName_update.setText(args.userData.firstName)
        mView.edtLastName_update.setText(args.userData.lastName)
        mView.edtIntroduction_update.setText(args.userData.introduction.toString())
        image = args.userData.image
        val bitmap = BitmapFactory.decodeByteArray(image, 0, image!!.size)
        mView.ivProfile_update.setImageBitmap(bitmap)

        userViewModel= ViewModelProvider(this).get(UserViewModel::class.java)
        mView.btnUpdateUser.setOnClickListener {
            updateItem()
        }
        setHasOptionsMenu(true)
        return mView
    }
    fun updateItem(){
        val fName= edtFirstName_update.text.toString()
        val lName= edtLastName_update.text.toString()
        val introduction= edtIntroduction_update.text.toString()
        if(check(fName,lName, introduction)){
            val user= User(args.userData.id,fName, lName , introduction, image!!)
            userViewModel.updateUser(user)
            Toast.makeText(requireContext(), "Update User Successfully.", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }else{
            Toast.makeText(requireContext(), "Please check all the Fields.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun check(fName: String, lName: String, introduction: String): Boolean {
        return !(TextUtils.isEmpty(fName) && TextUtils.isEmpty(lName)
                && TextUtils.isEmpty(introduction) && image == null )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_delete_user,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.delete_user){
            deleteUser()
        }
        return super.onOptionsItemSelected(item)
    }
    private fun deleteUser(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_,_ ->
            userViewModel.deleteUser(args.userData)
            Toast.makeText(requireContext(),
                "Delete ${args.userData.firstName}",
                Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("No"){_,_ -> }
        builder.setTitle("Delete ${args.userData.firstName} ?")
        builder.setMessage("Do you want to delete ${args.userData.firstName} ?")
        builder.create().show()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            try {
                val imageUri = data!!.data
                val imageStream: InputStream? = requireContext().getContentResolver().openInputStream(
                    imageUri!!
                )
                val selectedImage = BitmapFactory.decodeStream(imageStream)
                val sp = getResizedBitmap(selectedImage, 500, 500)!!
                mView.ivProfile_update.setImageBitmap(sp)
                image = getBitmapAsByteArray(sp)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_LONG).show()
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