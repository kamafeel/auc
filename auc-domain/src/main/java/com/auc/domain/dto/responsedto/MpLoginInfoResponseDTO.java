package com.auc.domain.dto.responsedto;

import com.auc.dao.User;
import io.swagger.annotations.ApiModel;
import java.util.List;
import lombok.Data;


/**
 * @author zhangqi
 */
@SuppressWarnings("deprecation")
@Data
@ApiModel(value = "手机号码登录用户信息dto")
public class MpLoginInfoResponseDTO extends LoginInfoResponseDTO {

  public MpLoginInfoResponseDTO(User user, List<String> authorities) {
    super(user,authorities);
  }
}
