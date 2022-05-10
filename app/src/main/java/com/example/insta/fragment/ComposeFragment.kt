package com.example.insta.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.insta.Post
import com.example.insta.R
import com.parse.ParseFile
import com.parse.ParseQuery
import com.parse.ParseUser
import com.parse.SaveCallback
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ComposeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ComposeFragment : Fragment() {

    lateinit var etDescription: EditText
    lateinit var ivPic: ImageView

    val TAG = "ComposeFragment"
    val APP_TAG = "Insta"
    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034
    val photoFileName = "photo.jpg"
    lateinit var photoFile: File

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        etDescription = view.findViewById(R.id.etDescription)
        ivPic = view.findViewById(R.id.ivPic)
        ivPic.setOnClickListener{
            Log.d(TAG, "clicked image")
            onLaunchCamera()
        }
        view.findViewById<Button>(R.id.btnSubmit).setOnClickListener {
            Log.d(TAG, "submit button clicked")

            val description = etDescription.text.toString()
            if (photoFile != null) {
                Log.d(TAG, "getCurrentUser: "+ ParseUser.getCurrentUser().username)

                savePost(description, photoFile)
            }
        }
        // or for a more robust offline save
        // todoItem.saveEventually();
    }
    fun onLaunchCamera() {
        // create Intent to take a picture and return control to the calling application
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName)

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        if (photoFile != null) {
            val fileProvider: Uri =
                FileProvider.getUriForFile(requireContext(), "com.codepath.fileprovider", photoFile!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.
            if (intent.resolveActivity(requireContext().packageManager) != null) {
                // Start the image capture intent to take photo
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            val rawImage: Bitmap
            if (resultCode == AppCompatActivity.RESULT_OK) {
                rawImage = BitmapFactory.Options().run {
                    inJustDecodeBounds = true
                    BitmapFactory.decodeFile(photoFile!!.absolutePath)
                }
                // by this point we have the camera photo on disk
//                val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                ivPic.setImageBitmap(Bitmap.createScaledBitmap(rawImage, 200, 200, false))
            } else { // Result was a failure
                Toast.makeText(requireContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    fun getPhotoFileUri(fileName: String): File {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        val mediaStorageDir =
            File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.e(APP_TAG, "failed to create directory")
        }

        // Return the file target for the photo based on filename
        return File(mediaStorageDir.path + File.separator + fileName)
    }

    private fun queryPosts() {
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)

        // By default, when fetching an object, related ParseObjects (pointer to user) are not fetched
        query.include(Post.KEY_AUTHOR)
        query.findInBackground { posts, e ->
            if (e != null) {
                Log.e(TAG, "error in fetching posts")
                e.printStackTrace()
            } else {
                if (posts != null) {
                    for (post in posts) {
                        Log.d(
                            TAG,
                            "done: " + post.getDescription() + " username: " + post.getAuthor()?.username
                        )
                    }
                }
            }
        }
    }

    private fun savePost(description: String, photoFile: File) {
        val newPost = Post()
        newPost.setAuthor(ParseUser.getCurrentUser())
        newPost.setDescription(description)
        val parsefile: ParseFile = ParseFile(photoFile)
        parsefile.saveInBackground(SaveCallback { e ->
            if (e==null){
                Log.d(TAG, "save image: success")
            } else{
                Log.e(TAG, "save image: failed", e)
                e.printStackTrace()
            }
        })
        newPost.setImage(ParseFile(photoFile))
        newPost.saveInBackground { e ->
            if (e==null){
                etDescription.setText("")
                Log.d(TAG, "savePost: success")
            } else{
                Log.e(TAG, "savePost: failed", e)
            }
        }

    }
}