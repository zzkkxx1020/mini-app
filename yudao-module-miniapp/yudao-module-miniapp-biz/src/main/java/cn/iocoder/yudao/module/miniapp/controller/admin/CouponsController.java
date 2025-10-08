package cn.iocoder.yudao.module.miniapp.controller.admin;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.member.api.user.MemberUserApi;
import cn.iocoder.yudao.module.member.api.user.dto.MemberUserRespDTO;
import cn.iocoder.yudao.module.miniapp.api.enums.AppErrorCodeConstants;
import cn.iocoder.yudao.module.miniapp.model.dao.coupons.AppCouponsDetailDO;
import cn.iocoder.yudao.module.miniapp.model.dao.coupons.AppCouponsInfoDO;
import cn.iocoder.yudao.module.miniapp.model.dao.course.AppCourseInfo;
import cn.iocoder.yudao.module.miniapp.model.quary.CouponsQuery;
import cn.iocoder.yudao.module.miniapp.model.vo.CouponsDetailVO;
import cn.iocoder.yudao.module.miniapp.service.AppCouponsDetailService;
import cn.iocoder.yudao.module.miniapp.service.AppCouponsInfoService;
import cn.iocoder.yudao.module.miniapp.service.AppCourseInfoService;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Tag(name = "小程序 - 课程优惠券管理")
@RestController
@RequestMapping("/coupons")
@Validated
public class CouponsController {

    @Resource
    private AdminUserApi adminUserApi;

    @Resource
    private AppCourseInfoService courseInfoService;

    @Resource
    private AppCouponsInfoService appCouponsInfoService;

    @Resource
    private AppCouponsDetailService appCouponsDetailService;

    @Resource
    private MemberUserApi memberUserApi;

    /**
     * 新增优惠券
     */
    @PostMapping("/addCoupons")
    @Operation(summary = "新增优惠券")
    public CommonResult<Boolean> addCoupons(@RequestBody @Valid AppCouponsInfoDO couponsInfoDO) {
        AppCourseInfo byId = couponsInfoDO.getCourseId() == null ? null : courseInfoService.getById(couponsInfoDO.getCourseId());
        if (byId == null && couponsInfoDO.getCourseType() == 1) {
            throw new ServiceException(AppErrorCodeConstants.COURSE_NOT_EXISTS);
        }
        if((couponsInfoDO.getCourseType() == 0 && couponsInfoDO.getCourseId() != null) ||
                (couponsInfoDO.getUserType() == 0 && couponsInfoDO.getMemberId() != null)){
            throw new ServiceException(AppErrorCodeConstants.PARAM_ERROR);
        }
        if(couponsInfoDO.getCourseType() == 1 && couponsInfoDO.getCourseId() == null){
            throw new ServiceException(AppErrorCodeConstants.COURSES_NOT_EMPTY);
        }
        if(couponsInfoDO.getUserType() == 1 && couponsInfoDO.getMemberId() == null){
            throw new ServiceException(AppErrorCodeConstants.USERS_NOT_EMPTY);
        }
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        AdminUserRespDTO user = adminUserApi.getUser(loginUserId);
        couponsInfoDO.setCreateUserName(user.getNickname());
        return CommonResult.success(appCouponsInfoService.save(couponsInfoDO));
    }

    /**
     * 删除优惠券 以领取但未使用的 也删除
     */
    @PostMapping("/delCoupons")
    @Operation(summary = "删除优惠券")
    public CommonResult<Boolean> delCoupons(@RequestBody  AppCouponsInfoDO couponsInfoDO) {
        if (couponsInfoDO.getId() == null){
            throw new RuntimeException("缺少参数 id");
        }
        boolean b = appCouponsInfoService.removeById(couponsInfoDO.getId());
        if (b){
            appCouponsDetailService.lambdaUpdate()
                    .eq(AppCouponsDetailDO::getCouponsId, couponsInfoDO.getId())
                    .eq(AppCouponsDetailDO::getStatus,0).remove();
        }
        return CommonResult.success(b);
    }

    /**
     * 修改优惠券
     */
    @PostMapping("/updateCoupons")
    @Operation(summary = "修改优惠券")
    public CommonResult<Boolean> updateCoupons(@RequestBody AppCouponsInfoDO couponsInfoDO) {
        AppCourseInfo byId = couponsInfoDO.getCourseId() == null ? null : courseInfoService.getById(couponsInfoDO.getCourseId());
        if (byId == null && couponsInfoDO.getCourseType() == 1) {
            throw new RuntimeException("课程不存在");
        }
        return CommonResult.success(appCouponsInfoService.updateById(couponsInfoDO));
    }

    /**
     * 获取优惠券列表
     * @return
     */
    @PostMapping("/getCourseList")
    public CommonResult<PageResult<AppCouponsInfoDO>> getCourseList(@RequestBody CouponsQuery couponsQuery) {
        Page<AppCouponsInfoDO> page = new Page<>(couponsQuery.getPageNo(), couponsQuery.getPageSize());
        LambdaQueryWrapper<AppCouponsInfoDO> wrapper = new LambdaQueryWrapper<AppCouponsInfoDO>()
            .or(couponsQuery.getStatus()!=null && couponsQuery.getStatus()==0,
                    w -> w.ge(AppCouponsInfoDO::getEndTime, LocalDateTime.now()).or().isNull(AppCouponsInfoDO::getEndTime))
            .le(couponsQuery.getStatus()!=null && couponsQuery.getStatus()==1,
                    AppCouponsInfoDO::getEndTime, LocalDateTime.now())
            .eq(couponsQuery.getCourseType()!=null,
                    AppCouponsInfoDO::getCourseType, couponsQuery.getCourseType())
            .eq(couponsQuery.getUserType()!=null,
                    AppCouponsInfoDO::getUserType, couponsQuery.getUserType())
            .orderByDesc(AppCouponsInfoDO::getCreateTime);
        Page<AppCouponsInfoDO> infoPage = appCouponsInfoService.page(page, wrapper);

        PageResult<AppCouponsInfoDO> result = new PageResult<>();
        result.setTotal(infoPage.getTotal());
        result.setList(infoPage.getRecords());
        return CommonResult.success(result);
    }

    /**
     * 优惠券详情
     */
    @GetMapping("/getCouponsDetail")
    public CommonResult<List<CouponsDetailVO>> getCouponsDetail(@RequestParam Long couponsId) {
        appCouponsDetailService.changeStatus(couponsId);
        List<AppCouponsDetailDO> list = appCouponsDetailService.lambdaQuery().eq(AppCouponsDetailDO::getCouponsId, couponsId).list();
        List<CouponsDetailVO> detailVOS = new ArrayList<>();
        for (AppCouponsDetailDO appCouponsDetailDO : list) {
            CouponsDetailVO couponsDetailVO = new CouponsDetailVO();
            MemberUserRespDTO user = memberUserApi.getUser(appCouponsDetailDO.getUserId());
            couponsDetailVO.setId(appCouponsDetailDO.getCouponsId());
            couponsDetailVO.setUserId(appCouponsDetailDO.getUserId());
            couponsDetailVO.setUserName(user.getNickname());
            couponsDetailVO.setStatus(appCouponsDetailDO.getStatus());
            detailVOS.add(couponsDetailVO);
        }
        return CommonResult.success(detailVOS);
    }
}
