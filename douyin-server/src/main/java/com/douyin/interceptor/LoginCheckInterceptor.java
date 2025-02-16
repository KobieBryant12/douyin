//package com.douyin.interceptor;
//
//import com.alibaba.fastjson.JSONObject;
//import com.douyin.result.Result;
//import com.douyin.utils.JwtUtils;
//import io.jsonwebtoken.Claims;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
///**
// * 拦截器
// */
//@Slf4j
//@Component
//public class LoginCheckInterceptor implements HandlerInterceptor {
//
//    @Autowired
//    private RedisTemplate redisTemplate;
//
//    @Override //目标资源方法运行前运行，返回为true：放行 返回false：不放行
//    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
//
//        //获取请求url。
//        String url = req.getRequestURL().toString();
//
//        log.info("请求的url：{}", url);
//
//        //判断请求url中是否包含login或exit或者alterStatus/register，如果包含，说明是登录/退出/启用禁用/注册操作，放行
//        if(url.contains("login") || url.contains("exit") || url.contains("alterStatus") || url.contains("register")){
//            return true;
//        }
//
//
//        //获取请求头中的令牌(token)。
//        String jwt = req.getHeader("token");
//
//        //判断令牌是否存在，如果不存在，返回错误结果(未登录)
//        if(!StringUtils.hasLength(jwt)){
//            log.info("请求头token为空！");
//            Result error = Result.error("NOT_LOGIN");
//            //手动转换 对象--JSON ---------->阿里巴巴fastJSON工具包
//            String notLogin = JSONObject.toJSONString(error);
//            resp.getWriter().write(notLogin);
//            return false;
//        }
//
//        //解析token，如果解析失败，返回错误结果(未登录)
//        try {
//            Claims claims = JwtUtils.parseJWT(jwt);
//
//            //没有解析失败进一步验证redis中的jwt
//            String username = (String) claims.get("username");
//            Object jwtRedis = redisTemplate.opsForValue().get(username);
//            if(jwtRedis == null){
//
//                Result error = Result.error("NOT_LOGIN");
//                //手动转换 对象--JSON ---------->阿里巴巴fastJSON工具包
//                String notLogin = JSONObject.toJSONString(error);
//                resp.getWriter().write(notLogin);
//                return false;
//            }
//
//        } catch (Exception e) {//解析失败
//            e.printStackTrace();
//            log.info("解析令牌失败！");
//            Result error = Result.error("NOT_LOGIN");
//            //手动转换 对象--JSON ---------->阿里巴巴fastJSON工具包
//            String notLogin = JSONObject.toJSONString(error);
//            resp.getWriter().write(notLogin);
//            return false;
//        }
//
//
//        //放行。
//        log.info("令牌合法，放行");
//        return true;
//
//    }
//
//    @Override //目标资源运行后运行
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//
//    }
//
//    @Override //视图渲染完毕后运行，最后运行
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//
//    }
//}
