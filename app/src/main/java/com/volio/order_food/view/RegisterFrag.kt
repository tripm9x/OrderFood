package com.volio.order_food.view

import com.volio.order_food.R
import com.volio.order_food.base.BaseFrag
import com.volio.order_food.util.setPreventDoubleClick
import kotlinx.android.synthetic.main.fragment_register.*


class RegisterFrag : BaseFrag(R.layout.fragment_register) {
    override fun onCreatedView() {
        btnBackRegister.setPreventDoubleClick {
            onBackPress()
        }

        btnRegister.setPreventDoubleClick {
            onBackPress()
        }

    }
}