package mypackage.h2hub.fragments.receive

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import mypackage.h2hub.R
import mypackage.h2hub.databinding.FragmentReceiveBinding
import mypackage.h2hub.adapter.ReceiveAdapter
import mypackage.h2hub.utils.Resource
import mypackage.h2hub.viewmodel.DonationsViewModel

@AndroidEntryPoint
class ReceiveFragment : Fragment() {
    private lateinit var binding: FragmentReceiveBinding
    private val adapter: ReceiveAdapter by lazy { ReceiveAdapter() }
    private val viewModel: DonationsViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentReceiveBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.receiveRv.adapter = adapter

        val recyclerView: RecyclerView = view.findViewById(R.id.receiveRv)
        recyclerView.layoutManager = LinearLayoutManager(requireContext()) // Set your desired layout manager
        recyclerView.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.getDonations()
        }

        viewModel.donations.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Loading -> {
                    binding.progressBar.isVisible = true
                }
                is Resource.Success -> {
                    binding.progressBar.isVisible = false
                    adapter.submitList(state.data)
                }
                is Resource.Error -> {
                    binding.progressBar.isVisible = false
                    Toast.makeText(requireContext(), "An error occurred..", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return view
    }
}