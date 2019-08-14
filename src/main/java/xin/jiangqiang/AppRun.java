package xin.jiangqiang;

import xin.jiangqiang.util.LocalPublicIpv4;
import xin.jiangqiang.util.PropertiesUtil;

import java.util.Arrays;

/**
 * @author JiangQiang
 * @date 2019/4/7 15:39
 */
public class AppRun {
    public static void main(String[] args) {
        System.out.println(Arrays.toString(args));
        int minute = 0;
        int hour = 0;
        try {
            minute = Integer.parseInt(args[args.length - 1]);
        } catch (Exception e) {
            String cycle = PropertiesUtil.getProperty("Cycle");
            String[] cyc = cycle.trim().split(":");
            if (cyc.length == 2) {
                hour = Integer.parseInt(cyc[0]);
                minute = Integer.parseInt(cyc[1]);
            } else if (cyc.length == 1) {
                minute = Integer.parseInt(cyc[0]);
            }
        }
        String[] rrs = null;
        if (args.length > 1) {
            rrs = Arrays.copyOfRange(args, 0, args.length - 1);
        } else {
            String rr = PropertiesUtil.getProperty("RR");
            rrs = rr.trim().split(",");
        }
        minute = hour * 60 + minute;


        System.out.println("当前执行周期为：" + 3 + "分钟");
        for (int i = 1; i<=3; i++) {
            try {
                System.out.println("第" + i + "次执行");
                run(rrs);
                System.out.println("本次执行完毕，等待" + 3 + "分钟进行下次执行");
                Thread.sleep(3 * 60 * 1000);
            } catch (Exception e) {
                System.out.println("解析出现异常，一分钟后将重新执行！");
                try {
                    Thread.sleep(60 * 1000);
                    run(rrs);
                } catch (Exception ex) {
                }
            }
        }

        System.out.println("当前执行周期为：" + minute + "分钟");
        for (int i = 4; ; ) {
            try {
                System.out.println("第" + i + "次执行");
                run(rrs);
                System.out.println("本次执行完毕，等待" + minute + "分钟进行下次执行");
                Thread.sleep(minute * 60 * 1000);
                i++;
            } catch (Exception e) {
                System.out.println("解析出现异常，一分钟后将重新执行！");
                try {
                    Thread.sleep(60 * 1000);
                    run(rrs);
                } catch (Exception ex) {
                }
            }
        }

    }

    public static void run(String[] rrs) throws Exception {
        System.out.println("开始ddns检查");
        // 获取公网ip
        LocalPublicIpv4 ip = new LocalPublicIpv4();
        String ipV4 = ip.publicip();
        if (ipV4 == null || "".equals(ipV4.trim())) {
            System.out.println("没有获取到IP");
            ipV4= PropertiesUtil.getProperty("IP");
        }
        UpdateDomainRecord record = new UpdateDomainRecord();
        record.analysisDns(rrs,ipV4);
        System.out.println("ddns运行结束");
    }
}
