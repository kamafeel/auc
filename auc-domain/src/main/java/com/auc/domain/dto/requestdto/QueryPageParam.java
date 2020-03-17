package com.auc.domain.dto.requestdto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * 分页查询入参
 */
@ApiModel(value = "分页查询")
public class QueryPageParam implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /**
   * 每页条数
   */
  @Setter
  @ApiModelProperty(value = "每页条数")
  private Integer pageSize;

  /**
   * 第几页
   */
  @Setter
  @ApiModelProperty(value = "第几页")
  private Integer pageNo;

  /**
   * 排序字段
   */
  @Getter
  @Setter
  @ApiModelProperty(value = "排序字段")
  private String sortField;

  /**
   * 升序，降序
   */
  @Getter
  @Setter
  @ApiModelProperty(value = "升序，降序")
  private String direction;

  public Integer getPageSize() {
    if (pageSize == null) {
      this.pageSize = 10;
    }
    return pageSize;
  }

  public Integer getPageNo() {
    if (pageNo == null) {
      this.pageNo = 1;
    }
    return pageNo;
  }

  @Override
  public String toString() {
    return "QueryPageParam [pageSize=" + pageSize + ", pageNo=" + pageNo + ", sortField="
        + sortField + ", direction=" + direction + "]";
  }
}
