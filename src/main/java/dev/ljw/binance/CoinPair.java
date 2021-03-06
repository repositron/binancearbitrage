package dev.ljw.binance;

import com.binance.api.client.domain.event.DepthEvent;

import java.math.BigDecimal;
import java.util.Map;
import java.util.NavigableMap;

public interface CoinPair {
  boolean update(DepthEvent depthEvent);
  Map.Entry<BigDecimal, BigDecimal> getBestAsk(); //sell
  Map.Entry<BigDecimal, BigDecimal> getBestBid(); //buy

  long eventTime();

  String symbol();

  boolean hasAsks();
  boolean hasBids();
}
