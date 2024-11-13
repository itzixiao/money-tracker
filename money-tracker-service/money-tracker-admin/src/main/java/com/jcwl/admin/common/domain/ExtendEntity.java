package com.jcwl.admin.common.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jcwl.common.core.domain.JcwlBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 基础数据管理对象 tdzy_basic_data
 *
 * @author jcwl
 * @date 2023-09-26
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExtendEntity extends JcwlBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    @JsonIgnore
    private Long userId;

    /**
     * 部门ID
     */
    @ApiModelProperty("部门ID")
    @JsonIgnore
    private Long deptId;
}
