package ua.com.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.api.entity.Car;
import ua.com.api.entity.Category;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    Page<Car> findByBrandId(long brandId, PageRequest pageRequest);

    List<Car> findByCategories(Category category);

}
