package com.auc.dubbo.user.dao;

import lombok.Data;

import java.io.Serializable;

@Data
public class MergeUser implements Serializable {

  private static final long serialVersionUID=1L;

  private String userName;

  private String sourceCode;
}
