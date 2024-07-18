package com.example.mobilebanking

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import com.example.mobilebanking.interfaces.UserLevelInit
import com.example.mobilebanking.security.generateKey
import com.example.mobilebanking.userdata.UserPrimaryData
import com.example.mobilebanking.utils.MyPreferences
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity: AppCompatActivity(), UserLevelInit {
    private var imageButton: ImageButton? = null
    private var pdfIv: ImageButton? = null
    private var documentationIv: ImageButton? = null
    private var keysIv: ImageButton? = null
    private var rootLayout: View? = null
    private var toastView: View? = null
    private var bottomAppBar: BottomAppBar? = null
    private var bottomNavigationView: BottomNavigationView? = null
    private var qrCodeFAB: FloatingActionButton? = null
    private var nameTv: TextView? = null
    private var nestedScrollView: NestedScrollView? = null
    private var includedLayout: View? = null
    private var toastText: TextView? = null
    private var levelType = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        storeString(this, "Name", "Mannan")

        imageButton = findViewById(R.id.imageButton)
        pdfIv = findViewById(R.id.pdf_iv)
        documentationIv = findViewById(R.id.documentation_iv)
        keysIv = findViewById(R.id.keys_iv)
        rootLayout = findViewById(R.id.root_layout)
        toastView = layoutInflater.inflate(R.layout.toast_customization, findViewById(R.id.toast_customization_parent))
        bottomAppBar = findViewById(R.id.bottomAppBar)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        qrCodeFAB = findViewById(R.id.qrCodeFAB)
        nameTv = findViewById(R.id.name_tv)
        nestedScrollView = findViewById(R.id.nestedScrollView)
        includedLayout = findViewById(R.id.includedLayout)
        toastText = findViewById(R.id.text)
        levelType = getUserLevel()

        val name = UserPrimaryData().firstName + " " + UserPrimaryData().lastName
        nameTv?.text = name
        imageButton?.setOnClickListener {
                val view = layoutInflater.inflate(R.layout.dialog_box, null)
                val builder = AlertDialog.Builder(this)
                builder.setView(view)
                val alertDialog = builder.create()
                alertDialog.show()

                val imageView = view.findViewById<ImageView>(R.id.animated_checkmark)
                imageView.visibility = View.VISIBLE

                val btnOk = view.findViewById<Button>(R.id.btn_ok)
                btnOk.setOnClickListener {
                    val toast = Toast(this)
                    toast.view = toastView
                    toast.duration = Toast.LENGTH_SHORT
                    toast.show()
                }

                val btnCancel = view.findViewById<Button>(R.id.btn_cancel)
                btnCancel.setOnClickListener {
                    alertDialog.dismiss()
                }
        }

        pdfIv?.setOnClickListener {
                val intent = Intent(this, PdfGenerator::class.java)
                startActivity(intent)
            }

        documentationIv?.setOnClickListener {
                val intent = Intent(this, DocumentUpload::class.java)
                startActivity(intent)
                main()
        }

        keysIv?.setOnClickListener() {
            generateKey("Mannan")
            val value = retrieveString(this, "Name")
            Toast.makeText(this, value, Toast.LENGTH_SHORT).show()
        }

//        rootLayout?.setOnTouchListener { v, event ->
//            if (event.action == MotionEvent.ACTION_UP) {
//                v.performClick()
//                count.cancel()
//                count.start()
//            }
//            false
//        }
//
//        bottomAppBar?.setOnTouchListener { v, event ->
//            count.cancel()
//            count.start()
//            false
//        }
//
//        qrCodeFAB?.setOnTouchListener {v, event ->
//            if (event.action == MotionEvent.ACTION_UP) {
//                v.performClick()
//                count.cancel()
//                count.start()
//            }
//            false
//        }
    }

    override fun onResume() {
        super.onResume()
        main()
        secondRequest()
    }
    override fun hasAccessLevel(levelType: String, levelCode: Int): Int {
        this.levelType = levelType
        return levelCode
    }

    override fun getUserLevel(): String {
        return levelType
    }

    private val count: CountDownTimer = object : CountDownTimer(10 * 10 * 10 * 10, 100000) {
        override fun onTick(millisUntilFinished: Long) {
            println("ticking")
        }

        override fun onFinish() {
            Toast.makeText(this@MainActivity, "The IDLE Works", Toast.LENGTH_SHORT).show()
        }
    }

    private fun storeString(context: Context, key: String, value: String) {
        val sharedPref = MyPreferences.getSharedPreferences(context)
        val editor = sharedPref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    private fun retrieveString(context: Context, key: String): String? {
        val sharedPref = MyPreferences.getSharedPreferences(context)
        return sharedPref.getString(key, "")
    }
}


