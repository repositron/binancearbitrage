package dev.ljw.binance.test;

import dev.ljw.binance.Arbitrage;
import dev.ljw.binance.CoinExchange;
import dev.ljw.binance.CoinPair;
import dev.ljw.binance.CoinPairImpl;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

public class Arbitrage_spec {

  public static class coins_of_equal_ask_bid {
    @Test
    public void dont_have_profit() {
      Arbitrage arbitrage = new Arbitrage(new CoinExchange(new ArrayList<>()));
      CoinPair c1 = new CoinPairImpl("BNCBTS");
      CoinPair c2 = new CoinPairImpl("BNBETC");
      List<Arbitrage.ArbitrageProfit> profit = arbitrage.findProfit("BNCBTS", new ArrayList<>());
      assertTrue(profit.isEmpty());
    }
  }

  public static class if_buy_coin_is_greater_than_sell_coin {
    @Test
    public void profit_is_returned() {

    }
  }

}
