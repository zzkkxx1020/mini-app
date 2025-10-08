package cn.iocoder.yudao.module.miniapp.model.vo;

import lombok.Data;

@Data
public class CouponsDetailVO {

    /**
     * 优惠券id
     */
    private Long id;

    /**
     * 领取人
     */
    private Long userId;

    /**
     * 领取人姓名
     */
    private String userName;

    /**
     * 使用状态 0 未使用 1 已使用 2 已过期
     */
    private Integer status;


}
