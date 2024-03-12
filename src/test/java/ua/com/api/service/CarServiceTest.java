package ua.com.api.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ua.com.api.entity.Brand;
import ua.com.api.entity.Car;
import ua.com.api.entity.Category;
import ua.com.api.entity.Model;
import ua.com.api.exception.ExceptionMessage;
import ua.com.api.repository.BrandRepository;
import ua.com.api.repository.CarRepository;
import ua.com.api.repository.CategoryRepository;
import ua.com.api.service.impl.BrandServiceImpl;
import ua.com.api.service.impl.CarServiceImpl;
import ua.com.api.service.impl.CategoryServiceImpl;
import ua.com.api.service.impl.ModelServiceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class CarServiceTest {

    private final long CAR_ID = 1;
    private final long BRAND_ID = 1;
    private final long MODEL_ID = 1;
    private final int YEAR = 2024;
    private final String BRAND_NAME = "Brand name";
    private List<Category> categoryList = new ArrayList<>();
    private final String MODEL_NAME = "model name";
    private final String CATEGORY_NAME = "Category name";
    private final long CATEGORY_ID = 1;

    @InjectMocks
    private CarServiceImpl carService;

    @Mock
    private CarRepository carRepository;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private ModelServiceImpl modelService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BrandServiceImpl brandService;
    
    @Mock
    private CategoryServiceImpl categoryService;
    
    private Car createTestCar(){
        Car car = new Car();
        car.setId(CAR_ID);
        car.setYear(YEAR);
        car.setBrandId(BRAND_ID);
        car.setModelId(MODEL_ID);
        car.setCategories(categoryList);
        return car;
    }

    private Brand createTestBrand(){
        Brand brand = new Brand();
        brand.setId(BRAND_ID);
        brand.setName(BRAND_NAME);
        return brand;
    }

    private PageRequest createTestPageRequest(){
        int pageNumber = 0;
        int pageSize = 10;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return pageRequest;
    }

    private Model createTestModel(){
        Model model = new Model();
        model.setModel(MODEL_NAME);
        model.setId(MODEL_ID);
        return model;
    }

    private Category createTestCategory(){
        Category category = new Category();
        category.setId(CATEGORY_ID);
        category.setCategory(CATEGORY_NAME);
        return category;
    }

    @Test
    void testGetCar_Success(){
        Car car = createTestCar();
        Mockito.when(carRepository.findById(CAR_ID)).thenReturn(Optional.of(car));
        assertNotNull(carService.getCar(CAR_ID));
        assertEquals(CAR_ID, carService.getCar(CAR_ID).getId());
    }

    @Test
    void testGetCar_WrongId_CarNotFound(){
        long wrongID = 23432432;
        EntityNotFoundException exception = Assertions.assertThrows(EntityNotFoundException.class,
                () -> carService.getCar(wrongID));
        assertEquals(ExceptionMessage.CAR_WAS_NOT_FOUND, exception.getMessage());
    }

    @Test
    void testDeleteCar_Success(){
        Car car = createTestCar();
        Mockito.when(carRepository.findById(CAR_ID)).thenReturn(Optional.of(car));
        carService.deleteCar(CAR_ID);
        Mockito.verify(carRepository).delete(car);
    }

    @Test
    void testCreateCar_Success(){
        Car car = createTestCar();
        carService.createCar(car);
        Mockito.verify(carRepository).save(car);
    }

    @Test
    void testGetAllCars() {
        PageRequest pageRequest = createTestPageRequest();

        List<Car> cars = new ArrayList<>();
        Collections.addAll(cars, new Car(), new Car(), new Car());

        Page<Car> page = new PageImpl<>(cars, pageRequest, cars.size());
        Mockito.when(carRepository.findAll(pageRequest)).thenReturn(page);
        List<Car> result = carService.getAllCars(pageRequest);
        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    void testGetCarByBrandAndMinYear() {
        PageRequest pageRequest = createTestPageRequest();
        Brand brand = createTestBrand();
        List<Car> cars = new ArrayList<>();
        Car car1 = createTestCar();
        Car car2 = createTestCar();
        Collections.addAll(cars, car1, car2);
        Page<Car> page = new PageImpl<>(cars, pageRequest, cars.size());
        Mockito.when(brandService.getBrandByName(BRAND_NAME)).thenReturn(brand);
        Mockito.when(carRepository.findByBrandId(brand.getId(), pageRequest)).thenReturn(page);
        List<Car> result = carService.getCarByBrandAndMinYear(BRAND_NAME, YEAR, pageRequest);
        assertEquals(2, result.size());
    }

    @Test
    void testGetCarsByBrandAndMaxYear(){
        PageRequest pageRequest = createTestPageRequest();
        Brand brand = createTestBrand();
        Model model = createTestModel();
        List<Car> cars = new ArrayList<>();
        Car car1 = createTestCar();
        Car car2 = createTestCar();
        Collections.addAll(cars, car1, car2);
        Page<Car> page = new PageImpl<>(cars, pageRequest, cars.size());
        Mockito.when(brandService.getBrandByName(BRAND_NAME)).thenReturn(brand);
        Mockito.when(carRepository.findByBrandId(brand.getId(), pageRequest)).thenReturn(page);
        List<Car> result = carService.getCarsByBrandAndMaxYear(BRAND_NAME, YEAR, pageRequest);
        assertEquals(2, result.size());
    }

    @Test
    void testGetCarsByBrandAndModel(){
    	List<Car> cars = new ArrayList<>();
        Car car1 = createTestCar();
        Car car2 = createTestCar();
        Collections.addAll(cars, car1, car2);
        Brand brand = createTestBrand();
        Model model = createTestModel();
        PageRequest pageRequest = createTestPageRequest();
        Page<Car> page = new PageImpl<>(cars, pageRequest, cars.size());
    
    	Mockito.when(brandService.getBrandByName(BRAND_NAME)).thenReturn(brand);
        Mockito.when(modelService.getModelByName(MODEL_NAME)).thenReturn(model);
        Mockito.when(carRepository.findByBrandId(brand.getId(), pageRequest)).thenReturn(page);
        List<Car> result = carService.getCarsByBrandAndModel(BRAND_NAME, MODEL_NAME, pageRequest);
        assertEquals(2, result.size());
    }

    @Test
    void testGetCarsByManufacturerAndCategory(){
        List<Car> cars = new ArrayList<>();
        Car car1 = createTestCar();
        Car car2 = createTestCar();
        Collections.addAll(cars, car1, car2);

        Brand brand = createTestBrand();
        Category category = createTestCategory();

        Mockito.when(brandService.getBrandByName(BRAND_NAME)).thenReturn(brand);
        Mockito.when(categoryService.getCategoryByName(CATEGORY_NAME)).thenReturn(category);
        Mockito.when(carRepository.findByCategories(category)).thenReturn(cars);
        assertEquals(2, carService.getCarsByManufacturerAndCategory(BRAND_NAME, CATEGORY_NAME).size());
    }
}
