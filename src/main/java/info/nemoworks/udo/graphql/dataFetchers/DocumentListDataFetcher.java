//package info.nemoworks.udo.graphql.dataFetchers;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.google.gson.JsonObject;
//import com.google.gson.internal.LinkedTreeMap;
//import graphql.schema.DataFetcher;
//import graphql.schema.DataFetchingEnvironment;
//import info.nemoworks.udo.model.Udo;
//import info.nemoworks.udo.service.UdoService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.*;
//
//@Component
//public class DocumentListDataFetcher implements DataFetcher<List<HashMap<String, LinkedTreeMap>>> {
//
//    private final UdoService udoService;
//    private String documentCollectionName;
//    private String keyNameInParent;
//
//    @Autowired
//    public DocumentListDataFetcher(UdoService udoService) {
//        this.udoService = udoService;
//    }
//
//    public void setDocumentCollectionName(String documentCollectionName) {
//        this.documentCollectionName = documentCollectionName;
//    }
//
//    public void setKeyNameInParent(String keyNameInParent) {
//        this.keyNameInParent = keyNameInParent;
//    }
//
//    @Override
//    public List<HashMap<String, LinkedTreeMap>> get(DataFetchingEnvironment dataFetchingEnvironment) {
////        if(keyNameInParent != null){
////            JsonObject JsonObject = dataFetchingEnvironment.getSource();
////            List<String> ids = (List<String>) JsonObject.get(keyNameInParent);
////            return this.getDocumentsByLinkList(ids);
////        }
////        String collection = dataFetchingEnvironment.getArgument("collection").toString();
//        List<Udo> udos = this.getDocuments(documentCollectionName);
//        List<JsonNode> udoContents = new ArrayList<>();
//        udos.forEach(udo -> {
//            JsonObject json = udo.getData();
//            json.put("udoi", udo.getUdoi());
//            udoContents.add(json);
//        });
//        LinkedHashMap<String, Object> filters = dataFetchingEnvironment.getArgument("filter");
//        if (filters == null)
//            return udoContents;
//        else {
//            return getFilterCuts(filters, udos);
//        }
//    }
//
//    private List<Udo> getUdos(String udoTypeId) {
//
//        return udoService.getUdoByType(udoTypeId);
//    }
//
//    private List<JsonNode> getFilterCuts(LinkedHashMap<String, Object> filters, List<Udo> udos) {
//        LinkedHashMap<String, String> filterCuts = new LinkedHashMap<>();
//        for (Map.Entry<String, Object> entry : filters.entrySet()) {
//            String key = entry.getKey();
//            switch (key) {
//                case "AND":
//
//
//                case "OR":
//                default:
//                    break;
//            }
//        }
//        return new ArrayList<>();
//    }
//
////   private List<Udo> getDocumentsByLinkList()
//}
