package cn.iocoder.yudao.module.miniapp.model.vo;

import lombok.Data;

@Data
public class AppCourseVO {

    private Long id;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 课程类别
     */
    private Integer courseCategory;

    /**
     * 课程性质
     */
    private Integer courseNature;

    /**
     * 价格
     */
    private Double price;

    /**
     * 简介
     */
    private String courseIntro;

    /**
     * 支付应用id
     */
    private Integer appId;

    /**
     * 封面图
     */
    private String homeFile;
}
