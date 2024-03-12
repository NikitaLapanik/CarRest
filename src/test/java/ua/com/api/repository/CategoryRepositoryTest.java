package ua.com.api.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.com.api.entity.Category;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class CategoryRepositoryTest {

    PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13");

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void testFindByCategory_Success(){
        String categoryName = "SUV";
        Category category = categoryRepository.findByCategory(categoryName);
        assertEquals(categoryName, category.getCategory());
        assertNotNull(category);
    }

    @Test
    void testFindByCategory_WrongName(){
        String wrongName = "wrong category name";
        Category category = categoryRepository.findByCategory(wrongName);
        assertNull(category);
    }
}
