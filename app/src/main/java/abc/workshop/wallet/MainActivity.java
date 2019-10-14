package abc.workshop.wallet;

import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import abc.workshop.wallet.model.ExpenseResponse;
import abc.workshop.wallet.service.APIService;
import abc.workshop.wallet.service.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private RecyclerView expenseRecyclerview;
    private ExpenseRecyclerViewAdapter adapter;
    List<ExpenseResponse> expenses = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calendarView = findViewById(R.id.calendar);
        expenseRecyclerview = findViewById(R.id.expense_recyclerview);

        onInteractionListener();
        initList();
        getExpensesByDate(calendarView.getDate());


    }


    private void onInteractionListener() {
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView dateView, int year, int month, int dayOfMonth) {
                Calendar c = Calendar.getInstance();
                c.set(year, month, dayOfMonth);
                Long time = c.getTimeInMillis();
                getExpensesByDate(time);
                Log.i("MainActivity", "onSelectedDayChange year: " + year + " month: " + month + " day: " + dayOfMonth);
            }
        });

    }

    private String getFormattedDate(Long timestamp) {

        Date date = new Date(timestamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        return dateFormat.format(date);
    }

    private void getExpensesByDate(Long timeStamp) {
        String currentDate = getFormattedDate(timeStamp);

        APIService apiService = RetrofitService.getAPIService();

        Call<List<ExpenseResponse>> responseCall = apiService.getExpenseByDate(currentDate);

        responseCall.enqueue(new Callback<List<ExpenseResponse>>() {
            @Override
            public void onResponse(Call<List<ExpenseResponse>> call, Response<List<ExpenseResponse>> response) {

                if (response.code() == 200) {
                    expenses = response.body();
                    adapter.setmData(expenses);

                }

            }

            @Override
            public void onFailure(Call<List<ExpenseResponse>> call, Throwable t) {
                Log.e("Error:", t.getLocalizedMessage());
            }
        });
    }

    private void initList() {
        adapter = new ExpenseRecyclerViewAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        expenseRecyclerview.setAdapter(adapter);
        expenseRecyclerview.setLayoutManager(linearLayoutManager);

        expenseRecyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }
}
