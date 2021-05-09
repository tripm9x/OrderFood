package com.volio.order_food.util

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.volio.order_food.R
import com.volio.order_food.adapter.ChooseColorAdapter
import kotlinx.android.synthetic.main.dialog_alert.*
import kotlinx.android.synthetic.main.dialog_choose_color.*
import kotlinx.android.synthetic.main.dialog_input_text.*
import kotlinx.android.synthetic.main.dialog_loading.*
import kotlinx.android.synthetic.main.dialog_more_note.*


object DialogUtil {
    fun showDialogAlert(
        context: Context,
        content: String,
        onClickOk: () -> Unit,
        onClickCancel: () -> Unit
    ) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_alert)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.tvContentDialog.text = content
        dialog.btnConfirmDialog.setPreventDoubleClickScaleView {
            onClickOk()
            dialog.cancel()
        }
        dialog.btnCancelDialog.setPreventDoubleClickScaleView {
            onClickCancel()
            dialog.cancel()
        }

        dialog.show()

    }

    fun showDialogInput(
        context: Context,
        title: String,
        content: String,
        onClickOk: (content: String) -> Unit,
        onClickCancel: () -> Unit
    ) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_input_text)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.tvDialogInput.text = title
        dialog.tvContentDialogInput.hint = content
        dialog.btnConfirmDialogInput.setPreventDoubleClickScaleView {
            onClickOk(dialog.tvContentDialogInput.text.toString())
            dialog.cancel()
        }
        dialog.btnCancelDialogInput.setPreventDoubleClickScaleView {
            onClickCancel()
            dialog.cancel()
        }

        dialog.show()

    }


    fun showDialogMoreNote(
        context: Context,
        onClickUpdate: () -> Unit,
        onClickDelete: () -> Unit,
        onClickCancel: () -> Unit
    ) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_more_note)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.btnUpdateDiary.setPreventDoubleClickScaleView {
            onClickUpdate()
            dialog.cancel()
        }
        dialog.btnDeleteDiary.setPreventDoubleClickScaleView {
            onClickDelete()
            dialog.cancel()
        }
        dialog.btnCancelMore.setPreventDoubleClickScaleView {
            onClickCancel()
            dialog.cancel()
        }
        dialog.show()
    }

    fun showDialogChooseColor(
        context: Context,
        listColor: MutableList<Int>,
        onClickColor: (position: Int) -> Unit
    ) {
        val dialog = Dialog(context)
        val chooseColorAdapter = ChooseColorAdapter(listColor) {
            onClickColor(it)
            dialog.cancel()
        }
        dialog.setContentView(R.layout.dialog_choose_color)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.rcvChooseImage.layoutManager =
            GridLayoutManager(context, 5, RecyclerView.VERTICAL, false)
        dialog.rcvChooseImage.adapter = chooseColorAdapter
        dialog.btnCancelColor.setPreventDoubleClickScaleView {
            dialog.cancel()
        }
        dialog.show()
    }


    private var dialogLoading: Dialog? = null

    @SuppressLint("SetTextI18n")
    fun showDialogLoading(
        context: Context,
        content: String,
        sideContent: String? = null
    ) {
        dialogLoading = Dialog(context)
        dialogLoading?.setContentView(R.layout.dialog_loading)
        dialogLoading?.setCancelable(false)
        dialogLoading?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialogLoading?.tvLoadingContent?.text = content
        if (!sideContent.isNullOrBlank()) {
            dialogLoading?.tvSideContent?.visibility = View.VISIBLE
            dialogLoading?.tvSideContent?.text = "($sideContent)"
        } else {
            dialogLoading?.tvSideContent?.visibility = View.GONE
        }
        dialogLoading?.show()
    }

    fun cancelDialogLoading() {
        dialogLoading?.cancel()
    }
}