package mypackage.h2hub.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import mypackage.h2hub.model.Donation
import mypackage.h2hub.model.User
import mypackage.h2hub.utils.Resource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val database: FirebaseFirestore,
    private val auth: FirebaseAuth,
) : MainRepository {
    private val TAG = "RepositoryImpl"

    companion object{
        @Volatile
        private var instance: RepositoryImpl? = null
        fun getInstance() = instance ?: synchronized(this) {
            instance ?: RepositoryImpl(
                FirebaseFirestore.getInstance(),
                FirebaseAuth.getInstance()
            ).also { instance = it }
        }
    }
    override suspend fun getDonations() {

        database.collection("donations")
            .get()
            .addOnSuccessListener { snapshot ->
                val donations = ArrayList<Donation>()
                for (data in snapshot.documents) {
                    val donation = data.toObject(Donation::class.java)
                    if (donation != null) {
                        donations.add(donation)
                    }
                }
            }
    }

    override suspend fun getDonations(result: (Resource<List<Donation>>) -> Unit) {

        database.collection("donations")
            .get()
            .addOnSuccessListener { snapshot ->
                val donations = ArrayList<Donation>()
                for (data in snapshot.documents) {
                    val donation = data.toObject(Donation::class.java)
                    if (donation != null) {
                        donations.add(donation)
                    }
                }
                result.invoke(
                    Resource.Success(donations)
                )
            }
            .addOnFailureListener {
                result.invoke(
                    Resource.Error(it.message.toString())
                )
            }

    }

    override suspend fun donate(donation: Donation, result: (Resource<List<Donation>>) -> Unit) {
        val donationId = donation.donorId!!
        database.collection("donations").document(donationId).set(donation)
            .addOnSuccessListener {
                result.invoke(
                    Resource.Success(listOf(donation))
                )
            }
            .addOnFailureListener {
                result.invoke(
                    Resource.Error(it.message.toString())
                )
            }
    }

    override suspend fun fetchHistory(result: (Resource<List<Donation>>) -> Unit) {
        //val user = FirebaseAuth.getInstance().currentUser
        val userId = auth.currentUser!!.uid
        database.collection("donations").whereEqualTo("id", userId)
            .get()
            .addOnSuccessListener { snapshot ->
                val donations = ArrayList<Donation>()
                for (data in snapshot.documents) {
                    val donation = data.toObject(Donation::class.java)
                    if (donation != null) {
                        donations.add(donation)
                    }
                }
                result.invoke(
                    Resource.Success(donations)
                )
            }
            .addOnFailureListener {
                result.invoke(
                    Resource.Error(it.message.toString())
                )
            }
    }

    override suspend fun udpateDonation(
        donation: Donation,
        data: HashMap<String,Any>,
        result: (Resource<List<Donation>>) -> Unit
    ) {
        database.collection("donation")
            .document(donation.donorId!!)
            .update(data)
            .addOnSuccessListener {
                result.invoke(
                    Resource.Success(listOf(donation))
                )
            }
            .addOnFailureListener {
                result.invoke(
                    Resource.Error(it.message.toString())
                )
            }
    }

    override suspend fun getAllUsersTotalNumber(result: (Resource<Int>) -> Unit) {
        database.collection("users")
            .get()
            .addOnSuccessListener { snapshot ->
                val users = ArrayList<User>()
                for (data in snapshot.documents) {
                    val user = data.toObject(User::class.java)
                    if (user != null) {
                        users.add(user)
                    }
                }
                result.invoke(
                    Resource.Success(users.size)
                )
            }
            .addOnFailureListener {
                result.invoke(
                    Resource.Error(it.message.toString())
                )
            }
    }

    override suspend fun getTotalDonations(result: (Resource<Int>) -> Unit) {
        database.collection("donations")
            .get()
            .addOnSuccessListener { snapshot ->
                val donations = ArrayList<Donation>()
                for (data in snapshot.documents) {
                    val donation = data.toObject(Donation::class.java)
                    if (donation != null) {
                        donations.add(donation)
                    }
                }
                result.invoke(
                    Resource.Success(donations.size)
                )
            }
            .addOnFailureListener {
                result.invoke(
                    Resource.Error(it.message.toString())
                )
            }
    }

    override suspend fun getTotalDonors(result: (Resource<Int>) -> Unit) {
        database.collection("users")
            .whereEqualTo("user_type", "Restaurant")
            .get()
            .addOnSuccessListener { snapshot ->
                val donations = ArrayList<Donation>()
                for (data in snapshot.documents) {
                    val donation = data.toObject(Donation::class.java)
                    if (donation != null) {
                        donations.add(donation)
                    }
                }
                result.invoke(
                    Resource.Success(donations.size)
                )
            }
            .addOnFailureListener {
                result.invoke(
                    Resource.Error(it.message.toString())
                )
            }
    }

    override suspend fun getAllDonations(result: (Resource<List<Donation>>) -> Unit) {
        database.collection("donations")
            .get()
            .addOnSuccessListener { snapshot ->
                val donations = ArrayList<Donation>()
                for (data in snapshot.documents) {
                    val donation = data.toObject(Donation::class.java)
                    if (donation != null) {
                        donations.add(donation)
                    }
                }
                result.invoke(
                    Resource.Success(donations)
                )
            }
            .addOnFailureListener {
                result.invoke(
                    Resource.Error(it.message.toString())
                )
            }
    }

    override suspend fun getAllUsers(result: (Resource<List<User>>) -> Unit) {
        database.collection("users")
            .get()
            .addOnSuccessListener { snapshot ->
                val users = ArrayList<User>()
                for (data in snapshot.documents) {
                    val user = data.toObject(User::class.java)
                    if (user != null) {
                        users.add(user)
                    }
                }
                result.invoke(
                    Resource.Success(users)
                )
            }
            .addOnFailureListener {
                result.invoke(
                    Resource.Error(it.message.toString())
                )
            }
    }

    override suspend fun deleteUser(userId: String, result: (Resource<String>) -> Unit) {
        database.collection("users")
            .document(userId)
            .delete()
            .addOnSuccessListener {
                result.invoke(Resource.Success("User deleted"))
            }
            .addOnFailureListener {
                result.invoke(Resource.Error(it.message.toString() ))
            }
    }

    override suspend fun deleteDonation(donationId: String, result: (Resource<String>) -> Unit) {
        database.collection("donations")
            .document(donationId)
            .delete()
            .addOnSuccessListener {
                result.invoke(Resource.Success("Deleted Successfully"))
            }
            .addOnFailureListener {
                result.invoke(Resource.Error(it.message.toString()))
            }
    }

    override suspend fun getUserId(email: String, result: (String) -> Unit) {
       val query =  database.collection("users")
            .whereEqualTo("email", email)
            .get()
            .await()
        for (i in query.documents){
            val id = i.id
            result.invoke(id)
        }
    }

    override suspend fun getCurrentUserEmail(result: (String) -> Unit) {
        database.collection("users")
            .document(auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener { snapshot ->
                val user = snapshot.toObject(User::class.java)
                if (user != null) {
                    result.invoke(user.email!!)
                }
            }
            .addOnFailureListener {
                Log.e(TAG, "Error: ${it.message}")
            }
    }
}