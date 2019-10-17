package abc.workshop.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import abc.workshop.wallet.model.ExpenseResponse;
import abc.workshop.wallet.service.APIService;
import abc.workshop.wallet.service.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddExpenseActivity extends AppCompatActivity {

    EditText descriptionEditText, priceEditText;
    Button addButton;
    private ExpenseResponse expense;
    Intent intent;
    String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        initViews();
        initListeners();
        initIntent();
    }

    private void initViews() {
        descriptionEditText = findViewById(R.id.description_et);
        priceEditText = findViewById(R.id.price_et);
        addButton = findViewById(R.id.add_button);
    }

    private void initListeners() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expense == null)
                    addExpenseCall();
                else
                    editExpenseCall();
            }
        });
    }

    private void initIntent() {
        intent = getIntent();
        expense = intent.getParcelableExtra("EXPENSE");
        selectedDate = intent.getStringExtra("DATE");

        setIsEditOrAdd();
    }


    private void setIsEditOrAdd() {
        if (expense == null) {
            // here we'r for ADD
            addButton.setText(getResources().getString(R.string.add));

        } else {
            // here we'r for EDIT
            addButton.setText(getResources().getString(R.string.edit));

            descriptionEditText.setText(expense.getDescription());
            priceEditText.setText(expense.getPrice().toString());
        }
    }

    private void addExpenseCall() {
        String desc = descriptionEditText.getText().toString();

        Double priceValue = Double.valueOf(priceEditText.getText().toString());

        ExpenseResponse expenseResponse = new ExpenseResponse();
        expenseResponse.setDate(selectedDate);
        expenseResponse.setPrice(priceValue);
        expenseResponse.setDescription(desc);
        APIService apiService = RetrofitService.getAPIService();

        Call<ExpenseResponse> responseCall = apiService.addExpense(expenseResponse);

        responseCall.enqueue(new Callback<ExpenseResponse>() {
            @Override
            public void onResponse(@NonNull Call<ExpenseResponse> call, @NonNull Response<ExpenseResponse> response) {
                if (response.code() == 200) {
                    onBackPressed();

                }
            }

            @Override
            public void onFailure(@NonNull Call<ExpenseResponse> call, @NonNull Throwable t) {

                Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void editExpenseCall() {
        expense.setDescription(descriptionEditText.getText().toString());
        expense.setPrice(Double.parseDouble(priceEditText.getText().toString()));
        APIService apiService = RetrofitService.getAPIService();

        Call<ExpenseResponse> responseCall = apiService.editExpense(expense);

        responseCall.enqueue(new Callback<ExpenseResponse>() {
            @Override
            public void onResponse(@NonNull Call<ExpenseResponse> call, @NonNull Response<ExpenseResponse> response) {
                if (response.code() == 200) {
                    onBackPressed();

                }
            }

            @Override
            public void onFailure(@NonNull Call<ExpenseResponse> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


}
