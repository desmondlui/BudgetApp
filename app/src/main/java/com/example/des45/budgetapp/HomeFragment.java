package com.example.des45.budgetapp;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class HomeFragment extends Fragment{

    //set UI variable
    private FloatingActionButton mFloatingActionButton;
    private ArrayList<Record> mDraftArrayList = new ArrayList<>();
    private RecyclerView mRecyclerView;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceStace)
    {
        //get the argument from DashboardActivity.class
        Bundle bundle = getArguments();
        mDraftArrayList = bundle.getParcelableArrayList("draft");

        View v = inflater.inflate(R.layout.fragment_home,container,false);
            mFloatingActionButton = v.findViewById(R.id.floatingActionButton);
            mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(),RecordActivity.class);
                    startActivity(intent);
                }
            });

        mRecyclerView = v.findViewById(R.id.home_fragment_recycle_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ListAdapter mAdapter = new HomeFragment.ListAdapter(mDraftArrayList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        return v;
    }

    private class DraftHolder extends RecyclerView.ViewHolder
    {
        private TextView mDisplayDate;
        private TextView mDisplayTime;
        private TextView mDisplayAmount;
        private TextView mDisplayType;
        private Button mEditButton;

        public DraftHolder(View itemView)
        {
            super(itemView);
            mDisplayDate = itemView.findViewById(R.id.draft_date);
            mDisplayTime = itemView.findViewById(R.id.draft_time);
            mDisplayAmount =  itemView.findViewById(R.id.draft_amount);
            mDisplayType = itemView.findViewById(R.id.draft_type);
            mEditButton = itemView.findViewById(R.id.button_edit);
        }
        public void bindResult(final Record resultrecord)
        {
            mDisplayDate.setText(resultrecord.getYear()+"/"+resultrecord.getMonth()+"/"+resultrecord.getDay());
            mDisplayTime.setText(resultrecord.getHours()+":"+resultrecord.getMinutes());
            mDisplayAmount.setText(resultrecord.getAmount()+"");
            mDisplayType.setText(resultrecord.getRecordTypeString());
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

    public class ListAdapter extends RecyclerView.Adapter<HomeFragment.DraftHolder> {
        private ArrayList<Record> mDraft;

        public ListAdapter(ArrayList<Record> records)
        {
            mDraft = records;
        }

        @Override
        public HomeFragment.DraftHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_draft,parent,false);
            return new HomeFragment.DraftHolder(view);
        }
        @Override
        public void onBindViewHolder(HomeFragment.DraftHolder holder, int position)
        {
            Record record = mDraft.get(position);
            holder.bindResult(record);
        }
        @Override
        public int getItemCount()
        {
            return mDraft.size();
        }
    }

}
