package com.auc.web.controller;

import com.auc.dao.Operational;
import com.auc.service.IOperationalService;
import com.auc.domain.vo.PrivilegeOperationalRelationVO;
import com.auc.web.help.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/operational")
public class OperationalController {
    @Autowired
    public IOperationalService operationalService;

    /**
     * 保存
     *
     * @param operational 传递的实体
     */
    @PreAuthorize("hasAuthority('system:operational:save')")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Result<Boolean> save(@RequestBody Operational operational) {
        operationalService.save(operational);

        return Result.success(true);
    }

    @PreAuthorize("hasAuthority('system:operational:save')")
    @RequestMapping(value = "/privilegeRelation", method = RequestMethod.POST)
    public Result savePrivilegeOperationalRelation(@RequestBody PrivilegeOperationalRelationVO privilegeOperationalRelation){
        operationalService.savePrivilegeOperationalRelation(privilegeOperationalRelation.getPrivilegeId(),
            privilegeOperationalRelation.getOperationalIds());
        return Result.success();
    }

    //删除对象信息
    @PreAuthorize("hasAuthority('system:operational:delete')")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result<Boolean> delete(@PathVariable("id") Integer id) {
        operationalService.deleteById(id);
        return Result.success(true);
    }

    //获取对象
    @PreAuthorize("hasAuthority('system:operational:query')")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result<Operational> get(@PathVariable("id") Integer id) {
        return Result.success(operationalService.getById(id));
    }


    /**
     * 分页查询数据：
     */
//    @RequestMapping(value = "/page", method = RequestMethod.POST)
//    public Result<IPage<Operational>> page(@RequestBody QueryPageParam dto) {
//        return Result.success(operationalService.page(new Page<Operational>(dto.getPageNo(), dto.getPageSize())));
//    }
}
