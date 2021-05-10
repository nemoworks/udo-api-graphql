package info.nemoworks.udo.graphql.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import graphql.ExecutionResult;
import graphql.GraphQL;
import info.nemoworks.udo.graphql.graphqlBuilder.GraphQLBuilder;
import info.nemoworks.udo.graphql.schemaParser.SchemaTree;
import info.nemoworks.udo.model.UdoSchema;
import info.nemoworks.udo.service.UdoService;
import info.nemoworks.udo.service.UdoServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class UdoController {

    private static final Logger logger = LoggerFactory.getLogger(UdoController.class);

    private final UdoService udoService;

    private GraphQL graphQL;
    private GraphQLBuilder graphQlBuilder;

    @Autowired
    public UdoController(GraphQLBuilder graphQlBuilder, UdoService udoService) {
        this.graphQL = graphQlBuilder.createGraphQl();
        this.graphQlBuilder = graphQlBuilder;
        this.udoService = udoService;
    }

    @CrossOrigin
    @PostMapping(value = "/documents/query")
    public ResponseEntity query(@RequestBody String query) {
        ExecutionResult result = graphQL.execute(query);
        logger.info("errors: " + result.getErrors());
//        try {
//            publisher.publishUdo(query);
//        } catch (MqttException e) {
//            e.printStackTrace();
//        }
        if (result.getErrors().isEmpty())
            return ResponseEntity.ok(result.getData());
        else return ResponseEntity.badRequest().body(result.getErrors());
    }

    @GetMapping("/schemas")
    public List<UdoSchema> allSchemas() {
        logger.info("find all schemas...");
//        Gson gson = new Gson();
        return udoService.getAllSchemas();
    }

    @PostMapping("/schemas")
    public UdoSchema createSchema(@RequestBody JsonNode params) {
        logger.info("now saving a new schema...");
//        System.out.println(params);
        String name = params.get("schemaName").asText();
        JsonNode content = params.get("schemaContent");

        UdoSchema udoSchema = new UdoSchema(name, content);
        SchemaTree schemaTree = new SchemaTree().createSchemaTree(new Gson()
                .fromJson(udoSchema.getSchema().toString(), JsonObject.class));

        this.graphQL = graphQlBuilder.addSchemaInGraphQL(schemaTree);
        try {
            return udoService.saveOrUpdateSchema(udoSchema);
        } catch (UdoServiceException e) {
            e.printStackTrace();
        }
        return null;
    }

    @DeleteMapping("/schemas/{udoi}")
    public List<UdoSchema> deleteSchema(@PathVariable String udoi){
        logger.info("now deleting schema " + udoi + "...");
        UdoSchema udoSchema = udoService.getSchemaById(udoi);
        SchemaTree schemaTree = new SchemaTree().createSchemaTree(new Gson()
                .fromJson(udoSchema.getSchema().toString(), JsonObject.class));
        this.graphQL = graphQlBuilder.deleteSchemaInGraphQl(schemaTree);
        try {
            udoService.deleteSchemaById(udoi);
        } catch (UdoServiceException e) {
            e.printStackTrace();
        }
        return udoService.getAllSchemas();
    }

    @GetMapping("/schemas/{udoi}")
    public UdoSchema getSchemaById(@PathVariable String udoi) {
        logger.info("now finding schema by udoi...");
//        Gson gson = new Gson();
        return udoService.getSchemaById(udoi);
    }

    @PutMapping("/schemas/{udoi}")
    public UdoSchema updateSchema(@RequestBody JsonNode params, @PathVariable String udoi){
//        String udoi = params.getString("udoi");
        logger.info("now updating schema " + udoi + "...");
//        String name = params.get("schemaName").getAsString();
        JsonNode content = params.get("schemaContent");
//        Gson gson = new Gson();
        return null;
    }
}