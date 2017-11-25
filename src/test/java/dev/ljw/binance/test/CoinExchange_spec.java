package dev.ljw.binance.test;

import dev.ljw.binance.CoinExchange;
import dev.ljw.binance.CoinPair;
import dev.ljw.binance.CoinPairImpl;

import dev.ljw.binance.test.tools.AssertSameCoinPair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CoinExchange_spec {

  static CoinPair bncbts = new CoinPairImpl("BNCBTS");
  static CoinPair bnbetc = new CoinPairImpl("BNBETC");
  static CoinPair etcbts =  new CoinPairImpl("ETCBTS");

  public static class A_ {
    @Test
    public void test1() {
      List<CoinPair> c1 = Arrays.asList(bncbts, bnbetc, etcbts);
      CoinExchange coinExchange = new CoinExchange(c1);
      List<CoinPair> route = coinExchange.getCoinExchangeRoute("BNCBTS");
      AssertSameCoinPair.assertSameListOfCoinPair(route, Arrays.asList(bncbts, bnbetc, etcbts));
    }
  }
}
