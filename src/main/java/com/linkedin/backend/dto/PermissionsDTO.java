package com.linkedin.backend.dto;



public class PermissionsDTO {
    final private static int ABOUT     = (1 << 0);
    final private static int EDUCATION = (1 << 1);
    final private static int JOB       = (1 << 2);
    final private static int SKILLS    = (1 << 3);
    final private static int FRIEND    = (1 << 4);

    private int permissions = 0;

    public PermissionsDTO() {

    }
    public PermissionsDTO(int permissions) {
        this.permissions = permissions;
    }

    public void setPermissions(int options){
        permissions = options;
    }

    public int getPermissions() {
        return permissions;
    }

    public Boolean about() {
        return (permissions & ABOUT) != 0;
    }

    public Boolean education() {
        return (permissions & EDUCATION) != 0;
    }

    public Boolean job() {
        return (permissions & JOB) != 0;
    }

    public Boolean skills() {
        return (permissions & SKILLS) != 0;
    }

    public Boolean friend() {
        return (permissions & FRIEND) != 0;
    }

    public PermissionsDTO setAbout() {
        permissions |= ABOUT;
        return this;
    }

    public PermissionsDTO setEducation() {
        permissions |= EDUCATION;
        return this;
    }

    public PermissionsDTO setJob() {
        permissions |= JOB;
        return this;
    }

    public PermissionsDTO setSkills() {
        permissions |= SKILLS;
        return this;
    }

    public PermissionsDTO setFriend() {
        permissions |= FRIEND;
        return this;
    }


}
