package info.nemoworks.udo.graphql.dataFetchers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import info.nemoworks.udo.service.UdoService;
import info.nemoworks.udo.service.UdoServiceException;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DeleteDocumentMutation implements DataFetcher<JsonNode> {
    private final UdoService udoService;

    private String documentCollectionName;

    public DeleteDocumentMutation(UdoService udoService) {
        this.udoService = udoService;
    }

    public void setDocumentCollectionName(String documentCollectionName) {
        this.documentCollectionName = documentCollectionName;
    }

    @SneakyThrows
    @Override
    public JsonNode get(DataFetchingEnvironment dataFetchingEnvironment) {
        String udoi = dataFetchingEnvironment.getArgument("udoi").toString();
//        String collection = dataFetchingEnvironment.getArgument("collection").toString();
        return deleteDocumentById(udoi, documentCollectionName);
    }

    private JsonNode deleteDocumentById(String id, String collection)  {
        try {
            udoService.deleteUdoById(id);
        } catch (UdoServiceException e) {
            e.printStackTrace();
        }
        Map<String,String> res = new HashMap<>();
        res.put("deleteResult", "document has been deleted");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(res, JsonNode.class);
    }
}
