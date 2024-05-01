package com.example.tickets_2.fragment

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.text.method.KeyListener
import android.util.AttributeSet
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.AdapterView
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Spinner
import com.example.tickets_2.R
import com.example.tickets_2.api.kvitki.KvitkiRestClient
import com.example.tickets_2.api.model.FilterDto
import com.example.tickets_2.models.api.KvitkiEventType
import com.example.tickets_2.service.NotificationService
import com.example.tickets_2.util.CommonUtil
import java.math.BigDecimal
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [FilterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FilterFragment : Fragment() {

    lateinit var filter: FilterDto

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
        filter = FilterDto(Date(calendar.date), BigDecimal(0.0), BigDecimal(10_000),
            KvitkiEventType.THEATER)
        calendar.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val cal = Calendar.getInstance()
            cal.set(year, month, dayOfMonth)
            filter.date = cal.time
        }
        fromPrice.setOnKeyListener { view, keyCode, keyEvent ->
            filter.fromPrice = BigDecimal(fromPrice.text.toString())
            true
        }
        toPrice.setOnKeyListener { view, keyCode, keyEvent ->
            filter.toPrice = BigDecimal(toPrice.text.toString())
            true
        }
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

}