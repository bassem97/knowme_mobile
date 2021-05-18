package com.esprit.knowme_mobile.services;

import com.codename1.io.*;
import com.codename1.processing.Result;
import com.codename1.ui.events.ActionListener;
import com.esprit.knowme_mobile.entities.Categorie;
import com.esprit.knowme_mobile.entities.Menu;
import com.esprit.knowme_mobile.utils.Statics;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuService {

    private static MenuService instance= null;
    private ConnectionRequest req;
    private boolean resultOK;
    ArrayList<Menu> menus;

    private MenuService() {
        req = new ConnectionRequest();
    }
    public static MenuService getInstance() {
        if (instance == null) {
            instance = new MenuService();
        }
        return instance;
    }

    public ArrayList<Menu> findAll(){
        String url = Statics.BASE_URL + "api/menu/list";
        req.setUrl(url);
        req.setPost(false);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                menus = parseMenus(new String(req.getResponseData()));
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return menus;
    }

    public ArrayList<Menu> parseMenus(String jsonText){
        try {
            menus =new ArrayList<>();
            JSONParser j = new JSONParser();
            Map<String,Object> menusListJson = j.parseJSON(new CharArrayReader(jsonText.toCharArray()));
            List<Map<String,Object>> list = (List<Map<String,Object>>)menusListJson.get("root");


            for(Map<String,Object> obj : list){

                Menu t = new Menu();
                float id = Float.parseFloat(obj.get("id").toString());
                t.setId((int)id);
                t.setName(obj.get("name").toString());
                t.setDescription(obj.get("description").toString());
                t.setImg((obj.get("img").toString()));
                t.setIs_expired(Boolean.parseBoolean(obj.get("isExpired").toString()));

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime creationDate = LocalDateTime.parse(obj.get("date").toString()
                        .replace("T", " ")
                        .split("\\+")[0], formatter);
                t.setCreation_date(creationDate);
                LocalDateTime expirationDate = LocalDateTime.parse(obj.get("expirationDate").toString()
                        .replace("T", " ")
                        .split("\\+")[0], formatter);
                t.setExpiration_date(expirationDate);
                float categorie_id = Float.parseFloat(obj.get("categorie_id").toString());
                t.setCategorie(CategorieService.getInstance().findAll().stream().filter(cat -> cat.getId() == (int) categorie_id).findFirst().orElse(null) );
                menus.add(t);
            }
            return menus;

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public boolean update(Menu menu) {
        String url = Statics.BASE_URL + "api/menu/modifier/" + menu.getId() +
                "?name=" + menu.getName() +
                "&description=" + menu.getDescription() +
                "&img=" + menu.getImg() +
                "&is_expired=" + menu.getIs_expired() +
                "&categorie_id=" + menu.getCategorie().getId();
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
        String url = Statics.BASE_URL + "api/menu/delete/"+id;
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

    public boolean save(Menu menu) {
        String url = Statics.BASE_URL + "api/menu/ajouter/categorie/"+menu.getCategorie().getId();
        req.setUrl(url);
        req.setPost(true);
        req.setContentType("application/json");
        try {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("name", menu.getName());
            hashMap.put("description", menu.getDescription());
            hashMap.put("img", menu.getImg());
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
