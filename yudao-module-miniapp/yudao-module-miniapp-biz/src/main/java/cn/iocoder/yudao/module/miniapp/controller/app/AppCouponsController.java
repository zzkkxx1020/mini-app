package cn.iocoder.yudao.module.miniapp.controller.app;

import cn.hutool.core.bean.BeanUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.miniapp.model.dao.coupons.AppCouponsDetailDO;
import cn.iocoder.yudao.module.miniapp.model.dao.coupons.AppCouponsInfoDO;
import cn.iocoder.yudao.module.miniapp.model.vo.CouponsVO;
import cn.iocoder.yudao.module.miniapp.service.AppCouponsDetailService;
import cn.iocoder.yudao.module.miniapp.service.AppCouponsInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "小程序 - 优惠券")
@RestController
@RequestMapping("/appCoupons")
@Validated
public class AppCouponsController {

    @Resource
    private AppCouponsInfoService appCouponsService;

    @Resource
    private AppCouponsDetailService appCouponsDetailService;

    /**
     * 获取优惠券列表
     * 需要用户登录
     */
    @GetMapping("/getCouponsList")
    @Operation(summary = "获取优惠券列表")
    public CommonResult<List<CouponsVO>> getCouponsList() {
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        List<AppCouponsInfoDO> list = appCouponsService.lambdaQuery()
                .and(w -> w.eq(AppCouponsInfoDO::getMemberId, loginUserId).or().isNull(AppCouponsInfoDO::getMemberId))
                .and(w -> w.ge(AppCouponsInfoDO::getEndTime, LocalDateTime.now()).or().isNull(AppCouponsInfoDO::getEndTime))
                .ne(AppCouponsInfoDO::getSize, 0).list();
        List<CouponsVO> couponsVOS = BeanUtil.copyToList(list, CouponsVO.class);
        for (CouponsVO couponsVO : couponsVOS) {
            Long count = appCouponsDetailService.lambdaQuery()
                    .eq(AppCouponsDetailDO::getCouponsId, couponsVO.getId())
                    .eq(AppCouponsDetailDO::getUserId, loginUserId).count();
            couponsVO.setStatus(count == 1 ? 1 : 0);
        }
        return CommonResult.success(couponsVOS);
    }

    /**
     * 当前用户以领取的优惠券
     * 需要用户登录
     */
    @GetMapping("/getMyCouponsList")
    @Operation(summary = "当前用户已领取的优惠券")
    public CommonResult<List<CouponsVO>> getMyCouponsList() {
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        List<AppCouponsDetailDO> list = appCouponsDetailService.lambdaQuery()
                .eq(AppCouponsDetailDO::getUserId, loginUserId).list();
        List<CouponsVO> couponsVOS = new ArrayList<>();
        if (list.size() > 0) {
            List<AppCouponsInfoDO> appCouponsInfoDOS = appCouponsService.listByIds(list.stream().map(AppCouponsDetailDO::getCouponsId).collect(Collectors.toList()));
            couponsVOS = BeanUtil.copyToList(appCouponsInfoDOS, CouponsVO.class);
            for (AppCouponsDetailDO appCouponsDetailDO : list) {
                CouponsVO appCouponsInfoDO = couponsVOS.stream().filter(detail -> detail.getId().equals(appCouponsDetailDO.getCouponsId())).findFirst().get();
                if (appCouponsDetailDO.getStatus() == 0 && appCouponsInfoDO.getEndTime() != null && appCouponsInfoDO.getEndTime().isBefore(LocalDateTime.now())) {
                    // 优惠券的状态需要实时校验 并更新状态
                    appCouponsInfoDO.setStatus(1);
                    appCouponsDetailService.lambdaUpdate().eq(AppCouponsDetailDO::getCouponsId, appCouponsInfoDO.getId())
                            .eq(AppCouponsDetailDO::getStatus,0).set(AppCouponsDetailDO::getStatus,1).update();
                } else {
                    appCouponsInfoDO.setStatus(appCouponsDetailDO.getStatus());
                }
            }
        }
        return CommonResult.success(couponsVOS);
    }

    /**
     * 领取优惠券
     * 需要用户登录
     */
    @PostMapping("/receiveCoupons")
    @Operation(summary = "领取优惠券")
    public CommonResult<Boolean> receiveCoupons(@RequestBody @Valid AppCouponsDetailDO couponsDetailDO) {
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        boolean b = appCouponsDetailService.lambdaQuery().eq(AppCouponsDetailDO::getUserId, loginUserId)
                .eq(AppCouponsDetailDO::getCouponsId, couponsDetailDO.getCouponsId()).count() > 0;
        if (b) {
            return CommonResult.error(9999, "已领取");
        }
        couponsDetailDO.setUserId(loginUserId);
        couponsDetailDO.setCreateTime(LocalDateTime.now());
        boolean save = appCouponsDetailService.save(couponsDetailDO);
        if (save) {
            appCouponsService.lambdaUpdate()
                    .eq(AppCouponsInfoDO::getId, couponsDetailDO.getCouponsId())
                    .gt(AppCouponsInfoDO::getSize, 0)
                    .setSql("size = size - 1")
                    .update();
        }

        return CommonResult.success(save);
    }
}
