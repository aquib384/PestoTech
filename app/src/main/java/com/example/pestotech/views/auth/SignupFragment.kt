package com.example.pestotech.views.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.pestotech.utils.isValidEmail
import com.example.pestotech.utils.isValidPasswordFormat
import com.example.pestotech.utils.showToast
import com.example.pestotech.views.acitvity.MainActivity
import com.example.pestotech.databinding.FragmentSignupBinding
import com.example.pestotech.viewmodel.SignupViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SignupFragment : Fragment() {

    private lateinit var binding: FragmentSignupBinding
    private val viewModel: SignupViewModel by viewModels()
    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        navController = findNavController()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signedUpTv.setOnClickListener {
            signup()
        }
        binding.loginTv.setOnClickListener {
            val action = SignupFragmentDirections.actionSignupFragmentToLoginFragment()
            navController.navigate(action)
        }
    }

    private fun signup() {

        val name = binding.nameEd.text.toString()
        val email = binding.emailEd.text.toString()
        val password = binding.passEd.text.toString()
        if (name.isNotEmpty() && (isValidEmail(email) && isValidPasswordFormat(password))) {
            viewModel.createUser(name, email, password).observe(viewLifecycleOwner) {

                if (it) {
                    Log.d("TAG", it.toString())

                    openMainScreen()


                } else {
                    Log.d("TAG", it.toString())
                    context?.showToast(it.toString())
                }
            }
        } else {
            context?.showToast("Please enter correct details")
        }

    }

    private fun openMainScreen() {
        val intent = Intent(requireActivity(), MainActivity::class.java)
        startActivity(intent)
        activity?.finish()

    }
}