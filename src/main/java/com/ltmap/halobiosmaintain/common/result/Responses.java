package com.ltmap.halobiosmaintain.common.result;

import java.util.Collection;
import java.util.Objects;

/**
 * 统一返回结果集扩展类
 * @author fjh
 * @date 2020/11/12 13:20
 */
public class Responses {

    private Responses() {
    }

    public static Response or(Boolean flag) {
        return flag.booleanValue() ? Response.ok("操作成功.") : Response.fail("操作失败.");
    }

    public static <T> Response<T> or(T t) {
        return Objects.nonNull(t) ? Response.ok(t) : Response.ok();
    }

    public static <T extends Collection> Response<T> or(T t) {
        return Objects.nonNull(t) && !t.isEmpty() ? Response.ok(t) : Response.ok();
    }

}
