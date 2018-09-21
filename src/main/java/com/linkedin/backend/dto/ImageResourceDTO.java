package com.linkedin.backend.dto;

public class ImageResourceDTO {
    String type;
    String image;

    public ImageResourceDTO(String type, String image) {
        this.type = type;
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
