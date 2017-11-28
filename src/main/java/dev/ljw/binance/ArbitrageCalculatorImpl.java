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
    trade.type = Trade.Type.Buy;
    w.trades.add(trade);
    return w;
  }

  private BigDecimal getAvailableCoin() {
    if (trades.getLast().type == Trade.Type.Buy)
      return trades.getLast().quantity;
    else
      return trades.getLast().price.multiply(trades.getLast().quantity);
  }

  // sell at the best bid price. If BNBBTC -> BNBETH we sell BNB for ETH
  @Override
  public ArbitrageCalculator takeBid(CoinPair exchangeTarget) {
    if (!exchangeTarget.symbol().substring(0, 3).equals(currency())) {
      throw new ArbitrageException(currency() + " can't be sold at " + exchangeTarget.symbol() + ".");
    }
    Trade trade = new Trade();
    trade.type = Trade.Type.Sell;
    trade.coinPair = exchangeTarget;
    trade.currency = exchangeTarget.symbol().substring(3); // currency we buy and what we receive on a sell
    trade.quantity = exchangeTarget.getBestBid().getValue();

    // Example BNBETH currency = ETH  ; SELL BNB to get ETH
    BigDecimal bidPrice = exchangeTarget.getBestBid().getKey(); // P ETH price per BNB
    BigDecimal requiredTotal = exchangeTarget.getBestBid().getValue();   // total BNB Q

    // from previous trade BNB
    BigDecimal availableCoin = trades.getLast().type == Trade.Type.Buy ?
      trades.getLast().quantity :
      trades.getLast().price.multiply(trades.getLast().quantity);

    trade.price = bidPrice;
    if (availableCoin.compareTo(requiredTotal) <= 0) {
      // need to reduce quantity of this trade as not enough coin
      if (trade.type == Trade.Type.Buy)
        trade.quantity = availableCoin;
      else
        trade.quantity = availableCoin;
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
      if (trade.type == Trade.Type.Buy)
        trade.quantity = quantity;
      else {
        trade.quantity = newQuantity.divide(trade.price, MathContext.DECIMAL32);
      }
      BigDecimal total = trade.quantity;
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
    BigDecimal startTotal = trades.getFirst().quantity.multiply(trades.getFirst().price);
    BigDecimal endTotal = trades.getLast().quantity.multiply(trades.getLast().price);
    return endTotal.subtract(startTotal);
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
