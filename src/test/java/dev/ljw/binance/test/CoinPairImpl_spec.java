package dev.ljw.binance.test;

import com.binance.api.client.domain.event.DepthEvent;
import dev.ljw.binance.CoinPair;
import dev.ljw.binance.CoinPairImpl;
import dev.ljw.binance.test.tools.OrderBookEntryFactory;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;


public class CoinPairImpl_spec {
  DepthEvent depthEventBnbBtc;
  DepthEvent depthEventBnbEth;
  DepthEvent depthEventEthBtc;
  private void createProfitableDepthEvent() {
    depthEventBnbBtc = new DepthEvent();
    depthEventBnbBtc.setSymbol("BNBBTC");
    depthEventBnbBtc.setUpdateId(1);
    depthEventBnbBtc.setBids(OrderBookEntryFactory.make(Arrays.asList("26", "100", "27", "101", "24", "300", "25", "100")));
    depthEventBnbBtc.setAsks(OrderBookEntryFactory.make(Arrays.asList("30", "100", "35", "100", "31", "100", "19", "100")));
  }

  @Nested
  public class A_coinpair {
    @Test
    public void has_best_bid_of_bid_27() {
      createProfitableDepthEvent();
      CoinPair bnbbtc = new CoinPairImpl("BNBBTC");
      bnbbtc.update(depthEventBnbBtc);
      assertEquals("27", bnbbtc.getBestBid().getKey().toString());
    }
    public void has_best_ask_of_bid_19() {
      createProfitableDepthEvent();
      CoinPair bnbbtc = new CoinPairImpl("BNBBTC");
      bnbbtc.update(depthEventBnbBtc);
      assertEquals("19", bnbbtc.getBestAsk().getKey().toString());
    }
  }
}
