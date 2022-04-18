package ru.patriotovsky.pricecalculator

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent.setEventListener
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import ru.patriotovsky.pricecalculator.adapters.CardAdapter
import ru.patriotovsky.pricecalculator.databinding.ActivityMainBinding
import ru.patriotovsky.pricecalculator.models.Card


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val adapter = CardAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter.setHasStableIds(true)

        init()
        addCardToAdapter()
        addCardToAdapter()
    }

    private fun init() = with(binding) {
        cards.layoutManager = LinearLayoutManager(this@MainActivity)
        cards.adapter = adapter

        setEventListener(
            this@MainActivity,
            KeyboardVisibilityEventListener { shown ->
                if (shown) {
                    copyrights.visibility = View.GONE
                } else {
                    copyrights.visibility = View.VISIBLE
                }
            })

        refresh.setOnRefreshListener {
            onRefresh()
            refresh.isRefreshing = false
        }

        copyrights.movementMethod = LinkMovementMethod.getInstance()
    }

    fun onAddCard(view: View) {
        addCardToAdapter()

        binding.cards.smoothScrollToPosition(adapter.itemCount - 1)
    }

    private fun onRefresh() {
        adapter.clearCards()

        addCardToAdapter()
        addCardToAdapter()

        adapter.notifyDataSetChanged()
    }

    private fun addCardToAdapter() {
        adapter.addCard(Card(0F, 0F))
    }
}