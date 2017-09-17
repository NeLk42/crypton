package nelk.io.crypton.retrofit.Bittrex;

import android.util.Log;

import java.io.IOException;
import java.util.List;

import nelk.io.crypton.models.rex.Portfolio;
import nelk.io.crypton.models.rex.User;
import nelk.io.crypton.recyclerview.BalanceAdapter;
import nelk.io.crypton.retrofit.Bittrex.models.RexResponse;
import nelk.io.crypton.retrofit.Bittrex.models.RexResponseData;
import nelk.io.crypton.retrofit.RexApi;
import nelk.io.crypton.retrofit.models.CoinData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RexPublicService implements Callback<RexResponse> {

    static final String TAG = RexPublicService.class.getSimpleName();
    private final RetrofitConnection retrofitConnection = new RetrofitConnection();
    private final Portfolio portfolio;

    List<? extends CoinData> rexDataList;
    private BalanceAdapter mBalanceAdapter;
    RexApi mRexApi;
    private String portfolioId;

    public RexPublicService(Portfolio portfolio, BalanceAdapter balanceAdapter){
        this.mRexApi = retrofitConnection.getRetrofitService(portfolio.getBroker().getBaseUrl());
        this.mBalanceAdapter = balanceAdapter;
        this.portfolio = portfolio;
    }

//    public void getTicker(RexResponseData rexResponseData) {
//        Call<RexResponse> call = mRexApi.getTicker(rexResponseData.getMarketName());
//        call.enqueue(this);
//    }

    public void getSummaries(String portfolioId) {
        this.portfolioId = portfolioId;
        Call<RexResponse> call = mRexApi.getSummaries();
        call.enqueue(this);
    }

    public void getMarkets(String portfolioId) {
        this.portfolioId = portfolioId;
        Call<RexResponse> call = mRexApi.getMarkets();
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<RexResponse> call, Response<RexResponse> response) {
        if(response.isSuccessful()){
            rexDataList = getResponseCoins(response);
//            mBalanceAdapter.updateCoinList(rexDataList);
        } else {
            try {
                Log.d(TAG, response.errorBody().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<RexResponseData> getResponseCoins(Response<RexResponse> response) {
        RexResponse rexResponseModel = response.body();
        return rexResponseModel.getDataFromResponse();
    }

    @Override
    public void onFailure(Call<RexResponse> call, Throwable t) {
        t.printStackTrace();
    }

}
