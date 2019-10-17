package abc.workshop.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import abc.workshop.wallet.model.ExpenseResponse;
import abc.workshop.wallet.service.APIService;
import abc.workshop.wallet.service.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddExpenseActivity extends AppCompatActivity {

    EditText description, price;
    Button addButton;
    private ExpenseResponse expenseObj;
    Intent mIntent;
    String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        expenseObj = getIntent().getParcelableExtra("expenseObj");
        mIntent = getIntent();

        selectedDate = mIntent.getStringExtra("DATE");
        description = findViewById(R.id.description_et);
        price = findViewById(R.id.price_et);

        addButton = findViewById(R.id.add_button);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expenseObj == null)
                    addExpense();
                else
                    editExpense();
            }
        });
        setIsEditOrAdd();
    }

    private void addExpense() {
        String desc = description.getText().toString();

        Double priceValue = Double.valueOf(price.getText().toString());

        ExpenseResponse expenseResponse = new ExpenseResponse();
        expenseResponse.setDate(selectedDate);
        expenseResponse.setPrice(priceValue);
        expenseResponse.setDescription(desc);
        APIService apiService = RetrofitService.getAPIService();

        Call<ExpenseResponse> responseCall = apiService.addExpense(expenseResponse);

        responseCall.enqueue(new Callback<ExpenseResponse>() {
            @Override
            public void onResponse(Call<ExpenseResponse> call, Response<ExpenseResponse> response) {
                if (response.code() == 200) {
                    onBackPressed();

                }
            }

            @Override
            public void onFailure(Call<ExpenseResponse> call, Throwable t) {

            }
        });
    }

    private void editExpense() {
        expenseObj.setDescription(description.getText().toString());
        expenseObj.setPrice(Double.parseDouble(price.getText().toString()));
        APIService apiService = RetrofitService.getAPIService();

        Call<ExpenseResponse> responseCall = apiService.editExpense(expenseObj);

        responseCall.enqueue(new Callback<ExpenseResponse>() {
            @Override
            public void onResponse(Call<ExpenseResponse> call, Response<ExpenseResponse> response) {
                if (response.code() == 200) {
                    onBackPressed();

                }
            }

            @Override
            public void onFailure(Call<ExpenseResponse> call, Throwable t) {

            }
        });
    }

    private void setIsEditOrAdd() {
        if (expenseObj == null) {
            // here we'r for ADD
            addButton.setText("ADD");

        } else {
            // here we'r for EDIT
            addButton.setText("EDIT");

            description.setText(expenseObj.getDescription());
            price.setText(expenseObj.getPrice() + "");
        }
    }
}
