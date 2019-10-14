package abc.workshop.wallet.service;

import java.util.List;

import abc.workshop.wallet.model.ExpenseResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {

    @GET("/expense")
    Call<ExpenseResponse> getExpenses();

    @GET("/expense/by_date")
    Call<List<ExpenseResponse>> getExpenseByDate(@Query("date") String date);

}
