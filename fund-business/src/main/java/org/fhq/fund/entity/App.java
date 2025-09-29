package org.fhq.fund.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fhq.common.core.validation.AddGroup;
import org.fhq.common.core.validation.DeleteGroup;
import org.fhq.common.core.validation.UpdateGroup;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * <p>
 * App应用信息
 * </p>
 *
 * @author fuhaiq@gmail.com
 * @since 2025-09-28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("app")
@Schema(name = "App", description = "App应用信息")
public class App extends Model<App> {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Null(message = "主键不能指定", groups = {AddGroup.class})
    @NotNull(message = "主键不能为空", groups = {UpdateGroup.class, DeleteGroup.class})
    private Long id;

    @TableField("name")
    private String name;

    @TableField("author")
    private String author;

    @TableField("price")
    private Integer price;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @Null(message = "不能指定创建时间", groups = {AddGroup.class, UpdateGroup.class})
    private Instant createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @Null(message = "不能指定更新时间", groups = {AddGroup.class, UpdateGroup.class})
    private Instant updateTime;

    @TableField(value = "create_by", fill = FieldFill.INSERT)
    @Null(message = "不能指定创建人", groups = {AddGroup.class, UpdateGroup.class})
    private String createBy;

    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    @Null(message = "不能指定更新人", groups = {AddGroup.class, UpdateGroup.class})
    private String updateBy;

    @TableField(value = "version", fill = FieldFill.INSERT)
    @Version
    @Null(message = "不能指定版本号", groups = {AddGroup.class})
    @NotNull(message = "版本号不能为空", groups = {UpdateGroup.class, DeleteGroup.class})
    private Integer version;

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
