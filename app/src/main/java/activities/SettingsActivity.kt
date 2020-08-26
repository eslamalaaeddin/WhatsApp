package activities

import android.R.attr
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.whatsapp.R
import com.example.whatsapp.databinding.ActivitySettingsBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView

private const val TAG = "SettingsActivity"
private const val GALLERY_PICK_NUMBER = 1
class SettingsActivity : AppCompatActivity() {
    private lateinit var activitySettingsBinding: ActivitySettingsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var rootRef:DatabaseReference
    private lateinit var progressDialog: ProgressDialog
    private lateinit var userProfileImageReference: StorageReference

    private lateinit var currentUser: FirebaseUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        rootRef = FirebaseDatabase.getInstance().reference

        currentUser = auth.currentUser!!

        activitySettingsBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings)

        userProfileImageReference = FirebaseStorage.getInstance().reference.child("Profile images")

        activitySettingsBinding.updateAccountButton.setOnClickListener {
            updateSettings()
        }

        //when clicking the image view

        activitySettingsBinding.userImageView.setOnClickListener {
            val imageIntent = Intent()
            imageIntent.action = Intent.ACTION_GET_CONTENT
            imageIntent.type = "image/*"

            startActivityForResult(imageIntent, GALLERY_PICK_NUMBER)
        }

        retrieveUserInfo()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY_PICK_NUMBER && resultCode == RESULT_OK && data != null) {
            val imageUri = data.data
            CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)

        if (resultCode == RESULT_OK) {

            getLoadingDialog()

            //to get the image uri
            val resultUri = result.uri
            val filePath = userProfileImageReference.child("${currentUser.uid}.jpg")
          filePath.putFile(resultUri).addOnCompleteListener { taskSnapshot ->
                if (taskSnapshot.isSuccessful) {
                    Toast.makeText(
                        this@SettingsActivity,
                        "profile image updated successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    


                   filePath.downloadUrl.addOnCompleteListener {task ->

                       if (task.isSuccessful) {
                           val imageUrl = task.result.toString()
                           rootRef.child("Users").child(currentUser.uid).child("image")
                               .setValue(imageUrl).addOnCompleteListener { task ->

                                   if (task.isSuccessful) {
                                       Toast.makeText(this, "Image added to the DB successfully", Toast.LENGTH_SHORT).show()
                                   } else {
                                       Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                                   }
                               }
                       }

                       else{
                           Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                       }

                       progressDialog.dismiss()
                   }
                    
                    
            

                } else {
                    Toast.makeText(
                        this@SettingsActivity,
                        "profile image updated successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }

        }
     }
    }

    private fun updateSettings() {
        val userName = activitySettingsBinding.userNameEditText.editableText.toString()
        val userStatus = activitySettingsBinding.userStatusEditText.editableText.toString()

        if (userName.isNotEmpty() && userStatus.isNotEmpty()) {
            currentUser = auth.currentUser!!
            val userMap = HashMap<String, Any>()
            userMap.put("uid", currentUser.uid)
            userMap.put("name", userName)
            userMap.put("status", userStatus)

            rootRef.child("Users").child(currentUser.uid).updateChildren(userMap)

                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                        sendUserToMainActivity()
                    }

                    else{
                        Toast.makeText(this, "${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        else{
            Toast.makeText(this, "Invalid user name or status", Toast.LENGTH_SHORT).show()
        }
    }

    private fun retrieveUserInfo() {
        rootRef.child("Users").child(currentUser.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists() && snapshot.hasChild("name") && snapshot.hasChild("status")) {
                        val userName = snapshot.child("name").value.toString()
                        val userStatus = snapshot.child("status").value.toString()
                        val userImageUrl = snapshot.child("image").value.toString()

                        Picasso.get()
                            .load(userImageUrl)
                            .placeholder(R.drawable.dummy_avatar)
                            .into(activitySettingsBinding.userImageView)

                        fillEditTexts(userName, userStatus)
                    } else {
                        Toast.makeText(
                            this@SettingsActivity,
                            "Please update your information",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }


    private fun fillEditTexts(userName: String, userStatus: String) {
        activitySettingsBinding.userNameEditText.setText(userName)
        activitySettingsBinding.userStatusEditText.setText(userStatus)
    }

    private fun sendUserToMainActivity() {
        val mainActivityIntent = Intent(this, MainActivity::class.java)
        //to handle the back button logic
        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(mainActivityIntent)
        finish()
    }

    private fun getLoadingDialog() {
        progressDialog = ProgressDialog(this)
            .also {
                title = "Updating your account"
                it.setMessage("Please be patient, We are Updating your account.")
                it.setCanceledOnTouchOutside(false)
                it.show()
            }
    }
}