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
import ua.com.api.entity.Model;
import ua.com.api.service.ModelService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/brands/models")
@Tag(name = "Model")
public class ModelController {

    Logger logger = LoggerFactory.getLogger(ModelController.class);

    @Autowired
    private ModelService modelService;

    @Operation(summary = "Get endpoint for all models", description = "Receives all models from database")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "All models were received")})
    @GetMapping
    public List<Model> getAllModels(){
        List<Model> models = modelService.getAllModels();
        logger.info("Received {} models", models.size());
        return models;
    }

    @Operation(summary = "Get endpoint for model by id", description = "Receives model by id from database")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Model by id were received")})
    @GetMapping("/{id}")
    public ResponseEntity<?> getModelById(@PathVariable(name = "id") long id){
        Model model = modelService.getModel(id);
        logger.info("Receive model with id: {}", id);
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Delete endpoint for model", description = "Delete model from database by id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "All models were deleted")})
    @DeleteMapping("/{id}")
    public void deleteModelById(@PathVariable(name = "id") long id){
        modelService.deleteModel(id);
        logger.info("Model with id: {} was deleted", id);
    }

    @Operation(summary = "Post endpoint for model", description = "Put a new model into database, by using object of model")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Model was created and placed in the database")})
    @PostMapping
    public ResponseEntity<String> addNewModel(@RequestBody Model model){
        modelService.save(model);
        logger.info("Model with id: {} was saved", model.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body("Model created successfully");
    }

    @Operation(summary = "Put endpoint for model", description = "Method takes the model by id and renames it")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Model was renamed successfully")})
    @PutMapping("/{id}")
    public void updateModel(@PathVariable(name = "id") long id, @RequestParam(name = "newName") String newName){
        Model model = modelService.getModel(id);
        if(model == null){
            logger.error("Model with id {} not found", id);
        }else{
            model.setModel(newName);
            modelService.save(model);
            logger.info("Model with id {} was renamed to {}", id, newName);
        }
    }
}
