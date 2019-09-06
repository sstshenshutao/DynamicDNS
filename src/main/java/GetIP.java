import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class GetIP {

  private static String getIPwithNetworkInterface (String[] ignoreIps) {
    String ret = null;
    try {
      for (Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces(); e.hasMoreElements(); ) {
        NetworkInterface ni = e.nextElement();
        String name = ni.getName();
        boolean br = false;
        if (!name.contains("docker") && !name.contains("lo")) {
          for (Enumeration<InetAddress> enumIpAddrs = ni.getInetAddresses(); enumIpAddrs.hasMoreElements(); ) {
            InetAddress addr = enumIpAddrs.nextElement();
            br = false;
            if (!addr.isLoopbackAddress()) {
              String ipAddress = addr.getHostAddress().toString();
              for (String s : ignoreIps) {
                if (ipAddress.matches(s.trim())) {
                  br = true;
                  break;
                }
              }
              if (br) {
                continue;
              } else {
                ret = ipAddress;
                break;
              }
            }
          }
        }
        if (br) {
          continue;
        } else {
          break;
        }
      }
    } catch (Exception e) {
    }
    return ret;
  }

  private static String getIPwithHttp (Config config) {
    CloseableHttpClient httpclient = HttpClients.createDefault();
    String getInfo = config.getParameter();
    HttpGet httpGet = new HttpGet(getInfo);
    CloseableHttpResponse response1 = null;
    String txt = null;
    try {
      response1 = httpclient.execute(httpGet);
      txt = EntityUtils.toString(response1.getEntity());
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    Gson gson = new GsonBuilder().create();
    IP iptxt = gson.fromJson(txt, IP.class);
    return iptxt.getIp();
  }

  public static String getIp (Config config) {
    if (config.getIpMode().toLowerCase().equals("http") || config.getIpMode().toLowerCase().equals("https")) {
      return getIPwithHttp(config);
    } else {
      return getIPwithNetworkInterface(config.getIgnoreIps());
    }
  }

//  public static void main (String[] args) {
//    Config config = null;
//    if (args != null && args.length > 0) {
//      config = Config.fromProperties(args[0]);
//    } else {
//      config = Config.fromProperties();
//    }
//    System.out.println(getIPwithHttp(config));
//  }

}
