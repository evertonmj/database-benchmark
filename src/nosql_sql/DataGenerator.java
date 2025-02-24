package src.nosql_sql;

import com.github.javafaker.Faker;
import com.google.gson.Gson;

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

        String city = faker.address().cityName();

        String country = faker.address().country();
        String street = faker.address().streetName();
        String name = faker.name().fullName(); // Added fullName() to make it specific
        List<Map<String, String>> favoriteQuotes = List.of(
                Map.of("quote", faker.twinPeaks().character(), "character", faker.twinPeaks().character()),
                Map.of("quote", faker.hitchhikersGuideToTheGalaxy().quote(), "character", faker.hitchhikersGuideToTheGalaxy().character()));

        for (; i < size; i++) {
            String json = new Gson().toJson(Map.of(
            "city", city,
                    "country", country,
                    "street", street,
                    "name", name,
                    "favoriteQuotes", favoriteQuotes
            ));

            result.put("data:"+i, json);
        }

        String json = new Gson().toJson(Map.of(
                "city", "Camacari",
                "country", "Brazil",
                "street", "Rua Cinco do Canal",
                "name", "Ever",
                "favoriteQuotes", List.of(
                        Map.of("quote", "She's dead, wrapped in plastic!", "character", "Peter Martell"),
                        Map.of("quote", "I am Groot", "character", "Groot")
                )
        ));

        result.put("data:"+(i+1), json);


        return result;
    }

    public static List<String> getSearchTermsFromData(Collection<String> data, Integer size) {
        List<String> terms = new ArrayList<>();

        List<String> dataAsList = new ArrayList<>(data);
        int boundedSize = Math.min(size, dataAsList.size());
        Random random = new Random();

        for(int i = 0; i < size
                ; i++) {
            int randomIndex = random.nextInt(dataAsList.size()); // Use dataAsList size
            String term = dataAsList.get(randomIndex); // Fetch directly from dataAsList
            terms.add(term);

        }

        return terms;
    }
    
}
