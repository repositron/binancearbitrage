package dev.ljw.binance;

import java.time.Instant;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;


public class TradePrinterImpl implements TradePrinter {
  private String printBuy(Trade t) {
    return t.currency + " " + t.quantity +
      " @ " + t.coinPair.symbol().substring(3) + " " + print(t.price) +
      "; Total " + t.coinPair.symbol().substring(3) + " " + print(t.price.multiply(t.quantity)) +
      " Time: " + printTime(t.coinPair.eventTime());
  }

  private String printSell(Trade t) {
    String sellCurrency =  t.coinPair.symbol().substring(0, 3);
    return sellCurrency + " total " + print(t.price.multiply(t.quantity)) +
      " for " + t.currency + " " + print(t.quantity) +
      "; Price " + sellCurrency + " " +  print(t.price) +
      " Time: " + printTime(t.coinPair.eventTime());
  }

  private static String print(BigDecimal bd) {
    return bd.toPlainString();
  }

  private static String printTime(long timeStampMilliseconds) {
    return Instant.ofEpochMilli(timeStampMilliseconds).toString();
  }

  @Override
  public void print(LinkedList<Trade> trades, BigDecimal profit) {
    Trade bnbbtc = trades.get(0);
    Trade bnbeth = trades.get(1);
    Trade etcbtc = trades.get(2);

    String output = bnbbtc.coinPair.symbol() + ": BUY " + printBuy(bnbbtc) + System.lineSeparator() +
      bnbeth.coinPair.symbol() + ": SELL " + printSell(bnbeth) + System.lineSeparator() +
      etcbtc.coinPair.symbol() + ": SELL " + printSell(etcbtc) + System.lineSeparator() +
      "profit " + etcbtc.currency + " " + profit.toString() + System.lineSeparator();
    System.out.println(output);
  }
}
