package mypackage.h2hub.fragments.donate

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.GeoPoint
import dagger.hilt.android.AndroidEntryPoint
import mypackage.h2hub.R
import mypackage.h2hub.databinding.FragmentDonorLocationBinding

@AndroidEntryPoint
class DonorLocationFragment : Fragment(), OnMapReadyCallback,
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    LocationListener {
    private lateinit var binding: FragmentDonorLocationBinding
    private val args: DonorLocationFragmentArgs by navArgs()
    private val REQUEST_CODE = 1
    private lateinit var mMap: GoogleMap
    private lateinit var mGoogleApiClient: GoogleApiClient
    private lateinit var mLastLocation: Location
    private lateinit var mLocationRequest: LocationRequest
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var auth: FirebaseAuth
    private lateinit var userID: String
    private lateinit var destinationLocation : GeoPoint

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDonorLocationBinding.inflate(inflater, container, false)
        val view = binding.root
        auth = FirebaseAuth.getInstance()
        userID = auth.currentUser!!.uid

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.google_map) as? SupportMapFragment
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mapFragment?.getMapAsync(this)
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE
            )
        }
        val fab = view.findViewById<FloatingActionButton>(R.id.fab)
        val args = DonorLocationFragmentArgs.fromBundle(requireArguments())

        // Retrieve latitude and longitude values
        val receivedLat: Float = args.latitude
        val receivedLong: Float = args.longitude
        destinationLocation = GeoPoint(receivedLat.toDouble(), receivedLong.toDouble())

        /*val donation: Donation? = arguments?.getParcelable("donation")
        if (donation != null) {
            destinationLocation = donation.location!!
        }*/
        /*parentFragmentManager.setFragmentResultListener("donation", this) { _, bundle ->
            val receivedDonation = bundle.getParcelable<Donation>("donation")
            if (receivedDonation != null && receivedDonation.location != null) {
                destinationLocation = receivedDonation.location!!
            }
            else{

            }
            // Use receivedPhotoUri here
        }*/

        fab.setOnClickListener {
            // Replace "latitude" and "longitude" with the actual coordinates of your destination
            val destinationLatitude = destinationLocation.latitude
            val destinationLongitude = destinationLocation.longitude

            val uri = Uri.parse("google.navigation:q=$destinationLatitude,$destinationLongitude")
            val mapIntent = Intent(Intent.ACTION_VIEW, uri)
            mapIntent.setPackage("com.google.android.apps.maps")

            if (mapIntent.resolveActivity(requireContext().packageManager) != null) {
                startActivity(mapIntent)
            } else {
                // Google Maps is not installed, prompt the user to install it
                Toast.makeText(
                    context,
                    "Google Maps is not installed. Install it from the Play Store.",
                    Toast.LENGTH_LONG
                ).show()

                // Redirect the user to Google Maps on the Play Store
                val playStoreIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=com.google.android.apps.maps")
                )
                startActivity(playStoreIntent)
            }
        }
        return view
    }

    @Synchronized
    protected fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(requireContext())
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
        mGoogleApiClient.connect()
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        buildGoogleApiClient()
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mMap.isMyLocationEnabled = true
    }

    override fun onConnected(p0: Bundle?) {
        mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
            mGoogleApiClient,
            mLocationRequest,
            this
        )
    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    override fun onLocationChanged(p0: Location) {
        mLastLocation = p0
        val latLng = LatLng(p0.latitude, p0.longitude)
        val markerOptions = MarkerOptions().position(latLng).title("You are here")

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        mMap.addMarker(markerOptions)!!.showInfoWindow()

        val location = destinationLocation

        val latLng2 = location?.let {
            location?.longitude?.let { it1 ->
                LatLng(
                    it.latitude,
                    it1
                )
            }
        }
        val markerOptions2 = latLng2?.let {
            MarkerOptions().position(it).title("Donor").icon(
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
            )
        }
        latLng2?.let { CameraUpdateFactory.newLatLngZoom(it, 15f) }?.let { mMap.animateCamera(it) }
        markerOptions2?.let { mMap.addMarker(it) }!!.showInfoWindow()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.isNotEmpty() && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                mapFragment.getMapAsync(this)
            } else {
                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

}