package com.example.pestotech.views.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.pestotech.R
import com.example.pestotech.databinding.FragmentLoginBinding
import com.example.pestotech.utils.isValidEmail
import com.example.pestotech.utils.isValidPasswordFormat
import com.example.pestotech.utils.showToast
import com.example.pestotech.viewmodel.LoginViewModel
import com.example.pestotech.views.acitvity.MainActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var navController: NavController
    private val viewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        navController = findNavController()



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signupTv.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToSignupFragment()
            navController.navigate(action)

        }
        binding.loginTv.setOnClickListener {
            isLogin()
        }


    }

    private fun isLogin() {
        val email = binding.emailEd.text.toString()
        val password = binding.passEd.text.toString()

        if (isValidEmail(email) && isValidPasswordFormat(password)) {
            viewModel.isLogin(email, password).observe(viewLifecycleOwner) {
                if (it) {
                    openMainScreen()

                } else {
                    context?.showToast(it.toString())
                }
            }
        } else {
            context?.showToast(getString(R.string.please_enter_details))
        }


    }

    private fun openMainScreen() {
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
        activity?.finish()

    }

}