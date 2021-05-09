package com.volio.order_food.view

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.volio.order_food.R
import com.volio.order_food.base.BaseFrag
import com.volio.order_food.util.setPreventDoubleClick
import kotlinx.android.synthetic.main.fragment_register.*

private const val TAG = "RegisterFrag"

class RegisterFrag : BaseFrag(R.layout.fragment_register) {

    val db = Firebase.firestore
    val user = hashMapOf(
        "user_name" to "Ada",
        "user_phone" to "0392662642",
        "password" to 12345678
    )

    override fun onCreatedView() {
        btnBackRegister.setPreventDoubleClick {
            onBackPress()
        }

        btnRegister.setPreventDoubleClick {

// Add a new document with a generated ID
            db.collection("users")
                .add(user)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
            onBackPress()
        }

    }
}