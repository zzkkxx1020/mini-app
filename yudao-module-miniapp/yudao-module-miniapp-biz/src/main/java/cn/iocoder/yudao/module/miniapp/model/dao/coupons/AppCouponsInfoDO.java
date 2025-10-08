package cn.iocoder.yudao.module.miniapp.model.dao.coupons;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 优惠券
 * @TableName app_coupons_info
 */
@TableName(value ="app_coupons_info")
@Data
public class AppCouponsInfoDO extends TenantBaseDO {
    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 优惠券名称
     */
    @NotNull(message = "优惠券名称不能为空")
    private String courseName;

    /**
     * 优惠券可用类型 0 全部课程 1 定向课程
     */
    @NotNull(message = "优惠券可用课程类型不能为空")
    private Integer courseType;

    /**
     * 定向课程id
     */
    private Long courseId;

    /**
     * 优惠券可用类型 0 全部会员 1 定向会员
     */
    @NotNull(message = "优惠券可用会员类型不能为空")
    private Integer userType;

    /**
     * 定向会员id
     */
    private Long memberId;

    /**
     * 优惠价格
     */
    @NotNull(message = "优惠价格不能为空")
    private String price;

    /**
     * 创建者名称
     */
    private String createUserName;

    /**
     * 优惠开始时间
     */
    private LocalDateTime startTime;

    /**
     * 优惠结束时间
     */
    private LocalDateTime endTime;

    /**
     * 发放数量 默认-1 不限
     */
    @NotNull(message = "发放数量不能为空")
    private Integer size = -1;
}