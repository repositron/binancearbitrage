package dev.ljw.binance.test;

import com.binance.api.client.domain.event.DepthEvent;
import dev.ljw.binance.ArbitrageCalculator;
import dev.ljw.binance.CoinPair;
import dev.ljw.binance.CoinPairImpl;
import dev.ljw.binance.ArbitrageCalculatorImpl;
import dev.ljw.binance.test.tools.OrderBookEntryFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Arrays;

public class ArbitrageCalculatorImpl_spec {
  DepthEvent depthEventBnbBtc;
  DepthEvent depthEventBnbEth;
  DepthEvent depthEventEthBtc;


  @AfterEach
  private void clearDepthEvents() {
    depthEventBnbBtc = null;
    depthEventBnbEth = null;
    depthEventEthBtc = null;
  }

  private void createProfitableDepthEvent() {
    depthEventBnbBtc = new DepthEvent();
    depthEventBnbBtc.setSymbol("BNBBTC");
    depthEventBnbBtc.setUpdateId(1);
    depthEventBnbBtc.setBids(OrderBookEntryFactory.make(Arrays.asList("26", "100", "27", "101")));
    // BUY
    depthEventBnbBtc.setAsks(OrderBookEntryFactory.make(Arrays.asList("30", "100", "35", "100")));

    depthEventBnbEth = new DepthEvent();
    depthEventBnbEth.setSymbol("BNBETH");
    depthEventBnbEth.setUpdateId(1);
    // SELL
    depthEventBnbEth.setBids(OrderBookEntryFactory.make(Arrays.asList("35", "100", "40", "5")));
    depthEventBnbEth.setAsks(OrderBookEntryFactory.make(Arrays.asList("40", "800", "41", "801")));

    depthEventEthBtc = new DepthEvent();
    depthEventEthBtc.setSymbol("ETHBTC");
    depthEventEthBtc.setUpdateId(1);
    // SELL
    depthEventEthBtc.setBids(OrderBookEntryFactory.make(Arrays.asList("2.4", "100", "2.5", "20")));
    depthEventEthBtc.setAsks(OrderBookEntryFactory.make(Arrays.asList("32", "800", "33", "801")));
  }

  private void createZeroProfitDepthEvent() {
    depthEventBnbBtc = new DepthEvent();
    depthEventBnbBtc.setSymbol("BNBBTC");
    depthEventBnbBtc.setUpdateId(1);
    depthEventBnbBtc.setBids(OrderBookEntryFactory.make(Arrays.asList()));

    // #1 BUY BNB 10 for 20 *10 BTC
    depthEventBnbBtc.setAsks(OrderBookEntryFactory.make(Arrays.asList("20", "10")));

    depthEventBnbEth = new DepthEvent();
    depthEventBnbEth.setSymbol("BNBETH");
    depthEventBnbEth.setUpdateId(1);

    // #2 SELL 4*2.5 = 10 BNB and receive ETH 2.5
    depthEventBnbEth.setBids(OrderBookEntryFactory.make(Arrays.asList("4", "2.5")));
    depthEventBnbEth.setAsks(OrderBookEntryFactory.make(Arrays.asList()));

    depthEventEthBtc = new DepthEvent();
    depthEventEthBtc.setSymbol("ETHBTC");
    depthEventEthBtc.setUpdateId(1);
    // #3 SELL 0.0125 * 200 = 25 ETH and receive 200 BTC
    depthEventEthBtc.setBids(OrderBookEntryFactory.make(Arrays.asList("5", "200")));
    depthEventEthBtc.setAsks(OrderBookEntryFactory.make(Arrays.asList()));
  }

