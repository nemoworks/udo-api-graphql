package info.nemoworks.udo.graphql.dataFetchers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import info.nemoworks.udo.model.Udo;
import info.nemoworks.udo.model.UdoType;
import info.nemoworks.udo.service.UdoService;
import info.nemoworks.udo.service.UdoServiceException;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class CreateDocumentMutation implements DataFetcher<HashMap<String, LinkedTreeMap>> {

    private final UdoService udoService;

    public CreateDocumentMutation(UdoService udoService) {
        this.udoService = udoService;
    }


    @Override
    public  HashMap<String, LinkedTreeMap> get(DataFetchingEnvironment dataFetchingEnvironment) {
        //String udoi = dataFetchingEnvironment.getArgument("udoi").toString();
        JsonObject content = new Gson().fromJson(dataFetchingEnvironment.getArgument("content").toString(), JsonObject.class);
        String udoTypeId = dataFetchingEnvironment.getArgument("udoTypeId").toString();
        Udo udo = this.createNewUdo(udoTypeId, content);
        assert udo != null;
        HashMap hashMap = new Gson().fromJson(udo.getData().toString(), HashMap.class);
        hashMap.put("udoi",udo.getId());
        return hashMap;
    }

    private Udo createNewUdo(String typeId, JsonObject content) {
        UdoType type = udoService.getTypeById(typeId);
//        type.setId(typeId);
//        assert type != null;
        Udo udo = new Udo(type, content);
        try {
            return udoService.saveOrUpdateUdo(udo);
        } catch (UdoServiceException e) {
            e.printStackTrace();
        }
        return null;
    }


}
