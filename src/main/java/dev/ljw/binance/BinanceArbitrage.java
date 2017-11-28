package dev.ljw.binance;

import java.math.BigDecimal;
import java.util.LinkedList;

public class BinanceArbitrage {
  private static class ThreeCoinArbitrage {
    CoinPair bnbbtc;
    CoinPair bnbeth;
    CoinPair ethbtc;
    TradePrinter tradePrinter;

    public ThreeCoinArbitrage(CoinPair bnbbtc, CoinPair bnbeth, CoinPair ethbtc, TradePrinter tradePrinter) {
      this.bnbbtc = bnbbtc;
      this.bnbeth = bnbeth;
      this.ethbtc = ethbtc;
      this.tradePrinter = tradePrinter;
    }

    public void monitorBinance() {
      BinanceDepth binanceDepthBnbbtc = new BinanceDepth();
      binanceDepthBnbbtc.monitorCoinPair(bnbbtc.symbol(), depthEvent -> {
        if (bnbbtc.update(depthEvent))
          arbitrage();
      });

      BinanceDepth binanceDepthBnbetc = new BinanceDepth();
      binanceDepthBnbetc.monitorCoinPair(bnbeth.symbol(), depthEvent -> {
        if (bnbeth.update(depthEvent))
          arbitrage();
      });

      BinanceDepth binanceDepthEthbtc = new BinanceDepth();
      binanceDepthEthbtc.monitorCoinPair(ethbtc.symbol(), depthEvent -> {
        if (ethbtc.update(depthEvent))
          arbitrage();
      });
    }

    private void arbitrage() {
      try {
        if (!(bnbbtc.hasAsks() && bnbeth.hasBids() && ethbtc.hasBids())) {
          // nothing to test
          return;
        }
        ArbitrageCalculator arbitrage = ArbitrageCalculatorImpl.startTrade(bnbbtc).
          takeBid(bnbeth).
          takeBid(ethbtc).
          complete();
        if (arbitrage.getProfit().compareTo(BigDecimal.ZERO) > 0) {
          tradePrinter.print(arbitrage.getTrades(), arbitrage.getProfit());
        }
      } catch (Exception ex) {
        System.out.println("An error occurred: " + ex);
      }
    }
  }

  public static void main(String[] args) {
    //  BNBBTC can buy BNB using BTC, and sell BNB receiving BTC
    //  ETCBTC: Buy ETC using BTC -> BNBETC buy BNB using ETC
    // BNBBTC -> BNBETC ... buy BNBBTC -> sell BNBETC (receiving ETC)
    // BNBETC -> ETCBTC
    // BNBETC -> BNBBTC

    String bnbbtcStr = "BNBBTC";
    String bnbethStr = "BNBETH";
    String ethbtcStr = "ETHBTC";
    CoinPair bnbbtc = new CoinPairImpl(bnbbtcStr);
    CoinPair bnbeth = new CoinPairImpl(bnbethStr);
    CoinPair ethbtc = new CoinPairImpl(ethbtcStr);
    TradePrinter tradePrinter = new TradePrinterImpl();
    ThreeCoinArbitrage threeCoinArbitrage = new ThreeCoinArbitrage(
      new CoinPairImpl(bnbbtcStr),
      new CoinPairImpl(bnbethStr),
      new CoinPairImpl(ethbtcStr),
      new TradePrinterImpl());
    threeCoinArbitrage.monitorBinance();
  }
}
