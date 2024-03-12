package ua.com.api.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.api.entity.Brand;
import ua.com.api.exception.ExceptionMessage;
import ua.com.api.repository.BrandRepository;
import ua.com.api.service.impl.BrandServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class BrandServiceTest {

    private final long BRAND_ID = 1;
    private final String BRAND_NAME = "Test brand";

    @InjectMocks
    private BrandServiceImpl brandService;

    @Mock
    private BrandRepository brandRepository;

    private Brand createTestBrand(){
        Brand brand = new Brand();
        brand.setName(BRAND_NAME);
        brand.setId(BRAND_ID);
        return brand;
    }

    @Test
    void testGetBrand_Success(){
        Brand brand = createTestBrand();
        Mockito.when(brandRepository.findById(BRAND_ID)).thenReturn(Optional.of(brand));
        assertNotNull(brandService.getBrand(BRAND_ID));
    }

    @Test
    void testGetBrand_BrandNotFound(){
        long wrongId = 23423423L;
        EntityNotFoundException exception = Assertions.assertThrows(EntityNotFoundException.class,
                () -> brandService.getBrand(wrongId));
        assertEquals(ExceptionMessage.BRAND_WAS_NOT_FOUND, exception.getMessage());
    }

    @Test
    void testDeleteBrand_Success(){
        Brand brand = createTestBrand();
        Mockito.when(brandRepository.findById(BRAND_ID)).thenReturn(Optional.of(brand));
        brandService.deleteBrand(BRAND_ID);
        Mockito.verify(brandRepository).delete(brand);
    }

    @Test
    void testSave_Success(){
        Brand brand = createTestBrand();
        brandService.save(brand);
        Mockito.verify(brandRepository).save(brand);
    }

    @Test
    void testGetAllBrands_Success(){
        List<Brand> brands = new ArrayList<>();
        Collections.addAll(brands, new Brand(), new Brand(), new Brand());
        Mockito.when(brandRepository.findAll()).thenReturn(brands);
        assertEquals(3, brandService.getAllBrands().size());
    }

    @Test
    void testGetByBrand_Success(){
        Brand brand = createTestBrand();
        Mockito.when(brandRepository.findByName(BRAND_NAME)).thenReturn(brand);
        assertNotNull(brandService.getBrandByName(BRAND_NAME));
    }

    @Test
    void testGetByBrand_WrongBrandName(){
        String wrongName = "wrong name of brand";
        EntityNotFoundException exception = Assertions.assertThrows(EntityNotFoundException.class,
                () -> brandService.getBrandByName(wrongName));
        assertEquals(ExceptionMessage.BRAND_WAS_NOT_FOUND, exception.getMessage());
    }
}
