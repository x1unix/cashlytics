package com.x1unix.cashlytics.ui.wallet

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.x1unix.cashlytics.R
import com.x1unix.cashlytics.core.payments.Amount
import com.x1unix.cashlytics.core.payments.Wallet
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

/**
 * Wallets list recycle view adapter
 *
 * @param wallets list
 */
class WalletListAdapter(private var wallets: List<Wallet>, var clickListener: (Wallet) -> Unit): RecyclerView.Adapter<WalletListAdapter.WalletViewHolder>() {
    private var fmt: DateTimeFormatter = DateTimeFormat.forPattern("dd MMM YYYY, k:m")

    override fun getItemCount(): Int {
        return this.wallets.size
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): WalletViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.bank_list_item, viewGroup, false)
        return WalletViewHolder(v)
    }

    override fun onBindViewHolder(walletViewHolder: WalletListAdapter.WalletViewHolder, i: Int) {
        val (bankName, status, lastUpdated, description, icon) = wallets[i]

        walletViewHolder.setBankName(bankName)
        walletViewHolder.setDescription(description)
        walletViewHolder.setIcon(icon)
        walletViewHolder.setDate(formatEventDate(lastUpdated))
        walletViewHolder.setAmount(status)
        walletViewHolder.bindEvents(wallets[i], clickListener)
    }

    private fun formatEventDate(dt: LocalDateTime): String {
        return fmt.print(dt)
    }


    class WalletViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val icon = itemView.findViewById<ImageView>(R.id.bankIcon)
        private val bankName = itemView.findViewById<TextView>(R.id.bankName)
        private val description = itemView.findViewById<TextView>(R.id.dataSource)
        private val displayedDate = itemView.findViewById<TextView>(R.id.lastPaymentDate)
        private val amount = itemView.findViewById<TextView>(R.id.amount)
        private val successColor = ContextCompat.getColor(itemView.context, R.color.danger)
        private val dangerColor = ContextCompat.getColor(itemView.context, R.color.success)

        fun setBankName(label: String) {
            bankName.text = label
        }

        fun setDescription(descr: String) {
            description.text = descr
        }

        fun setIcon(resourceId: Int) {
            icon.setImageResource(resourceId)
        }

        fun setAmount(newAmount: Amount) {
            val isNegative = newAmount.isNegative()

            amount.text = newAmount.toString()
            amount.setTextColor(if (isNegative) dangerColor else successColor)
        }

        fun setDate(newDate: String) {
            displayedDate.text = newDate
        }

        fun bindEvents(item: Wallet, listener: (Wallet) -> Unit) = with(itemView) {
            setOnClickListener{ listener(item) }
        }
    }
}