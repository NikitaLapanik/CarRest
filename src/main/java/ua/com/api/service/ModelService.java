package ua.com.api.service;

import ua.com.api.entity.Model;

import java.util.List;

public interface ModelService {

    public Model getModel(long id);

    public void deleteModel(long id);

    public void save(Model model);

    public List<Model> getAllModels();

    public Model getModelByName(String name);
}
