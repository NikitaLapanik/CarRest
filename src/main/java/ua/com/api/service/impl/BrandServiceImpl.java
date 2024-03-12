package ua.com.api.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.api.entity.Brand;
import ua.com.api.exception.ExceptionMessage;
import ua.com.api.repository.BrandRepository;
import ua.com.api.service.BrandService;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {

    private final Logger logger = LoggerFactory.getLogger(BrandServiceImpl.class);

    @Autowired
    private BrandRepository brandRepository;

    @Override
    public Brand getBrand(long id) {
        Brand brand = brandRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.BRAND_WAS_NOT_FOUND));
        logger.info("Brand with id: {} was received", id);
        return brand;
    }

    @Override
    public void deleteBrand(long id) {
        brandRepository.delete(getBrand(id));
        logger.info("Brand with id: {} was deleted", id);
    }

    @Override
    public void save(Brand brand) {
        brandRepository.save(brand);
        logger.info("Brand {} was created", brand.getName());
    }

    @Override
    public List<Brand> getAllBrands() {
        List<Brand> brands =brandRepository.findAll();
        logger.info("{} brands were received", brands.size());
        return brands;
    }

    @Override
    public Brand getBrandByName(String name){
        Brand brand = brandRepository.findByName(name);
        if(brand == null){
            logger.error("Brand {} wasn't received", name);
            throw new EntityNotFoundException(ExceptionMessage.BRAND_WAS_NOT_FOUND);
        }
        logger.info("Brand {} were received", name);
        return brand;
    }
}
