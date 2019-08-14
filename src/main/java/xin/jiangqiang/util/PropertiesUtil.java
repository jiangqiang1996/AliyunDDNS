package xin.jiangqiang.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author JiangQiang
 * @date 2019/4/7 15:38
 */
public class PropertiesUtil {
    //指定配置文件地址resource目录下:
    static Properties prop = new Properties();
    static {
        InputStream is = PropertiesUtil.class.getClassLoader().getResourceAsStream("ddns.properties");
        try {
            prop.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return prop.getProperty(key);
    }

    public static String getProperty(String key, String value) {
        return getProperty(key, value);
    }

    public static void main(String[] args) {
        System.out.println(PropertiesUtil.getProperty("AccessKeyID"));
    }
}
