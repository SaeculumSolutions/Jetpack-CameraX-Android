package com.saeculumsolutions.jetpackcamerax

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation

private const val PERMISSIONS_REQUEST_CODE = 10
private val PERMISSIONS_REQUIRED = arrayOf(Manifest.permission.CAMERA)

/**
 * The sole purpose of this fragment is to request permissions and, once granted, display the
 * camera fragment to the user.
 */
class PermissionsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!hasPermissions(requireContext())) {
            // Request camera-related permissions
            requestPermissions(PERMISSIONS_REQUIRED, PERMISSIONS_REQUEST_CODE)
        } else {
            // If permissions have already been granted, proceed
            navigateToCamera()
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (PackageManager.PERMISSION_GRANTED == grantResults.firstOrNull()) {
                // Take the user to the success fragment when permission is granted
                navigateToCamera()
            } else {
                //finish activity if permission is deny or permanently deny
                finishActivity()
            }
        }
    }

    private fun finishActivity() {
        lifecycleScope.launchWhenStarted {
            activity?.finish()
        }
    }

    private fun navigateToCamera() {
        lifecycleScope.launchWhenStarted {
            Navigation.findNavController(requireActivity(), R.id.fragment_container).navigate(
                PermissionsFragmentDirections.actionPermissionsToCamera()
            )
        }
    }

    companion object {

        /** Convenience method used to check if all permissions required by this app are granted */
        fun hasPermissions(context: Context) = PERMISSIONS_REQUIRED.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }
}
