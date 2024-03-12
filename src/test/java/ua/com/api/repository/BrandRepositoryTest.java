package ua.com.api.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.com.api.entity.Brand;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class BrandRepositoryTest {

    @Container
    PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13");

    @Autowired
    private BrandRepository brandRepository;

    @Test
    void testFindByName_Success(){
        String brandName = "BMW";
        Brand brand = brandRepository.findByName(brandName);
        assertNotNull(brand);
        assertEquals(brandName, brand.getName());
    }

    @Test
    void testFindByName_BrandWithInputNameNotExists(){
        String wrongName = "q.,wemrn";
        Brand brand = brandRepository.findByName(wrongName);
        assertNull(brand);
    }
}
