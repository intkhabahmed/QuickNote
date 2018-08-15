package com.intkhabahmed.quicknote.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.intkhabahmed.quicknote.R;

public class ViewUtils {

    public static void showUnsavedChangesDialog(final Context context) {
        DialogInterface.OnClickListener discardButtonListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((Activity) context).finish();
                ((Activity) context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        };
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setPositiveButton(context.getString(R.string.discard), discardButtonListener);
        dialogBuilder.setNegativeButton(context.getString(R.string.keep_editing), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialogBuilder.setMessage(context.getString(R.string.unsaved_changes_dialog_message));
        dialogBuilder.setTitle(context.getString(R.string.unsaved_changes));
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    public static void showDeleteConfirmationDialog(final Context context, DialogInterface.OnClickListener deleteButtonListener) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setPositiveButton(context.getString(R.string.yes), deleteButtonListener);
        dialogBuilder.setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialogBuilder.setMessage(context.getString(R.string.delete_dialog_message));
        dialogBuilder.setTitle(context.getString(R.string.delete_note));
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }
}
