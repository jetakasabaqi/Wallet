package abc.workshop.wallet;

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

    private List<ExpenseResponse> expenses = new ArrayList<>();
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

        String positionOrder = String.valueOf((position + 1));

        holder.description.setText(expenses.get(position).getDescription());
        holder.price.setText(expenses.get(position).getPriceAndCurrency());
        holder.order.setText(positionOrder);

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, AddExpenseActivity.class);
                i.putExtra("EXPENSE", expenses.get(position));
                context.startActivity(i);
            }
        });

        holder.container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                showDeleteDialog();

                return true;

            }

            private void showDeleteDialog() {
                new AlertDialog.Builder(context)
                        .setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this entry?")


                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                deleteExpenseCall(position, expenses.get(position).getId());
                            }
                        })

                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }


        });
    }


    @Override
    public int getItemCount() {
        return expenses.size();
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

    void setExpenses(List<ExpenseResponse> expenses) {
        this.expenses = expenses;
        notifyDataSetChanged();
    }


    private void deleteExpenseCall(final int position, Integer id) {
        APIService apiService = RetrofitService.getAPIService();

        Call<Void> responseCall = apiService.deleteExpense(id);

        responseCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {

                if (response.code() == 200) {
                    Toast.makeText(context, "Delete success", Toast.LENGTH_LONG).show();
                    expenses.remove(position);
                    notifyDataSetChanged();
                    ((MainActivity) context).calcTotal();
                }

            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.e("Error:", t.getLocalizedMessage());
            }
        });
    }


}