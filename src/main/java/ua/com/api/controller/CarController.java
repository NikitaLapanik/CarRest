package ua.com.api.controller;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import ua.com.api.DTO.CarDTO;
import ua.com.api.entity.Brand;
import ua.com.api.entity.Car;
import ua.com.api.entity.Model;
import ua.com.api.service.BrandService;
import ua.com.api.service.CarService;
import ua.com.api.service.ModelService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/cars")
@Tag(name = "Car")
public class CarController {

	private final Logger logger = LoggerFactory.getLogger(CarController.class);

	@Autowired
	private CarService carService;

	@Autowired
	private ModelService modelService;

	@Autowired
	private BrandService brandService;

	// CREATE CAR ADN ADD IT IN DB
	@Operation(summary = "Post endpoint for car", description = "Creates a new car using the manufacturer, model, and year. Puts it into the database")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Car was created successfully")})
	@PostMapping("/{manufacturers}/{model}/{year}")
	public ResponseEntity<String> createCar(@PathVariable(name = "manufacturers") String manufacturers,
			@PathVariable(name = "model") String model, @PathVariable(name = "year") int year) {

		if (!isInputDataCorrect(manufacturers, model, year)) {
			return ResponseEntity.badRequest().body("Wrong input data");
		}

		try {
			CarDTO carDTO = new CarDTO(manufacturers, model, year);
			Car car = createOrUpdateCar(carDTO);
			return ResponseEntity.ok("Car created successfully");
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Brand or model not found");
		}
	}

	@Operation(summary = "Get endpoint for car by manufacturer, model, year", description = "Gets a car based on input values: manufacturer, model, year")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Car was received successfully") })
	@GetMapping("/{manufacturers}/{model}/{year}")
	public CarDTO getCar(@PathVariable(name = "manufacturers") String manufacturers,
			@PathVariable(name = "model") String model, @PathVariable(name = "year") int year) {
		return new CarDTO(manufacturers, model, year);
	}

	@Operation(summary = "Get endpoint for all cars", description = "Gets all cars with pageble, size and sort it by id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "All car with input values were received") })
    @GetMapping
    public List<CarDTO> getAllCars(
		@RequestParam(name = "page", defaultValue = "0") int page,
		@RequestParam(name = "size", defaultValue = "10") int size,
		@RequestParam(name = "sort", defaultValue = "id,asc") String[] sort) {

	List<CarDTO> carsDTO = new ArrayList<>();
	Sort sorting = getSorting(sort);
	List<Car> cars = carService.getAllCars(PageRequest.of(page, size, sorting));
	return cars.stream().map(car -> carService.getCarDTO(car)).toList();
    }

	private Sort getSorting(String[] sort) {
		String property = sort[0];
		String direction = sort.length > 1 ? sort[1] : "asc";

		return "asc".equalsIgnoreCase(direction) ?
				Sort.by(property).ascending() :
				Sort.by(property).descending();
	}

	// delete car by id
	@Operation(summary = "Delete endpoint for car by id", description = "Deletes the car by entered id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Car was deleted successfully")})
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteCar(@PathVariable(name = "id") long carId) {
		try {
			carService.deleteCar(carId);
			logger.info("Car with id: {} was deleted", carId);
			return ResponseEntity.ok("Car deleted successfully");
		} catch (EntityNotFoundException exception) {
			logger.error(exception.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car not found");
		}
	}

	// get cars by brand and min year range
	@Operation(summary = "Get endpoint for all cars by minimal year", description = "Reseives all cars based on the entered minimum year of manufacture ")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Cars by minimal year were received successfully")})
	@GetMapping("/byMinYear")
	public List<CarDTO> getCarsByManufacturerAndMinYear(@RequestParam(name = "manufacturer") String manufacturer,
			@RequestParam(name = "minYear") int minYear, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size,
			@RequestParam(name = "sort", defaultValue = "id, asc") String[] sort) {
		List<CarDTO> carsDTO = new ArrayList<>();
		List<Car> cars = carService.getCarByBrandAndMinYear(manufacturer, minYear,
				PageRequest.of(page, size, Sort.by(sort)));
		return cars.stream().map(car -> carService.getCarDTO(car)).toList();
	}

	// get cars by brand and max year range
	@Operation(summary = "Get endpoint for all cars by maximum year", description = "Reseives all cars based on the entered maximum year of manufacture ")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Cars by maximum year were received successfully")})
	@GetMapping("/byMaxYear")
	public List<CarDTO> getCarsByManufacturerAndMaxYear(@RequestParam(name = "manufacturer") String manufacturer,
			@RequestParam(name = "maxYear") int maxYear, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size,
			@RequestParam(name = "sort", defaultValue = "id, asc") String[] sort) {
		List<CarDTO> carsDTO = new ArrayList<>();
		List<Car> cars = carService.getCarsByBrandAndMaxYear(manufacturer, maxYear,
				PageRequest.of(page, size, Sort.by(sort)));
		return cars.stream().map(car -> carService.getCarDTO(car)).toList();
	}

	// get car by manufacturer and model
	@Operation(summary = "Get endpoint for all cars by model", description = "Reseives all cars based on the entered model")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Cars by model were received successfully")})
	@GetMapping("/byModel")
	public List<CarDTO> getCarsByManufacturerAndModel(@RequestParam(name = "manufacturer") String manufacturer,
			@RequestParam(name = "model") String model, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size,
			@RequestParam(name = "sort", defaultValue = "id, asc") String[] sort) {
		List<CarDTO> carsDTO = new ArrayList<>();
		List<Car> cars = carService.getCarsByBrandAndModel(manufacturer, model,
				PageRequest.of(page, size, Sort.by(sort)));
		return cars.stream().map(car -> carService.getCarDTO(car)).toList();
	}

	// get car by manufacturer and category
	@Operation(summary = "Get endpoint for all cars by category", description = "Reseives all cars based on the entered category")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Cars by category were received successfully")})
	@GetMapping("/byCategory")
	public List<CarDTO> getCarsByManufacturerAndCategory(@RequestParam(name = "manufacturer") String brand,
			@RequestParam(name = "categoryName") String categoryName) {
		List<CarDTO> carsDTO = new ArrayList<>();
		List<Car> cars = carService.getCarsByManufacturerAndCategory(brand, categoryName);
		logger.info("Received {} cars by brand:{} and category:{}", cars.size(), brand, categoryName);
		return cars.stream().map(car -> carService.getCarDTO(car)).toList();
	}

	private Model getModel(String modelName) {
		return modelService.getModelByName(modelName);
	}

	private Brand getBrand(String brandName) {
		return brandService.getBrandByName(brandName);
	}

	private Car createOrUpdateCar(CarDTO carDTO) {
		Model model = getModel(carDTO.getModelName());
		Brand brand = getBrand(carDTO.getBrandName());
		Car car = new Car();
		car.setModelId(model.getId());
		car.setBrandId(brand.getId());
		car.setYear(carDTO.getYear());
		carService.createCar(car);
		return car;
	}

	private boolean isInputDataCorrect(String manufacturer, String model, int year) {
		return manufacturer != null && !manufacturer.isEmpty() && model != null && !model.isEmpty() && year > 1900
				&& year <= LocalDateTime.now().getYear();
	}
}
