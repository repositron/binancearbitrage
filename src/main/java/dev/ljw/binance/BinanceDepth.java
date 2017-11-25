package dev.ljw.binance;

import com.binance.api.client.BinanceApiCallback;
import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.domain.event.DepthEvent;

public class BinanceDepth {

  private BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance();
  private BinanceApiWebSocketClient client = factory.newWebSocketClient();

  // calls depthEvent when data appears on endpoint.
  public void monitorCoinPair(String symbol, BinanceApiCallback<DepthEvent> depthEvent) {
    client.onDepthEvent(symbol.toLowerCase(), response ->
      depthEvent.onResponse(response));
  }

}
