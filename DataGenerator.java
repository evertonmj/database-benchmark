import com.github.javafaker.Faker;
import com.github.javafaker.TwinPeaks;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.*;

public class DataGenerator {

    public static HashMap<String, String> generateDataQuotes(int size) {
        Faker faker = new Faker();
        HashMap<String, String> result = new HashMap<>();

        for (int i = 0; i < size; i++) {
            String randomValue = faker.twinPeaks().quote();
            
            result.put(String.valueOf(randomValue.hashCode()), randomValue);
        }

        return result;
    }

    public static HashMap<String, String> generateData(int size) {
        Faker faker = new Faker();
        HashMap<String, String> result = new HashMap<>();
        long i = 0;
        for (; i < size; i++) {
            String city = faker.address().cityName();
            String country = faker.address().country();
            String street = faker.address().streetAddress();
            String name = faker.name().name();

            String json = new Gson().toJson(Map.of(
                    "city", city,
                    "country", country,
                    "street", street,
                    "name", name
            ));

            result.put("data:"+i, json);
        }

        String json = new Gson().toJson(Map.of(
                "city", "Camacari",
                "country", "Brazil",
                "street", "Rua Cinco do Canal",
                "name", "Ever"
        ));

        result.put("data:"+(i+1), json);

        return result;
    }

    public static List<String> getSearchTermsFromData(Collection<String> data, Integer size) {
        List<String> terms = new ArrayList<>();

//        for(int i = 0; i < size; i++) {
//            String term = List.of(data).get(i).toString();
//            terms.add(term);
//        }

        terms.add("Brazil");
        return terms;
    }
    
}
