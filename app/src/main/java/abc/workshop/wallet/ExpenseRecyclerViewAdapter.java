package abc.workshop.wallet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import abc.workshop.wallet.model.ExpenseResponse;
import abc.workshop.wallet.service.APIService;
import abc.workshop.wallet.service.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpenseRecyclerViewAdapter extends RecyclerView.Adapter<ExpenseRecyclerViewAdapter.ExpenseViewHolder> {

    private List<ExpenseResponse> mData = new ArrayList<>();
    private Context context;


    ExpenseRecyclerViewAdapter(Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_list_item, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExpenseViewHolder holder, final int position) {


        holder.description.setText(mData.get(position).getDescription());
        holder.price.setText(mData.get(position).getPrice() + "€");
        holder.order.setText((position + 1) + "");

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, AddExpenseActivity.class);
                i.putExtra("expenseObj", mData.get(position));
                ((Activity) context).startActivity(i);
            }
        });

        holder.container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this entry?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                deleteItem(position, mData.get(position).getId());
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return true;
            }


        });


    }

    private void deleteItem(final int position, Integer id) {
        APIService apiService = RetrofitService.getAPIService();

        Call<Void> responseCall = apiService.deleteExpense(id);

        responseCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.code() == 200) {
                    Toast.makeText(context, "Delete success", Toast.LENGTH_LONG).show();
                    mData.remove(position);
                    notifyDataSetChanged();
                    ((MainActivity) context).calcTotal();
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Error:", t.getLocalizedMessage());
            }
        });
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }


    class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView description, price, order;
        LinearLayout container;

        ExpenseViewHolder(View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.description);
            price = itemView.findViewById(R.id.price);
            container = itemView.findViewById(R.id.container);
            order = itemView.findViewById(R.id.order_number);
        }
    }

    void setmData(List<ExpenseResponse> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }
}