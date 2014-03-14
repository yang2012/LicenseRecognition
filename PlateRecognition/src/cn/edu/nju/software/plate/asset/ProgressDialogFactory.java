package cn.edu.nju.software.plate.asset;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressDialogFactory {

	public static ProgressDialog create_progress_dialog(Context context,
			String message) {
		ProgressDialog dialog = new ProgressDialog(context);
		 dialog.setMessage(message);
         dialog.setIndeterminate(true);
         dialog.setCancelable(false);
         return dialog;

	}
}
