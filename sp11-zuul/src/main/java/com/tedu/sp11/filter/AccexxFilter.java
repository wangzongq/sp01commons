package com.tedu.sp11.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.tedu.web.util.JsonResult;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class AccexxFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return  FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
       return FilterConstants.PRE_DECORATION_FILTER_ORDER+1;
    }

    @Override
    public boolean shouldFilter() {
        //对指定的serviceId过滤,如果要过滤所有服务,直接返回true
        RequestContext ctx = RequestContext.getCurrentContext();
        String serviceId = (String) ctx.get(FilterConstants.SERVICE_ID_KEY);
        if (serviceId.equals("item-service")){
            return true;
        }
        return false;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String at = request.getParameter("token");
        if (at == null){
            //此设置会阻止请求被路由到后台微服务
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(200);
            ctx.setResponseBody(JsonResult.err().code(JsonResult.NOT_LOGIN).toString());
        }
        //zuul过滤器返回的数据设计为以后扩展使用，
        //目前该返回值没有被使用
        return null;
    }
}
