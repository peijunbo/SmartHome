package com.example.myapplication.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.LoginFragmentBinding
import com.example.myapplication.ui.home.HomeFragment

class LoginFragment : Fragment() {
    private lateinit var binding: LoginFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.login_fragment, container, false)
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.setOnClickListener {
            if (binding.userNameInput.text.toString() != "abcdef") {
                binding.userNameLayout.error = "用户名错误"
            } else if (binding.passwordInput.text.toString() != "123456") {
                binding.passwordLayout.error = "密码错误"
            } else {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment())
            }
        }
        binding.userNameInput.addTextChangedListener {
            if (binding.userNameLayout.error != null) {
                binding.userNameLayout.error = null
            }
        }
        binding.passwordInput.addTextChangedListener {
            binding.passwordLayout.error = null // 清空错误
        }

    }
}