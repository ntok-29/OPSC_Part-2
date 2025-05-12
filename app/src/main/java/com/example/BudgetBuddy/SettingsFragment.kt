package com.example.BudgetBuddy

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.BudgetBuddy.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())

        // Set up user profile info
        val userDetails = sessionManager.getUserDetails()
        binding.tvUserName.text = userDetails["name"]
        binding.tvUserEmail.text = userDetails["email"]

        // Set up logout button
        binding.btnLogout.setOnClickListener {
            sessionManager.logoutUser()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }

        // Set up other settings options
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            // This would toggle dark mode
            // For now, we'll just use a placeholder implementation
        }

        binding.switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            // This would toggle notifications
            // For now, we'll just use a placeholder implementation
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
