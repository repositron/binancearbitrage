package dev.ljw.binance;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class Arbitrage {
  private List<CoinPair> coinPairs;
  private CoinExchange coinExchange;
  public Arbitrage(CoinExchange coinExchange) {
    this.coinPairs = coinPairs;
    this.coinExchange = coinExchange;
  }

  public List<ArbitrageProfit> findProfit(String buyCoinSymbol, List<CoinPair> routeCoinPair) {
    // if we buy BNBBTC (using BTC) we must endup with BTC so we must so final sell is on ???BTC or buy on BTC???
    List<ArbitrageProfit> l = new Vector<>();
    // calculate buying at bid price then selling the ask price
    //Map.Entry<BigDecimal, BigDecimal> highestBid = bidCoinPair.getBestBid();
    //Map.Entry<BigDecimal, BigDecimal> lowestAsk = askCoinPair.getBestAsk();
    /*List<CoinPair> marketRoute = this.coinExchange.getCoinExchangeRoute(buyCoinSymbol);
    if (marketRoute.isEmpty())
      return l;

    return l;*/
    List<BigDecimal> bestTrade = getBestTrade(routeCoinPair);
    int i = 0;


    return l;
  }

  private List<BigDecimal> getBestTrade(List<CoinPair> coinPairs) {
    Map.Entry<BigDecimal, BigDecimal> buy = coinPairs.get(0).getBestAsk();
    for (int i = 1; i < coinPairs.size(); i++) {
      Map.Entry<BigDecimal, BigDecimal> v = coinPairs.get(i).getBestBid();
      //if (v.getValue() < buy.getValue())
    }
    return new ArrayList<>();
  }

  public class ArbitrageProfit {
    String sellSymbol;
    String sellPrice;
    String sellQuantity;

    String buySymbol;
    String buyPrice;
    String buyQuantity;

    String profit;
  }
}
