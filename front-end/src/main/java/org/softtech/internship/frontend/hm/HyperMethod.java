/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.softtech.internship.frontend.hm;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.softtech.internship.frontend.Currency;
import org.softtech.internship.frontend.Material;
import org.softtech.internship.frontend.Recipe;
import org.softtech.internship.frontend.RecipeMaterial;
import org.softtech.internship.frontend.dto.CurrencyAddDTO;
import org.softtech.internship.frontend.dto.CurrencyUpdateDTO;
import org.softtech.internship.frontend.dto.MaterialAddDTO;
import org.softtech.internship.frontend.dto.MaterialUpdateDTO;
import org.softtech.internship.frontend.dto.RecipeAddDTO;
import org.softtech.internship.frontend.dto.RecipeUpdateDTO;

/**
 *
 * @author yusa
 */
public class HyperMethod {

    private static final String HOST_URL = "http://localhost:8080/api/";
    private static List<Material> materials = new ArrayList<>();
    private static List<Currency> currencies = new ArrayList<>();
    private static List<Recipe> recipes = new ArrayList<>();

    private static void getAllMaterials() {
        try {
            materials = new ArrayList<>();
            String urlStr = HOST_URL.concat("materials");
            HttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(urlStr);
            HttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            String responseBodyJson = EntityUtils.toString(response.getEntity());
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> responseBody = objectMapper.readValue(responseBodyJson, Map.class);
            if (statusCode == 200) {
                ArrayList data = (ArrayList) responseBody.get("data");
                for (Object row : data) {
                    LinkedHashMap<String, ?> nRow = (LinkedHashMap) row;
                    String id = (String) nRow.get("material_id");
                    String name = (String) nRow.get("material_name");
                    Double unit_price = (Double) nRow.get("unit_price");
                    String currency_name = (String) nRow.get("currency_name");
                    Material material = Material.builder()
                            .id(id)
                            .name(name)
                            .unit_price(unit_price)
                            .currency_name(currency_name).build();
                    materials.add(material);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(HyperMethod.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void getAllCurrencies() {
        try {
            currencies = new ArrayList<>();
            String urlStr = HOST_URL.concat("currencies");
            HttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(urlStr);
            HttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            String responseBodyJson = EntityUtils.toString(response.getEntity());
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> responseBody = objectMapper.readValue(responseBodyJson, Map.class);
            if (statusCode == 200) {
                ArrayList data = (ArrayList) responseBody.get("data");
                for (Object row : data) {
                    LinkedHashMap<String, ?> nRow = (LinkedHashMap) row;
                    String id = (String) nRow.get("currency_id");
                    String name = (String) nRow.get("currency_name");
                    Double rate = (Double) nRow.get("currency_rate");
                    Currency currency = Currency.builder()
                            .id(id)
                            .name(name)
                            .rate(rate)
                            .build();
                    currencies.add(currency);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(HyperMethod.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void getAllRecipes() {
        try {
            recipes = new ArrayList<>();
            String urlStr = HOST_URL.concat("recipes");
            HttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(urlStr);
            HttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            String responseBodyJson = EntityUtils.toString(response.getEntity());
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> responseBody = objectMapper.readValue(responseBodyJson, Map.class);
            if (statusCode == 200) {
                ArrayList data = (ArrayList) responseBody.get("data");
                for (Object row : data) {
                    LinkedHashMap<String, ?> nRow = (LinkedHashMap) row;
                    String id = (String) nRow.get("recipe_id");
                    String name = (String) nRow.get("recipe_name");
                    ArrayList materialList = (ArrayList) nRow.get("materials");
                    List<RecipeMaterial> material_list = new ArrayList<>();
                    for (Object mRow : materialList) {
                        LinkedHashMap<String, ?> materialRow = (LinkedHashMap) mRow;
                        RecipeMaterial recipeMaterial = RecipeMaterial.builder()
                                .material_id((String) materialRow.get("material_id"))
                                .quantity((Double) materialRow.get("quantity"))
                                .build();
                        material_list.add(recipeMaterial);
                    }
                    Recipe recipe = Recipe.builder()
                            .id(id)
                            .name(name)
                            .materials(material_list)
                            .build();
                    recipes.add(recipe);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(HyperMethod.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static List<Material> getMaterials() {
        getAllMaterials();
        return materials;
    }

    public static List<Currency> getCurrencies() {
        getAllCurrencies();
        return currencies;
    }

    public static List<Recipe> getRecipes() {
        getAllRecipes();
        return recipes;
    }

    public static boolean deleteMaterial(String id) {
        try {
            String urlStr = HOST_URL.concat(String.format("materials/delete?id=%s", id));
            HttpClient httpClient = HttpClients.createDefault();
            HttpDelete httpDel = new HttpDelete(urlStr);
            HttpResponse response = httpClient.execute(httpDel);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                return true;
            }
            return false;
        } catch (IOException ex) {
            Logger.getLogger(HyperMethod.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public static boolean deleteCurrency(String id) {
        try {
            String urlStr = HOST_URL.concat(String.format("currencies/delete?id=%s", id));
            HttpClient httpClient = HttpClients.createDefault();
            HttpDelete httpDel = new HttpDelete(urlStr);
            HttpResponse response = httpClient.execute(httpDel);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                return true;
            }
            return false;
        } catch (IOException ex) {
            Logger.getLogger(HyperMethod.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public static boolean deleteRecipe(String id) {
        try {
            String urlStr = HOST_URL.concat(String.format("recipes/delete?id=%s", id));
            HttpClient httpClient = HttpClients.createDefault();
            HttpDelete httpDel = new HttpDelete(urlStr);
            HttpResponse response = httpClient.execute(httpDel);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                return true;
            }
            return false;
        } catch (IOException ex) {
            Logger.getLogger(HyperMethod.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public static boolean addMaterial(MaterialAddDTO materialAddDTO) {
        try {
            String urlStr = HOST_URL.concat("materials/create");
            HttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(urlStr);
            httpPost.setHeader("Content-Type", "application/json");
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonPayload = objectMapper.writeValueAsString(materialAddDTO);
            StringEntity entity = new StringEntity(jsonPayload);
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 201) {
                return true;
            }
            return false;
        } catch (IOException ex) {
            Logger.getLogger(HyperMethod.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public static boolean addCurrency(CurrencyAddDTO currencyAddDTO) {
        try {
            String urlStr = HOST_URL.concat("currencies/create");
            HttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(urlStr);
            httpPost.setHeader("Content-Type", "application/json");
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonPayload = objectMapper.writeValueAsString(currencyAddDTO);
            StringEntity entity = new StringEntity(jsonPayload);
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 201) {
                return true;
            }
            return false;
        } catch (IOException ex) {
            Logger.getLogger(HyperMethod.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public static boolean addRecipe(RecipeAddDTO recipeAddDTO) {
        try {
            String urlStr = HOST_URL.concat("recipes/create");
            HttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(urlStr);
            httpPost.setHeader("Content-Type", "application/json");
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonPayload = objectMapper.writeValueAsString(recipeAddDTO);
            StringEntity entity = new StringEntity(jsonPayload);
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 201) {
                return true;
            }
            return false;
        } catch (IOException ex) {
            Logger.getLogger(HyperMethod.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public static boolean updateMaterial(String id, MaterialUpdateDTO materialUpdateDTO){
        try {
            String urlStr = HOST_URL.concat(String.format("materials/update/%s", id));
            HttpClient httpClient = HttpClients.createDefault();
            HttpPut httpPut = new HttpPut(urlStr);
            httpPut.setHeader("Content-Type", "application/json");
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonPayload = objectMapper.writeValueAsString(materialUpdateDTO);
            StringEntity entity = new StringEntity(jsonPayload);
            httpPut.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPut);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                return true;
            }
            return false;
        } catch (IOException ex) {
            Logger.getLogger(HyperMethod.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public static boolean updateCurrency(String id, CurrencyUpdateDTO currencyUpdateDTO){
        try {
            String urlStr = HOST_URL.concat(String.format("currencies/update/%s", id));
            HttpClient httpClient = HttpClients.createDefault();
            HttpPut httpPut = new HttpPut(urlStr);
            httpPut.setHeader("Content-Type", "application/json");
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonPayload = objectMapper.writeValueAsString(currencyUpdateDTO);
            StringEntity entity = new StringEntity(jsonPayload);
            httpPut.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPut);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                return true;
            }
            return false;
        } catch (IOException ex) {
            Logger.getLogger(HyperMethod.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public static boolean updateRecipe(String id, RecipeUpdateDTO recipeUpdateDTO){
        try {
            String urlStr = HOST_URL.concat(String.format("recipes/update/%s", id));
            HttpClient httpClient = HttpClients.createDefault();
            HttpPut httpPut = new HttpPut(urlStr);
            httpPut.setHeader("Content-Type", "application/json");
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonPayload = objectMapper.writeValueAsString(recipeUpdateDTO);
            StringEntity entity = new StringEntity(jsonPayload);
            httpPut.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPut);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                return true;
            }
            return false;
        } catch (IOException ex) {
            Logger.getLogger(HyperMethod.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
