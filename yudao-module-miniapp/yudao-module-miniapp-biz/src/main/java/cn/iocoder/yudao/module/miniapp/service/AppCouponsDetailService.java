package cn.iocoder.yudao.module.miniapp.service;


import cn.iocoder.yudao.module.miniapp.model.dao.coupons.AppCouponsDetailDO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author DELL
* @description 针对表【app_coupons_detail(优惠券领取记录)】的数据库操作Service
* @createDate 2025-09-29 18:59:10
*/
public interface AppCouponsDetailService extends IService<AppCouponsDetailDO> {

    /**
     * 修改优惠券状态 (已经领取人的)
     * @param couponsId
     */
    void changeStatus(Long couponsId);
}
