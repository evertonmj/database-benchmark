package benchmarks.data;

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

        HashMap<String, String> data = new HashMap<>();
        data.put("quote", faker.twinPeaks().character());
        data.put("character", faker.twinPeaks().character());
        data.put("quote", faker.ancient().primordial());
        data.put("character", faker.ancient().primordial());

        List<Map<String, String>> favoriteQuotes = new ArrayList<>();
        favoriteQuotes.add(data);

        for (; i < size; i++) {
            HashMap<String, String> genData = new HashMap<>();
            genData.put("city", city);
            genData.put("country", country);
            genData.put("street", street);
            genData.put("name", name);
            genData.put("favoriteQuotes", favoriteQuotes.toString());
            String json = new Gson().toJson(genData);

            result.put("data:"+i, json);
        }
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
