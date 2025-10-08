package cn.iocoder.yudao.module.miniapp.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CouponsVO {

    /**
     * id
     */
    private Long id;

    /**
     * 优惠券名称
     */
    private String courseName;

    /**
     * 优惠券可用类型 0 全部课程 1 定向课程
     */
    private Integer courseType;

    /**
     * 定向课程id
     */
    private Long courseId;

    /**
     * 优惠券可用类型 0 全部会员 1 定向会员
     */
    private Integer userType;

    /**
     * 优惠价格
     */
    private Double price;


    /**
     * 优惠开始时间 空代表长期可用
     */
    private LocalDateTime startTime;

    /**
     * 优惠结束时间
     */
    private LocalDateTime endTime;

    /**
     * 剩余数量
     */
    private Integer size;

    /**
     * 当前用户领取状态 0 未领取 1 已领取
     * app使用时 0 可用 1 不可用 2 已使用
     */
    private Integer status;
}
