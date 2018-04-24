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

    public CoinsAdapter(List<Package> coinList, Context context) {
        this.coinList = coinList;
        this.context = context;
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
        holder.txtCoins.setText(aPackage.getCoins());

    }

    @Override
    public int getItemCount() {
        return coinList.size();
    }

}

class CoinsViewHolder extends RecyclerView.ViewHolder {

    TextView txtTitle, txtDesc, txtCoins;
    Button btnGetCoins;

    public CoinsViewHolder(View itemLayoutView) {
        super(itemLayoutView);

        txtTitle = itemLayoutView.findViewById(R.id.txtTitle);
        txtDesc = itemLayoutView.findViewById(R.id.txtDesc);
        txtCoins = itemLayoutView.findViewById(R.id.txtCoins);
        btnGetCoins = itemLayoutView.findViewById(R.id.btnGetCoins);
    }
}