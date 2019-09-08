import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

public class Config {

  public String getHost () {
    return host;
  }

  public void setHost (String host) {
    this.host = host;
  }

  public String getPassword () {
    return password;
  }

  public void setPassword (String password) {
    this.password = password;
  }

  public long getInterval () {
    return interval;
  }

  public void setInterval (long interval) {
    this.interval = interval;
  }

  public String getLogFile () {
    return logFile;
  }

  public void setLogFile (String logFile) {
    this.logFile = logFile;
  }

  public String[] getIgnoreIps () {
    return ignoreIps;
  }

  public void setIgnoreIps (String[] ignoreIps) {
    this.ignoreIps = ignoreIps;
  }

  public String getIpMode () {
    return ipMode;
  }

  public void setIpMode (String ipMode) {
    this.ipMode = ipMode;
  }

  public String getParameter () {
    return parameter;
  }

  public void setParameter (String parameter) {
    this.parameter = parameter;
  }

  private String host;
  private String domain;
  private String password;
  private long interval = 0;
  private String logFile;
  private String[] ignoreIps;
  private String ipMode;
  private String parameter;

  public static Config fromProperties () {
    return fromProperties("default.Properties");
  }

  private Config () {
  }

  public String getDomain () {
    return domain;
  }

  public void setDomain (String domain) {
    this.domain = domain;
  }

  public static Config fromProperties (String configFile) {
    HashMap<String, String> h = getConfig(configFile);
    Config ret = new Config();
    ret.host = h.get("host");
    ret.domain = h.get("domain");
    ret.password = h.get("password");
    ret.interval = Long.parseLong(h.get("interval"));
    ret.logFile = h.get("logFile");
    ret.ignoreIps = h.get("ignoreIps").split(";");
    ret.ipMode = h.get("ipMode");
    ret.parameter = h.get("parameter");
    return ret;
  }

  private static HashMap<String, String> getConfig (String filename) {
    Properties pps = new Properties();
    try {
      pps.load(new FileInputStream(filename));
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      System.out.println("getConfig FileNotFoundException");
      System.exit(0);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      System.out.println("getConfig IOException");
      System.exit(0);
    }
    Enumeration<?> enum1 = pps.propertyNames();
    HashMap<String, String> h = new HashMap<>();
    while (enum1.hasMoreElements()) {
      String strKey = (String) enum1.nextElement();
      String strValue = pps.getProperty(strKey);
      h.put(strKey, strValue);
    }
    return h;
  }

}
