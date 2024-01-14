package mypackage.h2hub.viewmodel

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import mypackage.h2hub.model.Donation
import mypackage.h2hub.repository.MainRepository
import mypackage.h2hub.utils.Resource
import javax.inject.Inject
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@HiltViewModel
class DonationsViewModel @Inject constructor(
    private val repository: MainRepository,
    private val database: FirebaseFirestore
) : ViewModel() {
    private  val TAG = "DonationsViewModel"
    private val _donations = MutableLiveData<Resource<List<Donation>>>()
    val donations : LiveData<Resource<List<Donation>>> = _donations
    val imageuri = MutableLiveData<String>()
    suspend fun getDonations()  {
        try {
            _donations.value = Resource.Loading
            repository.getDonations { donations ->
                _donations.value = donations
            }
        }
        catch (e: Exception) {
            _donations.value = Resource.Error("Failed to fetch donations: ${e.message}")
        }
    }
    // Function to update the string value
    fun updateStringValue(newValue: String) {
        imageuri.value = newValue
    }

    private val _donate = MutableLiveData<Resource<List<Donation>>>()
    val donate : LiveData<Resource<List<Donation>>> = _donate

    suspend fun donate(donation : Donation){
        _donate.value = Resource.Loading
        repository.donate(donation){ donate ->
            _donate.value = donate
        }
    }

    private val _history = MutableLiveData<Resource<List<Donation>>>()
    val history : LiveData<Resource<List<Donation>>> = _history

    suspend fun getHistory() {
        _history.value = Resource.Loading
            repository.fetchHistory { history ->
            _history.value = history
        }
    }
    fun sortDonationsByName() {
        val currentDonations = _donations.value
        when (currentDonations) {
            is Resource.Success -> {
                //val sortedDonations = currentDonations.data.sortedBy { it.name.orEmpty() }
                val sortedDonations = currentDonations.data.sortedWith(compareBy { it.foodItem.orEmpty().lowercase() })
                _donations.value = Resource.Success(sortedDonations)
            }

            is Resource.Error -> {
                val errorMessage = currentDonations.string
                // Handle the error message appropriately
            }

            is Resource.Loading -> {
                // Handle loading state if needed
            }
            // Handle other states if available
        }
    }
    fun sortDonationsByLocation(location: Location) {
        val currentDonations = _donations.value
        when (currentDonations) {
            is Resource.Success -> {
                val sortedDonations = currentDonations.data.sortedBy { donation ->
                    calculateDistance(location, donation.location)
                }
                _donations.value = Resource.Success(sortedDonations)
            }

            is Resource.Error -> {
                val errorMessage = currentDonations.string
                // Handle the error message appropriately
            }

            is Resource.Loading -> {
                // Handle loading state if needed
            }
            // Handle other states if available
        }
    }
    private fun calculateDistance(userLocation: Location, donationLocation: GeoPoint?): Float {
        if (donationLocation != null) {
            val donorLatLng = LatLng(donationLocation.latitude, donationLocation.longitude)
            val userLatLng = LatLng(userLocation.latitude, userLocation.longitude)

            // Calculate distance using Haversine formula
            val earthRadius = 6371.0  // Earth radius in kilometers

            val latDiff = Math.toRadians(donorLatLng.latitude - userLatLng.latitude)
            val lonDiff = Math.toRadians(donorLatLng.longitude - userLatLng.longitude)

            val a = sin(latDiff / 2) * sin(latDiff / 2) +
                    cos(Math.toRadians(userLatLng.latitude)) * cos(Math.toRadians(donorLatLng.latitude)) *
                    sin(lonDiff / 2) * sin(lonDiff / 2)

            val c = 2 * atan2(sqrt(a), sqrt(1 - a))
            val distance = (earthRadius * c).toFloat()

            return distance * 1000  // Convert to meters
        }

        return Float.MAX_VALUE  // Return a large value for donations with missing location
    }
    /*private val _listener = MutableLiveData<Resource<List<Donation>>>()
    val listener : LiveData<Resource<List<Donation>>> = _listener

     fun listenToChanges(){
        _listener.value = Resource.Loading
        database.collection("Donations").addSnapshotListener { value, error ->
            if (error != null){
                Log.d(TAG, "listenToChanges: ${error.message}")
            }
            if (value != null){
                val donations = ArrayList<Donation>()
                val documents = value.documents
                documents.forEach {
                    val donation = it.toObject(Donation::class.java)
                    if (donation != null){
                        donations.add(donation)
                    }
                }
                _listener.value = Resource.Success(donations)
            }
        }
    }*/
}