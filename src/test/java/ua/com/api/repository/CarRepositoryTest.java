package ua.com.api.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.com.api.entity.Car;
import ua.com.api.entity.Category;
import ua.com.api.service.CategoryService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class CarRepositoryTest {

    @Container
    PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13");

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CarRepository carRepository;

    @Test
    void testFindByBrandId_Success(){
        long brandId = 1;
        PageRequest pageRequest = null;
        assertNotNull(carRepository.findByBrandId(brandId, pageRequest));
    }

    @Test
    void testFindByBrandId_WrongId(){
        long wrongBrandId = 1234232234;
        PageRequest pageRequest = null;
        Page<Car> page = carRepository.findByBrandId(wrongBrandId, pageRequest);
        List<Car> cars = page.getContent();
        assertEquals(0, cars.size());
    }

    @Test
    void testFindByCategories_Success(){
        long categoryId = 1;
        Category category = categoryService.getCategory(categoryId);
        List<Car> cars = carRepository.findByCategories(category);
        assertNotNull(cars);
    }
}
