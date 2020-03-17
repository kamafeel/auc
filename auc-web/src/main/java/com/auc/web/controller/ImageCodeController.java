package com.auc.web.controller;

import com.auc.common.enums.ErrorCodeEnum;
import com.auc.common.enums.RedisKey;
import com.auc.service.RedisService;
import com.auc.web.help.Result;
import com.google.code.kaptcha.Producer;
import io.swagger.annotations.Api;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 验证码控制层
 *
 */
@RestController
@RequestMapping("api/imageCode")
@Api(value = "图像验证码模块", tags = "图像验证码模块")
public class ImageCodeController {
    @Autowired
    private Producer captchaProducer;

    @Autowired
    private RedisService redisService;
    /**
     * 生成验证码
     *
     * @param response
     * @return void
     * @author zhanghao
     * @date 2019/7/17 2019/7/17
     */
    @GetMapping("code")
    public void getImageCode(HttpServletResponse response,@RequestParam("uuid") String uuid) throws Exception {

        //禁止缓存
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        //设置响应格式为png图片
        response.setContentType("image/png");

        //为验证码创建一个文本
        String codeText = captchaProducer.createText();
        //将验证码存到redis
        redisService.set(RedisKey.IMG_CODE_KEY,uuid,codeText,60,TimeUnit.SECONDS);
        // 用创建的验证码文本生成图片
        BufferedImage bi = captchaProducer.createImage(codeText);
        ServletOutputStream out = response.getOutputStream();
        //写出图片
        ImageIO.write(bi, "png", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
    }

    /**
     * 图片验证码校验
     */
    @GetMapping("check")
    public Result<Boolean> checkImageCode(
            @RequestParam(value = "image_code") String validImageCode,
            @RequestParam("uuid")String uuid){
        try {
            if (StringUtils.isBlank(validImageCode)){
                return Result.failed(ErrorCodeEnum.IMG_CODE_IS_EMPTY,"验证码不能为空");
            }
            String imageCode = redisService.get(RedisKey.IMG_CODE_KEY, uuid);
            if (!imageCode.equals(validImageCode)){
                return Result.failed(ErrorCodeEnum.IMG_CODE_INCORRECT,"验证码不正确");
            }
            return Result.success();
        } catch (Exception e) {
            return Result.failed(e);
        }
    }


}

