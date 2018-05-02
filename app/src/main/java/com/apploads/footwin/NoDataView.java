package com.apploads.footwin;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NoDataView extends LinearLayout {

    private String message;
    TextView txtNoData;

    public NoDataView(Context context, String message) {
        super(context);
        this.message = message;

        initView();
    }

    public NoDataView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NoDataView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public NoDataView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initView(){
        inflate(getContext(), R.layout.no_data_layout, this);

        txtNoData = findViewById(R.id.txtNoData);
        txtNoData.setText(message);
    }
}
