package dev.ljw.binance;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;

public class ArbitrageCalculatorImpl implements ArbitrageCalculator {
  private LinkedList<Trade> trades = new LinkedList<>();

  private ArbitrageCalculatorImpl() {
  }

  public static ArbitrageCalculator startTrade(CoinPair coinPair) {
    ArbitrageCalculatorImpl w = new ArbitrageCalculatorImpl();
    Map.Entry<BigDecimal, BigDecimal> bestAsk = coinPair.getBestAsk();
    Trade trade = new Trade();
    trade.coinPair = coinPair;
    trade.currency = coinPair.symbol().substring(0, 3);
    trade.price = bestAsk.getKey();
    trade.quantity = bestAsk.getValue();
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
    BigDecimal requiredTotal = exchangeTarget.getBestBid().getKey().
      multiply(exchangeTarget.getBestBid().getValue());   // P * Q
    BigDecimal availableCoin = trades.getLast().quantity;

    trade.price = bidPrice;
    if (availableCoin.compareTo(requiredTotal) <= 0) {
      // need to reduce quantity of this trade as not enough coin
      trade.quantity = availableCoin.divide(bidPrice, MathContext.DECIMAL64);
    }
    else {
      // we have more than is required to exchange for this coin
      // so we need to reduce previous trades
      reducePreviousQuantities(requiredTotal);
    }
    trades.add(trade);
    return this;
  }

  private void reducePreviousQuantities(BigDecimal newQuantity) {
    ListIterator<Trade> i = trades.listIterator(trades.size());
    BigDecimal quantity = newQuantity;
    while (i.hasPrevious()) {
      Trade trade = i.previous();
      trade.quantity = quantity;
      BigDecimal total = trade.quantity.multiply(trade.price);
      quantity = total; // to change previous trade quantity
    }
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
    if (!trades.getFirst().coinPair.symbol().substring(3).equals(trades.getLast().coinPair.symbol().substring(3))) {
      throw new ArbitrageException("Incompatiable trade: " + trades.getFirst().coinPair.symbol().substring(3)  + " -> " +
        trades.getLast().coinPair.symbol().substring(3));
    }
    BigDecimal startPrice = trades.getFirst().quantity.multiply(trades.getFirst().price);
    return trades.getLast().quantity.subtract(startPrice);
  }

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
