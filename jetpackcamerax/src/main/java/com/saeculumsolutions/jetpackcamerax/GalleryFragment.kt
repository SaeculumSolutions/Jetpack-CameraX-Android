package com.saeculumsolutions.jetpackcamerax

import android.app.Activity
import android.content.Intent
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import java.io.File
import java.util.*


/** Fragment used to present the user with a gallery of photos taken */
class GalleryFragment internal constructor() : Fragment() {

    /** AndroidX navigation arguments */
    private val args: GalleryFragmentArgs by navArgs()

    private lateinit var mediaList: MutableList<File>

    /** Adapter class used to present a fragment containing one photo or video as a page */
    inner class MediaPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getCount(): Int = mediaList.size
        override fun getItem(position: Int): Fragment = PhotoFragment.create(mediaList[position])
        override fun getItemPosition(obj: Any): Int = POSITION_NONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Mark this as a retain fragment, so the lifecycle does not get restarted on config change
        retainInstance = true

        // Get root directory of media from navigation arguments
        val rootDirectory = File(args.rootDirectory)

        // Walk through all files in the root directory
        // We reverse the order of the list to present the last photos first
        mediaList = rootDirectory.listFiles { file ->
            EXTENSION_WHITELIST.contains(file.extension.toUpperCase(Locale.ROOT))
        }?.sortedDescending()?.toMutableList() ?: mutableListOf()

        val onBackPressedCallback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                deletePhotoAnGoBack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_gallery, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Checking media files list
        if (mediaList.isEmpty()) {
            view.findViewById<ImageButton>(R.id.delete_button).isEnabled = false
            view.findViewById<ImageButton>(R.id.confirm_button).isEnabled = false
        }

        val resource = mediaList[0].absolutePath?.let { File(it) } ?: R.drawable.progress_animation

        val photo = view.findViewById<ImageView>(R.id.photo)
        Glide.with(photo).load(resource).into(photo)

        // Make sure that the cutout "safe area" avoids the screen notch if any
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // Use extension method to pad "inside" view containing UI using display cutout's bounds
            view.findViewById<ConstraintLayout>(R.id.cutout_safe_area).padWithDisplayCutout()
        }

        // Handle back button press
        view.findViewById<ImageButton>(R.id.back_button).setOnClickListener {
            deletePhotoAnGoBack()
        }

        // Handle share button press
        view.findViewById<ImageButton>(R.id.confirm_button).setOnClickListener {

            // index is 0 because our mediaList is all item which is in that folder and we want operation on last item
            //it will never be null or empty
            //you can modify your logic here
            mediaList.getOrNull(0)?.let { mediaFile -> //media file is click image
                val resultIntent = Intent()
                resultIntent.putExtra(CameraXImage.GET_CLICKED_IMAGE_URI, mediaFile.absoluteFile)
                requireActivity().setResult(Activity.RESULT_OK, resultIntent)
                requireActivity().finish()
            }
        }

        // Handle delete button press
        view.findViewById<ImageButton>(R.id.delete_button).setOnClickListener {
            deletePhotoAnGoBack()
        }
    }

    private fun deletePhotoAnGoBack() {
        // index is 0 because our mediaList is all item which is in that folder and we want operation on last item
        //it will never be null or empty
        //you can modify your logic here
        mediaList.getOrNull(0)?.let { mediaFile ->
            // Delete current photo
            mediaFile.delete()

            // Send relevant broadcast to notify other apps of deletion
            MediaScannerConnection.scanFile(
                requireContext(), arrayOf(mediaFile.absolutePath), null, null)

            // Notify our view pager
            mediaList.removeAt(0)
            // If photos have been deleted, return to camera
            Navigation.findNavController(requireActivity(), R.id.fragment_container).navigateUp()
        }
    }
}
