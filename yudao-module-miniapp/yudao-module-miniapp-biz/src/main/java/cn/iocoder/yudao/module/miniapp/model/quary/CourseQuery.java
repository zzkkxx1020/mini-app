package cn.iocoder.yudao.module.miniapp.model.quary;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 获取课程列表 query")
@Data
public class CourseQuery extends PageParam {

    /**
     * 课程名称
     */
    @Schema(description = "课程名称，模糊查询")
    private String name;
}
