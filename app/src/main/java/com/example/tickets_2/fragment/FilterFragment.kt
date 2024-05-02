package com.example.tickets_2.fragment

import android.animation.ObjectAnimator
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Spinner
import com.example.tickets_2.R
import com.example.tickets_2.TicketsApplication
import com.example.tickets_2.models.common.FilterDto
import com.example.tickets_2.models.api.KvitkiEventType
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal
import java.util.Calendar
import java.util.Date

/**
 * A simple [Fragment] subclass.
 * Use the [FilterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class FilterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_filter, container, false)
        val calendar = view.findViewById<CalendarView>(R.id.calendar)
        val fromPrice = view.findViewById<EditText>(R.id.fromByn)
        val toPrice = view.findViewById<EditText>(R.id.toByn)
        val eventType = view.findViewById<Spinner>(R.id.eventTypeSpinner)
        filter = FilterDto.createDefault()
        filter.date = Date(calendar.date)
        calendar.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val cal = Calendar.getInstance()
            cal.set(year, month, dayOfMonth)
            filter.date = cal.time
        }
        fromPrice.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val fromPriceString = fromPrice.text.toString()
                try {
                    filter.fromPrice = BigDecimal(fromPriceString)
                } catch (ignored: NumberFormatException) {}
            }
            override fun afterTextChanged(s: Editable?) {}
        })
        toPrice.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val toPriceString = toPrice.text.toString()
                try {
                    filter.toPrice = BigDecimal(toPriceString)
                } catch (ignored: NumberFormatException) {}
            }
            override fun afterTextChanged(s: Editable?) {}
        })
        populateEventTypesView(eventType)
        eventType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position) as String
                filter.eventType = KvitkiEventType.nameToEvent(selectedItem)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment FilterFragment.
         */
        @JvmStatic
        fun newInstance() = FilterFragment()

        lateinit var filter: FilterDto

    }

    private fun zoomView(view: View) {
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 1.5f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 1.5f)
        scaleX.duration = 300
        scaleY.duration = 300
        scaleX.interpolator = AccelerateDecelerateInterpolator()
        scaleY.interpolator = AccelerateDecelerateInterpolator()
        scaleX.repeatCount = 1
        scaleY.repeatCount = 1
        scaleX.repeatMode = ObjectAnimator.REVERSE
        scaleY.repeatMode = ObjectAnimator.REVERSE
        scaleX.start()
        scaleY.start()
    }

    private fun populateEventTypesView(eventType: Spinner) {
        val items = KvitkiEventType.entries.map {
            it.value
        }
        val adapter = ArrayAdapter(TicketsApplication.instance, android.R.layout.simple_spinner_item, items)
        eventType.adapter = adapter
    }

}