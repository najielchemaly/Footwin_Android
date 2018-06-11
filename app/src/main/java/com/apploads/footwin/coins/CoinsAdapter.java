package com.apploads.footwin.coins;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.apploads.footwin.R;
import com.apploads.footwin.model.Package;

import java.util.List;


public class CoinsAdapter extends RecyclerView.Adapter<CoinsViewHolder> {

    private List<Package> coinList;
    private Context context;
    private CoinsActivity activity;

    public CoinsAdapter(List<Package> coinList, Context context, CoinsActivity activity) {
        this.coinList = coinList;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public CoinsViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.coin_package_row_item, null);
        CoinsViewHolder viewHolder = new CoinsViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CoinsViewHolder holder, final int position) {

        final Package aPackage = coinList.get(position);

        holder.txtTitle.setText(aPackage.getTitle());
        holder.txtDesc.setText(aPackage.getDescription());
        holder.txtCoins.setText(aPackage.getCoins() + " COINS");
        holder.txtPrice.setText(aPackage.getPrice() + " $");

        holder.btnPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.purchaseProduct(aPackage);
            }
        });

    }

    @Override
    public int getItemCount() {
        return coinList.size();
    }

}

class CoinsViewHolder extends RecyclerView.ViewHolder {

    TextView txtTitle, txtDesc, txtCoins, txtPrice;
    Button btnPurchase;

    public CoinsViewHolder(View itemLayoutView) {
        super(itemLayoutView);

        txtTitle = itemLayoutView.findViewById(R.id.txtTitle);
        txtDesc = itemLayoutView.findViewById(R.id.txtDesc);
        txtCoins = itemLayoutView.findViewById(R.id.txtCoins);
        txtPrice = itemLayoutView.findViewById(R.id.txtPrice);
        btnPurchase = itemLayoutView.findViewById(R.id.btnPurchase);
    }
}