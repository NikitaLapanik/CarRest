package ua.com.api.DTO;

import ua.com.api.entity.Category;

import java.util.ArrayList;
import java.util.List;

public class CarDTO {

    private String brandName;

    private String modelName;

    private int year;

    private String objectId;

    private List<Category> categoryList = new ArrayList<>();

    public CarDTO() {
    }

    public CarDTO(String brandName, String modelName, int year) {
        this.brandName = brandName;
        this.modelName = modelName;
        this.year = year;
    }

    public CarDTO(String brandName, String modelName, int year, List<Category> categoryList) {
        this.brandName = brandName;
        this.modelName = modelName;
        this.year = year;
        this.categoryList = categoryList;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
}
