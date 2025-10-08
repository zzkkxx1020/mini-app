package cn.iocoder.yudao.module.miniapp.model.dao.course;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 课程表
 * @TableName app_course_info
 */
@TableName(value ="app_course_info")
@Data
public class AppCourseInfo extends TenantBaseDO {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 课程名称
     */
    @NotBlank(message = "课程名称不能为空")
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
     * 课程标识
     */
    private String courseTag;

    /**
     * 价格
     */
    @NotNull(message = "课程价格不能为空")
    private Double price;

    /**
     * 创建者名称
     */
    private String createUserName;


    /**
     * 是否公开（0否，1是）
     */
    private Integer isPublic;

    /**
     * 排序
     */
    private Long sort;

    /**
     * 简介
     */
    private String courseIntro;

    /**
     * 支付应用id
     */
    @NotNull(message = "支付应用id不能为空")
    private Integer appId;

    /**
     * 封面图
     */
    private String homeFile;
}