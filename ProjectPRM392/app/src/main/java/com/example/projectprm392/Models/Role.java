package com.example.projectprm392.Models;

public class Role {
    private int roleId;
    private String roleName;
    private int isDelete;

    public Role(int roleId, String roleName, int isDelete) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.isDelete = isDelete;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }
}
