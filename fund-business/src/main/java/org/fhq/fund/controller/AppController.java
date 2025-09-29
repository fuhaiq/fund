package org.fhq.fund.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.fhq.common.core.exception.BusinessException;
import org.fhq.common.core.validation.AddGroup;
import org.fhq.common.core.validation.DeleteGroup;
import org.fhq.common.core.validation.UpdateGroup;
import org.fhq.common.util.ErrorCode;
import org.fhq.common.util.R;
import org.fhq.common.util.WebUtils;
import org.fhq.fund.entity.App;
import org.fhq.fund.service.IAppService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * App应用信息 前端控制器
 * </p>
 *
 * @author fuhaiq@gmail.com
 * @since 2025-09-28
 */
@RestController
@RequestMapping("/app")
@AllArgsConstructor
@SecurityRequirement(name = "Keycloak")
@Tag(name = "App应用信息模块")
public class AppController {

    private final IAppService appService;

    @GetMapping("page")
    @Operation(summary = "分页查询App应用信息")
    @Parameters({
            @Parameter(name = "page", description = "当前页码,默认1开始", schema = @Schema(type = "integer", defaultValue = "1", minimum = "1")),
            @Parameter(name = "size", description = "每页显示记录数,默认10", schema = @Schema(type = "integer", defaultValue = "10", maximum = "100")),
            @Parameter(description = "排序字段格式: 字段名,(asc|desc). 默认按照创建时间倒排序,支持多字段排序."
                    , name = "sort"
                    , array = @ArraySchema(schema = @Schema(type = "string")))
    })
    public R page(@PageableDefault(page = 1, sort = {"create_time"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return R.ok(appService.page(WebUtils.getPage(pageable)));
    }

    @GetMapping("{id}")
    @Operation(summary = "根据id查询App应用信息")
    public R getById(@PathVariable("id") Long id) {
        return R.ok(appService.getById(id));
    }

    @PostMapping
    @Operation(summary = "新增App应用信息")
    public R save(@Validated(AddGroup.class) @RequestBody App app) {
        if (!appService.save(app)) {
            throw new BusinessException(HttpStatus.CONFLICT, ErrorCode.CREATE_FAILED);
        }
        return R.ok();
    }

    @PutMapping
    @Operation(summary = "根据id修改App应用信息")
    public R updateById(@Validated(UpdateGroup.class) @RequestBody App app) {
        if (!appService.updateById(app)) {
            throw new BusinessException(HttpStatus.CONFLICT, ErrorCode.UPDATE_FAILED);
        }
        return R.ok();
    }

    @DeleteMapping
    @Operation(summary = "根据id删除App应用信息")
    public R removeById(@Validated(DeleteGroup.class) @RequestBody App app) {
        if (!appService.removeById(app)) {
            throw new BusinessException(HttpStatus.CONFLICT, ErrorCode.DELETE_FAILED);
        }
        return R.ok();
    }

}
