package com.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.auth.service.TokenService;
import com.common.entity.vo.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>AuthFilter</pre>
 * 统一认证过滤器
 * @author <p>ADROITWOLF</p> 2021-05-28
 */
@Component
@Slf4j
public class AuthFilter implements GlobalFilter, Ordered {

    @Autowired
    TokenService tokenService;

    private List<String> whiteList =  new ArrayList<>();

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    private static  final  String TOKEN = "Authentication";

    public AuthFilter() {
        this.whiteList.add("/**/api/**");
        this.whiteList.add("/iblog-attach/images/**");
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();

        String path = request.getURI().getPath();


        String token = request.getHeaders().getFirst(TOKEN);
        ServerHttpResponse response = exchange.getResponse();
        // 是否是跨域请求
        HttpMethod method = request.getMethod();

        if(method.equals(HttpMethod.OPTIONS)){
            return chain.filter(exchange);
        }

        //是否属于白名单
        if(shouldNotFilter(path)){
            log.info("不需要进行拦截{}",path);
            return chain.filter(exchange);
        }

        if(StringUtils.isEmpty(token)){
            log.info("用户没有登录");
            BaseResponse result = BaseResponse.builder().status(HttpStatus.UNAUTHORIZED.value()).message("用户未登录，请先登录！").build();
            return responseResult(response,result);
        }
        if(!tokenService.verifierToken(token)){
            BaseResponse result = BaseResponse.builder().status(HttpStatus.UNAUTHORIZED.value()).message("用户Token无效").build();
            return responseResult(response,result);
        }

        if(tokenService.isExpire(token)){
            BaseResponse result = BaseResponse.builder().status(HttpStatus.UNAUTHORIZED.value()).message("用户Token已经过期").build();
            return responseResult(response,result);
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }


    private boolean shouldNotFilter(String path){
        return this.whiteList.stream().anyMatch(p -> antPathMatcher.match(p,path));
    }


    /**
     * 返回http结果
     * @param response
     * @param result
     * @return
     */
    private Mono<Void> responseResult(ServerHttpResponse response, Object result) {
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setStatusCode(HttpStatus.OK);
        DataBufferFactory bufferFactory = response.bufferFactory();
        byte[] bytes = JSON.toJSONString(result).getBytes();
        DataBuffer buffer = bufferFactory.wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }
}
