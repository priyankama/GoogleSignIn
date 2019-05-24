package com.example.signgoogle

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.SignInButton
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.OnCompleteListener


class MainActivity : AppCompatActivity(),GoogleApiClient.OnConnectionFailedListener {
    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    lateinit var mGoogleSignInClient: GoogleApiClient
    lateinit var mGoogleSign :GoogleSignInClient
    lateinit var gso:GoogleSignInOptions
val REQUEST_CODE_SIGN_IN=1

    private var btnLogout: Button? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       val  btnLogin = findViewById<View>(R.id.btnSignIn) as SignInButton
        btnLogout = findViewById(R.id.btnSignOut)

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()

       mGoogleSign = GoogleSignIn.getClient(this,gso)

        btnLogout!!.visibility = View.INVISIBLE
        btnLogin?.setOnClickListener(View.OnClickListener {

           var signInIntent : Intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleSignInClient)
           startActivityForResult(signInIntent,REQUEST_CODE_SIGN_IN)

        })
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==REQUEST_CODE_SIGN_IN){
           val task : GoogleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
           updateUI(task)
        }
    }
    private fun updateUI(account: GoogleSignInResult) {
        val userDetail = findViewById<TextView>(R.id.tvDetail)
        tvDetail.text = "welcome "


        btnSignIn.visibility = View.INVISIBLE
        btnLogout!!.visibility = View.VISIBLE
        btnLogout!!.setOnClickListener{
            view: View? ->

            mGoogleSign.signOut()
                .addOnCompleteListener(this, OnCompleteListener<Void> {
                    // ...

                    btnSignIn.visibility = View.VISIBLE
                    btnLogout!!.visibility = View.INVISIBLE
                    tvDetail.text = " "
                    Toast.makeText(this,"You have successfully logged out !!",Toast.LENGTH_LONG).show()
                })
        }
    }

}