  private void createProfitableDepthEvent2() {
    depthEventBnbBtc = new DepthEvent();
    depthEventBnbBtc.setSymbol("BNBBTC");
    depthEventBnbBtc.setUpdateId(1);
    depthEventBnbBtc.setBids(OrderBookEntryFactory.make(Arrays.asList()));
    //
    depthEventBnbBtc.setAsks(OrderBookEntryFactory.make(Arrays.asList("0.01", "300")));

    depthEventBnbEth = new DepthEvent();
    depthEventBnbEth.setSymbol("BNBETH");
    depthEventBnbEth.setUpdateId(1);
    //
    depthEventBnbEth.setBids(OrderBookEntryFactory.make(Arrays.asList("2", "40")));
    depthEventBnbEth.setAsks(OrderBookEntryFactory.make(Arrays.asList()));

    depthEventEthBtc = new DepthEvent();
    depthEventEthBtc.setSymbol("ETHBTC");
    depthEventEthBtc.setUpdateId(1);
    //
    depthEventEthBtc.setBids(OrderBookEntryFactory.make(Arrays.asList("6", "4")));
    depthEventEthBtc.setAsks(OrderBookEntryFactory.make(Arrays.asList()));
  }

  private void createLoseDepthEvent() {
    depthEventBnbBtc = new DepthEvent();
    depthEventBnbBtc.setSymbol("BNBBTC");
    depthEventBnbBtc.setUpdateId(1);
    depthEventBnbBtc.setBids(OrderBookEntryFactory.make(Arrays.asList("20", "100")));

    depthEventBnbBtc.setAsks(OrderBookEntryFactory.make(Arrays.asList("20", "10")));

    depthEventBnbEth = new DepthEvent();
    depthEventBnbEth.setSymbol("BNBETH");
    depthEventBnbEth.setUpdateId(1);

    depthEventBnbEth.setBids(OrderBookEntryFactory.make(Arrays.asList("4", "20")));
    depthEventBnbEth.setAsks(OrderBookEntryFactory.make(Arrays.asList()));

    depthEventEthBtc = new DepthEvent();
    depthEventEthBtc.setSymbol("ETHBTC");
    depthEventEthBtc.setUpdateId(1);

    depthEventEthBtc.setBids(OrderBookEntryFactory.make(Arrays.asList("0.025", "100")));
    depthEventEthBtc.setAsks(OrderBookEntryFactory.make(Arrays.asList()));
  }

  @Nested
  public  class a_initialised_wallet_with_BNBBTC {
    @Test
    public void will_have_currency_of_BNB() {
      createProfitableDepthEvent();
      CoinPair bnbbtc = new CoinPairImpl("BNBBTC");
      bnbbtc.update(depthEventBnbBtc);
      ArbitrageCalculator wallet = ArbitrageCalculatorImpl.startTrade(bnbbtc);
      assertEquals("BNB", wallet.currency());
    }
    @Test void will_have_full_price_and_quantity_of_lowest_ask() {
      createProfitableDepthEvent();
      CoinPair bnbbtc = new CoinPairImpl("BNBBTC");
      bnbbtc.update(depthEventBnbBtc);
      ArbitrageCalculator wallet = ArbitrageCalculatorImpl.startTrade(bnbbtc);
      assertEquals(new BigDecimal("30"), wallet.getPrice());
      assertEquals(new BigDecimal("100"), wallet.getQuantity());
    }
  }
  @Nested
  public class a_wallet_init_with_BNBBTC_then_taking_bid_price_of_BNBETH {
    @Test
    public void with_have_currency_of_ETH()  {
      createProfitableDepthEvent();
      CoinPair bnbbtc = new CoinPairImpl("BNBBTC");
      bnbbtc.update(depthEventBnbBtc);
      ArbitrageCalculator arbitrageCalculator = ArbitrageCalculatorImpl.startTrade(bnbbtc);

      CoinPair bnbeth = new CoinPairImpl("BNBETH");
      bnbeth.update(depthEventBnbEth);
      arbitrageCalculator.takeBid(bnbeth);

      assertEquals("ETH", arbitrageCalculator.currency());
    }
  }

  @Nested
  public class profitable_depthEvent_trades_BNBBTC_BNBETH_ETCBTC {

