package cn.iocoder.yudao.module.miniapp.model.dao.coupons;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 优惠券领取记录
 * @TableName app_coupons_detail
 */
@TableName(value ="app_coupons_detail")
@Data
public class AppCouponsDetailDO {
    /**
     * 
     */
    @TableId
    private Long id;

    /**
     * 领取人
     */
    private Long userId;

    /**
     * 优惠券id
     */
    @NotNull(message = "优惠券id不能为空")
    private Long couponsId;

    /**
     * 状态 0 可用 1不可用 2已使用
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}