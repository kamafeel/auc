package com.auc.web.controller;

import com.auc.service.IPrivilegeService;
import com.auc.dao.Privilege;
import com.auc.domain.dto.requestdto.QueryPageParam;
import com.auc.web.help.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/privilege")
public class PrivilegeController {
    @Autowired
    public IPrivilegeService privilegeService;

    /**
     * 保存
     *
     * @param privilege 传递的实体
     */
    @PreAuthorize("hasAuthority('system:privilege:save')")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Result<Boolean> save(@RequestBody Privilege privilege) {
        privilegeService.save(privilege);
        return Result.success(true);
    }

    //删除对象信息
    @PreAuthorize("hasAuthority('system:privilege:delete')")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result<Boolean> delete(@PathVariable("id") Integer id) {
        privilegeService.deleteById(id);
        return Result.success(true);
    }

    //获取对象
    @PreAuthorize("hasAuthority('system:privilege:query')")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result<Privilege> get(@PathVariable("id") Integer id) {
        return Result.success(privilegeService.getById(id));
    }


    /**
     * 分页查询数据：
     */
//    @RequestMapping(value = "/page",method = RequestMethod.POST)
//    public Result<IPage<Privilege>> page(@RequestBody QueryPageParam dto) {
//        return Result.success(privilegeService.page(new Page<Privilege>(dto.getPageNo(),dto.getPageSize())));
//    }
}
