package com.babra.plan24;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private final List<task_data> localDataSet;


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
                    Toast.makeText(view.getContext(), getTextView().getText(), Toast.LENGTH_SHORT).show();
                    task_data task = localDataSet.get(getAdapterPosition());

                    Intent intent = new Intent(view.getContext(),MainActivity.class);
                    intent.putExtra("task", String.valueOf(task));
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

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public CustomAdapter(List<task_data> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.task_list, viewGroup, false);

        return new ViewHolder(view);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTextView().setText(localDataSet.get(position).getTask_name());
        viewHolder.getList_HH().setText(String.valueOf(localDataSet.get(position).getHH()));
        viewHolder.getList_MM().setText(String.valueOf(localDataSet.get(position).getMM()));
        viewHolder.getList_SS().setText(String.valueOf(localDataSet.get(position).getSS()));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}

