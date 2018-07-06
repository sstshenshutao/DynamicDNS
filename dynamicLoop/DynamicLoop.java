package dynamicLoop;

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
 *
 */
public class DynamicLoop {
	private static String host;
	private static String passwd ;
	private static String ip ;
	private static String[] ignoreIps;
	private static long interval = 0;
	public static void main(String[] args) {
		// config :
		DynamicLoop d = new DynamicLoop();
		String logfile= null;
		String configFile="default.Properties";
		if (args != null && args.length > 0) {
			configFile= args[0];
		}
		HashMap<String, String> h = d.getConfig(configFile);
		host = h.get("host");
		passwd = h.get("password");
		interval = Long.parseLong(h.get("interval"));
		logfile = h.get("logFile");
		ignoreIps = h.get("ignoreIps").split(";");
		d.createFile(logfile);
		Logger log = d.loginit(logfile);
		while (true) {
			String newIp = getIp();
			logging(log, newIp);
			if (!newIp.equals(ip)) {
				ip = newIp;
				CloseableHttpClient httpclient = HttpClients.createDefault();
				String getInfo = "https://dynamicdns.park-your-domain.com/update?" + "host=@&domain=" + host + "&"
						+ "password=" + passwd + "&" + "ip=" + ip;
				logging(log, getInfo);
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
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
	private void createFile(String ofilepath) {
		String filepath= ofilepath + ".log";
		File file=new File(filepath);
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("createFile: " +filepath +" wrongIO");
				System.exit(0);
			}
		}
	}
	private Logger loginit(String filename) {
		Logger log = Logger.getLogger(filename);
		log.setLevel(Level.ALL);
		FileHandler fileHandler = null;
		try {
			fileHandler = new FileHandler(filename+".log");
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
			public String format(LogRecord record) {
				// TODO Auto-generated method stub
				Date date = new Date();
				String sDate = date.toString();
				return "[" + sDate + "]" + "[" + record.getLevel() + "]" + record.getMessage()
						+ "\n";
			}
		});
		log.addHandler(fileHandler);
		return log;
	}
	
	private static void logging(Logger log,String info) {
		log.info(info);
	}

	private static String getIp() {
			String ret = null;
			try {
				for (Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces(); e.hasMoreElements();) {
					NetworkInterface ni = e.nextElement();
					String name = ni.getName();
					boolean br = false;
					if (!name.contains("docker") && !name.contains("lo")) {
						for (Enumeration<InetAddress> enumIpAddrs = ni.getInetAddresses(); enumIpAddrs.hasMoreElements();) {
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

	private HashMap<String, String> getConfig(String filename) {
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
