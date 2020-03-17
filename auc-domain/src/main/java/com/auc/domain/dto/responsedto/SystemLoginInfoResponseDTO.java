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
@ApiModel(value = "系统登录用户信息dto")
public class SystemLoginInfoResponseDTO extends LoginInfoResponseDTO {


  public SystemLoginInfoResponseDTO(User user, List<String> authorities) {
    super(user,authorities);
  }
}
