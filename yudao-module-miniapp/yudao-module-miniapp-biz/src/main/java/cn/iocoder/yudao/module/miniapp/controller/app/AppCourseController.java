package cn.iocoder.yudao.module.miniapp.controller.app;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.miniapp.model.dao.course.AppCourseInfo;
import cn.iocoder.yudao.module.miniapp.model.vo.CourseVO;
import cn.iocoder.yudao.module.miniapp.service.AppCourseInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import java.util.List;

/**
 * 课程管理
 *
 * @author DELL
 * @date 2025/9/30
 * @description
 */
@Tag(name = "小程序 - 课程管理")
@RestController
@RequestMapping("/appCourse")
public class AppCourseController {

    @Resource
    private AppCourseInfoService courseInfoService;

    @GetMapping("/getCourseList")
    @PermitAll
    @Operation(summary = "获取课程列表")
    public CommonResult<List<CourseVO>> getCourseList() {
        List<AppCourseInfo> list = courseInfoService.lambdaQuery().eq(AppCourseInfo::getIsPublic, 1).list();
        return CommonResult.success(BeanUtils.toBean(list, CourseVO.class));
    }
}
