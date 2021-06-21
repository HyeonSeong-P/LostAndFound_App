package com.example.soseolsil.fragment

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.loader.content.CursorLoader
import androidx.navigation.fragment.findNavController
import com.example.soseolsil.PostListner
import com.example.soseolsil.data.PostData
import com.example.soseolsil.R
import com.example.soseolsil.model.PostDataRepository
import com.example.soseolsil.model.UserDataRepository
import com.example.soseolsil.viewmodel.BoardViewModel
import com.example.soseolsil.viewmodel.BoardViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_write_post.*
import java.io.File

class WritePostFragment: Fragment(), PostListner {
    val GALLERY_CODE = 199
    // [START declare_auth]
    private lateinit var auth: FirebaseAuth
    lateinit var postLocation:Triple<Double,Double,String>
    lateinit var  db:FirebaseFirestore
    lateinit var stor:FirebaseStorage
    lateinit var imagePath:String
    lateinit var repository: PostDataRepository
    lateinit var repository2: UserDataRepository
    lateinit var factory: BoardViewModelFactory
    lateinit var viewModel: BoardViewModel
    lateinit var nickname: String
    private var lostOrFound:Boolean = true
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_write_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //stor = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        repository = PostDataRepository(db)
        repository2 = UserDataRepository(db)
        factory = BoardViewModelFactory(repository,repository2)
        viewModel = ViewModelProvider(requireActivity(),factory).get(
            BoardViewModel::class.java)

        viewModel.postLocation.observe(viewLifecycleOwner,Observer{
            postLocation = it
            location_edit_text.text = postLocation.third
        })
        viewModel.LFmapToPost.observe(viewLifecycleOwner,Observer{
            lostOrFound = it
        })
        viewModel.writeLost.observe(viewLifecycleOwner, Observer {
            lostOrFound = it
            Log.d("D1DD",lostOrFound.toString())
        })

        viewModel.writeFound.observe(viewLifecycleOwner, Observer {
            lostOrFound = it
            Log.d("D2DD",lostOrFound.toString())
        })
        viewModel.initU()

        viewModel.allUserData.observe(viewLifecycleOwner, Observer {

        })

        viewModel.getUserCallBackState().observe(viewLifecycleOwner, Observer {
            Log.d("DDD","asdasd")
            this.loadPage()
        })


        gallery_btn.setOnClickListener {
            stor = Firebase.storage
            var intent = Intent(Intent.ACTION_PICK)
            intent.setType(MediaStore.Images.Media.CONTENT_TYPE)

            startActivityForResult(intent,GALLERY_CODE)
        }
        location_btn_write.setOnClickListener {
            viewModel.LFpostToMap.setValue(lostOrFound)
            findNavController().navigate(R.id.setMapPostFragment)
        }



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == GALLERY_CODE){
            imagePath = getPath(data?.data!!)
            var f:File? = File(imagePath)
            imageView.setImageURI(Uri.fromFile(f))

        }
    }
    fun getPath(uri: Uri):String{
        var proj:Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var cursorLoader = CursorLoader(requireContext(),uri,proj,null,null,null)
        var cursor = cursorLoader.loadInBackground()
        //var c = getContentResolver().query
        var index = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()

        return cursor!!.getString(index!!)
    }
    fun upload(uri:String){
        var downloadUrl1:Uri?
        // 파이어스토리지에 사진 올리기
        // Create a storage reference from our app
        val storageRef = stor.reference
        var file = Uri.fromFile(File(uri))
        val riversRef = storageRef.child("images/${file.lastPathSegment}")
        var uploadTask = riversRef.putFile(file)


        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener {
            //파이어스토리지로 사진 올렸다면 그 url정보를 토대로 파이어스토어로 데이터 올리기기
           var postDTO: PostData =
               PostData()
            riversRef.downloadUrl.addOnSuccessListener {
                    it1 ->
                Log.d("성공?","성공했다!!!!")
                downloadUrl1 = it1
                Log.d("성공2?",downloadUrl1.toString())
                postDTO.imageUrl = downloadUrl1.toString()
                postDTO.thing= thing_edit_text.text.toString()
                postDTO.description = description_edit_text.text.toString()
                postDTO.uid = auth.currentUser?.uid.toString()
                postDTO.nickname = nickname
                postDTO.where = lostOrFound
                postDTO.latitude = postLocation.first
                postDTO.longitude = postLocation.second
                postDTO.location = postLocation.third

                db.collection("post")
                    .add(postDTO)
                    .addOnSuccessListener { documentReference ->
                        Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                        findNavController().navigateUp()
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding document", e)
                    }
            }




            //db.reference.child("post").push().setValue(postDTO)

        }
    }
    override fun loadPage() {
        var user = viewModel.getUser(auth.currentUser?.uid.toString())
        nickname = user?.nickName.toString()
        upload_btn.setOnClickListener {
            upload(imagePath)
        }
    }
}