    @Test
    public void has_currency_of_BTC() {
      createProfitableDepthEvent();
      CoinPair bnbbtc = new CoinPairImpl("BNBBTC");
      bnbbtc.update(depthEventBnbBtc);
      ArbitrageCalculator arbitrageCalculator = ArbitrageCalculatorImpl.startTrade(bnbbtc);

      CoinPair bnbeth = new CoinPairImpl("BNBETH");
      bnbeth.update(depthEventBnbEth);
      arbitrageCalculator.takeBid(bnbeth);

      CoinPair etcbtc = new CoinPairImpl("ETHBTC");
      etcbtc.update(depthEventEthBtc);
      arbitrageCalculator.takeBid(etcbtc);
      arbitrageCalculator.complete();
      assertEquals("BTC", arbitrageCalculator.currency());
    }

    @Test
    public void at_first_takebid_no_change_to_current_quantity() {
      createProfitableDepthEvent();
      CoinPair bnbbtc = new CoinPairImpl("BNBBTC");
      bnbbtc.update(depthEventBnbBtc);
      ArbitrageCalculator arbitrageCalculator = ArbitrageCalculatorImpl.startTrade(bnbbtc);

      CoinPair bnbeth = new CoinPairImpl("BNBETH");
      bnbeth.update(depthEventBnbEth);
      assertEquals("5", bnbeth.getBestBid().getValue().toString());
      arbitrageCalculator.takeBid(bnbeth);
      assertEquals("5", arbitrageCalculator.getTrades().getLast().quantity.toString());
    }

    @Test
    public void at_second_takebid_no_change_to_current_quantity_profit_is_50() {
      createProfitableDepthEvent();
      CoinPair bnbbtc = new CoinPairImpl("BNBBTC");
      bnbbtc.update(depthEventBnbBtc);
      ArbitrageCalculator arbitrageCalculator = ArbitrageCalculatorImpl.startTrade(bnbbtc);

      CoinPair bnbeth = new CoinPairImpl("BNBETH");
      bnbeth.update(depthEventBnbEth);
      arbitrageCalculator.takeBid(bnbeth);
      assertEquals("5", arbitrageCalculator.getTrades().getLast().quantity.toString());

      CoinPair etcbtc = new CoinPairImpl("ETHBTC");
      etcbtc.update(depthEventEthBtc);
      assertEquals("20", etcbtc.getBestBid().getValue().toString());
      arbitrageCalculator.takeBid(etcbtc);
      assertEquals("20", arbitrageCalculator.getTrades().getLast().quantity.toString());
      arbitrageCalculator.complete();
      assertEquals("35.0", arbitrageCalculator.getProfit().toString());
    }
  }

  @Nested
  public class profitable_depthEvent2_trades_BNBBTC_BNBETH_ETCBTC {
    @Test
    public void has_profit_of_23point98BTC() {
      createProfitableDepthEvent2();
      CoinPair bnbbtc = new CoinPairImpl("BNBBTC");
      bnbbtc.update(depthEventBnbBtc);
      ArbitrageCalculator arbitrageCalculator = ArbitrageCalculatorImpl.startTrade(bnbbtc);

      CoinPair bnbeth = new CoinPairImpl("BNBETH");
      bnbeth.update(depthEventBnbEth);
      arbitrageCalculator.takeBid(bnbeth);

      CoinPair etcbtc = new CoinPairImpl("ETHBTC");
      etcbtc.update(depthEventEthBtc);
      arbitrageCalculator.takeBid(etcbtc);
      arbitrageCalculator.complete();

      assertEquals("23.98", arbitrageCalculator.getProfit().toString());

    }

    @Test
    public void reduces_BTC_to_80_on_first_takeBid() {
      createProfitableDepthEvent2();
      CoinPair bnbbtc = new CoinPairImpl("BNBBTC");
      bnbbtc.update(depthEventBnbBtc);
      ArbitrageCalculator arbitrageCalculator = ArbitrageCalculatorImpl.startTrade(bnbbtc);

      CoinPair bnbeth = new CoinPairImpl("BNBETH");
      bnbeth.update(depthEventBnbEth);
      assertEquals("300", arbitrageCalculator.getTrades().getFirst().quantity.toString());
      arbitrageCalculator.takeBid(bnbeth);
      assertEquals("40", arbitrageCalculator.getTrades().getFirst().quantity.toString());
    }

