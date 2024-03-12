package ua.com.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ua.com.api.entity.Model;
import ua.com.api.service.impl.ModelServiceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ModelControllerTest {

    @InjectMocks
    private ModelController modelController;

    @MockBean
    private ModelServiceImpl modelService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Model createTestModel(){
        Model model = new Model();
        model.setId(1);
        model.setModel("X5");
        return model;
    }

    @Test
    void testGetAllModels() throws Exception{
        List<Model> models = new ArrayList<>();
        Collections.addAll(models, new Model(), new Model());

        Mockito.when(modelService.getAllModels()).thenReturn(models);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/brands/models"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*]").isNotEmpty());
    }

    @Test
    void testGetModelById() throws Exception{
        Model model = createTestModel();

        Mockito.when(modelService.getModel(model.getId())).thenReturn(model);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/brands/models/{id}", model.getId()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(model.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.model").value(model.getModel()));
        Mockito.verify(modelService, Mockito.times(1)).getModel(model.getId());
    }

    @Test
    void testDeleteModelById() throws Exception {
        Model model = createTestModel();
        Mockito.when(modelService.getModel(model.getId())).thenReturn(model);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/brands/models/{id}", model.getId()))
                .andExpect(status().isOk());
        Mockito.verify(modelService).deleteModel(model.getId());
    }

    @Test
    void testAddNewModel() throws Exception {
        Model model = createTestModel();
        String modelJson = objectMapper.writeValueAsString(model);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/brands/models").contentType(MediaType.APPLICATION_JSON).content(modelJson))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void testUpdateModel() throws Exception {
        String newModelName = "new model name";
        Model model = createTestModel();
        Mockito.when(modelService.getModel(model.getId())).thenReturn(model);

        mockMvc.perform(put("/api/v1/brands/models/{id}", model.getId())
                .param("newName", newModelName)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testUpdateModel_ModelNotFound() throws Exception {
        long wrongId = 999999;
        String newName = "new model name";
        Mockito.when(modelService.getModel(wrongId)).thenReturn(null);

        mockMvc.perform(put("/api/v1/brands/models/{id}", wrongId)
                .param("newName", newName)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk());
    }
}
