package mypackage.h2hub.activities

//import mypackage.h2hub.fragments.camera.PhotoListener
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.core.view.WindowCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenCreated
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import mypackage.h2hub.R
import mypackage.h2hub.databinding.ActivityMainBinding
import mypackage.h2hub.repository.RepositoryImpl


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setSupportActionBar(binding.toolbar)

        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navigationView
        navView.setupWithNavController(navController)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.adminHomeFragment,
                R.id.donorsHomeFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id in listOf(R.id.splashFragment, R.id.loginFragment)) {
                supportActionBar?.hide()
            }
            if (destination.id in listOf(
                    R.id.donateFragment,
                    R.id.receiveFragment,
                    R.id.donationsFragment,
                    R.id.foodMapFragment,
                    R.id.historyFragment,
                    R.id.aboutUsFragment,
                )
            ) {
                supportActionBar?.show()
                supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
            }
        }

        val header = binding.navigationView.getHeaderView(0)
        val imageView = header.findViewById<ImageView>(R.id.imageView)
        val userImage = auth.currentUser?.photoUrl
        if (auth.currentUser != null) {
            lifecycleScope.launch {
                whenCreated {
                    RepositoryImpl.getInstance().getCurrentUserEmail {
                        val userEmailText =
                            header.findViewById<android.widget.TextView>(R.id.useremail)
                        userEmailText.text = it
                    }
                }
            }

            Glide
                .with(this)
                .load(userImage)
                .apply(RequestOptions().override(150, 150))
                .placeholder(R.drawable.ic_person)
                .into(imageView)
        }


        binding.navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_help -> {
                    startActivity(Intent(this, HelpActivity::class.java))
                    true
                }
                R.id.action_home -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.action_nutritioninfo-> {
                    startActivity(Intent(this, InfoActivity::class.java))
                    true
                }
                R.id.nav_share -> {
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.type = "text/plain"
                    intent.putExtra(Intent.EXTRA_SUBJECT, "FOODONOR")
                    intent.putExtra(Intent.EXTRA_TEXT, "")
                    startActivity(Intent.createChooser(intent, "Share via"))
                    true
                }
                else -> {
                    false
                }
            }
        }

    }
    override fun onResume() {
        super.onResume()
        setSupportActionBar(binding.toolbar)
        binding.drawerLayout.closeDrawer(GravityCompat.START)
    }
    /*override fun onPhotoReceived(photo: Bitmap) {
        // Handle the received photo from the child fragment here in MainActivity
        // You can pass this photo to the parent fragment or perform necessary actions
        val parentFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main)
        val currentFragment = parentFragment?.childFragmentManager?.fragments?.get(0)
        if (currentFragment is DonateFragment) {
            currentFragment.onPhotoReceived(photo)
        }
        if (currentFragment is CameraFragment) {
            currentFragment.dummy()
        }
    }*/
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}