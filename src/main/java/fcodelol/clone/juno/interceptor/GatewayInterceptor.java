package fcodelol.clone.juno.interceptor;


import fcodelol.clone.juno.dto.ApiEntity;
import fcodelol.clone.juno.exception.CustomException;
import fcodelol.clone.juno.service.AuthorizationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class GatewayInterceptor implements HandlerInterceptor {
    private static Logger logger = LogManager.getLogger(GatewayInterceptor.class);
    @Autowired
    AuthorizationService authorizationService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            return verifyRequest(request);
        } catch (Exception e) {
            response.sendError(400,"Wrong token or token time out ");
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    private boolean verifyRequest(HttpServletRequest request) {
        String httpMethod = request.getMethod();
        String servletPath = request.getServletPath();
        String accessToken = request.getHeader(GatewayConstant.AUTHORIZATION_HEADER);
        String role;
        if(accessToken == null){
            logger.info("No access token");
            role = "none";
        }
        else {
            logger.info("Access token:{}",accessToken);
            authorizationService.getRoleByToken(accessToken);
            role = authorizationService.getRoleByToken(accessToken);
            //if token is not valid
            if(role.equals(GatewayConstant.ROLE_GUESS))
            {
                logger.warn("Wrong token or token time out");
                throw new CustomException(400,"Wrong token or token time out");
            }
        }
        logger.info("Api:{} Role:{}",servletPath,role);
        ApiEntity apiEntity = getMatchingAPI(httpMethod, servletPath);
        if (apiEntity != null) {
            if (accessToken == null) {
                logger.error("Authorization field in header is null or empty");
                return false;
            }
            if (verifyRole(apiEntity.getRole(), role)) {
                logger.info("Request validated. Start forward request to controller");
                return true;
            } else {
                logger.info("User don't have permission to access");
                return false;
            }
        }
        logger.info("Request validated. Start forward request to controller");
        return true;
    }

    private boolean verifyRole(String pathRole, String userRole) {
        String[] roles = pathRole.split(GatewayConstant.ROLE_SPLIT_STRING);
        for (String role : roles)
            if (role.equals(userRole)) {
                return true;
            }
        return false;
    }

    private ApiEntity getMatchingAPI(String httpMethod, String path) {
        AntPathMatcher matcher = new AntPathMatcher();
        for (ApiEntity apiEntity : GatewayConstant.apiEntities) {
            if (matcher.match(apiEntity.getPattern(), path) && httpMethod.equals(apiEntity.getHttpMethod())) {
                logger.info("Found api matched");
                return apiEntity;
            }
        }
        return null;
    }

}