    @Test
    public void reduces_BNB_to_2_on_second_takeBid() {
      createProfitableDepthEvent2();
      CoinPair bnbbtc = new CoinPairImpl("BNBBTC");
      bnbbtc.update(depthEventBnbBtc);
      ArbitrageCalculator arbitrageCalculator = ArbitrageCalculatorImpl.startTrade(bnbbtc);

      CoinPair bnbeth = new CoinPairImpl("BNBETH");
      bnbeth.update(depthEventBnbEth);
      arbitrageCalculator.takeBid(bnbeth);

      CoinPair etcbtc = new CoinPairImpl("ETHBTC");
      etcbtc.update(depthEventEthBtc);
      arbitrageCalculator.takeBid(etcbtc);
      arbitrageCalculator.complete();
      assertEquals("2", arbitrageCalculator.getTrades().get(0).quantity.toString());
      assertEquals("2", arbitrageCalculator.getTrades().get(1).quantity.toString());
      assertEquals("4", arbitrageCalculator.getTrades().get(2).quantity.toString());
    }
  }

  @Nested
  public class zero_depthEvent_trades_BNBBTC_BNBETH_ETCBTC {
    @Test void makes_0_profit() {
      createZeroProfitDepthEvent();
      CoinPair bnbbtc = new CoinPairImpl("BNBBTC");
      bnbbtc.update(depthEventBnbBtc);
      ArbitrageCalculator arbitrageCalculator = ArbitrageCalculatorImpl.startTrade(bnbbtc);

      CoinPair bnbeth = new CoinPairImpl("BNBETH");
      bnbeth.update(depthEventBnbEth);
      arbitrageCalculator.takeBid(bnbeth);

      CoinPair etcbtc = new CoinPairImpl("ETHBTC");
      etcbtc.update(depthEventEthBtc);
      arbitrageCalculator.takeBid(etcbtc);
      arbitrageCalculator.complete();
      assertEquals("0.0", arbitrageCalculator.getProfit().toString());
    }
  }

  @Nested
  public class lose_depthEvent_trades_BNBBTC_BNBETH_ETCBTC {
    @Test
    public void makes_199_BTC_loss() {
      createLoseDepthEvent();
      CoinPair bnbbtc = new CoinPairImpl("BNBBTC");
      bnbbtc.update(depthEventBnbBtc);
      ArbitrageCalculator arbitrageCalculator = ArbitrageCalculatorImpl.startTrade(bnbbtc);

      CoinPair bnbeth = new CoinPairImpl("BNBETH");
      bnbeth.update(depthEventBnbEth);
      arbitrageCalculator.takeBid(bnbeth);

      CoinPair etcbtc = new CoinPairImpl("ETHBTC");
      etcbtc.update(depthEventEthBtc);
      arbitrageCalculator.takeBid(etcbtc);
      arbitrageCalculator.complete();

      assertEquals("-199.000", arbitrageCalculator.getProfit().toString());
    }
  }

  @Nested
  public class If_no_asks_or_bids {
    @Disabled
    public void dont_throw() {
      DepthEvent depthEvent = new DepthEvent();
      depthEvent.setSymbol("BNBBTC");
      depthEvent.setUpdateId(1);
      depthEvent.setBids(OrderBookEntryFactory.make(Arrays.asList()));
      depthEvent.setAsks(OrderBookEntryFactory.make(Arrays.asList("0.01", "300")));
      CoinPair bnbbtc = new CoinPairImpl("BNBBTC");
      bnbbtc.update(depthEvent);
      ArbitrageCalculator arbitrageCalculator = ArbitrageCalculatorImpl.startTrade(bnbbtc);
      assertTrue(true);
    }
  }
}
