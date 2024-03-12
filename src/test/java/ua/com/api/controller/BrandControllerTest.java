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
import ua.com.api.entity.Brand;
import ua.com.api.service.impl.BrandServiceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BrandControllerTest {

    @InjectMocks
    private BrandController brandController;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BrandServiceImpl brandService;

    @Autowired
    private ObjectMapper objectMapper;

    private Brand createTestBrand() {
        long brandId = 1L;
        Brand brand = new Brand();
        brand.setId(brandId);
        brand.setName("Toyota");
        return brand;
    }

    @Test
    void testGetBrandById_Success() throws Exception {
        Brand brand = createTestBrand();

        Mockito.when(brandService.getBrand(brand.getId())).thenReturn(brand);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/brands/{id}", brand.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(brand.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(brand.getName()));

        Mockito.verify(brandService, Mockito.times(1)).getBrand(brand.getId());
    }

    @Test
    void testGetAllBrands_Success() throws Exception {
        List<Brand> brands = new ArrayList<>();
        Brand brand1 = new Brand();
        brand1.setId(1);
        Brand brand2 = new Brand();
        brand2.setId(2);
        Collections.addAll(brands, brand1, brand2);

        Mockito.when(brandService.getAllBrands()).thenReturn(brands);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/brands"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*]").isNotEmpty());
    }

    @Test
    void testAddNewBrand() throws Exception {
        Brand brand = createTestBrand();
        String brandJson = objectMapper.writeValueAsString(brand);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/brands")
                        .contentType(MediaType.APPLICATION_JSON).content(brandJson))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void testDeleteBrandById() throws Exception {
        Brand brand = createTestBrand();
        Mockito.when(brandService.getBrand(brand.getId())).thenReturn(brand);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/brands/{id}", brand.getId()))
                .andExpect(status().isOk());
        Mockito.verify(brandService).deleteBrand(brand.getId());
    }

    @Test
    void testRenameBrand() throws Exception {
        String newName = "new brand name";
        Brand brand = createTestBrand();
        Mockito.when(brandService.getBrand(brand.getId())).thenReturn(brand);

        mockMvc.perform(put("/api/v1/brands/{id}", brand.getId())
                        .param("new-name", newName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testRenameBrand_BrandNotFound() throws Exception {
        long wrongId = 999999;
        String newName = "new name";
        Mockito.when(brandService.getBrand(wrongId)).thenReturn(null);

        mockMvc.perform(put("/api/v1/brands/{id}", wrongId)
                .param("new-name", newName)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk());
    }
}
