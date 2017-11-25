package dev.ljw.binance.endpoint;

public interface EndPoint {
  void onMessage(String message);
  void addMessageHandler(EndPointImpl.MessageHandler msgHandler);
}
