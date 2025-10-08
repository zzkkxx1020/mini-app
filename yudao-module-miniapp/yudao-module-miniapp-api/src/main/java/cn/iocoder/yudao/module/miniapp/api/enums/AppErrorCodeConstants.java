package cn.iocoder.yudao.module.miniapp.api.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * miniapp 错误码枚举类
 *
 * miniapp 错误码区间 [1-023-000-000 ~ 1-024-000-000)
 */
public interface AppErrorCodeConstants {

    ErrorCode PARAM_ERROR = new ErrorCode(1-023-000-0004, "参数异常");

    ErrorCode COURSE_NOT_EXISTS = new ErrorCode(1-023-000-0001, "课程不存在");

    ErrorCode COURSES_NOT_EMPTY = new ErrorCode(1-023-000-0002, "定向课程不能为空");

    ErrorCode USERS_NOT_EMPTY = new ErrorCode(1-023-000-0003, "定向会员不能为空");


}
