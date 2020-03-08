package com.example.convertapp;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;


import java.text.DecimalFormat;
import java.util.ArrayList;


public class Adapter extends BaseAdapter {

    private Context context;
    private ArrayList<Data> conversions;
    boolean checked_decimal;
    private DecimalFormat df = new DecimalFormat("#.####");

    Adapter(Context context , ArrayList<Data> conversions) {
        this.context = context;
        this.conversions = conversions;
    }

    @Override
    public int getCount() {
        return conversions.size();
    }

    @Override
    public Data getItem(int position) {
        return conversions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final WB wb;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_layout,parent, false);
            wb = new WB();
            wb.type = convertView.findViewById(R.id.type_convert);
            wb.tResult = convertView.findViewById(R.id.tResult);

            convertView.setTag(wb);

        } else {
            wb = (WB) convertView.getTag();
        }

        final Data conversion = getItem(position);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager myClipboard = (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Conversion_label", wb.tResult.getText().toString());
                if (myClipboard != null) {
                    myClipboard.setPrimaryClip(clipData);
                }
                Toast.makeText(v.getContext(), conversion.type+"\n"+"تم النسخ بنجاح" , Toast.LENGTH_SHORT ).show();
            }
        });

        if(checked_decimal)
            wb.tResult.setText(df.format(conversion.result)+"");
        else
            wb.tResult.setText(conversion.result+"");

        wb.type.setText(conversion.type);
        return convertView;
    }

    void listChanged(ArrayList<Data> con) {
        conversions = con;
        notifyDataSetChanged();
    }

    private class WB {
        TextView type,tResult;
    }

}
