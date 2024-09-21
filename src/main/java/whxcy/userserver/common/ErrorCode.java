package whxcy.userserver.common;

public enum ErrorCode  {
    /**
     * 成功
     */
    SUCCESS(0,"ok",""),
    /**
     * 请求参数错误
     */
    PARAMS_ERROR(40000,"请求参数错误",""),
    /**
     * 请求数据为空
     */
    NULL_ERROR(40000,"请求数据为空",""),
    /**
     * 未登入
     */
    NOT_LOGIN(40100,"未登入",""),
    /**
     * 无权限
     */
    NO_AUTH(40101,"无权限",""),
    /**
     * 系统内部异常
     */
    SYSTEM_ERROR(50000, "系统内部异常", "");
    /**
     * 错误码
     */
    private final int code;
    /**
     * 异常信息
     */
    private final String message;
    /**
     * 更详细描述
     */
    private final String description;
    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}

