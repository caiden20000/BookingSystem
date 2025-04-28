package codes.c2k.BookingSystem;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class NameResolverService {

    @Value("${nameResolver.employerBaseUrl}")
    private String employerRoot;
    @Value("${nameResolver.chefBaseUrl}")
    private String chefRoot;
    
    public NameResolverService() {
    }

    private String getFullEmployerUrl(String id) {
        // TODO: replace when available
        return this.employerRoot + "/api/" + id + "/name";
    }

    private String getFullChefUrl(String id) {
        // TODO: replace when available
        return this.chefRoot + "/api/chefs/getChef?username=" + id;
    }

    private String getSingleParamFromRequest(String url, String paramPath) {
        RestTemplate restTemplate = new RestTemplate();
        String json = restTemplate.getForObject(url, String.class);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);
            String param = root.at(paramPath).asText();
            return param;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON!", e);
        }
    }

    private String getEmployerNameFromId(String id) {
        // TODO: Change paramPath when final information is confirmed
        // return getSingleParamFromRequest(getFullEmployerUrl(id), "/name");

        // PLACEHOLDER!
        return "Mr. Employer " + id;
    }

    private String getChefNameFromId(String id) {
        // TODO: Change paramPath when final information is confirmed
        StringBuilder fullName = new StringBuilder();
        fullName.append(getSingleParamFromRequest(getFullChefUrl(id), "/firstName"));
        fullName.append(" ");
        fullName.append(getSingleParamFromRequest(getFullChefUrl(id), "/lastName"));
        return fullName.toString();

        // PLACEHOLDER!
        // return "Mr. Chef " + id;
    }

    public String getName(String id, String idType) {
        if (idType.equalsIgnoreCase("employer")) {
            return getEmployerNameFromId(id);
        } else if (idType.equalsIgnoreCase("chef")) {
            return getChefNameFromId(id);
        } else {
            return "ERR";
        }
    }

}
