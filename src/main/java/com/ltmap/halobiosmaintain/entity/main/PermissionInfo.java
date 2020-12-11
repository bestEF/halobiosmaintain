package com.ltmap.halobiosmaintain.entity.main;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author Niko
 * @since 2020-11-21
 */
@TableName("permission_info")
public class PermissionInfo extends Model<PermissionInfo> {

    private static final long serialVersionUID=1L;

    /**
     * 权限表id
     */
    @TableId(value = "permission_id", type = IdType.ID_WORKER_STR)
    private String permissionId;
    /**
     * 权限代码
     */
    private String permissionCode;
    /**
     * 权限名称
     */
    private String permissionName;
    /**
     * 权限父节点
     */
    private String parent;

    /**
     * 权限序号
     */
    private Integer permissionOrder;

    public Integer getPermissionOrder() {
        return permissionOrder;
    }

    public void setPermissionOrder(Integer permissionOrder) {
        this.permissionOrder = permissionOrder;
    }

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    @Override
    protected Serializable pkVal() {
        return this.permissionId;
    }

    @Override
    public String toString() {
        return "PermissionInfo{" +
        "permissionId=" + permissionId +
        ", permissionCode=" + permissionCode +
        ", permissionName=" + permissionName +
        ", parent=" + parent +
        "}";
    }
}
