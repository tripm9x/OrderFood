package com.volio.order_food.view

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.volio.order_food.R
import com.volio.order_food.base.BaseFrag
import com.volio.order_food.util.setPreventDoubleClick
import kotlinx.android.synthetic.main.fragment_login.*

private const val TAG = "LoginFag"
class LoginFag : BaseFrag(R.layout.fragment_login) {
    val db = Firebase.firestore
    val user = hashMapOf(
        "user_name" to "Ada",
        "user_phone" to "Lovelace",
        "user_name" to 1815
    )
    override fun onCreatedView() {
        db.collection("users")
            .whereEqualTo("user_phone","0392662642")
            .get()
            .addOnSuccessListener { result ->
                Log.e(TAG, "onCreatedView: ${result.size()}" )
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }

        btnRegister.setPreventDoubleClick {
            gotoFrag(R.id.loginFag,R.id.action_loginFag_to_registerFrag)
        }
        btnLogin.setPreventDoubleClick {
            gotoFrag(R.id.loginFag,R.id.action_loginFag_to_homeFrag)
        }
    }
}