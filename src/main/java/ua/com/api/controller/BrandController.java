package ua.com.api.controller;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import ua.com.api.entity.Brand;
import ua.com.api.service.BrandService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/brands")
@Tag(name = "Brand")
public class BrandController {

    private final Logger logger = LoggerFactory.getLogger(BrandController.class);

    @Autowired
    private BrandService brandService;


	@Operation(summary = "Post endpoint for new brand", description = "Takes a new brand object using the brand service, puts this brand into the database")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Brand created successfully") })
    @PostMapping
    public ResponseEntity<String> addNewBrand(@RequestBody Brand brand) {
        logger.info("Save new brand {}", brand.getName());
        brandService.save(brand);
        return ResponseEntity.status(HttpStatus.CREATED).body("Brand created successfully");
    }

    @Operation(summary = "Get endpoint for all brands", description = "Receives all brands from database")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "All brands were received") })
    @GetMapping
    public List<Brand> getAllBrands() {
        List<Brand> brands = brandService.getAllBrands();
        logger.info("Received {} brands", brands.size());
        return brands;
    }

    @Operation(summary = "Get endpoint for brand by id", description = "Receives brand by id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Brand was received successfully") })
    @GetMapping("/{id}")
    public Brand getBrandById(@PathVariable(name = "id") long id) {
        Brand brand = brandService.getBrand(id);
        logger.info("Get brand with id: {}", id);
        return brand;
    }

    @Operation(summary = "Delete endpoint for brand", description = "Delete brand by id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Brand was deleted successfully")})
    @DeleteMapping("/{id}")
    public void deleteBrandById(@PathVariable(name = "id") long id) {
        brandService.deleteBrand(id);
        logger.info("Brand with id: {} was deleted", id);
    }

    @Operation(summary = "Put endpoint for brand", description = "Renames the brand")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Brand was renamed successfully")})
    @PutMapping("/{id}")
    public void renameBrand(@PathVariable(name = "id") long id, @RequestParam(name = "new-name") String newName) {
        Brand brand = brandService.getBrand(id);
        if (brand == null) {
            logger.error("No such brand with id: {}", id);
        } else {
            brand.setName(newName);
            brandService.save(brand);
            logger.info("Brand with id {} renamed to: {}", id, newName);
        }
    }
}
