package com.daikou.p2parking.helper

import android.content.Context
import android.content.DialogInterface
import cn.pedant.SweetAlert.SweetAlertDialog
import cn.pedant.SweetAlert.SweetAlertDialog.OnSweetClickListener
import com.daikou.p2parking.R
import com.daikou.p2parking.base.base.EazySweetAlert

object MessageUtils {

    fun showError(context: Context, title: String?, text: String?) {
        var text = text
        try {
            text = text?.replace("\r\n".toRegex(), "<br />") ?: ""
            val alertDialog: SweetAlertDialog =
                EazySweetAlert(context, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText(title ?: context.getString(R.string.oops))
                    .setContentText(String.format("%s", text))
            alertDialog.setCanceledOnTouchOutside(false)
            alertDialog.show()
        } catch (ex: RuntimeException) {
            ex.printStackTrace()
        }
    }

    fun showError(
        context: Context,
        title: String?,
        text: String?,
        onSweetClickListener: OnSweetClickListener?,
    ) {
        var text = text
        try {
            text = text?.replace("\r\n".toRegex(), "<br />") ?: ""
            val alertDialog: SweetAlertDialog =
                EazySweetAlert(context, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText(title ?: "")
                    .setContentText(text)
                    .setConfirmClickListener(onSweetClickListener)
            alertDialog.setCanceledOnTouchOutside(false)
            alertDialog.show()
        } catch (ex: java.lang.RuntimeException) {
            ex.printStackTrace()
        }
    }

    fun showErrorDismiss(
        context: Context,
        title: String?,
        text: String?,
        onDismissListener: DialogInterface.OnDismissListener?,
    ) {
        var text = text
        try {
            text = text?.replace("\r\n".toRegex(), "<br />") ?: ""
            val alertDialog: SweetAlertDialog =
                EazySweetAlert(context, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText(title ?: "")
                    .setContentText(text)
            alertDialog.setOnDismissListener(onDismissListener)
            alertDialog.setCanceledOnTouchOutside(false)
            alertDialog.show()
        } catch (ex: java.lang.RuntimeException) {
            ex.printStackTrace()
        }
    }

    fun showWarning(context: Context?, title: String?, text: String?, confirmText: String?) {
        var text = text
        try {
            text = text?.replace("\r\n".toRegex(), "<br />") ?: ""
            val alertDialog: SweetAlertDialog =
                EazySweetAlert(context, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(title)
                    .setContentText(text)
                    .setConfirmText(confirmText)
            alertDialog.setCanceledOnTouchOutside(false)
            alertDialog.show()
        } catch (ex: java.lang.RuntimeException) {
            ex.printStackTrace()
        }
    }

    fun showWarning(
        context: Context?,
        title: String?,
        text: String?,
        confirmText: String?,
        onSweetClickListener: OnSweetClickListener?,
    ) {
        var text = text
        try {
            text = text?.replace("\r\n".toRegex(), "<br />") ?: ""
            val alertDialog: SweetAlertDialog =
                EazySweetAlert(context, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(title)
                    .setContentText(text)
                    .setConfirmText(confirmText)
                    .setConfirmClickListener(onSweetClickListener)
            alertDialog.setCanceledOnTouchOutside(false)
            alertDialog.show()
        } catch (ex: java.lang.RuntimeException) {
            ex.printStackTrace()
        }
    }

    fun showWarning(
        context: Context?,
        title: String?,
        text: String?,
        onSweetClickListener: OnSweetClickListener?,
    ) {
        var text = text
        try {
            text = text?.replace("\r\n".toRegex(), "<br />") ?: ""
            val alertDialog: SweetAlertDialog =
                EazySweetAlert(context, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(title)
                    .setContentText(text)
                    .setConfirmClickListener(onSweetClickListener)
            alertDialog.setCanceledOnTouchOutside(false)
            alertDialog.show()
        } catch (ex: java.lang.RuntimeException) {
            ex.printStackTrace()
        }
    }

    fun showWarning(
        context: Context?,
        title: String?,
        text: String?,
        onCancelClickListener: OnSweetClickListener?,
        onSweetClickListener: OnSweetClickListener?,
    ) {
        var text = text
        try {
            text = text?.replace("\r\n".toRegex(), "<br />") ?: ""
            val alertDialog: SweetAlertDialog =
                EazySweetAlert(context, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(title)
                    .setContentText(text)
                    .setCancelClickListener(onCancelClickListener)
                    .setConfirmClickListener(onSweetClickListener)
            alertDialog.setCanceledOnTouchOutside(false)
            alertDialog.show()
        } catch (ex: java.lang.RuntimeException) {
            ex.printStackTrace()
        }
    }

    fun showSuccess(context: Context?, title: String?, text: String?) {
        var text = text
        try {
            text = text?.replace("\r\n".toRegex(), "<br />") ?: ""
            val alertDialog: SweetAlertDialog =
                EazySweetAlert(context, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText(title)
                    .setContentText(text)
            alertDialog.setCanceledOnTouchOutside(false)
            alertDialog.show()
        } catch (ex: java.lang.RuntimeException) {
            ex.printStackTrace()
        }
    }

    fun showSuccess(
        context: Context?,
        title: String?,
        text: String?,
        onSweetClickListener: OnSweetClickListener?,
    ) {
        var text = text
        try {
            text = text?.replace("\r\n".toRegex(), "<br />") ?: ""
            val alertDialog: SweetAlertDialog =
                EazySweetAlert(context, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText(title)
                    .setContentText(text)
                    .setConfirmClickListener(onSweetClickListener)
            alertDialog.setCanceledOnTouchOutside(false)
            alertDialog.show()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun showNormal(context: Context?, title: String?, text: String?) {
        var text = text
        text = text?.replace("\r\n".toRegex(), "<br />") ?: ""
        val alertDialog: SweetAlertDialog =
            EazySweetAlert(context, SweetAlertDialog.NORMAL_TYPE)
                .setTitleText(title)
                .setContentText(text)
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.show()
    }

    fun showSuccess(
        context: Context?,
        title: String?,
        text: String?,
        onSweetClickListener: OnSweetClickListener?,
        onDismissListener: DialogInterface.OnDismissListener?,
    ) {
        var text = text
        try {
            text = text?.replace("\r\n".toRegex(), "<br />") ?: ""
            val alertDialog: SweetAlertDialog =
                EazySweetAlert(context, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText(title)
                    .setContentText(text)
                    .setConfirmClickListener(onSweetClickListener)
            alertDialog.setOnDismissListener(onDismissListener)
            alertDialog.setCanceledOnTouchOutside(false)
            alertDialog.show()
        } catch (ex: java.lang.RuntimeException) {
            ex.printStackTrace()
        }
    }

    fun showSuccess(
        context: Context?,
        title: String?,
        text: String?,
        confirm: String?,
        cancel: String?,
        onSweetClickListener: OnSweetClickListener?,
        onCancelListener: OnSweetClickListener?,
    ) {
        var text = text
        try {
            text = text?.replace("\r\n".toRegex(), "<br />") ?: ""
            val alertDialog: SweetAlertDialog =
                EazySweetAlert(context, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText(title)
                    .setContentText(text)
                    .setConfirmText(confirm)
                    .setCancelText(cancel)
                    .setCancelClickListener(onCancelListener)
                    .setConfirmClickListener(onSweetClickListener)
            alertDialog.setCanceledOnTouchOutside(false)
            alertDialog.show()
        } catch (ex: java.lang.RuntimeException) {
            ex.printStackTrace()
        }
    }

    fun showSuccessDismiss(
        context: Context?,
        title: String?,
        text: String?,
        onDismissListener: DialogInterface.OnDismissListener?,
    ) {
        var text = text
        try {
            text = text?.replace("\r\n".toRegex(), "<br />") ?: ""
            val alertDialog: SweetAlertDialog =
                EazySweetAlert(context, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText(title)
                    .setContentText(text)
            alertDialog.setOnDismissListener(onDismissListener)
            alertDialog.setCanceledOnTouchOutside(false)
            alertDialog.show()
        } catch (ex: java.lang.RuntimeException) {
            ex.printStackTrace()
        }
    }

    fun showConfirm(
        context: Context,
        title: String?,
        text: String?,
        onSweetClickListener: OnSweetClickListener?,
    ) {
        var text = text
        try {
            text = text?.replace("\r\n".toRegex(), "<br />") ?: ""
            val alertDialog: SweetAlertDialog =
                EazySweetAlert(context, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(title)
                    .setContentText(text)
                    .setCancelText(context.getString(R.string.no))
                    .setConfirmText(
                        context.getString(R.string.yes_f)
                    )
                    .setCancelButtonTextColor(R.color.colorGPASPrimaryDark)
                    .showCancelButton(true)
                    .setConfirmClickListener(onSweetClickListener)
            alertDialog.setCanceledOnTouchOutside(false)
            alertDialog.show()
        } catch (ex: java.lang.RuntimeException) {
            ex.printStackTrace()
        }
    }

}