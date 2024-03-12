package ua.com.api.service;

import org.springframework.data.domain.PageRequest;
import ua.com.api.DTO.CarDTO;
import ua.com.api.entity.Car;

import java.util.List;

public interface CarService {

    public Car getCar(long id);

    public void deleteCar(long id);

    public void createCar(Car car);

    public List<Car> getAllCars(PageRequest pageRequest);

    public List<Car> getCarByBrandAndMinYear(String brandName, int minYear, PageRequest pageRequest);

    public List<Car> getCarsByBrandAndMaxYear(String brandName, int maxYear, PageRequest pageRequest);

    public List<Car> getCarsByBrandAndModel(String brandName, String modelName, PageRequest pageRequest);

    public List<Car> getCarsByManufacturerAndCategory(String brandName, String category);

    public CarDTO getCarDTO(Car car);

}
