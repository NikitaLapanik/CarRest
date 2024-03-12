package ua.com.api.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.api.entity.Model;
import ua.com.api.exception.ExceptionMessage;
import ua.com.api.repository.ModelRepository;
import ua.com.api.service.impl.ModelServiceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class ModelServiceTest {

    private final long MODEL_ID = 1;
    private final String MODEL_NAME = "Some model name";

    @InjectMocks
    private ModelServiceImpl modelService;

    @Mock
    private ModelRepository modelRepository;

    private Model createTestModel(){
        Model model = new Model();
        model.setModel(MODEL_NAME);
        model.setId(MODEL_ID);
        return model;
    }

    @Test
    void testGetModel_Success(){
        Model model = createTestModel();
        Mockito.when(modelRepository.findById(MODEL_ID)).thenReturn(Optional.of(model));
        assertNotNull(modelService.getModel(MODEL_ID));
    }

    @Test
    void testGetModel_WrongId(){
        long wrongId = 749238234;
        EntityNotFoundException exception = Assertions.assertThrows(EntityNotFoundException.class,
                () -> modelService.getModel(wrongId));
        assertEquals(ExceptionMessage.MODEL_WAS_NOT_FOUND, exception.getMessage());
    }

    @Test
    void testDeleteModel_Success(){
        Model model = createTestModel();
        Mockito.when(modelRepository.findById(MODEL_ID)).thenReturn(Optional.of(model));
        modelService.deleteModel(MODEL_ID);
        Mockito.verify(modelRepository).delete(model);
    }

    @Test
    void testSave_Success(){
        Model model = createTestModel();
        modelService.save(model);
        Mockito.verify(modelRepository).save(model);
    }

    @Test
    void testGetAllModels_Success(){
        List<Model> models = new ArrayList<>();
        Collections.addAll(models, new Model(), new Model(), new Model());
        Mockito.when(modelRepository.findAll()).thenReturn(models);
        assertNotNull(modelService.getAllModels());
        assertEquals(3, modelService.getAllModels().size());
    }

    @Test
    void testGetModelByName_Success(){
        Model model = createTestModel();
        Mockito.when(modelRepository.findByModel(MODEL_NAME)).thenReturn(model);
        assertNotNull(modelService.getModelByName(MODEL_NAME));
        assertEquals(1, modelService.getModelByName(MODEL_NAME).getId());
    }

    @Test
    void testGetModelByName_WrongName(){
        String wrongModelName = "wrong model name";
        EntityNotFoundException exception = Assertions.assertThrows(EntityNotFoundException.class,
                () -> modelService.getModelByName(wrongModelName));
        assertEquals(ExceptionMessage.MODEL_WAS_NOT_FOUND, exception.getMessage());
    }
}
