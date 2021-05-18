package com.esprit.knowme_mobile.services;

import com.codename1.io.*;
import com.codename1.processing.Result;
import com.codename1.ui.events.ActionListener;
import com.esprit.knowme_mobile.entities.Categorie;
import com.esprit.knowme_mobile.utils.Statics;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategorieService {

    private static CategorieService instance= null;
    private ConnectionRequest req;
    private boolean resultOK;
    ArrayList<Categorie> categories;

    private CategorieService() {
        req = new ConnectionRequest();
    }
    public static CategorieService getInstance() {
        if (instance == null) {
            instance = new CategorieService();
        }
        return instance;
    }

    public ArrayList<Categorie> findAll(){
        String url = Statics.BASE_URL + "api/categorie/list";
        req.setUrl(url);
        req.setPost(false);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                categories = parseCategories(new String(req.getResponseData()));
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return categories;
    }

    public ArrayList<Categorie> parseCategories(String jsonText){
        try {
            categories =new ArrayList<>();
            JSONParser j = new JSONParser();
            Map<String,Object> categoriesListJson = j.parseJSON(new CharArrayReader(jsonText.toCharArray()));
            List<Map<String,Object>> list = (List<Map<String,Object>>)categoriesListJson.get("root");


            for(Map<String,Object> obj : list){

                Categorie t = new Categorie();
                float id = Float.parseFloat(obj.get("id").toString());
                t.setId((int)id);
                t.setNom((obj.get("nom").toString()));
                t.setDescription(obj.get("description").toString());
                categories.add(t);
            }
            return categories;

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public boolean update(Categorie categorie) {
        String url = Statics.BASE_URL + "api/categorie/modifier/" + categorie.getId() + "?nom=" + categorie.getNom() + "&description=" + categorie.getDescription();
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
        String url = Statics.BASE_URL + "api/categorie/delete/"+id;
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

    public boolean save(Categorie categorie) {
        String url = Statics.BASE_URL + "api/categorie/ajouter";
        req.setUrl(url);
        req.setPost(true);
        req.setContentType("application/json");
        try {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("nom", categorie.getNom());
            hashMap.put("description", categorie.getDescription());
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
