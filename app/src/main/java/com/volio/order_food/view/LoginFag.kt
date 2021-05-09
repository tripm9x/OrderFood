package com.volio.order_food.view

import com.volio.order_food.R
import com.volio.order_food.base.BaseFrag
import com.volio.order_food.util.setPreventDoubleClick
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFag : BaseFrag(R.layout.fragment_login) {
    override fun onCreatedView() {
        btnRegister.setPreventDoubleClick {
            gotoFrag(R.id.loginFag,R.id.action_loginFag_to_registerFrag)
        }
        btnLogin.setPreventDoubleClick {
            gotoFrag(R.id.loginFag,R.id.action_loginFag_to_homeFrag)
        }
    }
}