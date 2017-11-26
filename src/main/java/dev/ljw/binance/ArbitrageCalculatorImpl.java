package dev.ljw.binance;

import java.math.BigDecimal;
import java.util.LinkedList;

public class ArbitrageCalculatorImpl implements ArbitrageCalculator {
  private LinkedList<Trade> trades = new LinkedList<>();

  private ArbitrageCalculatorImpl() {
  }

  public static ArbitrageCalculator startTrade(CoinPair coinPair) {
    ArbitrageCalculatorImpl w = new ArbitrageCalculatorImpl();

    Trade trade = new Trade();
    trade.coinPair = coinPair;
    trade.currency = coinPair.symbol().substring(0, 3);
    trade.price = coinPair.getBestAsk().getKey();
    trade.quantity = coinPair.getBestAsk().getValue();
    w.trades.add(trade);
    return w;
  }

  // sell at the best bid price. If BNBBTC -> BNBETH we sell BNB for ETH
  @Override
  public ArbitrageCalculator takeBid(CoinPair exchangeTarget) {
    if (!exchangeTarget.symbol().substring(0, 3).equals(currency())) {
      throw new ArbitrageException(currency() + " can't be sold at " + exchangeTarget.symbol() + ".");
    }
    Trade trade = new Trade();
    trade.coinPair = exchangeTarget;
    trade.currency = exchangeTarget.symbol().substring(3);
    trade.quantity = exchangeTarget.getBestBid().getValue();
    BigDecimal bidPrice = exchangeTarget.getBestBid().getKey();
    if (bidPrice.compareTo(trades.getLast().quantity) == -1) {
      // The price is less than available quantity. So adjust the original quantity
      trades.getLast().quantity = bidPrice;
    }
    else {
      bidPrice = trades.getLast().quantity;
    }
    trade.price = bidPrice;
    trades.add(trade);
    return this;
  }

  @Override
  public ArbitrageCalculator complete() {
    return this;
  }

  @Override
  public LinkedList<Trade> getTrades() {
    return trades;
  }

  @Override
  public BigDecimal getProfit() {
    return trades.getLast().quantity.subtract(trades.getFirst().quantity);
  };

  @Override
  public String currency() {
    return trades.getLast().currency;
  }

  @Override
  public BigDecimal getPrice() {
    return trades.getLast().price;
  }

  @Override
  public BigDecimal getQuantity() {
    return trades.getLast().quantity;
  }

}
