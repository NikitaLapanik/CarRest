package ua.com.api.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.api.entity.Category;
import ua.com.api.exception.ExceptionMessage;
import ua.com.api.repository.CategoryRepository;
import ua.com.api.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category getCategory(long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.CATEGORY_WAS_NOT_FOUND));
        logger.info("Category with id: {} was received", id);
        return category;
    }

    @Override
    public void deleteCategory(long id) {
        categoryRepository.delete(getCategory(id));
        logger.info("Category with id: {} was deleted", id);
    }

    @Override
    public void createCategory(String categoryName) {
        Category category = new Category();
        category.setCategory(categoryName);
        categoryRepository.save(category);
        logger.info("Category {} was created", categoryName);
    }

    @Override
    public Category getCategoryByName(String categoryName) {
       return categoryRepository.findByCategory(categoryName);
    }
}
