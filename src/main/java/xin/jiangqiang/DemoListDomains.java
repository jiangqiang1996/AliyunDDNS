package xin.jiangqiang;

import com.aliyuncs.alidns.model.v20150109.DescribeDomainsRequest;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainsResponse;
import xin.jiangqiang.entity.Aliyun;
import xin.jiangqiang.util.AliDdnsUtils;

import java.util.List;

/**
 * @author JiangQiang
 * @date 2019/4/7 15:40
 */
public class DemoListDomains {
    /**
     * 设置参数
     *
     * @param request
     */
    public void setParam(DescribeDomainsRequest request, Aliyun yun) {
        // 设置参数
        request.putQueryParameter("RecordId", yun.getRecordId());
        request.putQueryParameter("RR", yun.getRr());
        request.putQueryParameter("Type", yun.getType());
        request.putQueryParameter("Value", yun.getIpV4());
        request.putQueryParameter("TTL", yun.getTTL());
    }

    /**
     * 解析DNS信息
     */
    public void analysisDns(Aliyun yun) {
        String actionName = "UpdateDomainRecord";
        DescribeDomainsRequest request = AliDdnsUtils.getRequest(actionName);
        DescribeDomainsResponse response;
        setParam(request, yun);

        try {
            response = AliDdnsUtils.getClient().getAcsResponse(request);
            List<DescribeDomainsResponse.Domain> list = response.getDomains();
            for (DescribeDomainsResponse.Domain domain : list) {
                System.out.println(domain.getDomainName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
