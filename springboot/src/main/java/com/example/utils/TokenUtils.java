package com.example.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.common.Constants;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.service.AdminService;
import com.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * JWT Token utility class
 * Handles token generation and user authentication
 */
@Component
public class TokenUtils {

    private static final Logger log = LoggerFactory.getLogger(TokenUtils.class);

    private static AdminService staticAdminService;
    private static UserService staticUserService;

    @Resource
    AdminService adminService;
    @Resource
    UserService userService;

    @PostConstruct
    public void setUserService() {
        staticAdminService = adminService;
        staticUserService = userService;
    }

    /**
     * Generate JWT token
     * @param data User data to encode in token (format: userId-role)
     * @param sign Secret key for signing the token
     * @return Generated JWT token string
     */
    public static String createToken(String data, String sign) {
        return JWT.create().withAudience(data) // Store userId-role in token as payload
                .withExpiresAt(DateUtil.offsetHour(new Date(), 2)) // Token expires in 2 hours
                .sign(Algorithm.HMAC256(sign)); // Use password as token secret key
    }

    /**
     * Get currently logged-in user information from JWT token
     * @return Account object of current user, or empty Account if not found
     */
    public static Account getCurrentUser() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader(Constants.TOKEN);
            if (ObjectUtil.isNotEmpty(token)) {
                String userRole = JWT.decode(token).getAudience().get(0);
                String userId = userRole.split("-")[0];  // Extract user ID
                String role = userRole.split("-")[1];    // Extract role
                if (RoleEnum.ADMIN.name().equals(role)) {
                    return staticAdminService.selectById(Integer.valueOf(userId));
                } else if (RoleEnum.USER.name().equals(role)) {
                    return staticUserService.selectById(Integer.valueOf(userId));
                }
            }
        } catch (Exception e) {
            log.error("Failed to get current user information", e);
        }
        return new Account();  // Return empty account object if user not found
    }
}

