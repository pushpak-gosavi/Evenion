package com.evineon.evinion.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.evineon.evinion.R
import com.evineon.evinion.adapter.UserAdapter
import com.evineon.evinion.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.fragment_list.view.*

private lateinit var mView:View
private lateinit var userViewModel: UserViewModel
private  val   userAdapter:UserAdapter by lazy { UserAdapter() }

class ListFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mView= inflater.inflate(R.layout.fragment_list, container, false)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
            mView.floatingActionButton.setOnClickListener {
                findNavController().navigate(R.id.action_listFragment_to_addFragment)
            }
        mView.rvUsers.adapter= userAdapter
        userViewModel.readAllData.observe(viewLifecycleOwner, Observer { user ->
            userAdapter.setData(user)
        })
        setHasOptionsMenu(true)
        return mView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_delete_all_users,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.delete_all_users){
            deleteAllUsers()
        }
        return super.onOptionsItemSelected(item)
    }
    private fun deleteAllUsers(){
            val builder = AlertDialog.Builder(requireContext())
            builder.setPositiveButton("Yes"){_,_ ->
                userViewModel.deleteAll()
                Toast.makeText(requireContext(),
                    "Delete All Users.",
                    Toast.LENGTH_SHORT).show()
            }
            builder.setNegativeButton("No"){_,_ -> }
            builder.setTitle("Delete All Users ?")
            builder.setMessage("Do you want to delete All Users ?")
            builder.create().show()
    }

}