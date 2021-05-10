//package info.nemoworks.udo.graphql.dataFetchers;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import graphql.schema.DataFetcher;
//import graphql.schema.DataFetchingEnvironment;
//import info.nemoworks.udo.model.Udo;
//import info.nemoworks.udo.service.UdoService;
//import info.nemoworks.udo.service.UdoServiceException;
//import info.nemoworks.udo.storage.UdoPersistException;
//import lombok.SneakyThrows;
//import org.springframework.stereotype.Component;
//
//import java.util.Objects;
//
//@Component
//public class UpdateDocumentMutation implements DataFetcher<JsonNode> {
//
//    private final UdoService udoService;
//
//    private String documentCollectionName;
//
//    public UpdateDocumentMutation(UdoService udoService) {
//        this.udoService = udoService;
//    }
//
//    public void setDocumentCollectionName(String documentCollectionName) {
//        this.documentCollectionName = documentCollectionName;
//    }
//
//    @SneakyThrows
//    @Override
//    public JsonNode get(DataFetchingEnvironment dataFetchingEnvironment) {
//        String udoi = dataFetchingEnvironment.getArgument("udoi").toString();
//        ObjectMapper mapper = new ObjectMapper();
//        JsonNode content = mapper.convertValue(dataFetchingEnvironment.getArgument("content"), JsonNode.class);
////        Gson gson = new Gson();
////        JsonObject content = JsonParser.parseString(cont.toJSONString()).getAsJsonObject();
////        String collection = dataFetchingEnvironment.getArgument("collection").toString();
//        return Objects.requireNonNull(this.updateDocumentById(udoi, content, documentCollectionName)).getContent();
//    }
//
//    private Udo updateDocumentById(String id, JsonNode content, String collection){
//        Udo udo = udoService.findUdoById(id, collection);
//        assert udo != null;
//        udo.setContent(content);
//        try {
//            return udoService.saveOrUpdateUdo(udo);
//        } catch (UdoServiceException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//}
