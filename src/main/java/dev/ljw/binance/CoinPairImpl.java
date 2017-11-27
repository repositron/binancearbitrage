package dev.ljw.binance;

import com.binance.api.client.domain.event.DepthEvent;
import com.binance.api.client.domain.market.OrderBookEntry;

import java.math.BigDecimal;
import java.util.*;

public class CoinPairImpl implements CoinPair {
  private long lastUpdateId = 0;
  private NavigableMap<BigDecimal, BigDecimal> asks =
    new TreeMap<>(Comparator.reverseOrder());

  private NavigableMap<BigDecimal, BigDecimal> bids =
    new TreeMap<>(Comparator.reverseOrder());

  private String symbol;

  @Override
  public String symbol() {
    return symbol;
  }

  @Override
  public boolean hasAsks() {
    return !asks.isEmpty();
  }

  @Override
  public boolean hasBids() {
    return !bids.isEmpty();
  }

  public CoinPairImpl(String symbol) {
    this.symbol = symbol;
  }

  @Override
  public void update(DepthEvent depthEvent) {
    if (depthEvent.getUpdateId() == this.lastUpdateId)
      return;

    lastUpdateId = depthEvent.getUpdateId();
    updateOrderBook(asks, depthEvent.getAsks());
    updateOrderBook(bids, depthEvent.getBids());
    System.out.println(symbol);
  }

  @Override
  public Map.Entry<BigDecimal, BigDecimal> getBestAsk() {
    return asks.lastEntry(); // best ask (sell price)
  }

  @Override
  public Map.Entry<BigDecimal, BigDecimal> getBestBid() {
    return bids.firstEntry();
  }

  private void updateOrderBook(NavigableMap<BigDecimal, BigDecimal> lastOrderBook,
                               List<OrderBookEntry> updatedOrderBook) {
    updatedOrderBook.forEach(updateOrder -> {
      BigDecimal price = new BigDecimal(updateOrder.getPrice());
      BigDecimal qty = new BigDecimal(updateOrder.getQty());
      if (qty.compareTo(BigDecimal.ZERO) == 0)
        // remove this order
        lastOrderBook.remove(price);
      else
        lastOrderBook.put(price, qty);
    });
  }
}
