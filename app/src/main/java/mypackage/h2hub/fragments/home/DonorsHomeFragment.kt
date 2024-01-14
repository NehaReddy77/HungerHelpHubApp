package mypackage.h2hub.fragments.home

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import mypackage.h2hub.R
import mypackage.h2hub.databinding.FragmentDonorsHomeBinding
import javax.inject.Inject

@AndroidEntryPoint
class DonorsHomeFragment : Fragment() {
    private lateinit var binding: FragmentDonorsHomeBinding
    @Inject lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDonorsHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        (activity as AppCompatActivity).supportActionBar?.show()
        //(activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        binding.cardLogout.setOnClickListener {
            auth.signOut()
            findNavController().navigate(R.id.action_donorsHomeFragment_to_loginFragment)
        }
        binding.cardDonate.setOnClickListener {
            //val ir = R.drawable.ic_help
            val photoUri: String = "FAKE"
            val bundle = bundleOf("photoUri" to photoUri)
            findNavController().navigate(R.id.action_donorsHomeFragment_to_donateFragment, bundle)
            //val action = CameraFragmentDirections.actionDonorsHomeFragmentToDonateFragment("FAKE")
            //findNavController().navigate(action)
        }
        binding.cardHistory.setOnClickListener {
            findNavController().navigate(R.id.action_donorsHomeFragment_to_historyFragment)
        }
        binding.cardAboutus.setOnClickListener {
            findNavController().navigate(R.id.action_donorsHomeFragment_to_aboutUsFragment)
        }
        binding.cardContact.setOnClickListener {
            findNavController().navigate(R.id.action_donorsHomeFragment_to_contactUsFragment)
        }
        return view
    }
    // Companion object declaration
    companion object {
        // Properties or functions specific to the companion object
        // For example:
        fun create(): DonorsHomeFragment {
            return DonorsHomeFragment()
        }
    }
    fun drawableToBitmap(drawable: Drawable): Bitmap {
        val bitmap: Bitmap
        if (drawable is BitmapDrawable) {
            val bitmapDrawable = drawable as BitmapDrawable
            if (bitmapDrawable.bitmap != null) {
                return bitmapDrawable.bitmap
            }
        }

        bitmap = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
            Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888) // Single color bitmap will be created of 1x1 pixel
        } else {
            Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
        }

        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

}