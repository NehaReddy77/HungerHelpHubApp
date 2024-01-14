package mypackage.h2hub.fragments.donations

// DonationsFragment.kt
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import mypackage.h2hub.R
import mypackage.h2hub.adapter.DonationsAdapter
import mypackage.h2hub.databinding.FragmentDonationsBinding
import mypackage.h2hub.utils.Resource
import mypackage.h2hub.viewmodel.DonationsViewModel

@AndroidEntryPoint
class DonationsFragment : Fragment() {
    private lateinit var binding: FragmentDonationsBinding
    private val viewModel: DonationsViewModel by viewModels()
    private val adapter by lazy { DonationsAdapter() }
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDonationsBinding.inflate(inflater, container, false)
        val view = binding.root

        setHasOptionsMenu(true)  // Enable options menu for this fragment

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        binding.recyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.getDonations()
        }

        viewModel.donations.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Loading -> {
                    binding.progressCircular.isVisible = true
                }
                is Resource.Success -> {
                    binding.progressCircular.isVisible = false
                    adapter.submitList(state.data)
                }
                is Resource.Error -> {
                    binding.progressCircular.isVisible = false
                    Toast.makeText(requireContext(), "An error occurred", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.donate_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_sort_location -> {
                // Handle sort by location
                sortDonationsByLocation()
                return true
            }
            R.id.action_sort_donation_name -> {
                // Handle sort by donation name
                viewModel.sortDonationsByName()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun sortDonationsByLocation() {
        // Sample code for fetching current location
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    //val userLocation = LatLng(location.latitude, location.longitude)
                    viewModel.sortDonationsByLocation(location)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Location not available",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    requireContext(),
                    "Failed to fetch location: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}