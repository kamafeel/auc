package com.auc.service;

import com.auc.dao.AccountConvert;
import com.auc.dubbo.user.dao.MergeUser;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 登录名转换配置表 服务类
 * </p>
 *
 * @author qin.ye
 * @since 2019-10-21
 */
public interface IAccountConvertService extends IService<AccountConvert> {
  List<String> importExcel(MultipartFile file,String userId);
  MergeUser convertUserName(MergeUser original, String userId,String clientId);
}
