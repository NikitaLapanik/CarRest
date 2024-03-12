package ua.com.api.service;

import ua.com.api.entity.Brand;

import java.util.List;

public interface BrandService {

    public Brand getBrand(long id);

    public void deleteBrand(long id);

    public void save(Brand brand);

    public List<Brand> getAllBrands();

    public Brand getBrandByName(String name);
}
