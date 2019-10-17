package abc.workshop.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import abc.workshop.wallet.model.ExpenseResponse;
import abc.workshop.wallet.service.APIService;
import abc.workshop.wallet.service.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private RecyclerView expenseRecyclerView;
    private ExpenseRecyclerViewAdapter adapter;
    private TextView totalTextView;
    List<ExpenseResponse> expenses = new ArrayList<>();
    Button addButton;
    String selectedDate;

    TextView emptyMessageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        onInteractionListener();
        initList();
        getExpensesByDate(getTodayDate());
    }

    private void initViews() {
        calendarView = findViewById(R.id.calendar);
        totalTextView = findViewById(R.id.total);
        expenseRecyclerView = findViewById(R.id.expense_recyclerview);
        addButton = findViewById(R.id.add_btn);
        emptyMessageTextView = findViewById(R.id.empty_message);

    }


    public String getTodayDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.UK);
        Date date = new Date();
        return simpleDateFormat.format(date);


    }

    private void onInteractionListener() {
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView dateView, int year, int month, int dayOfMonth) {
                selectedDate = dayOfMonth + "-" + (month + 1) + "-" + year;
                showPlus(selectedDate);
                getExpensesByDate(selectedDate);

                Log.i("MainActivity", "onSelectedDayChange year: " + year + " month: " + month + " day: " + dayOfMonth);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddExpenseActivity.class);
                intent.putExtra("DATE", selectedDate);
                startActivity(intent);
            }
        });


    }

    private void showPlus(String selectedDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.UK);
        Date date = null;
        try {
            date = simpleDateFormat.parse(selectedDate);
            Date todayDate = new Date();

            if (date.after(todayDate)) {
                addButton.setVisibility(View.INVISIBLE);
            } else {
                addButton.setVisibility(View.VISIBLE);
            }

        } catch (ParseException e) {
            Log.e("Date exception", e.getLocalizedMessage());
        }


    }


    @Override
    protected void onResume() {
        super.onResume();
        getExpensesByDate(selectedDate);
    }

    private void getExpensesByDate(String date) {
        APIService apiService = RetrofitService.getAPIService();

        Call<List<ExpenseResponse>> responseCall = apiService.getExpenseByDate(date);

        responseCall.enqueue(new Callback<List<ExpenseResponse>>() {
            @Override
            public void onResponse(Call<List<ExpenseResponse>> call, Response<List<ExpenseResponse>> response) {

                if (response.code() == 200) {
                    expenses = response.body();
                    showEmptyMessage();

                    adapter.setExpenses(expenses);

                    calcTotal();
                }

            }

            @Override
            public void onFailure(Call<List<ExpenseResponse>> call, Throwable t) {
                Log.e("Error:", t.getLocalizedMessage());
            }
        });
    }

    private void showEmptyMessage() {
        if (expenses.isEmpty()) {
            emptyMessageTextView.setVisibility(View.VISIBLE);
        } else {
            emptyMessageTextView.setVisibility(View.GONE);
        }
    }

    public void calcTotal() {
        Double total = 0.0;

        for (ExpenseResponse e : expenses
        ) {
            total += e.getPrice();
        }

        totalTextView.setText("Total: " + total + "€");
    }

    private void initList() {
        adapter = new ExpenseRecyclerViewAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        expenseRecyclerView.setAdapter(adapter);
        expenseRecyclerView.setLayoutManager(linearLayoutManager);

        expenseRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }
}
