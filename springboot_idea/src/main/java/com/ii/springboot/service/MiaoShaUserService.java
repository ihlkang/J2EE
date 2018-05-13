package com.ii.springboot.service;

import com.ii.springboot.dao.MiaoShaUserDao;

import com.ii.springboot.domain.MiaoShaUser;
import com.ii.springboot.exception.GloblaException;
import com.ii.springboot.redis.MiaoshaUserKey;
import com.ii.springboot.redis.RedisService;
import com.ii.springboot.result.CodeMsg;
import com.ii.springboot.util.MD5Util;
import com.ii.springboot.util.UUIDUtil;
import com.ii.springboot.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class MiaoShaUserService {
    public static final String COOKI_NAME_TOKEN = "token";
    @Autowired
    MiaoShaUserDao miaoshaUserDao;

    @Autowired
    RedisService redisService;

    public MiaoShaUser getByToken(HttpServletResponse response,String token) {
        if(StringUtils.isEmpty(token)){
            return null;
        }
        MiaoShaUser user = redisService.get(MiaoshaUserKey.token, token, MiaoShaUser.class);
        //延迟有效期
        if(user != null){
            addCookie(response,token,user);
        }
        return user;
    }

    public MiaoShaUser getById(long id){
        //取缓存
        MiaoShaUser user = redisService.get(MiaoshaUserKey.getById,""+id,MiaoShaUser.class);
        if(user!=null){
            return user;
        }
        //取数据库
        user = miaoshaUserDao.getById(id);
        if(user != null){
            redisService.set(MiaoshaUserKey.getById,""+id,user);
        }
        return user;
    }

    public boolean updatePassword(String token, long id, String formPass) {
        //取user
        MiaoShaUser user = getById(id);
        if(user == null) {
            throw new GloblaException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //更新数据库
        MiaoShaUser toBeUpdate = new MiaoShaUser();
        toBeUpdate.setId(id);
        toBeUpdate.setPassword(MD5Util.formPassToDBPass(formPass, user.getSalt()));
        miaoshaUserDao.update(toBeUpdate);
        //处理缓存
        redisService.delete(MiaoshaUserKey.getById, ""+id);
        user.setPassword(toBeUpdate.getPassword());
        redisService.set(MiaoshaUserKey.token, token, user);
        return true;
    }


    public boolean login(HttpServletResponse response, LoginVo loginVo){
        if(loginVo == null){
            throw new GloblaException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String formpass = loginVo.getPassword();
        //判断手机号是否存在
        MiaoShaUser user = getById(Long.parseLong(mobile));
        if(user == null){
            throw new GloblaException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //验证密码
        String dbpass = user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(formpass, saltDB);
        if(!calcPass.equals(dbpass)){
            throw new GloblaException(CodeMsg.PASSWORD_ERROR);
        }
        String token = UUIDUtil.uuid();
        addCookie(response,token,user);
        return true;
    }

    private void addCookie(HttpServletResponse response,String token,MiaoShaUser user){
        //登陆成功后生成cookie
        redisService.set(MiaoshaUserKey.token,token,user);
        Cookie cookie = new Cookie(COOKI_NAME_TOKEN,token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
