package com.esprit.knowme_mobile;


import static com.codename1.ui.CN.*;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Dialog;
import com.codename1.ui.Label;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.codename1.io.Log;
import com.codename1.ui.Toolbar;
import java.io.IOException;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.io.NetworkEvent;
import com.esprit.knowme_mobile.GUI.Categorie.CategorieListForm;
import com.esprit.knowme_mobile.entities.Categorie;
import com.esprit.knowme_mobile.entities.Ingredient;
import com.esprit.knowme_mobile.entities.Menu;
import com.esprit.knowme_mobile.services.CategorieService;
import com.esprit.knowme_mobile.services.IngredientService;
import com.esprit.knowme_mobile.services.MenuService;

/**
 * This file was generated by <a href="https://www.codenameone.com/">Codename One</a> for the purpose 
 * of building native mobile applications using Java.
 */
public class MyApplication {

    private Form current;
    private Resources theme;

    public void init(Object context) {
        // use two network threads instead of one
        updateNetworkThreadCount(2);

        theme = UIManager.initFirstTheme("/theme");

        // Enable Toolbar on all Forms by default
        Toolbar.setGlobalToolbar(true);

        // Pro only feature
        Log.bindCrashProtection(true);

        addNetworkErrorListener(err -> {
            // prevent the event from propagating
            err.consume();
            if(err.getError() != null) {
                Log.e(err.getError());
            }
            Log.sendLogAsync();
            Dialog.show("Connection Error", "There was a networking error in the connection to " + err.getConnectionRequest().getUrl(), "OK", null);
        });        
    }
    
    public void start() {
        if(current != null){
            current.show();
            return;
        }
        new CategorieListForm().show();


//        IngredientService.getInstance().delete(14);


//        Menu menu = MenuService.getInstance().findAll().get(0);
//        Menu menu = new Menu();
//        menu.setName("save etst");
//        menu.setDescription("save etst");
//        menu.setImg("save etst");
//        menu.setCategorie(CategorieService.getInstance().findAll().stream().filter(cat -> cat.getId() == 75).findAny().orElse(null));
//        MenuService.getInstance().save(menu);
//        System.out.println(CategorieService.getInstance().delete(76));
//        Categorie categorie = new Categorie();
//        categorie.setNom("test save");
//        categorie.setDescription("specialte mexicaine");
//        System.out.println(CategorieService.getInstance().save(categorie));

    }

    public void stop() {
        current = getCurrentForm();
        if(current instanceof Dialog) {
            ((Dialog)current).dispose();
            current = getCurrentForm();
        }
    }
    
    public void destroy() {
    }

}
