package com.e.auth

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class AdminActivity : AppCompatActivity() {

    //XML Elements
    private lateinit var loadPdf: MaterialButton
    private lateinit var loadImg: MaterialButton
    private lateinit var mFileTitle: EditText
    private lateinit var mFileAuthor: EditText
    private lateinit var mFileDescription: EditText
    private lateinit var addFile: MaterialButton
    private lateinit var adminSignOut: MaterialButton
    private lateinit var progressBar: ProgressBar

    //Pick request
    val PDF_REQUEST_CODE: Int = 1
    val IMG_REQUEST_CODE: Int = 2

    //Firebase Storage and Database Reference
    private lateinit var storageRef: StorageReference
    private lateinit var databaseRef: DatabaseReference

    //Uniform Resourse Identifier
    lateinit var pdfURI: Uri
    private lateinit var imgURI: Uri




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        init()



        storageRef = FirebaseStorage.getInstance().getReference("Uploads")
        databaseRef = FirebaseDatabase.getInstance().getReference("Uploads")

        loadPdf.setOnClickListener {
            val intent = Intent()
            intent.setType("application/pdf")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, "Select File"), PDF_REQUEST_CODE)
        }

        loadImg.setOnClickListener {
            val intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, "Select Image"), IMG_REQUEST_CODE)
        }

        addFile.setOnClickListener {
            upload()
        }

        adminSignOut.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == PDF_REQUEST_CODE && resultCode == RESULT_OK){
            pdfURI = data?.data!!
        }else if(requestCode == IMG_REQUEST_CODE && resultCode == RESULT_OK){
            imgURI = data?.data!!
        }
        super.onActivityResult(requestCode, resultCode, data)

    }

    fun init(){
        loadPdf = findViewById(R.id.pdfBTN)
        loadImg = findViewById(R.id.thumbBTN)
        mFileTitle = findViewById(R.id.titleET)
        mFileAuthor = findViewById(R.id.authorET)
        mFileDescription = findViewById(R.id.descriptionET)
        addFile = findViewById(R.id.addFileBTN)
        adminSignOut = findViewById(R.id.adminSignOutBTN)
        progressBar = findViewById(R.id.adminPanelPB)
    }

    fun getFileExtension(uri: Uri): String? {
        val contResolver: ContentResolver = contentResolver
        val mimeTypeMap: MimeTypeMap = MimeTypeMap.getSingleton()

        return mimeTypeMap.getExtensionFromMimeType(contResolver.getType(uri))
    }

    fun upload(){

        progressBar.visibility = View.VISIBLE
        val sRef = storageRef.child(System.currentTimeMillis().toString() + "." + getFileExtension(pdfURI))
        sRef.putFile(pdfURI)
                .addOnSuccessListener(object : OnSuccessListener<UploadTask.TaskSnapshot>{
                    override fun onSuccess(p1: UploadTask.TaskSnapshot?) {
                        val sRef2 = storageRef.child(System.currentTimeMillis().toString() + "." + getFileExtension(imgURI))
                        sRef2.putFile(imgURI).addOnSuccessListener(object : OnSuccessListener<UploadTask.TaskSnapshot>{
                            override fun onSuccess(p0: UploadTask.TaskSnapshot?) {
                                Toast.makeText(applicationContext, "Upload success!", Toast.LENGTH_SHORT).show()
                                sRef.downloadUrl.addOnSuccessListener(object : OnSuccessListener<Uri>{
                                    override fun onSuccess(download1: Uri?) {
                                        sRef2.downloadUrl.addOnSuccessListener(object : OnSuccessListener<Uri>{
                                            override fun onSuccess(download2: Uri?) {
                                                val filePDF = download1.toString()
                                                val fileIMG = download2.toString()
                                                val fileTitle = mFileTitle.text.toString().trim()
                                                val fileAuthor = mFileAuthor.text.toString().trim()
                                                val fileDescription = mFileDescription.text.toString().trim()

                                                val fileData = FileData(filePDF, fileIMG, fileTitle, fileAuthor, fileDescription)

                                                val uploadID: String = databaseRef.push().key!!
                                                databaseRef.child(uploadID).setValue(fileData)
                                                progressBar.visibility = View.GONE
                                            }
                                        })
                                    }

                                })




                            }

                        }).addOnFailureListener{
                            progressBar.visibility = View.GONE
                            Toast.makeText(applicationContext, "Upload Error!", Toast.LENGTH_SHORT).show()
                        }
                    }

                }).addOnFailureListener{
                    progressBar.visibility = View.GONE
                    Toast.makeText(applicationContext, "Upload Error!", Toast.LENGTH_SHORT).show()
                }


    }
}