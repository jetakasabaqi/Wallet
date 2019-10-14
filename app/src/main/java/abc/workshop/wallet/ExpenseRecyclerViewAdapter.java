package abc.workshop.wallet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import abc.workshop.wallet.R;
import abc.workshop.wallet.model.ExpenseResponse;

public class ExpenseRecyclerViewAdapter extends RecyclerView.Adapter<ExpenseRecyclerViewAdapter.ViewHolder> {

    private List<ExpenseResponse> mData = new ArrayList<>();
    private Context context;
    // data is passed into the constructor
    public ExpenseRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_list_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.description.setText(mData.get(position).getDescription());
        holder.price.setText(mData.get(position).getPrice()+"");


    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView description, price;
        LinearLayout container;

        ViewHolder(View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.description);
            price = itemView.findViewById(R.id.price);
            container = itemView.findViewById(R.id.container);
        }
    }

    public void setmData(List<ExpenseResponse> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }
}