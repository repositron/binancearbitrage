package dev.ljw.binance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

public class CoinExchange {
  private ListMultimap<String, CoinPair> marketSymbolCoinPair = ArrayListMultimap.create();
  private ListMultimap<String, CoinPair> symbolCoinPair = ArrayListMultimap.create();

  public CoinExchange(List<CoinPair> coinPairs) {
    coinPairs.forEach(coinPair -> {
      this.marketSymbolCoinPair.put(marketSymbol(coinPair.symbol()), coinPair);
      this.symbolCoinPair.put(coinSymbol(coinPair.symbol()), coinPair);
    });
  }


  public List<CoinPair> getCoinExchangeRoute(String symbol) {
    // for example BNBBTC => BNBETC => ETCBTC
    // buy BNB with BTC => sell BNB receive ETC => sell ETC receive BTC (start and end with BTC)
    List<CoinPair> route = new ArrayList<>();
    String buyCoin = marketSymbol(symbol);
    List<CoinPair> marketCoinPairs = marketSymbolCoinPair.get(buyCoin);
    for (CoinPair i: marketCoinPairs) {

    }  
    for (int i = 0; i < 4; i++) {

    }
    
    return new ArrayList<>();
  }

  
  static private String coinSymbol(String symbol) {
    return symbol.substring(0, 3);
  }

  static private String marketSymbol(String symbol) {
    return symbol.substring(3);
  }
}
