package dev.ljw.binance;

import com.binance.api.client.domain.market.OrderBookEntry;

import java.math.BigDecimal;
import java.util.LinkedList;


public class ArbitrageCalculator {
  private LinkedList<Trade> trades = new LinkedList<>();

  private ArbitrageCalculator() {
  }

  public static ArbitrageCalculator makeWallet(CoinPair coinPair) {
    ArbitrageCalculator w = new ArbitrageCalculator();

    Trade trade = new Trade();

    trade.coinPair = coinPair;
    trade.currency = coinPair.symbol().substring(0, 3);
    trade.price = coinPair.getBestAsk().getKey();
    trade.quantity = coinPair.getBestAsk().getValue();
    w.trades.add(trade);
    return w;
  }

  // sell at the best bid price
  public ArbitrageCalculator takeBid(CoinPair exchangeTarget) {
    if (exchangeTarget.symbol().substring(3) != currency()) {
      throw new ArbitrageException("Coins aren't compatible");
    }
    Trade trade = new Trade();
    trade.coinPair = exchangeTarget;
    trade.currency = exchangeTarget.symbol().substring(3);
    trade.price = exchangeTarget.getBestBid().getKey();
    trade.quantity = exchangeTarget.getBestBid().getValue();
    trades.add(trade);
    return this;
  }

  public ArbitrageCalculator complete() {
    return this;
  }

  public String currency() {
    return trades.getLast().currency;
  }

  public BigDecimal getPrice() {
    return trades.getLast().price;
  }

  public BigDecimal getQuantity() {
    return trades.getLast().quantity;
  }

  public static class Trade {

    private CoinPair coinPair;
    private String currency;
    private OrderBookEntry orderBookEntry;
    private BigDecimal price;
    private BigDecimal quantity;
  }
}
