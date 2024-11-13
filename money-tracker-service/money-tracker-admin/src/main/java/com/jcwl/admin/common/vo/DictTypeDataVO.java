package com.jcwl.admin.common.vo;

import com.jcwl.common.core.domain.entity.SysDictData;
import com.jcwl.common.core.domain.entity.SysDictType;
import lombok.Data;

import java.util.List;

/**
 * 字典类型
 *
 * @author jcwl
 * @date 2023-12-02
 */
@Data
public class DictTypeDataVO extends SysDictType {

    /**
     * 字典数据
     */
    private List<SysDictData> dataList;


}
