package com.hunt.controller;

import com.hunt.model.dto.LoginUserInfo;
import com.hunt.model.entity.SysUser;
import com.hunt.service.SysUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import system.DataStatus;
import system.ResponseCode;
import system.Result;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author ouyangan
 * @Date 2016/10/8/13:37
 * @Description
 */
@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @param platform 平台
     * @param request
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public Result login(@RequestParam String username,
                        @RequestParam String password,
                        @RequestParam int platform,
                        HttpServletRequest request) {
        SysUser user = sysUserService.selectUserByLoginName(username);
        if (user == null) {
            return Result.error(ResponseCode.unknown_account.getMsg());
        }
        if (user.getState() == DataStatus.forbidden.getCode()) {
            return Result.error(ResponseCode.forbidden_account.getMsg());
        }
        SecurityUtils.getSubject().login(new UsernamePasswordToken(username, password));
        LoginUserInfo loginUserInfo = sysUserService.selectUserLoginInfo(user.getId());
        loginUserInfo.setPlatform(platform);
        request.getSession().setAttribute("userInfo", loginUserInfo);
        return Result.success(loginUserInfo);

    }
}
