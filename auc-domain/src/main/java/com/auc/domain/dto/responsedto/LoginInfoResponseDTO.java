package com.auc.domain.dto.responsedto;


import com.auc.dao.Role;
import com.auc.dao.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;

import java.io.Serializable;
import org.springframework.util.CollectionUtils;


/**
 * @author zhangqi
 */
@SuppressWarnings("deprecation")
@Data
@ApiModel(value = "登录用户信息dto")
public class LoginInfoResponseDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "用户ID")
  private Integer userId;
  @ApiModelProperty(value = "数据源ID")
  private String sourceId;
  @ApiModelProperty(value = "数据源编码")
  private String sourceCode;
  @ApiModelProperty(value = "真实姓名")
  private String realName;
  @ApiModelProperty(value = "登录名")
  private String userName;
  @ApiModelProperty(value = "员工编码")
  private String personnelCode;
  @ApiModelProperty(value = "防止CSRF随机数")
  private String state;
  @ApiModelProperty(value = "权限")
  private List<String> authorities;

  public LoginInfoResponseDTO(User user,List<String> authorities) {
    if (user != null) {
      this.userId = user.getId();
      this.sourceId = user.getSourceId().toString();
      this.sourceCode = user.getSourceCode();
      this.realName = user.getRealName();
      this.userName = user.getUserName();
      this.personnelCode = user.getPersonnelCode();
    }
    if (!CollectionUtils.isEmpty(authorities)) {
      this.authorities = authorities;
    }
  }

}
