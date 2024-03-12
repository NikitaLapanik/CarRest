package ua.com.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.com.api.entity.Model;

@Repository
public interface ModelRepository extends JpaRepository<Model, Long> {

    Model findByModel(String model);

}
