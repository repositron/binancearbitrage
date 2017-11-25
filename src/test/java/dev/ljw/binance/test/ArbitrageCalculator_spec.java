package dev.ljw.binance.test;

import com.binance.api.client.domain.event.DepthEvent;
import com.binance.api.client.domain.market.OrderBookEntry;
import dev.ljw.binance.CoinPair;
import dev.ljw.binance.CoinPairImpl;
import dev.ljw.binance.ArbitrageCalculator;
import dev.ljw.binance.test.tools.OrderBookEntryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Arrays;

public class ArbitrageCalculator_spec {
  DepthEvent depthEventBnbBtc;
  DepthEvent depthEventBnbEth;
  DepthEvent depthEventEthBtc;

  @BeforeEach
  public void setupDepthEvent() {
    depthEventBnbBtc = new DepthEvent();
    depthEventBnbBtc.setSymbol("BNBBTC");
    depthEventBnbBtc.setUpdateId(1);
    depthEventBnbBtc.setBids(OrderBookEntryFactory.make(Arrays.asList("20", "100", "10", "101")));
    depthEventBnbBtc.setAsks(OrderBookEntryFactory.make(Arrays.asList("23", "800", "24", "801")));

    depthEventEthBtc = new DepthEvent();
    depthEventEthBtc.setSymbol("ETHBTC");
    depthEventEthBtc.setUpdateId(1);
    depthEventEthBtc.setBids(OrderBookEntryFactory.make(Arrays.asList("30", "100", "31", "101")));
    depthEventEthBtc.setAsks(OrderBookEntryFactory.make(Arrays.asList("32", "800", "33", "801")));

    depthEventBnbEth = new DepthEvent();
    depthEventBnbEth.setSymbol("BNBETH");
    depthEventBnbEth.setUpdateId(1);
    depthEventBnbEth.setBids(OrderBookEntryFactory.make(Arrays.asList("35", "100", "36", "101")));
    depthEventBnbEth.setAsks(OrderBookEntryFactory.make(Arrays.asList("40", "800", "41", "801")));

  }

  @Nested
  public  class a_initialised_wallet_with_BNBBTC {
    @Test
    public void will_have_currency_of_BNB() {
      CoinPair bnbbtc = new CoinPairImpl("BNBBTC");
      bnbbtc.update(depthEventBnbBtc);
      ArbitrageCalculator wallet = ArbitrageCalculator.makeWallet(bnbbtc);
      assertEquals("BNB", wallet.currency());
    }
    @Test void will_have_full_price_and_quantity_of_lowest_ask() {
      CoinPair bnbbtc = new CoinPairImpl("BNBBTC");
      bnbbtc.update(depthEventBnbBtc);
      ArbitrageCalculator wallet = ArbitrageCalculator.makeWallet(bnbbtc);
      assertEquals(new BigDecimal("23"), wallet.getPrice());
      assertEquals(new BigDecimal("800"), wallet.getQuantity());
    }
  }
  @Nested
  public class a_wallet_init_with_BNBBTC_then_taking_bid_price_of_BNBETH {
    @Test
    public void with_have_currency_of_ETH()  {
      CoinPair bnbbtc = new CoinPairImpl("BNBBTC");
      bnbbtc.update(depthEventBnbBtc);
      ArbitrageCalculator arbitrageCalculator = ArbitrageCalculator.makeWallet(bnbbtc);

      CoinPair bnbeth = new CoinPairImpl("BNBETH");
      bnbeth.update(depthEventBnbEth);
      arbitrageCalculator.takeBid(bnbeth);

      assertEquals("ETH", arbitrageCalculator.currency());
    }

    @Test
    public void with_has_price_of_highest_bid() {
      CoinPair bnbbtc = new CoinPairImpl("BNBBTC");
      bnbbtc.update(depthEventBnbBtc);
      ArbitrageCalculator arbitrageCalculator = ArbitrageCalculator.makeWallet(bnbbtc);

      CoinPair bnbeth = new CoinPairImpl("BNBETH");
      bnbeth.update(depthEventBnbEth);
      arbitrageCalculator.takeBid(bnbeth);

      assertEquals(new BigDecimal("31"), arbitrageCalculator.getPrice());
    }
  }
}
