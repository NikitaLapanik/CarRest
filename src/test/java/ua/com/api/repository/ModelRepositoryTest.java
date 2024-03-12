package ua.com.api.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.com.api.entity.Model;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class ModelRepositoryTest {

    PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13");

    @Autowired
    private ModelRepository modelRepository;

    @Test
    void testFindByModel_Success(){
        String modelName = "X5";
        Model model = modelRepository.findByModel(modelName);
        assertEquals(modelName, model.getModel());
        assertNotNull(model);
    }

    @Test
    void testFindByModel_WrongModelName(){
        String wrongModelName = "wrong model name";
        Model model = modelRepository.findByModel(wrongModelName);
        assertNull(model);
    }
}
