package com.auc.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PrivilegeOperationalRelationVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer privilegeId;

    private List<Integer> operationalIds;
}
