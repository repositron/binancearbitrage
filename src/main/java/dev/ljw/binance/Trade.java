package dev.ljw.binance;

import com.binance.api.client.domain.market.OrderBookEntry;

import java.math.BigDecimal;

public class Trade {

  public CoinPair coinPair;
  public String currency;
  public OrderBookEntry orderBookEntry;
  public BigDecimal price;
  public BigDecimal quantity;
  public enum Type {Buy, Sell}
  public Type type;
}
