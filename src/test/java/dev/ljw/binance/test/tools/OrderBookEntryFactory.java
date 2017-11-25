package dev.ljw.binance.test.tools;

import com.binance.api.client.domain.market.OrderBookEntry;

import java.util.ArrayList;
import java.util.List;

public class OrderBookEntryFactory {
  static public OrderBookEntry make(String price, String quantity) {
    OrderBookEntry o = new OrderBookEntry();
    o.setPrice(price);
    o.setQty(quantity);
    return o;
  }
  static public List<OrderBookEntry> make(List<String> priceQuantityList) {
    List<OrderBookEntry> orderBookEntries = new ArrayList<>();
    for (int i = 0; i < priceQuantityList.size(); i = i + 2 ) {
      orderBookEntries.add(make(priceQuantityList.get(i), priceQuantityList.get(i + 1)));
    }
    return orderBookEntries;
  }
}
