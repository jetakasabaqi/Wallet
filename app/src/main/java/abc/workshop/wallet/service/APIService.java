package abc.workshop.wallet.service;

import java.util.List;

import abc.workshop.wallet.model.ExpenseResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {

    @GET("/expense")
    Call<ExpenseResponse> getExpenses();

    @GET("/expense/by_date")
    Call<List<ExpenseResponse>> getExpenseByDate(@Query("date") String date);

    @POST("/expense")
    Call<ExpenseResponse> addExpense(@Body ExpenseResponse expenseResponse);

    @PUT("/expense")
    Call<ExpenseResponse> editExpense(@Body ExpenseResponse expenseResponse);

    @DELETE("/expense/{id}")
    Call<Void> deleteExpense(@Path("id") Integer expenseId);

}
