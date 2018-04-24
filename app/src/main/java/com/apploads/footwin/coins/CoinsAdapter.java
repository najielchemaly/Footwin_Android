package com.apploads.footwin.coins;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apploads.footwin.R;

import java.util.List;


public class CoinsAdapter extends RecyclerView.Adapter<CoinsViewHolder> {

    private List<Coin> coinList;
    private Context context;

    public CoinsAdapter(List<Coin> coinList, Context context) {
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

        final Coin coin = coinList.get(position);

        holder.txtTitle.setText(coin.getTitle());
        holder.txtDesc.setText(coin.getDescription());
        holder.txtCoins.setText(coin.getAmount());

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