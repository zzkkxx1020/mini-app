package cn.iocoder.yudao.module.miniapp.controller.admin;

import cn.hutool.core.bean.BeanUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.miniapp.model.dao.course.AppCourseInfo;
import cn.iocoder.yudao.module.miniapp.model.quary.CourseQuery;
import cn.iocoder.yudao.module.miniapp.model.vo.CourseVO;
import cn.iocoder.yudao.module.miniapp.service.AppCourseInfoService;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDateTime;

/**
 * @author DELL
 * @date 2025/9/30
 * @description
 */
@Tag(name = "小程序 - 课程管理")
@RestController
@RequestMapping("/course")
@Validated
public class CourseController {

    @Resource
    private AppCourseInfoService courseInfoService;

    @Resource
    private AdminUserApi adminUserApi;


    /**
     * 获取课程列表
     * @return
     */
    @PostMapping("/getCourseList")
    @Operation(summary = "获取课程列表")
    public CommonResult<PageResult<CourseVO>> getCourseList(@RequestBody CourseQuery courseQuery) {
        Page<AppCourseInfo> page = new Page<>(courseQuery.getPageNo(), courseQuery.getPageSize());
        LambdaQueryWrapper<AppCourseInfo> wrapper = new LambdaQueryWrapper<AppCourseInfo>()
                .like(StringUtils.isNotBlank(courseQuery.getName()), AppCourseInfo::getCourseName, courseQuery.getName())
                .orderByDesc(AppCourseInfo::getCreateTime);
        Page<AppCourseInfo> infoPage = courseInfoService.page(page, wrapper);
        // todo: 缺少图片
        PageResult<CourseVO> result = new PageResult<>();
        result.setTotal(infoPage.getTotal());
        result.setList(BeanUtil.copyToList(infoPage.getRecords(),CourseVO.class));
        return CommonResult.success(result);
    }

    /**
     * 新增课程
     */
    @PostMapping("/addCourse")
    @Operation(summary = "新增课程")
    public CommonResult<Boolean> addCourse(@RequestBody @Valid AppCourseInfo courseInfo) {
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        AdminUserRespDTO user = adminUserApi.getUser(loginUserId);
        courseInfo.setCreateUserName(user.getNickname());
        courseInfo.setCreateTime(LocalDateTime.now());
        boolean save = courseInfoService.save(courseInfo);
        return CommonResult.success(save);
    }

    /**
     * 删除课程
     */
    @PostMapping("/delCourse")
    @Operation(summary = "删除课程")
    public CommonResult<Boolean> delCourse(@RequestBody AppCourseInfo courseInfo) {
        if (courseInfo.getId() == null){
            throw new RuntimeException("缺少课程id");
        }
        boolean del = courseInfoService.removeById(courseInfo.getId());
        return CommonResult.success(del);
    }

    /**
     * 修改课程
     */
    @PostMapping("/updateCourse")
    @Operation(summary = "修改课程")
    public CommonResult<Boolean> updateCourse(@RequestBody AppCourseInfo courseInfo) {
        boolean save = courseInfoService.updateById(courseInfo);
        return CommonResult.success(save);
    }
}
