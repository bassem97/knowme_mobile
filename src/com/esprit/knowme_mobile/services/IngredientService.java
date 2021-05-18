package com.esprit.knowme_mobile.services;

import com.codename1.io.*;
import com.codename1.processing.Result;
import com.codename1.ui.events.ActionListener;
import com.esprit.knowme_mobile.entities.Ingredient;
import com.esprit.knowme_mobile.utils.Statics;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IngredientService {

    private static IngredientService instance= null;
    private ConnectionRequest req;
    private boolean resultOK;
    ArrayList<Ingredient> ingredients;

    private IngredientService() {
        req = new ConnectionRequest();
    }
    public static IngredientService getInstance() {
        if (instance == null) {
            instance = new IngredientService();
        }
        return instance;
    }

    public ArrayList<Ingredient> findAll(){
        String url = Statics.BASE_URL + "api/ingredient/list";
        req.setUrl(url);
        req.setPost(false);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                ingredients = parseMenus(new String(req.getResponseData()));
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return ingredients;
    }

    public ArrayList<Ingredient> parseMenus(String jsonText){
        try {
            ingredients =new ArrayList<>();
            JSONParser j = new JSONParser();
            Map<String,Object> menusListJson = j.parseJSON(new CharArrayReader(jsonText.toCharArray()));
            List<Map<String,Object>> list = (List<Map<String,Object>>)menusListJson.get("root");


            for(Map<String,Object> obj : list){

                Ingredient t = new Ingredient();
                float id = Float.parseFloat(obj.get("id").toString());
                t.setId((int)id);
                t.setDescription(obj.get("description").toString());

                float menu_id = Float.parseFloat(obj.get("menu_id").toString());
                t.setMenu(MenuService.getInstance().findAll().stream().filter(menu -> menu.getId() == (int) menu_id).findFirst().orElse(null) );
                ingredients.add(t);
            }
            return ingredients;

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public boolean update(Ingredient ingredient) {
        String url = Statics.BASE_URL + "api/ingredient/modifier/" + ingredient.getId() +
                "?description=" + ingredient.getDescription() +
                "&menu_id=" + ingredient.getMenu().getId();
        req.setUrl(url);
        req.setContentType("application/json");
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultOK = req.getResponseCode() == 200; //Code HTTP 200 OK
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return resultOK;
    }

    public boolean delete(int id) {
        String url = Statics.BASE_URL + "api/ingredient/delete/"+id;
        req.setUrl(url);
        req.setHttpMethod("DELETE");
        req.setContentType("application/json");
        try {
            req.addResponseListener(new ActionListener<NetworkEvent>() {
                @Override
                public void actionPerformed(NetworkEvent evt) {
                    resultOK = req.getResponseCode() == 200; //Code HTTP 200 OK
                    req.removeResponseListener(this);
                }
            });
            NetworkManager.getInstance().addToQueueAndWait(req);
            return resultOK;
        } catch (Exception e) {
            return false;
        }

    }

    public boolean save(Ingredient ingredient) {
        String url = Statics.BASE_URL + "api/ingredient/ajouter/menu/"+ingredient.getMenu().getId();
        req.setUrl(url);
        req.setPost(true);
        req.setContentType("application/json");
        try {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("description", ingredient.getDescription());
            req.setRequestBody(Result.fromContent(hashMap).toString());
            req.addResponseListener(new ActionListener<NetworkEvent>() {
                @Override
                public void actionPerformed(NetworkEvent evt) {
                    resultOK = req.getResponseCode() == 200; //Code HTTP 200 OK
                    req.removeResponseListener(this);
                }
            });
            NetworkManager.getInstance().addToQueueAndWait(req);
            return resultOK;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
