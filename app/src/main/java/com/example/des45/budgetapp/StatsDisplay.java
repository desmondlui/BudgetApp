package com.example.des45.budgetapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class StatsDisplay extends Fragment {

    //value for display Stats
    private String mYear;
    private String mMonth;
    private Boolean isPie;
    private Boolean recordMode;
    private PieChart pieChart;
    private RecyclerView mListRecycle;
    private ListAdapter mAdapter;
    private ArrayList<Record> mRecordArrayList;
    private ArrayList<Record> mResultArrayList;
    private ArrayList<Double> categoryExpenseList;
    private ArrayList<Double> categoryIncomeList;

    private double totalAmount;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceStace) {

        View v;
        mResultArrayList = new ArrayList<>();
        categoryExpenseList = new ArrayList<>();
        for(int i = 0;  i < 8; i++)
        {
            categoryExpenseList.add(i,0.0);
        }
        categoryIncomeList  = new ArrayList<>();
        for(int i = 0;  i < 5; i++)
        {
            categoryIncomeList.add(i,0.0);
        }
        //get value from fragments(StatsFragment)
        Bundle bundle = getArguments();
        isPie = bundle.getBoolean("isPie");
        mYear = bundle.getString("year");
        mMonth = bundle.getString("month");
        recordMode = bundle.getBoolean("recordMode");
        mRecordArrayList = bundle.getParcelableArrayList("databaseList");
        processRecordArrayList(recordMode,mYear,mMonth);
        if(isPie) {
            //set pie
            v = inflater.inflate(R.layout.fragment_stats_pie, container, false);
            pieChart = v.findViewById(R.id.pieChart);
            pieChart.setDrawHoleEnabled(false);
        }
        else {
            //set recycle view
            v = inflater.inflate(R.layout.fragment_stats_display, container, false);
            mListRecycle = v.findViewById(R.id.display_stats_recycleView);
            mListRecycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

        displayResults(recordMode,isPie,mYear,mMonth);
        return v;
    }

    private void processRecordArrayList(boolean mode,String year,String month)
    {
        //for the first process
        for(int i=0 ; i<mRecordArrayList.size(); i++)
        {
            if(mRecordArrayList.get(i).getRecordType() == mode && mRecordArrayList.get(i).getYear() == Integer.parseInt(year) && mRecordArrayList.get(i).getMonth() == Integer.parseInt(month)&& mRecordArrayList.get(i).getDraft()==false)
            {
                mResultArrayList.add(mRecordArrayList.get(i));
            }
        }

        //Second batch for the pie chart
        if(mode)//expenses
        {
            for(int i = 0; i < mResultArrayList.size(); i++)
            {
                if(mResultArrayList.get(i).getCategory().equals("Food")) {
                    double value = mResultArrayList.get(i).getAmount();
                    totalAmount += value;
                    double preValue = categoryExpenseList.get(0);//get the previous from category
                    categoryExpenseList.add(0, (value + preValue));
                } else if (mResultArrayList.get(i).getCategory().equals("Social Life")) {
                    double value = mResultArrayList.get(i).getAmount();
                    totalAmount += value;
                    double preValue = categoryExpenseList.get(1);//get the previous from category
                    categoryExpenseList.add(1, (value + preValue));
                } else if (mResultArrayList.get(i).getCategory().equals("Transfer")) {
                    double value = mResultArrayList.get(i).getAmount();
                    totalAmount += value;
                    double preValue = categoryExpenseList.get(2);//get the previous from category
                    categoryExpenseList.add(2, (value + preValue));
                } else if (mResultArrayList.get(i).getCategory().equals("Transportation")) {
                    double value = mResultArrayList.get(i).getAmount();
                    totalAmount += value;
                    double preValue = categoryExpenseList.get(3);//get the previous from category
                    categoryExpenseList.add(3, (value + preValue));
                } else if (mResultArrayList.get(i).getCategory().equals("Utility")) {
                    double value = mResultArrayList.get(i).getAmount();
                    totalAmount += value;
                    double preValue = categoryExpenseList.get(4);//get the previous from category
                    categoryExpenseList.add(4, (value + preValue));
                } else if (mResultArrayList.get(i).getCategory().equals("Household")) {
                    double value = mResultArrayList.get(i).getAmount();
                    totalAmount += value;
                    double preValue = categoryExpenseList.get(5);//get the previous from category
                    categoryExpenseList.add(5, (value + preValue));}
                else if (mResultArrayList.get(i).getCategory().equals("Education")) {
                    double value = mResultArrayList.get(i).getAmount();
                    totalAmount += value;
                    double preValue = categoryExpenseList.get(6);//get the previous from category
                    categoryExpenseList.add(6, (value + preValue));
                } else {
                    double value = mResultArrayList.get(i).getAmount();
                    totalAmount += value;
                    double preValue = categoryExpenseList.get(7);//get the previous from category
                    categoryExpenseList.add(7, (value + preValue));
                }
            }
        }
        else {
            for (int i = 0; i < mResultArrayList.size(); i++) {
                if(mResultArrayList.get(i).getCategory().equals("Salary")) {
                    double value = mResultArrayList.get(i).getAmount();
                    totalAmount += value;
                    double preValue = categoryIncomeList.get(0);//get the previous from category
                    categoryIncomeList.add(0, (value + preValue));
                }
                else if (mResultArrayList.get(i).getCategory().equals("Bonus")) {
                    double value = mResultArrayList.get(i).getAmount();
                    totalAmount += value;
                    double preValue = categoryIncomeList.get(1);//get the previous from category
                    categoryIncomeList.add(1, (value + preValue));
                }
                else if (mResultArrayList.get(i).getCategory().equals("Transfer")) {
                    double value = mResultArrayList.get(i).getAmount();
                    totalAmount += value;
                    double preValue = categoryIncomeList.get(2);//get the previous from category
                    categoryIncomeList.add(2, (value + preValue));
                }
                else if (mResultArrayList.get(i).getCategory().equals("Investment")) {
                    double value = mResultArrayList.get(i).getAmount();
                    totalAmount += value;
                    double preValue = categoryIncomeList.get(3);//get the previous from category
                    categoryIncomeList.add(3, (value + preValue));
                }
                else {
                    double value = mResultArrayList.get(i).getAmount();
                    totalAmount += value;
                    double preValue = categoryIncomeList.get(4);//get the previous from category
                    categoryIncomeList.add(4, (value + preValue));
                }
            }//end for
        }//end else
    }//end fun

    public void displayResults(boolean mode,boolean isPie,String year,String month)
    {
        if(isPie)
        {
            displayPie(mode,year,month);
        }
        else
        {
            displayList();
        }

    }

    public void displayList()
    {
        mAdapter = new ListAdapter(mResultArrayList);
        mListRecycle.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    public void displayPie(boolean mode,String year,String month)
    {
        double test = totalAmount;
        ArrayList<PieEntry> pieValue = new ArrayList<>();
        ArrayList<String> pieValueExpenses = new ArrayList<>();
        ArrayList<String> pieValueIncome = new ArrayList<>();
        pieValueExpenses.add(0,"Food");
        pieValueExpenses.add(1,"Social Life");
        pieValueExpenses.add(2,"Transfer");
        pieValueExpenses.add(3,"Transportation");
        pieValueExpenses.add(4,"Utility");
        pieValueExpenses.add(5,"Household");
        pieValueExpenses.add(6,"Healthcare");
        pieValueExpenses.add(7,"Others");

        pieValueIncome.add(0,"Salary");
        pieValueIncome.add(1,"Bonus");
        pieValueIncome.add(2,"Transfer");
        pieValueIncome.add(3,"Investment");
        pieValueIncome.add(4,"Lottery");
        //Make pie chart
        DecimalFormat df = new DecimalFormat("#####.##");
        if(recordMode)//expenses
        {

            for(int i=0; i<categoryExpenseList.size();i++)
            {
                double value =categoryExpenseList.get(i)/totalAmount*100;
                if(value != 0.0) {
                    pieValue.add(new PieEntry((float)value,pieValueExpenses.get(i)));
                }
            }
            PieDataSet set = new PieDataSet(pieValue,"Expenses");
            set.setColors(ColorTemplate.JOYFUL_COLORS);
            set.setSliceSpace(2f);
            set.setValueTextColor(Color.BLACK);
            set.setValueTextSize(16f);
            PieData data = new PieData(set);
            data.setValueFormatter(new PercentFormatter());
            pieChart.setData(data);
            pieChart.invalidate();
        }
        //income
        else {
            for(int i=0; i<categoryIncomeList.size();i++)
            {
                double value = categoryIncomeList.get(i)/totalAmount*100;
                if(value != 0.0) {
                    pieValue.add(new PieEntry((float)value,pieValueExpenses.get(i)));
                }
            }
            PieDataSet set = new PieDataSet(pieValue,"Income");
            set.setColors(ColorTemplate.JOYFUL_COLORS);
            set.setSliceSpace(2f);
            set.setValueTextColor(Color.BLACK);
            set.setValueTextSize(16f);
            PieData data = new PieData(set);
            data.setValueFormatter(new PercentFormatter());
            pieChart.setData(data);
            pieChart.invalidate();
        }
    }

    private class StatsHolder extends RecyclerView.ViewHolder
    {
        private TextView mDisplayDate;
        private TextView mDisplayTime;
        private TextView mDisplayAmount;
        private TextView mDisplayCategory;
        private TextView mDisplayType;
        private TextView mDisplayContent;
        private Button mEditButton;
        public StatsHolder(View itemView)
        {
            super(itemView);
            mDisplayDate = itemView.findViewById(R.id.list_value_date);
            mDisplayTime = itemView.findViewById(R.id.list_value_time);
            mDisplayAmount =  itemView.findViewById(R.id.list_value_amount);
            mDisplayCategory = itemView.findViewById(R.id.list_value_category);
            mDisplayType = itemView.findViewById(R.id.list_value_type);
            mDisplayContent = itemView.findViewById(R.id.list_value_content);
            mEditButton = itemView.findViewById(R.id.list_edit_button);

        }
        public void bindResult(final Record resultrecord)
        {
           mDisplayDate.setText(resultrecord.getYear()+"/"+resultrecord.getMonth()+"/"+resultrecord.getDay());
           mDisplayTime.setText(resultrecord.getHours()+":"+resultrecord.getMinutes());
           mDisplayAmount.setText(resultrecord.getAmount()+"");
           mDisplayCategory.setText(resultrecord.getCategory());
           mDisplayType.setText(resultrecord.getType());
           mDisplayContent.setText(resultrecord.getContents());
           mEditButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {

                   Intent intent = new Intent(getContext(),EditActivity.class);
                   intent.putExtra("editid",resultrecord.getId());
                   intent.putExtra("editrecordmode",resultrecord.getRecordType());
                   intent.putExtra("edityear",resultrecord.getYear());
                   intent.putExtra("editmonth",resultrecord.getMonth());
                   intent.putExtra("editday",resultrecord.getDay());
                   intent.putExtra("edithours",resultrecord.getHours());
                   intent.putExtra("editminutes",resultrecord.getHours());
                   intent.putExtra("editamount",resultrecord.getAmount());
                   intent.putExtra("editcontent",resultrecord.getContents());
                   startActivity(intent);

               }
           });
        }
    }
    public class ListAdapter extends RecyclerView.Adapter<StatsHolder> {
        private ArrayList<Record> mRecords;

        public ListAdapter(ArrayList<Record> records)
        {
            mRecords = records;
        }

        @Override
        public StatsDisplay.StatsHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_record,parent,false);
            return new StatsDisplay.StatsHolder(view);
        }
        @Override
        public void onBindViewHolder(StatsHolder holder, int position)
        {
            Record record = mRecords.get(position);
            record.getRecordType();
            holder.bindResult(record);
        }

        @Override
        public int getItemCount()
        {
            return mRecords.size();
        }

    }
}
