package com.esprit.knowme_mobile.GUI.Ingredient;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.esprit.knowme_mobile.GUI.Categorie.CategorieListForm;
import com.esprit.knowme_mobile.GUI.Menu.MenuListForm;
import com.esprit.knowme_mobile.entities.Ingredient;
import com.esprit.knowme_mobile.entities.Menu;
import com.esprit.knowme_mobile.services.IngredientService;
import com.esprit.knowme_mobile.services.MenuService;

import java.util.ArrayList;

public class IngredientListForm extends Form {
    private final ArrayList<Ingredient> ingredients;

    public IngredientListForm() {
        super("Ingredients", BoxLayout.y());
        ingredients = IngredientService.getInstance().findAll();
        addGUI();

    }

    private void addGUI() {
        this.getToolbar().addCommandToSideMenu(".",null,evt1 -> System.out.println(evt1));
        this.getToolbar().addCommandToSideMenu("Categories",null,evt1 -> new CategorieListForm().show());
        this.getToolbar().addCommandToSideMenu("Menu",null,evt1 -> new MenuListForm().show());
        this.getToolbar().addCommandToRightBar(null, FontImage.createMaterial(FontImage.MATERIAL_ADD, "TitleCommand", 5), evt1 -> new IngredientAddForm().show());

        ingredients.forEach(ingredient -> this.add(item(ingredient)));

        this.getToolbar().addSearchCommand(e ->{
            String text = (String)e.getSource();
            if (text != null && text.length() != 0){
                this.removeAll();
                ingredients
                        .stream()
                        .filter(ingredient -> ingredient.getDescription().contains(text))
                        .forEach(ingredient ->{
                            this.add(item(ingredient));
                        } );
            }else{
                this.removeAll();
                ingredients.stream().forEach(ingredient -> this.add(item(ingredient)));
            }

        });
    }

    private Container item(Ingredient ingredient){
        try {
            Container global = new Container(BoxLayout.x());
            Label lbDescription = new Label(ingredient.getDescription());
            global.add(lbDescription);
            lbDescription.addPointerReleasedListener(evt -> new IngredientUpdateForm(ingredient).show());
            global.setLeadComponent(lbDescription);
            return global;

        } catch (Exception e) {
            e.printStackTrace();
            return new Container();
        }
    }
}
