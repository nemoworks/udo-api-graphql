package info.nemoworks.udo.graphql.graphqlBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.SchemaGenerator;
import info.nemoworks.udo.graphql.schemaParser.SchemaTree;
import info.nemoworks.udo.model.UdoSchema;
import info.nemoworks.udo.service.UdoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static graphql.GraphQL.newGraphQL;

@Component
public class GraphQLBuilder {
    private final TypeRegistryBuilder typeRegistryBuilder;
    private final RuntimeWiringBuilder runtimeWiringBuilder;
    private final UdoService udoService;

    private static final Logger logger = LoggerFactory.getLogger(GraphQLBuilder.class);


    @Autowired
    public GraphQLBuilder(TypeRegistryBuilder typeRegistryBuilder, RuntimeWiringBuilder runtimeWiringBuilder, UdoService udoService) {
        this.typeRegistryBuilder = typeRegistryBuilder;
        this.runtimeWiringBuilder = runtimeWiringBuilder;
        this.udoService = udoService;
    }

    @PostConstruct
    public GraphQL createGraphQl(){

        typeRegistryBuilder.initSchemaDefinition();
        typeRegistryBuilder.initTypeDefinition();
        runtimeWiringBuilder.initRuntimeWiring();
        String s = "{\n" +
                "  \"type\": \"object\",\n" +
                "  \"title\": \"VirtualMachineInstance\",\n" +
                "  \"properties\": {\n" +
                "    \"CPU\": {\n" +
                "      \"properties\": {\n" +
                "        \"cores\": {\n" +
                "          \"type\": \"meter\"\n" +
                "        },\n" +
                "        \"sockets\": {\n" +
                "          \"type\": \"integer\"\n" +
                "        },\n" +
                "        \"threads\": {\n" +
                "          \"type\": \"integer\"\n" +
                "        },\n" +
                "        \"model\": {\n" +
                "          \"type\": \"string\"\n" +
                "        },\n" +
                "        \"features\": {\n" +
                "          \"items\": {\n" +
                "            \"type\": \"embedded\",\n" +
                "            \"typeName\": \"CPUFeature\"\n" +
                "          },\n" +
                "          \"type\": \"array\"\n" +
                "        }\n" +
                "      },\n" +
                "      \"additionalProperties\": false,\n" +
                "      \"title\": \"CPU\",\n" +
                "      \"type\": \"object\"\n" +
                "    },\n" +
                "    \"CPUFeature\": {\n" +
                "      \"required\": [\n" +
                "        \"name\"\n" +
                "      ],\n" +
                "      \"properties\": {\n" +
                "        \"name\": {\n" +
                "          \"type\": \"string\"\n" +
                "        },\n" +
                "        \"policy\": {\n" +
                "          \"type\": \"string\"\n" +
                "        }\n" +
                "      },\n" +
                "      \"additionalProperties\": false,\n" +
                "      \"title\": \"CPUFeature\",\n" +
                "      \"type\": \"object\"\n" +
                "    }\n" +
                "  }\n" +
                "}";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = null;
        try {
            actualObj = mapper.readTree(s);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        UdoSchema schema = new UdoSchema("purifier", actualObj);
        SchemaTree schemaTree = new SchemaTree().createSchemaTree(new Gson().fromJson(schema.getSchema().toString(), JsonObject.class));
        typeRegistryBuilder.addSchema(schemaTree);
        runtimeWiringBuilder.addNewSchemaDataFetcher(udoService, schemaTree);
        typeRegistryBuilder.buildTypeRegistry();

        //MeterCluster.addSchemaMeter(schemaTree);
        GraphQLSchema graphQLSchema = new SchemaGenerator().makeExecutableSchema(typeRegistryBuilder.getTypeDefinitionRegistry(), runtimeWiringBuilder.getRuntimeWiring());
        return newGraphQL(graphQLSchema).build();
    }

    public synchronized GraphQL addSchemaInGraphQL(SchemaTree schemaTree) {
        logger.info("add new schema definition in graphql " + schemaTree.getName() + "...");
        this.addNewTypeAndDataFetcherInGraphQL(schemaTree);
        GraphQLSchema graphQLSchema = new SchemaGenerator().makeExecutableSchema(typeRegistryBuilder.getTypeDefinitionRegistry(), runtimeWiringBuilder.getRuntimeWiring());
        return newGraphQL(graphQLSchema).build();
    }

    public synchronized GraphQL deleteSchemaInGraphQl(SchemaTree schemaTree) {
        this.deleteTypeAndDataFetcherInGraphQl(schemaTree);
        GraphQLSchema graphQLSchema = new SchemaGenerator().makeExecutableSchema(typeRegistryBuilder.getTypeDefinitionRegistry(), runtimeWiringBuilder.getRuntimeWiring());
        return newGraphQL(graphQLSchema).build();
    }

    private void addNewTypeAndDataFetcherInGraphQL(SchemaTree schemaTree) {
        typeRegistryBuilder.addSchema(schemaTree);
        runtimeWiringBuilder.addNewSchemaDataFetcher(udoService, schemaTree);
        typeRegistryBuilder.buildTypeRegistry();
    }


    private void deleteTypeAndDataFetcherInGraphQl(SchemaTree schemaTree) {
        typeRegistryBuilder.deleteSchema(schemaTree);
        typeRegistryBuilder.buildTypeRegistry();
        runtimeWiringBuilder.deleteSchemaDataFetcher(schemaTree);
    }

    //todo finish update
    public GraphQL updateTypeInGraphQl(SchemaTree schemaTree) {
        this.updateTypeAndDataFetcherInGraphQl(schemaTree);
        GraphQLSchema graphQLSchema = new SchemaGenerator().makeExecutableSchema(typeRegistryBuilder.getTypeDefinitionRegistry()
                , runtimeWiringBuilder.getRuntimeWiring());
        return newGraphQL(graphQLSchema).build();
    }

    private void updateTypeAndDataFetcherInGraphQl(SchemaTree schemaTree) {
    }

}

