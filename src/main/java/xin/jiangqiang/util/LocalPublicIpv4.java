package xin.jiangqiang.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author JiangQiang
 * @date 2019/4/7 15:38
 */
public class LocalPublicIpv4 {
    public String publicip() {
        String IP = null;
        try {
            List<String> strings = getURLCollection("http://myip.ipip.net/");
            String HtmlContent = "";
            for (String str : strings) {
                HtmlContent += str;
            }
            String[] strs = HtmlContent.split("(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]" +
                    "\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)");
            IP = HtmlContent.substring(strs[0].length(), HtmlContent.indexOf(strs[1])).trim();
        } catch (Exception e) {
            if (IP == null) {
                IP = PropertiesUtil.getProperty("IP");
            }
        } finally {
            System.out.println("当前IP： " + IP);
            return IP;
        }
    }

    public static List<String> getURLCollection(String address) throws Exception {
        List<String> list = new LinkedList<String>();
        URL url = new URL(address);
        URLConnection conn = url.openConnection();
        conn.connect();
        InputStream in = conn.getInputStream();
        InputStreamReader input = new InputStreamReader(in, "UTF-8");
        BufferedReader buf = new BufferedReader(input);
        String nextLine = buf.readLine();

        while (nextLine != null) {
            list.add(nextLine);
            nextLine = buf.readLine();
        }

        return list;
    }

    public static void main(String[] args) {
        LocalPublicIpv4 ip = new LocalPublicIpv4();
        System.out.println(ip.publicip());
    }
}
