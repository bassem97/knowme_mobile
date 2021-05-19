package com.esprit.knowme_mobile.GUI.Categorie;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.util.Resources;

import com.esprit.knowme_mobile.GUI.Ingredient.IngredientListForm;
import com.esprit.knowme_mobile.GUI.Menu.MenuListForm;
import com.esprit.knowme_mobile.entities.Categorie;
import com.esprit.knowme_mobile.services.CategorieService;

import java.io.IOException;
import java.util.ArrayList;

public class CategorieListForm extends Form {

    private Resources theme;
    private ArrayList<Categorie> categories;



    public CategorieListForm() {
        super("Categories", BoxLayout.y());
        try {
            theme = Resources.openLayered("/theme");
        } catch (IOException e) {
            e.printStackTrace();
        }
        categories = CategorieService.getInstance().findAll();
        addGUIs();
    }

    private void addGUIs() {
        this.getToolbar().addCommandToSideMenu(".",null,evt1 -> System.out.println(evt1));
        this.getToolbar().addCommandToSideMenu("Ingredients",null,evt1 -> new IngredientListForm().show());
        this.getToolbar().addCommandToSideMenu("Menus",null,evt1 -> new MenuListForm().show());
        this.getToolbar().addCommandToRightBar(null, FontImage.createMaterial(FontImage.MATERIAL_ADD, "TitleCommand", 5), evt1 -> new CategorieAddForm().show());

        categories.forEach(categorie -> this.add(item(categorie)));
        this.getToolbar().addSearchCommand(e ->{
            String text = (String)e.getSource();
            if (text != null && text.length() != 0){
                this.removeAll();
                categories
                        .stream()
                        .filter(cat -> cat.getNom().contains(text) || cat.getDescription().contains(text))
                        .forEach(categorie ->{
                            this.add(item(categorie));
                        } );
            }else{
                this.removeAll();
                categories.stream().forEach(categorie -> this.add(item(categorie)));
            }

        });


    }

    private Container item(Categorie categorie) {
        Container global = new Container(BoxLayout.x());
        Label lbName = new Label(categorie.getNom());
        lbName.getAllStyles().setFgColor(ColorUtil.rgb(228, 53, 83));
        lbName.getBaseline(10,20);
        lbName.getAllStyles().setFont(Font.createSystemFont(lbName.getUnselectedStyle().getFont().getFace(), Font.STYLE_UNDERLINED, lbName.getUnselectedStyle().getFont().getSize()));
        lbName.getAllStyles().setFont(Font.createSystemFont(lbName.getUnselectedStyle().getFont().getFace(), Font.STYLE_UNDERLINED, 50));

        Container labels = new Container(BoxLayout.y()).add(lbName);
        global.add(labels);

        lbName.addPointerReleasedListener(evt -> new CategorieUpdateForm(categorie).show());
        global.setLeadComponent(lbName);

        return global;
    }
}

