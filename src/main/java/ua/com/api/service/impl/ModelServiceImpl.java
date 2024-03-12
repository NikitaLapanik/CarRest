package ua.com.api.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.api.entity.Model;
import ua.com.api.exception.ExceptionMessage;
import ua.com.api.repository.ModelRepository;
import ua.com.api.service.ModelService;

import java.util.List;

@Service
public class ModelServiceImpl implements ModelService {

    private final Logger logger = LoggerFactory.getLogger(ModelServiceImpl.class);

    @Autowired
    private ModelRepository modelRepository;

    @Override
    public Model getModel(long id) {
        Model model = modelRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.MODEL_WAS_NOT_FOUND));
        logger.info("Model with id: {} was received", id);
        return model;
    }

    @Override
    public void deleteModel(long id) {
        modelRepository.delete(getModel(id));
        logger.info("Model with id: {} was deleted", id);
    }

    @Override
    public void save(Model model) {
        modelRepository.save(model);
        logger.info("Model {} was saved", model.getModel());
    }

    @Override
    public List<Model> getAllModels() {
        List<Model> models = modelRepository.findAll();
        logger.info("{} models were received", models.size());
        return models;
    }

    @Override
    public Model getModelByName(String name) {
        Model model = modelRepository.findByModel(name);
        if (model == null) {
            logger.error("Model with name {} wasn't found", name);
            throw new EntityNotFoundException(ExceptionMessage.MODEL_WAS_NOT_FOUND);
        }
        logger.info("Model {} was received", name);
        return model;
    }
}
