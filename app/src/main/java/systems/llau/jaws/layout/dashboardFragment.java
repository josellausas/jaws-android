package systems.llau.jaws.layout;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import systems.llau.jaws.LLau.LLContact;
import systems.llau.jaws.LLau.LLMessage;
import systems.llau.jaws.LLau.LLOrganization;
import systems.llau.jaws.LLau.LLOutcome;
import systems.llau.jaws.LLau.LLTask;
import systems.llau.jaws.LLau.LLUser;
import systems.llau.jaws.R;

/**
 * Shows the Dashboard for the app
 * Created by pp on 12/30/15.
 */
public class DashboardFragment extends Fragment implements
        OnChartValueSelectedListener
{
    private PieChart pieChart;      /** A chart */
    private Typeface tf;            /** Our font */

    /**
     * Entry point for the Fragment
     * @param inflater  The layout inflater
     * @param container The parent cointainer
     * @param savedInstane  The bundle instance
     * @return  The rootview already inflated
     */
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstane)
    {
        // Inflate the view
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // Setup the chart
        setupChart(rootView);

        // Return the inflated view
        return rootView;
    }

    /**
     * Creates a test chart
     * @param rootView The parent view
     */
    private void testChart(View rootView)
    {
        // Init the pie chart
        pieChart = (PieChart)rootView.findViewById(R.id.chart1);
        pieChart.setUsePercentValues(true);
        pieChart.setDescription("Time usage");
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColorTransparent(true);

        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);

        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);

        pieChart.setDrawCenterText(true);

        pieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        pieChart.setOnChartValueSelectedListener(this);

        setData();

        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        Legend l = pieChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
    }

    /**
     * Setup the dashboard chart
     * @param rootView The parent rootview
     */
    private void setupChart(View rootView)
    {
        // Init the pie chart
        pieChart = (PieChart)rootView.findViewById(R.id.chart1);

        pieChart.setUsePercentValues(true);     // Use percentage values

        pieChart.setDescription("System Stats");  // Description
        pieChart.setExtraOffsets(5, 10, 5, 5);


        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColorTransparent(true);

        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);

        pieChart.setHoleRadius(20f);
        pieChart.setTransparentCircleRadius(30f);

        pieChart.setDrawCenterText(true);
        pieChart.setCenterText("Analitics");

        pieChart.setRotationAngle(0);

        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        pieChart.setOnChartValueSelectedListener(this);

        // Set the chart's data
        setData();

        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        Legend l = pieChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
    }

    /**
     * Sets the data for the chart
     */
    private void setData()
    {
        long tasksCount     = LLTask.count(LLTask.class);
        long contactsCount  = LLContact.count(LLContact.class);
        long messagesCount  = LLMessage.count(LLMessage.class);
        long organizationsCount = LLOrganization.count(LLOrganization.class);
        long usersCount     = LLUser.count(LLUser.class);
        long outcomesCount  = LLOutcome.count(LLOutcome.class);


        String[] mParties = {"Tasks","Contacts","Messages","Orgs", "Users", "Outcomes"};

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.

        int indexCount = 0;

        if(tasksCount > 0)
        {
            yVals1.add(new Entry(tasksCount, indexCount++));
            xVals.add("Tasks");
        }

        if(contactsCount > 0){
            yVals1.add(new Entry(contactsCount, indexCount++));
            xVals.add("Contacts");
        }

        if(messagesCount > 0){
            yVals1.add(new Entry(messagesCount, indexCount++));
            xVals.add("Messages");
        }

        if(organizationsCount > 0)
        {
            yVals1.add(new Entry(organizationsCount, indexCount++));
            xVals.add("Orgs");
        }


        if(usersCount > 0 )
        {
            yVals1.add(new Entry(usersCount, indexCount++));
            xVals.add("Users");
        }

        if(outcomesCount > 0)
        {
            yVals1.add(new Entry(outcomesCount, indexCount++));
            xVals.add("Outcomes");
        }

        PieDataSet dataSet = new PieDataSet(yVals1, "Count");
        dataSet.setSliceSpace(2f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
//
//        for (int c : ColorTemplate.COLORFUL_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.LIBERTY_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.PASTEL_COLORS)
//            colors.add(c);

//        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        data.setValueTypeface(tf);
        pieChart.setData(data);

        // undo all highlights
        pieChart.highlightValues(null);

        pieChart.invalidate();
    }

    /**
     * Reacts to a chart value selection
     * @param e Entry point
     * @param dataSetIndex The index selected
     * @param h Highlight
     */
    @Override public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
                        + ", DataSet index: " + dataSetIndex);

        Toast.makeText(getActivity(),"Val="+e.getVal(), Toast.LENGTH_SHORT).show();
    }

    /**
     * React to a nothing touch on the chart
     */
    @Override public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }
}
