package com.auc.common.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;

/**
 * 客户端IP地址获取工具类
 */
public class IpUtils {

  private IpUtils() {
  }

  public static String getIpAddr(HttpServletRequest request) {
    if (request == null) {
      return "";
    }
    String ip = request.getHeader("X-Forwarded-For");
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("HTTP_CLIENT_IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("HTTP_X_FORWARDED_FOR");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("HTTP_X_REAL_FORWARDED_FOR");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getRemoteAddr();
    }

    if (ip.contains(",")) {
      ip = ip.split(",")[0];
    }

    if (ip.indexOf(':') != -1) {
      try {
        return ipV6ToV4(ip);
      } catch (UnknownHostException e) {
        throw new RuntimeException(e);
      }
    }

    return ip;
  }

  private static String ipV6ToV4(String ipV6) throws UnknownHostException {
    InetAddress address = InetAddress.getByName(ipV6);
    if ("0:0:0:0:0:0:0:1".equals(address.getHostAddress())) {
      return "127.0.0.1";
    }

    return InetAddress.getByAddress(
        Arrays.copyOfRange(address.getAddress(), 12, 16))
        .getHostAddress();
  }

  public static String getLocalIP() {
    try {
      Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
      InetAddress ip = null;
      while (networkInterfaces.hasMoreElements()) {
        NetworkInterface networkInterface = (NetworkInterface) networkInterfaces.nextElement();
        Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
        while (inetAddresses != null && inetAddresses.hasMoreElements()) {
          ip = (InetAddress) inetAddresses.nextElement();
          if (!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {
            return ip.getHostAddress();
          }
        }
      }
    } catch (SocketException e) {

    }
    return "127";
  }

  /**
   * 获取域名
   */
  public static String getHost(HttpServletRequest request) {
    StringBuffer http = new StringBuffer();
    if (request == null) {
      return "";
    }
    if ("http".equals(request.getScheme().toString())) {
      http.append("http://");
    }
    if ("https".equals(request.getScheme().toString())) {
      http.append("https://");
    }
    http.append(request.getServerName() + ":" + request.getServerPort());
    return http.toString();
  }


}