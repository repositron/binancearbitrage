package dev.ljw.binance;

import java.math.BigDecimal;
import java.util.LinkedList;

public interface ArbitrageCalculator {
  // sell at the best bid price. If BNBBTC -> BNBETH we sell BNB for ETH
  ArbitrageCalculator takeBid(CoinPair exchangeTarget);

  ArbitrageCalculator complete();

  LinkedList<Trade> getTrades();

  BigDecimal getProfit();

  String currency();

  BigDecimal getPrice();

  BigDecimal getQuantity();
}
