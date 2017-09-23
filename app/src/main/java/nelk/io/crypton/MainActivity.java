package nelk.io.crypton;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import nelk.io.crypton.models.app.Credentials;
import nelk.io.crypton.models.app.Portfolio;
import nelk.io.crypton.models.app.User;
import nelk.io.crypton.models.enums.Brokers;
import nelk.io.crypton.models.enums.Fiats;
import nelk.io.crypton.recyclerview.BalanceAdapter;
import nelk.io.crypton.retrofit.Bittrex.RexAccountService;
import nelk.io.crypton.retrofit.Bittrex.RexConf;
import nelk.io.crypton.retrofit.Bittrex.RexPublicService;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    // Android OS
    private RecyclerView mBalanceRecyclerView;
    private BalanceAdapter mBalanceAdapter;

    // Data
    RexAccountService rexAccountService;
    private User user = new User("user1", Fiats.USD.getSymbol());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBalanceRecyclerView = (RecyclerView) findViewById(R.id.rv_balances_grid);
        mBalanceAdapter = new BalanceAdapter(this, user);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        mBalanceRecyclerView.setLayoutManager(layoutManager);
        mBalanceRecyclerView.setAdapter(mBalanceAdapter);
        mBalanceRecyclerView.setHasFixedSize(true);

        initializeBalanceView();
    }

    private void initializeBalanceView() {
        // TODO : Remove this!
        user.getPortfolios().clear();

        // User is prompted for a portfolio 'name' and decides to use bittrex as Broker
        String portfolioName = "My Portfolio";

        // User is prompted for bittrex api keys
        Credentials portfolioCredentials = new Credentials(RexConf.API_KEY, RexConf.API_SECRET_KEY);

        // New portfolio is created and populated with data from Bittrex
        createPortfolio(portfolioName, Brokers.BITTREX, portfolioCredentials);
    }

    private void createPortfolio(String portfolioName, Brokers bittrex, Credentials portfolioCredentials) {
        // New portfolio is assigned previously captured name and credentials
        Portfolio rexPortfolio = new Portfolio(portfolioName, bittrex, portfolioCredentials);

        // New portfolio is added to user object.
        user.updatePortfolio(rexPortfolio);

        // Pull markets Information from Broker
        initializeBrokerMarketsData(rexPortfolio);

        // Pull user balance from Broker
        initializeBrokerUserBalance(rexPortfolio);
    }

    private void initializeBrokerMarketsData(Portfolio rexPortfolio) {
        // Once portfolio has been assigned against the user, a connection has to be made to
        // pull information, first we load all the markets info.
        RexPublicService rexPublicService = new RexPublicService(rexPortfolio, mBalanceAdapter);
        rexPublicService.pullSummariesData();
        rexPublicService.pullMarketsData();
    }

    private void initializeBrokerUserBalance(Portfolio rexPortfolio) {
        rexAccountService = new RexAccountService(rexPortfolio, mBalanceAdapter);
        rexAccountService.updateAccountBalance();
    }

}
