package com.apploads.footwin.helpers;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.apploads.footwin.R;

public class CustomDialogClass extends Dialog {

    public Activity c;
    public Button yes, no;
    private String title = "";
    private String message = "";
    private TextView txtTitle;
    private TextView txtMessage;
    private boolean isSingleButton;
    private AbstractCustomDialogListener listener;

    public CustomDialogClass(Activity a, AbstractCustomDialogListener listener, boolean isSingleButton) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.isSingleButton = isSingleButton;
        this.listener = listener;
        this.listener.setDialog(this);
        this.listener.setDialog(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);

        yes = findViewById(R.id.btn_yes);
        no = findViewById(R.id.btn_no);
        txtTitle = findViewById(R.id.txtTitle);
        txtMessage = findViewById(R.id.txtMessage);
        yes.setOnClickListener(listener);
        no.setOnClickListener(listener);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        txtTitle.setText(title);
        txtMessage.setText(message);

        if(isSingleButton){
            no.setVisibility(View.GONE);
            yes.setText("DONE");
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DialogResponse{
        private Activity activity;
        private Dialog dialog;
        private View btnClicked;

        public DialogResponse(Activity activity, Dialog dialog, View btnClicked) {
            this.activity = activity;
            this.dialog = dialog;
            this.btnClicked = btnClicked;
        }

        public Activity getActivity() {
            return activity;
        }

        public Dialog getDialog() {
            return dialog;
        }

        public View getBtnClicked() {
            return btnClicked;
        }
    }

    private interface CustomDialogListener extends View.OnClickListener{
        void onConfirm(DialogResponse response);
        void onCancel(DialogResponse dialogResponse);
    }

    public static abstract class AbstractCustomDialogListener implements CustomDialogListener{
        private CustomDialogClass dialog;

        public void setDialog(CustomDialogClass dialog) {
            this.dialog = dialog;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_yes:
                  onConfirm(new DialogResponse(dialog.c, dialog, view));
                    break;
                case R.id.btn_no:
                    onCancel(new DialogResponse(dialog.c, dialog, view));
                    break;
                default:
                    break;
            }
        }
    }
}