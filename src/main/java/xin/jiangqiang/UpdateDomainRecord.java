package xin.jiangqiang;

import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsRequest;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsResponse;
import xin.jiangqiang.entity.Aliyun;
import xin.jiangqiang.util.AliDdnsUtils;
import xin.jiangqiang.util.LocalPublicIpv4;
import xin.jiangqiang.util.PropertiesUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JiangQiang
 * @date 2019/4/7 15:40
 */
public class UpdateDomainRecord {
    /**
     * 设置域名参数
     *
     * @param request
     */
    public void setParam(DescribeDomainRecordsRequest request) {
        String domainName = PropertiesUtil.getProperty("DomainName");
        request.putQueryParameter("DomainName", domainName);
    }

    /**
     * 解析DNS信息
     */
    public void analysisDns(String[] rrs, String ipV4) throws Exception {

        // 获取解析的数据
        String actionName = "DescribeDomainRecords";
        DescribeDomainRecordsResponse response;
        // 获取request
        DescribeDomainRecordsRequest request = AliDdnsUtils.getRequestQuery(actionName);
        // 设置request参数
        setParam(request);
        response = AliDdnsUtils.getClient().getAcsResponse(request);
        // 声明解析对象
        DemoListDomains demo = new DemoListDomains();
        // 获取阿里云的数据
        List<DescribeDomainRecordsResponse.Record> list = response.getDomainRecords();
        if (list == null || list.isEmpty()) {
            return;
        }
        List<DescribeDomainRecordsResponse.Record> records = new ArrayList<>();
        for (DescribeDomainRecordsResponse.Record rec : list) {
            for (String rr : rrs) {
                if (rec.getRR().equals(rr)) {
                    records.add(rec);
                }
            }
        }
        if (records.isEmpty()) {
            return;
        }

        try {
            for (DescribeDomainRecordsResponse.Record rec : records) {
                System.out.println("记录名：" + rec.getRR());
                if (rec.getValue().equals(ipV4)) {
                    // 不需要更新，继续下次循环
                    System.out.println("当前域名解析地址为：" + ipV4 + "不需要更新！");
                } else {
                    Aliyun yun = new Aliyun();
                    System.out.println("更新域名：" + rec.getRR() + "." + rec.getDomainName());
                    System.out.println("当前域名解析地址为：" + ipV4);
                    // 进行替换关键数据
                    yun.setIpV4(ipV4);
                    //只替换ip，其余的属性保持不变
                    yun.setRecordId(rec.getRecordId());
                    yun.setRr(rec.getRR());
                    yun.setTTL(rec.getTTL());
                    yun.setType(rec.getType());
                    System.out.println("域名更换ip开始");
                    demo.analysisDns(yun);
                    System.out.println("域名更换ip结束");
                }
            }
        } catch (Exception e) {
            System.out.println("----------------");
            e.printStackTrace();
            System.out.println("域名更换异常");
            throw new Exception();
        }
    }
}
