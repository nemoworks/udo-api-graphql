package info.nemoworks.udo.graphql.dataFetchers;

import com.fasterxml.jackson.databind.JsonNode;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import info.nemoworks.udo.service.UdoService;
import org.springframework.stereotype.Component;


@Component
public class DocumentDataFetcher implements DataFetcher<JsonNode> {

    private String documentCollectionName;
    private String keyNameInParent;

    private final UdoService udoService;

    public DocumentDataFetcher(UdoService udoService) {
        this.udoService = udoService;
    }


    public void setDocumentCollectionName(String documentCollectionName) {
        this.documentCollectionName = documentCollectionName;
    }

    public void setKeyNameInParent(String keyNameInParent) {
        this.keyNameInParent = keyNameInParent;
    }


    @lombok.SneakyThrows
    @Override
    public JsonNode get(DataFetchingEnvironment dataFetchingEnvironment) {
        String id = String.valueOf(dataFetchingEnvironment.getArguments().get("udoi"));
//        if (id.equals("null")) {
//            JSONObject JsonObject = JSONObject.fromObject(dataFetchingEnvironment.getSource());
//            id = JsonObject.getString(keyNameInParent);
//        }
//        String collection = dataFetchingEnvironment.getArgument("collection").toString();
        return this.getDocumentByAggregation(id, documentCollectionName);
    }

    private JsonNode getDocumentByAggregation(String id, String collection){
        return udoService.getUdoById(id).getData();
    }
}
