package info.nemoworks.udo.graphql.dataFetchers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import info.nemoworks.udo.model.Udo;
import info.nemoworks.udo.service.UdoService;
import info.nemoworks.udo.service.UdoServiceException;
import org.springframework.stereotype.Component;

@Component
public class CreateDocumentMutation implements DataFetcher<JsonNode> {

    private final UdoService udoService;

    public CreateDocumentMutation(UdoService udoService) {
        this.udoService = udoService;
    }


    @Override
    public JsonNode get(DataFetchingEnvironment dataFetchingEnvironment) {
        String udoi = dataFetchingEnvironment.getArgument("udoi").toString();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode content = mapper.convertValue(dataFetchingEnvironment.getArgument("content"), JsonNode.class);
        String schemaId = dataFetchingEnvironment.getArgument("schemaId").toString();
        Udo udo = this.createNewUdo(udoi, schemaId, content);
        assert udo != null;
        JsonNode json = udo.getData();
//        json.put("udoi", udo.getId());
//        json.put("schemaId", udo.getSchemaId());
        return json;
    }

    private Udo createNewUdo(String id,String schemaId, JsonNode content) {
        Udo udo = new Udo(id,schemaId, content);
        try {
            return udoService.saveOrUpdateUdo(udo);
        } catch (UdoServiceException e) {
            e.printStackTrace();
        }
        return null;
    }


}
