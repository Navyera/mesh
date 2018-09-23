package com.linkedin.backend.dto;

public class ProfileViewDTO {

    private ProfileDTO profile;
    private PermissionsDTO permissions;

    public ProfileViewDTO(ProfileDTO profile, PermissionsDTO permissions) {
        this.profile = profile;
        this.permissions = permissions;

    }

    public ProfileDTO getProfileDTO() {
        return profile;
    }

    public void setProfileDTO(ProfileDTO profile) {
        this.profile = profile;
    }

    public PermissionsDTO getPermissionsDTO() {
        return permissions;
    }

    public void setPermissionsDTO(PermissionsDTO permissions) {
        this.permissions = permissions;
    }

    public void hidePrivateDetails() {
        if (!permissions.about())
            profile.setAbout(null);
        if (!permissions.education())
            profile.setEducation(null);
        if (!permissions.job())
            profile.setJob(null);
        if (!permissions.skills())
            profile.setSkills(null);
    }
}
