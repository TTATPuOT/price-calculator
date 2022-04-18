package ru.patriotovsky.pricecalculator.adapters

import android.graphics.drawable.AnimationDrawable
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.patriotovsky.pricecalculator.R
import ru.patriotovsky.pricecalculator.databinding.ItemCardBinding
import ru.patriotovsky.pricecalculator.databinding.ItemHeaderBinding
import ru.patriotovsky.pricecalculator.models.Card
import ru.patriotovsky.pricecalculator.models.HeaderFooter

class CardAdapter: RecyclerView.Adapter<CardAdapter.CardHolder>() {
    private val cardsList = ArrayList<Card>()

    inner class PositionTextWatcher(var position: Int, private val isPrice: Boolean): TextWatcher {
        override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
            var value = 0F
            if (charSequence.toString() != "") value = charSequence.toString().toFloat()

            if (isPrice) {
                cardsList[position].price = value
            } else {
                cardsList[position].amount = value
            }
            refreshResults()
        }
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {}
    }

    inner class CardHolder(private val item: View): RecyclerView.ViewHolder(item) {
        private val binding = ItemCardBinding.bind(item)
        private var priceWatcher = PositionTextWatcher(0, true)
        private var amountWatcher = PositionTextWatcher(0, false)
        private var lastCard: Card? = null

        fun bind(card: Card) = with(binding) {
            if (lastCard == null || System.identityHashCode(lastCard) != System.identityHashCode(card)) {
                binding.priceInput.removeTextChangedListener(priceWatcher)
                binding.amountInput.removeTextChangedListener(amountWatcher)

                binding.priceInput.setText("")
                binding.amountInput.setText("")

                binding.priceInput.addTextChangedListener(priceWatcher)
                binding.amountInput.addTextChangedListener(amountWatcher)
            }
            lastCard = card

            val position = cardsList.indexOf(card)
            priceWatcher.position = position
            amountWatcher.position = position

            priceResult.text = card.getFormattedResult()
            amountResult.text = item.context.resources.getString(R.string.result_description)

            animateBackground(card.winner)
        }

        private fun animateBackground(winner: Boolean) = with(binding) {
            val animationIn = AppCompatResources.getDrawable(item.context, R.drawable.card_transition_in)
            val animationOut = AppCompatResources.getDrawable(item.context, R.drawable.card_transition_out)

            if (winner && cardContainer.tag == "winner") return
            if (!winner && cardContainer.tag == null) return

            if (winner) {
                cardContainer.tag = "winner"
                cardContainer.background = animationIn
                amountResult.setTextColor(ContextCompat.getColor(item.context, R.color.white))
            } else {
                cardContainer.tag = null
                cardContainer.background = animationOut
                amountResult.setTextColor(ContextCompat.getColor(item.context, R.color.grey_text))
            }

            val animationDrawable = cardContainer.background as AnimationDrawable
            animationDrawable.setExitFadeDuration(300)
            animationDrawable.start()
        }
    }

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)

        val holder = CardHolder(view)
        //holder.setIsRecyclable(false)

        return holder
    }

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        holder.bind(cardsList[position])
    }

    override fun getItemCount(): Int {
        return cardsList.size
    }

    fun addCard(card: Card) {
        cardsList.add(card)

        notifyItemInserted(cardsList.size - 1)
    }

    fun clearCards() {
        val maxIndex = cardsList.size

        cardsList.clear()

        notifyItemRangeRemoved(0, maxIndex)
    }

    fun refreshResults() {
        var bestIndex = -1
        var bestResult = -1F

        cardsList.forEach { card ->
            card.winner = false

            if (card.result > 0 && (bestResult == -1F || card.result < bestResult)) {
                bestIndex = cardsList.indexOf(card)
                bestResult = card.result
            }
        }

        if (bestIndex > -1) {
            cardsList[bestIndex].winner = true
        }

        notifyDataSetChanged()
    }
}