package cn.iocoder.yudao.module.miniapp.service.impl;


import cn.iocoder.yudao.module.miniapp.mapper.AppCouponsDetailMapper;
import cn.iocoder.yudao.module.miniapp.mapper.AppCouponsInfoMapper;
import cn.iocoder.yudao.module.miniapp.model.dao.coupons.AppCouponsDetailDO;
import cn.iocoder.yudao.module.miniapp.model.dao.coupons.AppCouponsInfoDO;
import cn.iocoder.yudao.module.miniapp.service.AppCouponsDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
* @author DELL
* @description 针对表【app_coupons_detail(优惠券领取记录)】的数据库操作Service实现
* @createDate 2025-09-29 18:59:10
*/
@Service
public class AppCouponsDetailServiceImpl extends ServiceImpl<AppCouponsDetailMapper, AppCouponsDetailDO>
    implements AppCouponsDetailService {

    @Resource
    private AppCouponsInfoMapper appCouponsInfoMapper;

    @Override
    public void changeStatus(Long couponsId) {
        AppCouponsInfoDO appCouponsInfoDO = appCouponsInfoMapper.selectById(couponsId);
        if (appCouponsInfoDO.getEndTime()!=null && LocalDateTime.now().isAfter(appCouponsInfoDO.getEndTime())){
            this.lambdaUpdate()
                    .eq(AppCouponsDetailDO::getCouponsId, couponsId)
                    .set(AppCouponsDetailDO::getStatus, 1);
        }
    }
}




