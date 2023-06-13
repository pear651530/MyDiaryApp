package com.example.kimochinikki.ui.setting

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.kimochinikki.MainActivity
import com.example.kimochinikki.R
import com.example.kimochinikki.databinding.FragmentSettingBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.yalantis.ucrop.UCrop
import java.io.File
import java.io.IOException


class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private lateinit var now_img: ImageView
    private lateinit var btn_changeimg: Button
    private lateinit var btn_changepassword: Button
    private lateinit var btn_changeusername: Button
    private lateinit var btn_changeuserkey: Button
    private lateinit var wait_sign_btn: Uri
    private var choose_img = 0

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
///////user

        val user = Firebase.auth.currentUser
        user?.let {
            // Name, email address, and profile photo Url
            val user_name = it.displayName
            val user_email = it.email
            val user_photoUrl = it.photoUrl

            // Check if user's email is verified
            val emailVerified = it.isEmailVerified

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            val user_uid = it.uid

        }
////////////
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val btn_sign_out: Button = binding.btnSignOut
        btn_sign_out.setOnClickListener {
            signOut()
        }
        //binding.nowUsername.text = user_email
        now_img = binding.nowImg
        Glide.with(this)
            .load(R.drawable.head_preimg)
            .apply(RequestOptions.circleCropTransform())
            .into(now_img)

        btn_changeimg = binding.btnChangeimg
        btn_changeimg.setOnClickListener {
            openGallery()
        }

        btn_changepassword = binding.btnChangepassword
        btn_changepassword.setOnClickListener {
            changepassword()
        }

        btn_changeusername = binding.btnChangeusername
        btn_changeusername.setOnClickListener {
            changeusername()
        }

        btn_changeuserkey = binding.btnChangeuserkey
        btn_changeuserkey.setOnClickListener {
            changeuserkey()
        }

        return root
    }

    private fun changepassword() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("欲修改的密碼")

        // 创建第一个 EditText 作为第一个输入字段
        val input1 = EditText(requireContext())
        builder.setView(input1)

        // 创建第二个 EditText 作为第二个输入字段
        val input2 = EditText(requireContext())
        builder.setView(input2)

        // 将两个输入字段添加到对话框中
        val layout = LinearLayout(requireContext())
        layout.orientation = LinearLayout.VERTICAL
        layout.addView(input1)
        layout.addView(input2)
        builder.setView(layout)

        // 添加确定按钮
        builder.setPositiveButton("确定") { dialog, which ->
            val userInput = input1.text.toString()
            if (input1.text.toString() == input2.text.toString()) {
                binding.nowPassword.text = userInput

            }else {
                Toast.makeText(requireContext(), "兩次輸入不一致! 請重試一次", Toast.LENGTH_SHORT).show()
            }
        }

        // 添加取消按钮
        builder.setNegativeButton(
            "取消"
        ) { dialog, which -> dialog.cancel() }

        // 创建并显示 AlertDialog
        val dialog = builder.create()
        dialog.show()
    }

    private fun changeusername() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("欲修改的名稱")

        // 创建一个 EditText 作为输入字段
        val input = EditText(requireContext())
        builder.setView(input)

        // 添加确定按钮
        builder.setPositiveButton("确定") { dialog, which ->
            val userInput = input.text.toString()
            binding.nowUsername.text = userInput
        }

        // 添加取消按钮
        builder.setNegativeButton(
            "取消"
        ) { dialog, which -> dialog.cancel() }

        // 创建并显示 AlertDialog
        val dialog = builder.create()
        dialog.show()
    }

    private fun changeuserkey() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("欲修改的日記鎖")

        // 创建第一个 EditText 作为第一个输入字段
        val input1 = EditText(requireContext())
        builder.setView(input1)

        // 创建第二个 EditText 作为第二个输入字段
        val input2 = EditText(requireContext())
        builder.setView(input2)

        // 将两个输入字段添加到对话框中
        val layout = LinearLayout(requireContext())
        layout.orientation = LinearLayout.VERTICAL
        layout.addView(input1)
        layout.addView(input2)
        builder.setView(layout)

        // 添加确定按钮
        builder.setPositiveButton("确定") { dialog, which ->
            val userInput = input1.text.toString()
            if (input1.text.toString() == input2.text.toString()) binding.nowUserkey.text = userInput
            else {
                Toast.makeText(requireContext(), "兩次輸入不一致! 請重試一次", Toast.LENGTH_SHORT).show()
            }
        }

        // 添加取消按钮
        builder.setNegativeButton(
            "取消"
        ) { dialog, which -> dialog.cancel() }

        // 创建并显示 AlertDialog
        val dialog = builder.create()
        dialog.show()
    }

    private fun signOut() {
        AlertDialog.Builder(requireContext())
            .setTitle("即將登出")
            .setMessage(" ")
            .setPositiveButton("確認") { _, _ ->
                Firebase.auth.signOut()
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finish() // 如果需要结束当前Activity，请添加此行
            }
            .setNegativeButton("取消", null)
            .create()
            .show()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            selectedImageUri?.let { uri ->
                cropImage(uri)
                Log.e("pic", uri.toString())
                choose_img = 1
                wait_sign_btn = uri
            }
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == AppCompatActivity.RESULT_OK) {
            val croppedImageUri = UCrop.getOutput(data!!)
            displayCroppedImage(croppedImageUri)
        }
    }

    private fun cropImage(sourceUri: Uri) {
        val destinationUri = Uri.fromFile(File(requireContext().cacheDir, "cropped"))
        val options = UCrop.Options().apply {
            setCircleDimmedLayer(true)
        }

        UCrop.of(sourceUri, destinationUri)
            .withOptions(options)
            .start(requireContext(), this)
    }

    private fun displayCroppedImage(croppedImageUri: Uri?) {
        croppedImageUri?.let { uri ->
            try {
                val bitmap =
                    MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)
                Glide.with(this)
                    .load(bitmap)
                    .apply(RequestOptions.circleCropTransform())
                    .into(now_img)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}