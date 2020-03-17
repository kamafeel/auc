package com.auc.domain.dto.responsedto;

import com.auc.dao.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;


/**
 * @author zhangqi
 */
@SuppressWarnings("deprecation")
@Data
@ApiModel(value = "域登录用户信息dto")
public class DomainLoginInfoResponseDTO extends LoginInfoResponseDTO {

  @ApiModelProperty(value = "域编码")
  private String domainCode;

  public DomainLoginInfoResponseDTO(User user, List<String> authorities, String domainCode) {
    super(user,authorities);
    this.domainCode = domainCode;
  }

}
