package com.auc.web.controller;

import com.auc.service.IDatasourceService;
import com.auc.dao.Datasource;
import com.auc.domain.dto.requestdto.QueryPageParam;
import com.auc.web.help.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 数据源接口
 * @author zhangqi73
 */
@RestController
@Api(value = "数据源接口", tags = "数据源接口")
@RequestMapping("api/datasource")
public class DatasourceController {
    @Autowired
    public IDatasourceService datasourceService;

    //获取对象
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public Result<Datasource> get(@PathVariable("id")Long id)
    {
        return Result.success(datasourceService.getById(id));
    }


    //获取所有对象
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public Result<List<Datasource>> list(){
        return Result.success(datasourceService.list(null));
    }

}
