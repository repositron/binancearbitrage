package dev.ljw.binance;

import java.math.BigDecimal;
import java.util.LinkedList;

public interface TradePrinter {
  void print(LinkedList<Trade> trades, BigDecimal profit);
}
