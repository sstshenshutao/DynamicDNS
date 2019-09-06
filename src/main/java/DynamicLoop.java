import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * @author Shutao Shen
 */
public class DynamicLoop {

  public static void main (String[] args) {
    try {
      Thread.sleep(60000);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    // config :
    Config config = null;
    if (args != null && args.length > 0) {
      config = Config.fromProperties(args[0]);
    } else {
      config = Config.fromProperties();
    }
    createFile(config.getLogFile());
    Logger log = loginit(config.getLogFile());
    String stableIP = "init";
    while (true) {
      String newIp = GetIP.getIp(config);
      logging(log, "stableIP:" + stableIP + " | newIP:" + newIp);
      if (!newIp.equals(stableIP)) {
        stableIP = newIp;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String getInfo =
          "https://dynamicdns.park-your-domain.com/update?" + "host=@&domain=" + config.getHost() + "&" + "password=" + config.getPassword() + "&"
            + "ip=" + newIp;
        logging(log, "changing:"+getInfo);
        HttpGet httpGet = new HttpGet(getInfo);
        CloseableHttpResponse response1 = null;
        try {
          response1 = httpclient.execute(httpGet);
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        try {
          logging(log, response1.getStatusLine().toString());
          HttpEntity entity1 = response1.getEntity();
          logging(log, "http return:"+entity1.toString());
          // do something useful with the response body
          // and ensure it is fully consumed
          EntityUtils.consume(entity1);
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } finally {
          try {
            response1.close();
          } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        }
      }
      try {
        Thread.sleep(config.getInterval());
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

  private static void createFile (String ofilepath) {
    String filepath = ofilepath + ".log";
    File file = new File(filepath);
    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        System.out.println("createFile: " + filepath + " wrongIO");
        System.exit(0);
      }
    }
  }

  private static Logger loginit (String filename) {
    Logger log = Logger.getLogger(filename);
    log.setLevel(Level.ALL);
    FileHandler fileHandler = null;
    try {
      fileHandler = new FileHandler(filename + ".log");
    } catch (SecurityException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    fileHandler.setLevel(Level.ALL);
    fileHandler.setFormatter(new Formatter() {

      @Override
      public String format (LogRecord record) {
        // TODO Auto-generated method stub
        Date date = new Date();
        String sDate = date.toString();
        return "[" + sDate + "]" + "[" + record.getLevel() + "]" + record.getMessage() + "\n";
      }
    });
    log.addHandler(fileHandler);
    return log;
  }

  private static void logging (Logger log, String info) {
    log.info(info);
  }

}
