package dev.ljw.binance;

import java.util.List;

public class TradePrinterImpl implements TradePrinter {
  @Override
  public void print(List<Trade> trades) {
    trades.forEach(x -> {
      System.out.println("Buy " + x.quantity + " " + x.currency + " at " + x.price);
    });
  }
}
