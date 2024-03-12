package ua.com.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.api.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByCategory(String categoryName);

}
