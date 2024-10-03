package com.example.kotlintaskly

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import java.time.LocalDate


class GraphOptionsFragment : Fragment(), View.OnClickListener{

    private val viewModel by activityViewModels<TaskVModel>()
    // pie chart documentation https://weeklycoding.com/mpandroidchart-documentation/
    private lateinit var pieChart: PieChart
    private var dateText: TextView? = null
    private var date = LocalDate.now()
    private var total = 0f;
    private var left: ImageButton? = null
    private var right: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_graph_options, container, false)

        left = view.findViewById(R.id.back)
        right = view.findViewById(R.id.forward)

        left!!.setOnClickListener(this)
        right!!.setOnClickListener(this)


        dateText = view.findViewById(R.id.date)
        dateText!!.text = date.toString()

        pieChart = view.findViewById(R.id.pieChart);
        pieChart.animateY(1000, Easing.EaseInOutQuad)
        pieChart.setHoleRadius(40f)
        pieChart.setTransparentCircleRadius(45f)
        pieChart.setExtraOffsets(5f, 5f, 5f, 5f)

        val entries: ArrayList<PieEntry> = ArrayList()
        entries.add(PieEntry(0f, "Added"))
        entries.add(PieEntry(0f, "Started"))
        entries.add(PieEntry(0f, "Completed"))

        val colors: ArrayList<Int> = ArrayList()

        viewModel.countDayLocation(date.toString())

        viewModel.countsReady.observe(viewLifecycleOwner) {
            total = viewModel.addedCount.value!!.toFloat() + viewModel.startedCount.value!!.toFloat() + viewModel.completedCount.value!!.toFloat()
            entries.clear()
            colors.clear()

            if(viewModel.addedCount.value!! != 0) {
                val addedPie = PieEntry(viewModel.addedCount.value!!.toFloat() / total, "Added")
                colors.add(resources.getColor(R.color.gray))
                entries.add(addedPie)
            }
            if(viewModel.startedCount.value!! != 0) {
                val startedPie = PieEntry(viewModel.startedCount.value!!.toFloat() / total, "Started")
                colors.add(resources.getColor(R.color.lightBlue))
                entries.add(startedPie)
            }
            if(viewModel.completedCount.value!! != 0) {
                val completedPie = PieEntry(viewModel.completedCount.value!!.toFloat() / total, "Completed")
                colors.add(resources.getColor(R.color.lightForestGreen))
                entries.add(completedPie)
            }

            pieChart.invalidate()
            pieChart.notifyDataSetChanged()
        }

        if(entries.isNotEmpty()) {
            val set = PieDataSet(entries, "")
            set.colors = colors
            set.valueTextSize = 0f
            set.sliceSpace = 5f
            val data = PieData(set)
            pieChart.data = data
        }


        pieChart.centerText = "Daily Task Breakdown"
        pieChart.setCenterTextSize(15f)
        pieChart.getDescription().setEnabled(false)
        pieChart.legend.textSize = 15f

        pieChart.setNoDataText("Go add some tasks!")
        pieChart.setNoDataTextColor(R.color.black)

        pieChart.invalidate()

        return view
    }

    override fun onClick(p0: View?) {
        when(p0!!.id) {
            R.id.back -> {
                date = date.minusDays(1)
                viewModel.countDayLocation(date.toString())
                dateText!!.text = date.toString()
                Log.d("Change Date", "Move Back")
            }
            R.id.forward -> {
                date = date.plusDays(1)
                viewModel.countDayLocation(date.toString())
                dateText!!.text = date.toString()
                Log.d("Change Date", "Move forward")
            }
        }
    }

}