package ua.com.api.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ua.com.api.DTO.CarDTO;
import ua.com.api.entity.Brand;
import ua.com.api.entity.Car;
import ua.com.api.entity.Category;
import ua.com.api.entity.Model;
import ua.com.api.exception.ExceptionMessage;
import ua.com.api.repository.CarRepository;
import ua.com.api.service.BrandService;
import ua.com.api.service.CarService;
import ua.com.api.service.CategoryService;
import ua.com.api.service.ModelService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarServiceImpl implements CarService {

    private final Logger logger = LoggerFactory.getLogger(CarServiceImpl.class);

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private ModelService modelService;

    //just CRUD
    @Override
    public Car getCar(long id) {
        Car car = carRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.CAR_WAS_NOT_FOUND));
        logger.info("Car with id: {} was received", id);
        return car;
    }

    @Override
    public void deleteCar(long id) {
        Car car = carRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.CAR_WAS_NOT_FOUND));
        carRepository.delete(car);
        logger.info("Car with id: {} was deleted", id);
    }

    @Override
    public void createCar(Car car) {
        carRepository.save(car);
        logger.info("Car: brandId-{}, modelId-{}, year-{} was created", car.getBrandId(), car.getModelId(), car.getYear());
    }

    @Override
    public List<Car> getAllCars(PageRequest pageRequest) {
        Page<Car> page = carRepository.findAll(pageRequest);
        logger.info("{} cars were received", page.getSort());
        return page.getContent();
    }

    //For endpoints
    @Override
    public List<Car> getCarByBrandAndMinYear(String brandName, int minYear, PageRequest pageRequest) {
        Brand brand = brandService.getBrandByName(brandName);

        Page<Car> page = carRepository.findByBrandId(brand.getId(), pageRequest);
        List<Car> cars = page.getContent();
        return cars.stream()
                .filter(car -> car.getYear() >= minYear)
                .collect(Collectors.toList());
    }

    @Override
    public List<Car> getCarsByBrandAndMaxYear(String brandName, int maxYear, PageRequest pageRequest) {
        Brand brand = brandService.getBrandByName(brandName);

        Page<Car> page = carRepository.findByBrandId(brand.getId(), pageRequest);
        List<Car> cars = page.getContent();
        return cars.stream()
                .filter(car -> car.getYear() <= maxYear)
                .collect(Collectors.toList());
    }

    @Override
    public List<Car> getCarsByBrandAndModel(String brandName, String modelName, PageRequest pageRequest) {
        Brand brand = brandService.getBrandByName(brandName);
        Model model = modelService.getModelByName(modelName);

        Page<Car> page = carRepository.findByBrandId(brand.getId(), pageRequest);
        List<Car> cars = page.getContent();
        return cars.stream()
                .filter(car -> car.getModelId() == model.getId())
                .collect(Collectors.toList());
    }

    @Override
    public List<Car> getCarsByManufacturerAndCategory(String brandName, String categoryName) {
        Brand brand = brandService.getBrandByName(brandName);
        Category category = categoryService.getCategoryByName(categoryName);
        List<Car> cars = carRepository.findByCategories(category);
        logger.info("Received {} cars", cars.size());
        for (Car car : cars) {
            logger.info("Car id {}, brand {}, categories {}", car.getId(), car.getBrandId(), car.getCategories().size());
        }
        return cars.stream().filter(car -> car.getBrandId() == brand.getId()).collect(Collectors.toList());
    }

    @Override
    public CarDTO getCarDTO(Car car) {
        Brand brand = brandService.getBrand(car.getBrandId());
        Model model = modelService.getModel(car.getModelId());
        int year = car.getYear();
        List<Category> categoryList = car.getCategories();
        return new CarDTO(brand.getName(), model.getModel(), year, categoryList);
    }
}
