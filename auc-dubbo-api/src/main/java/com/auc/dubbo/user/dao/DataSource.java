package com.auc.dubbo.user.dao;

import lombok.Data;

import java.io.Serializable;

@Data
public class DataSource implements Serializable {

  private static final long serialVersionUID=1L;

  private String sourceId;

  private String sourceCode;

  private String sourceName;
}
