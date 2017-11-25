package dev.ljw.binance;

import java.util.Arrays;
import java.util.List;

public class Main {
  public static void main(String[] args) {
    BinanceDepth binanceDepth = new BinanceDepth();
    //  BNBBTC can buy BNB using BTC, and sell BNB receiving BTC
    //  ETCBTC: Buy ETC using BTC -> BNBETC buy BNB using ETC
    // BNBBTC -> BNBETC ... buy BNBBTC -> sell BNBETC (receiving ETC)
    // BNBETC -> ETCBTC
    // BNBETC -> BNBBTC

    String bnbbtcStr = "BNBBTC";
    String bnbethStr = "BNBETH";
    String ethbtcStr = "ETHBTC";
    CoinPair bnbbtc = new CoinPairImpl(bnbbtcStr);
    CoinPair bnbeth = new CoinPairImpl(bnbethStr);
    CoinPair ethbtc = new CoinPairImpl(ethbtcStr);

    List<CoinPair> route1 = Arrays.asList(bnbbtc, bnbeth, ethbtc);


    Arbitrage arbitrage = new Arbitrage(new CoinExchange(route1));
    binanceDepth.monitorCoinPair(bnbbtc.symbol(), depthEvent -> {
      bnbbtc.update(depthEvent);
      //List<Arbitrage.ArbitrageProfit> profit = arbitrage.findProfit(bnbbtcStr);
    });
    binanceDepth.monitorCoinPair(bnbeth.symbol(), depthEvent -> {
      bnbeth.update(depthEvent);
     // List<Arbitrage.ArbitrageProfit> profit = arbitrage.findProfit(bnbethStr);
    });
    binanceDepth.monitorCoinPair(ethbtc.symbol(), depthEvent -> {
      ethbtc.update(depthEvent);
      //List<Arbitrage.ArbitrageProfit> profit = arbitrage.findProfit(ethbtcStr);
    });
  }
}
