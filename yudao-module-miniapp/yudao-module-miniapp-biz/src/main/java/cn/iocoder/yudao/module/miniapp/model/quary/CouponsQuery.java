package cn.iocoder.yudao.module.miniapp.model.quary;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 获取课程列表 query")
@Data
public class CouponsQuery extends PageParam {

    /**
     * 状态 0 可用 1过期
     */
    private Integer status;
    /**
     * 优惠券可用类型 0 全部课程 1 定向课程
     */
    private Integer courseType;

    /**
     * 优惠券可用类型 0 全部会员 1 定向会员
     */
    private Integer userType;
}
