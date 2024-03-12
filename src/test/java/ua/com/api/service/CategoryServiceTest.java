package ua.com.api.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.api.entity.Category;
import ua.com.api.exception.ExceptionMessage;
import ua.com.api.repository.CategoryRepository;
import ua.com.api.service.impl.CategoryServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    private final long CATEGORY_ID = 1;
    private final String CATEGORY_NAME = "CategoryName";

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    private Category createTestCategory(){
        Category category = new Category();
        category.setCategory(CATEGORY_NAME);
        category.setId(CATEGORY_ID);
        return category;
    }

    @Test
    void testGetCategory_Success(){
        Category category = createTestCategory();
        Mockito.when(categoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.of(category));
        assertNotNull(categoryService.getCategory(CATEGORY_ID));
        assertEquals(CATEGORY_NAME, category.getCategory());
    }

    @Test
    void testGetCategory_CategoryNotFound(){
        long wrongId = 12312312;
        EntityNotFoundException exception = Assertions.assertThrows(EntityNotFoundException.class,
                () -> categoryService.getCategory(wrongId));
        assertEquals(ExceptionMessage.CATEGORY_WAS_NOT_FOUND, exception.getMessage());
    }

    @Test
    void testDeleteCategory_Success(){
        Category category = createTestCategory();
        Mockito.when(categoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.of(category));
        categoryService.deleteCategory(CATEGORY_ID);
        Mockito.verify(categoryRepository).delete(category);
    }

    @Test
    void testCreateCategory_Success(){
        categoryService.createCategory(CATEGORY_NAME);
        Mockito.verify(categoryRepository).save(Mockito.any());
    }
}
