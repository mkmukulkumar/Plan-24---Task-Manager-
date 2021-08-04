package com.babra.plan24;


import android.content.Context;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private final List<task_data> localDataSet;
    private final LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */

    public CustomAdapter(Context context,List<task_data> dataSet) {
        this.mInflater = LayoutInflater.from(context);
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = mInflater
                .inflate(R.layout.task_list, viewGroup, false);

        return new ViewHolder(view);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTextView().setText(localDataSet.get(position).getTask_name());
        DecimalFormat formatter = new DecimalFormat("00");
        String HH=formatter.format(localDataSet.get(position).getHH());
        String MM=formatter.format(localDataSet.get(position).getMM());
        String SS=formatter.format(localDataSet.get(position).getSS());
        viewHolder.getList_HH().setText(HH);
        viewHolder.getList_MM().setText(MM);
        viewHolder.getList_SS().setText(SS);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView list_task_name;
        private final TextView list_HH;
        private final TextView list_MM;
        private final TextView list_SS;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            list_task_name = view.findViewById(R.id.list_task_name);
            list_HH=view.findViewById(R.id.list_HH);
            list_MM=view.findViewById(R.id.list_MM);
            list_SS=view.findViewById(R.id.list_SS);
            Button del_button = view.findViewById(R.id.del_button);
            del_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
                }
            });
        }

        public TextView getTextView() {
            return list_task_name;
        }
        public TextView getList_HH() {
            return list_HH;
        }

        public TextView getList_MM() {
            return list_MM;
        }

        public TextView getList_SS() {
            return list_SS;
        }
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}

