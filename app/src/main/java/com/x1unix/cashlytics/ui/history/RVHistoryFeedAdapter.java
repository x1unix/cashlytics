package com.x1unix.cashlytics.ui.history;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.x1unix.cashlytics.R;
import com.x1unix.cashlytics.core.payments.Amount;
import com.x1unix.cashlytics.core.payments.PaymentEvent;
import com.x1unix.cashlytics.core.payments.PaymentMetadata;
import com.x1unix.cashlytics.core.payments.PaymentType;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Transaction events recycle view adapter.
 *
 * Done in Java, cuz shitty Kotlinx doesn't want to recognise views from view.* package.
 * I've spent 5 hours trying to get it work, but now is 7AM and I want to sleep.
 * Java is stablest solution for this shit.
 */
public class RVHistoryFeedAdapter extends RecyclerView.Adapter<RVHistoryFeedAdapter.EventViewHolder> {

    private List<PaymentEvent> events;

    private NumberFormat currencyFormatter;

    private DateTimeFormatter fmt;

    RVHistoryFeedAdapter(List<PaymentEvent> events) {
        this.events = events;
        currencyFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault());
        fmt = DateTimeFormat.forPattern("dd MMM YYYY, k:m");
    }

    @Override
    public int getItemCount() {
        return this.events.size();
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.transaction_list_item, viewGroup, false);
        EventViewHolder pvh = new EventViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(EventViewHolder eventViewHolder, int i) {
        PaymentEvent event = events.get(i);
        PaymentMetadata metadata = event.getMetadata();
        PaymentType pt = metadata.getType();

        eventViewHolder.setReceiver(metadata.getReceiver());

        // Set icon and human readable payment type
        eventViewHolder.setIcon(getCardIconResource(pt));
        eventViewHolder.setPaymentTypeLabel(getPaymentTypeLabel(pt));

        String amount = formatChargedAmount(event.getChanges().getCharged(), pt);
        eventViewHolder.setChangedAmount(amount, amount.startsWith("-"));

        eventViewHolder.setDate(formatEventDate(event.getDate()));

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    private String formatEventDate(LocalDateTime dt) {
        return fmt.print(dt);
    }

    private String formatChargedAmount(Amount amount, PaymentType type) {
        double saldo = amount.getAmount();

        if (type != PaymentType.Refill) {
            saldo = saldo * -1;
        }

        return Double.toString(saldo) + " " + amount.getCurrency();
//        currencyFormatter.setCurrency(Currency.getInstance(amount.getCurrency()));
//        return currencyFormatter.format(saldo);
    }

    /**
     * Gets resource id of string represents payment type description
     * @param pt payment type
     * @return
     */
    private int getPaymentTypeLabel(PaymentType pt) {
        switch (pt) {
            case Internet:
                return R.string.pt_internet;
            case Purchase:
                return R.string.pt_purchase;
            case Transfer:
                return R.string.pt_transfer;
            case Withdrawal:
                return R.string.pt_withdrawal;
            case Refill:
                return R.string.pt_refill;
            case Debit:
                return R.string.pt_debit;
            default:
                return R.string.pt_unknown;
        }
    }

    /**
     * Gets resource id of view's icon for specific payment type
     * @param pt payment type
     * @return
     */
    private int getCardIconResource(PaymentType pt) {
        switch (pt) {
            case Transfer:
                return R.drawable.ic_pa_transfer;
            case Internet:
                return R.drawable.ic_pa_web;
            case Purchase:
                return R.drawable.ic_pa_purchase;
            case Withdrawal:
                return R.drawable.ic_pa_withdrawal;
            default:
                return R.drawable.ic_pa_common;
        }
    }


    public static class EventViewHolder extends RecyclerView.ViewHolder {
        private ImageView icon;
        private TextView receiver;
        private TextView paymentType;
        private TextView displayedDate;
        private TextView changedAmount;
        private int successColor;
        private int dangerColor;

        EventViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.paymentTypeIcon);
            receiver = (TextView) itemView.findViewById(R.id.paymentReceiver);
            paymentType = (TextView) itemView.findViewById(R.id.paymentType);
            displayedDate = (TextView) itemView.findViewById(R.id.paymentDate);
            changedAmount = (TextView) itemView.findViewById(R.id.amount);

            dangerColor = ContextCompat.getColor(itemView.getContext(), R.color.danger);
            successColor = ContextCompat.getColor(itemView.getContext(), R.color.success);
        }

        public void setReceiver(String label) {
            receiver.setText(label);
        }

        public void setPaymentTypeLabel(int resourceId) {
            paymentType.setText(resourceId);
        }

        public void setIcon(int resourceId) {
            icon.setImageResource(resourceId);
        }

        public void setChangedAmount(String newAmount, boolean isNegative) {
            changedAmount.setText(newAmount);
            changedAmount.setTextColor(isNegative ? dangerColor : successColor);
        }

        public void setDate(String newDate) {
            displayedDate.setText(newDate);
        }
    }
}
