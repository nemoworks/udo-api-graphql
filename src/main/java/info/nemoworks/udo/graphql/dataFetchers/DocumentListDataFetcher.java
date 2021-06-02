package info.nemoworks.udo.graphql.dataFetchers;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import info.nemoworks.udo.model.Udo;
import info.nemoworks.udo.service.UdoService;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DocumentListDataFetcher implements DataFetcher<List<HashMap<String, LinkedTreeMap>>> {

    private final UdoService udoService;
    private String documentCollectionName;
    private String keyNameInParent;

    @Autowired
    public DocumentListDataFetcher(UdoService udoService) {
        this.udoService = udoService;
    }

    public void setDocumentCollectionName(String documentCollectionName) {
        this.documentCollectionName = documentCollectionName;
    }

    public void setKeyNameInParent(String keyNameInParent) {
        this.keyNameInParent = keyNameInParent;
    }

    @Override
    public List<HashMap<String, LinkedTreeMap>> get(
        DataFetchingEnvironment dataFetchingEnvironment) {
        String typeId = dataFetchingEnvironment.getArgument("udoTypeId").toString();
        List<HashMap<String, LinkedTreeMap>> udos = this.getDocumentsByAggregation(typeId);
        return udos;
    }

    private List<HashMap<String, LinkedTreeMap>> getDocumentsByAggregation(String typeId) {
        List<Udo> udos = udoService.getUdoByType(udoService.getTypeById(typeId));
        List<HashMap<String, LinkedTreeMap>> udoList = new LinkedList<>();
        for (Udo udo : udos) {
            HashMap hashMap = new Gson().fromJson(udo.getData().toString(), HashMap.class);
            hashMap.put("udoi", udo.getId());
            udoList.add(hashMap);
        }
        return udoList;
    }

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

//   private List<Udo> getDocumentsByLinkList()
}
