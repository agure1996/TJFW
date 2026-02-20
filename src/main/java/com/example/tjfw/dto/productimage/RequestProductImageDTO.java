package com.example.tjfw.dto.productimage;

public class RequestProductImageDTO {
        private Long imageId;
        private String imageUrl;
        private String publicId;
        private Boolean isMain;
        private Integer displayOrder;
        private String altText;

        // No-argument constructor
        public RequestProductImageDTO() {}

        // All-argument constructor
        public RequestProductImageDTO(Long imageId, String imageUrl, String publicId, Boolean isMain, Integer displayOrder, String altText) {
            this.imageId = imageId;
            this.imageUrl = imageUrl;
            this.publicId = publicId;
            this.isMain = isMain;
            this.displayOrder = displayOrder;
            this.altText = altText;
        }

        // Getters and Setters
        public Long getImageId() {
            return imageId;
        }

        public void setImageId(Long imageId) {
            this.imageId = imageId;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getPublicId() {
            return publicId;
        }

        public void setPublicId(String publicId) {
            this.publicId = publicId;
        }

        public Boolean getIsMain() {
            return isMain;
        }

        public void setIsMain(Boolean isMain) {
            this.isMain = isMain;
        }

        public Integer getDisplayOrder() {
            return displayOrder;
        }

        public void setDisplayOrder(Integer displayOrder) {
            this.displayOrder = displayOrder;
        }

        public String getAltText() {
            return altText;
        }

        public void setAltText(String altText) {
            this.altText = altText;
        }
    }
