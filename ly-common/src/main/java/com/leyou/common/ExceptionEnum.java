package com.leyou.common;

import lombok.Getter;

/**
 * @author Nie ZongXin
 * @date 2019/9/6 22:03
 */
@Getter
public enum  ExceptionEnum {
    INVALID_FILE_TYPE(400, "无效的文件类型！"),
    INVALID_PARAM_ERROR(400, "无效的请求参数！"),
    INVALID_PHONE_NUMBER(400, "无效的手机号码"),
    INVALID_VERIFY_CODE(400, "验证码错误！"),
    INVALID_USERNAME_PASSWORD(400, "无效的用户名和密码！"),
    INVALID_SERVER_ID_SECRET(400, "无效的服务id和密钥！"),
    INVALID_NOTIFY_PARAM(400, "回调参数有误！"),
    INVALID_NOTIFY_SIGN(400, "回调签名有误！"),
    SEND_MESSAGE_REPEAT(400, "5分钟内手机号码已经接受过验证码！"),
    SEND_MESSAGE_SPILL(400, "24小时内验证码接收已超过3次！"),


    CATEGORY_NOT_FOUND(404, "商品分类不存在！"),
    BRAND_NOT_FOUND(404, "品牌不存在！"),
    SPEC_NOT_FOUND(404, "规格不存在！"),
    PRARM_NOT_FOUND(404, "规格参数不存在！"),
    GOODS_NOT_FOUND(404, "商品不存在！"),
    CARTS_NOT_FOUND(404, "购物车不存在！"),
    APPLICATION_NOT_FOUND(404, "应用不存在！"),
    ORDER_NOT_FOUND(404, "订单不存在！"),
    ORDER_DETAIL_NOT_FOUND(404, "订单数据不存在！"),


    DATA_TRANSFER_ERROR(500, "数据转换异常！"),
    INSERT_OPERATION_FAIL(500, "新增操作失败！"),
    UPDATE_OPERATION_FAIL(500, "更新操作失败！"),
    DELETE_OPERATION_FAIL(500, "删除操作失败！"),
    FILE_UPLOAD_ERROR(500, "文件上传失败！"),
    DIRECTORY_WRITER_ERROR(500, "目录写入失败！"),
    FILE_WRITER_ERROR(500, "文件写入失败！"),
    SEND_MESSAGE_ERROR(500, "短信发送失败！"),
    INVALID_ORDER_STATUS(500, "订单状态不正确！"),
    MINUS_STOCK_FALL(500,"库存不足"),

    UNAUTHORIZED(401, "登录失效或未登录！");


    private int status;
    private String message;

    ExceptionEnum(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
