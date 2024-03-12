package ua.com.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.api.DTO.CarDTO;
import ua.com.api.entity.Brand;
import ua.com.api.entity.Car;
import ua.com.api.entity.Category;
import ua.com.api.entity.Model;
import ua.com.api.service.impl.BrandServiceImpl;
import ua.com.api.service.impl.CarServiceImpl;
import ua.com.api.service.impl.CategoryServiceImpl;
import ua.com.api.service.impl.ModelServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CarControllerTest {

    private final String MANUFACTURER = "brand name";
    private final String MODEL = "model name";
    private long CAR_ID = 1;
    private final int YEAR = 2023;

    @InjectMocks
    private CarController carController;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CarServiceImpl carService;

    @Mock
    private ModelServiceImpl modelService;

    @Mock
    private CategoryServiceImpl categoryService;

    @Mock
    private BrandServiceImpl brandService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateCar_Success() {
        Car car = new Car();

        when(modelService.getModelByName(MODEL)).thenReturn(new Model());
        when(brandService.getBrandByName(MANUFACTURER)).thenReturn(new Brand());
        when(carService.getCarDTO(car)).thenReturn(new CarDTO());
        ResponseEntity<String> response = carController.createCar(MANUFACTURER, MODEL, YEAR);

        verify(carService, times(1)).createCar(any());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Car created successfully", response.getBody());
    }

    @Test
    void testGetCar(){
        Car car = new Car();
        when(brandService.getBrandByName(MANUFACTURER)).thenReturn(new Brand());
        when(modelService.getModelByName(MODEL)).thenReturn(new Model());
        when(carService.getCarDTO(car)).thenReturn(new CarDTO());
        CarDTO carDTO = carController.getCar(MANUFACTURER, MODEL, YEAR);
        assertEquals(MANUFACTURER, carDTO.getBrandName());
    }

    @Test
    void testGetAllCars() throws Exception {
        List<CarDTO> carsDTO = new ArrayList<>();
        Collections.addAll(carsDTO,
                new CarDTO("Brand1", "Model1", 2022, new ArrayList<>()),
                new CarDTO("Brand2", "Model2", 2023, new ArrayList<>())
        );

        when(carService.getAllCars(PageRequest.of(0, 10, Sort.by("id", "asc")))).thenReturn(Arrays.asList(
                new Car(),
                new Car()
        ));
        when(carService.getCarDTO(any())).thenReturn(carsDTO.get(0), carsDTO.get(1));

        mockMvc = MockMvcBuilders.standaloneSetup(carController).build();

        mockMvc.perform(get("/api/v1/cars")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id,asc"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2));

        verify(carService, times(1)).getAllCars(PageRequest.of(0, 10, Sort.by("id", "asc")));
        verify(carService, times(2)).getCarDTO(any());
    }

    @Test
    void testDeleteCar() throws Exception {
        Car car = new Car();
        car.setId(1);
        Mockito.when(carService.getCar(car.getId())).thenReturn(car);

        mockMvc = MockMvcBuilders.standaloneSetup(carController).build();
        mockMvc.perform(delete("/api/v1/cars/{id}", 1)).andExpect(status().isOk());
        Mockito.verify(carService).deleteCar(car.getId());
    }

    @Test
    void testGetCarsByManufacturerAndMinYear() throws Exception{
        List<CarDTO> carsDTO = new ArrayList<>();
        Collections.addAll(carsDTO,
                new CarDTO("Brand1", "Model1", 2022, new ArrayList<>()),
                new CarDTO("Brand1", "Model1", 2023, new ArrayList<>())
        );

        Mockito.when(carService.getCarByBrandAndMinYear("Brand1", 2022,PageRequest.of(0, 10, Sort.by("id", "asc"))) )
                .thenReturn(Arrays.asList(new Car(), new Car()));
        Mockito.when(carService.getCarDTO(any())).thenReturn(carsDTO.get(0), carsDTO.get(1));

        mockMvc = MockMvcBuilders.standaloneSetup(carController).build();

        mockMvc.perform(get("/api/v1/cars/byMinYear")
                        .param("manufacturer", "Brand1")
                        .param("minYear", "2022")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id,asc"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2));

        Mockito.verify(carService, Mockito.times(1))
                .getCarByBrandAndMinYear("Brand1", 2022,PageRequest.of(0, 10, Sort.by("id", "asc")));
        Mockito.verify(carService, Mockito.times(2)).getCarDTO(Mockito.any());
    }

    @Test
    void testGetCarsByManufacturerAndMxYear() throws Exception{
        List<CarDTO> carsDTO = new ArrayList<>();
        Collections.addAll(carsDTO,
                new CarDTO("Brand1", "Model1", 2022, new ArrayList<>())
        );

        Mockito.when(carService.getCarsByBrandAndMaxYear("Brand1", 2022,PageRequest.of(0, 10, Sort.by("id", "asc"))) )
                .thenReturn(Arrays.asList(new Car()));
        Mockito.when(carService.getCarDTO(any())).thenReturn(carsDTO.get(0));

        mockMvc = MockMvcBuilders.standaloneSetup(carController).build();

        mockMvc.perform(get("/api/v1/cars/byMaxYear")
                        .param("manufacturer", "Brand1")
                        .param("maxYear", "2022")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id,asc"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1));

        Mockito.verify(carService, Mockito.times(1))
                .getCarsByBrandAndMaxYear("Brand1", 2022,PageRequest.of(0, 10, Sort.by("id", "asc")));
        Mockito.verify(carService, Mockito.times(1)).getCarDTO(Mockito.any());
    }


    @Test
    void testGetCarsByManufacturerAndCategory() throws Exception {
        List<CarDTO> carsDTO = new ArrayList<>();
        Collections.addAll(carsDTO,
                new CarDTO("Brand1", "Model1", 2022),
                new CarDTO("Brand1", "Model1", 2024));

        Brand brand = new Brand();
        brand.setName("Brand1");
        brand.setId(1);
        Mockito.when(brandService.getBrandByName("Brand1")).thenReturn(brand);

        Category category = new Category(); // Создаем действительный объект Category
        Mockito.when(categoryService.getCategoryByName(Mockito.any())).thenReturn(category);

        Mockito.when(carService.getCarsByManufacturerAndCategory("Brand1", "SUV"))
                .thenReturn(Arrays.asList(new Car(), new Car()));

        mockMvc = MockMvcBuilders.standaloneSetup(carController).build();

        mockMvc.perform(get("/api/v1/cars/byCategory")
                        .param("manufacturer", "Brand1")
                        .param("categoryName", "SUV"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2));
        Mockito.verify(carService, Mockito.times(2)).getCarDTO(Mockito.any());
    }
}
