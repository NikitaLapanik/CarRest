package ua.com.api.service;

import ua.com.api.entity.Category;

public interface CategoryService {

    public Category getCategory(long id);

    public void deleteCategory(long id);

    public void createCategory(String categoryName);

    public Category getCategoryByName(String categoryName);
}
