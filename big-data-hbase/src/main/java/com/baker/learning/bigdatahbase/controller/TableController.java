package com.baker.learning.bigdatahbase.controller;

import com.baker.learning.bigdatahbase.model.RespVO;
import com.baker.learning.bigdatahbase.service.TableService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @description
 * @date 2020/5/5 15:16
 */
@RestController
@RequestMapping("/table")
@Api(value = "/table", description = "table")
@Slf4j
public class TableController {

    @Autowired
    private TableService tableService;


    @ApiOperation(value = "createTable", notes = "createTable", httpMethod = "POST", response = Object.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tableName", value = "表名称", required = true, paramType = "form", dataType = "String"),
            @ApiImplicitParam(name = "familyColumn", value = "列簇", required = true, paramType = "form", dataType = "String")
    })
    @PostMapping("/createTable")
    public RespVO createTable(@RequestParam(name = "tableName", defaultValue = "") String tableName,
                              @RequestParam(name = "familyColumn", defaultValue = "") String familyColumn) {
        try {
            Set<String> familyColumns = new HashSet<>(Arrays.asList(familyColumn.split(",")));
            tableService.createTable(tableName, familyColumns);
            return new RespVO().setSuccess("");
        } catch (Exception e) {
            log.error("异常：", e);
            return new RespVO().setFailure(e.getMessage());
        }
    }

    @DeleteMapping("/dropTable")
    public RespVO dropTable(String tableName) {
        try {
            tableService.dropTable(tableName);
            return new RespVO().setSuccess("");
        } catch (Exception e) {
            log.error("异常：", e);
            return new RespVO().setFailure(e.getMessage());
        }
    }

    @PutMapping("/addTableFamilyColumn")
    public RespVO addTableFamilyColumn(String tableName, String familyColumnName) {
        try {
            tableService.addTableFamilyColumn(tableName, familyColumnName);
            return new RespVO().setSuccess("");
        } catch (Exception e) {
            log.error("异常：", e);
            return new RespVO().setFailure(e.getMessage());
        }
    }

    @DeleteMapping("/dropTableFamilyColumn")
    public RespVO dropTableFamilyColumn(String tableName, String familyColumnName) {
        try {
            tableService.dropTableFamilyColumn(tableName, familyColumnName);
            return new RespVO().setSuccess("");
        } catch (Exception e) {
            log.error("异常：", e);
            return new RespVO().setFailure(e.getMessage());
        }
    }

    @GetMapping("/getFamilys/{tableName}")
    public RespVO getFamilys(@PathVariable String tableName) {
        try {
            List<String> familys = tableService.getFamilys(tableName);
            return new RespVO().setSuccess(familys);
        } catch (Exception e) {
            log.error("异常：", e);
            return new RespVO().setFailure(e.getMessage());
        }
    }
}
