package com.x1unix.cashlytics.ui.wallet;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.x1unix.cashlytics.R;
import com.x1unix.cashlytics.core.payments.Amount;
import com.x1unix.cashlytics.core.payments.Wallet;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

public class RVWalletListAdapter extends RecyclerView.Adapter<RVWalletListAdapter.WalletViewHolder> {

    private List<Wallet> wallets;

    private DateTimeFormatter fmt;

    RVWalletListAdapter(List<Wallet> wallets) {
        this.wallets = wallets;
        fmt = DateTimeFormat.forPattern("dd MMM YYYY, k:m");
    }

    @Override
    public int getItemCount() {
        return this.wallets.size();
    }

    @Override
    public WalletViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bank_list_item, viewGroup, false);
        WalletViewHolder pvh = new WalletViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(RVWalletListAdapter.WalletViewHolder walletViewHolder, int i) {
        Wallet w = wallets.get(i);

        walletViewHolder.setBankName(w.getBankName());
        walletViewHolder.setDescription(w.getDescription());
        walletViewHolder.setIcon(w.getIcon());
        walletViewHolder.setDate(formatEventDate(w.getLastUpdated()));
        walletViewHolder.setAmount(w.getStatus());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    private String formatEventDate(LocalDateTime dt) {
        return fmt.print(dt);
    }


    public static class WalletViewHolder extends RecyclerView.ViewHolder {
        private ImageView icon;
        private TextView bankName;
        private TextView description;
        private TextView displayedDate;
        private TextView amount;
        private int successColor;
        private int dangerColor;

        WalletViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.bankIcon);
            bankName = (TextView) itemView.findViewById(R.id.bankName);
            description = (TextView) itemView.findViewById(R.id.dataSource);
            displayedDate = (TextView) itemView.findViewById(R.id.lastPaymentDate);
            amount = (TextView) itemView.findViewById(R.id.amount);

            dangerColor = ContextCompat.getColor(itemView.getContext(), R.color.danger);
            successColor = ContextCompat.getColor(itemView.getContext(), R.color.success);
        }

        public void setBankName(String label) {
            bankName.setText(label);
        }

        public void setDescription(String descr) {
            description.setText(descr);
        }

        public void setIcon(int resourceId) {
            icon.setImageResource(resourceId);
        }

        public void setAmount(Amount newAmount) {
            boolean isNegative = newAmount.isNegative();

            amount.setText(newAmount.toString());
            amount.setTextColor(isNegative ? dangerColor : successColor);
        }

        public void setDate(String newDate) {
            displayedDate.setText(newDate);
        }
    }
}
