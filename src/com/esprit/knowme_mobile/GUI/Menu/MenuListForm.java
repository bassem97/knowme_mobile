package com.esprit.knowme_mobile.GUI.Menu;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.esprit.knowme_mobile.GUI.Categorie.CategorieListForm;
import com.esprit.knowme_mobile.GUI.Ingredient.IngredientListForm;
import com.esprit.knowme_mobile.entities.Menu;
import com.esprit.knowme_mobile.services.MenuService;

import java.util.ArrayList;

public class MenuListForm extends Form {
    private final ArrayList<Menu> menus;

    public MenuListForm() {
        super("Menus", BoxLayout.y());
        menus = MenuService.getInstance().findAll();
        addGUI();
    }

    private void addGUI() {
        this.getToolbar().addCommandToSideMenu(".",null,evt1 -> System.out.println(evt1));
        this.getToolbar().addCommandToSideMenu("Categories",null,evt1 -> new CategorieListForm().show());
        this.getToolbar().addCommandToSideMenu("Ingredients",null,evt1 -> new IngredientListForm().show());
        this.getToolbar().addCommandToRightBar(null, FontImage.createMaterial(FontImage.MATERIAL_ADD, "TitleCommand", 5), evt1 -> new MenuAddForm().show());

        menus.forEach(menu -> this.add(item(menu)));

        this.getToolbar().addSearchCommand(e ->{
            String text = (String)e.getSource();
            if (text != null && text.length() != 0){
                this.removeAll();
                menus
                        .stream()
                        .filter(menu -> menu.getName().contains(text))
                        .forEach(menu ->{
                            this.add(item(menu));
                        } );
            }else{
                this.removeAll();
                menus.stream().forEach(categorie -> this.add(item(categorie)));
            }

        });
    }

    private Container item(Menu menu){
        try {
            Container global = new Container(BoxLayout.x());
            EncodedImage placeholder = EncodedImage.createFromImage(Image.createImage(300, 300, 0xffff0000), true);
            URLImage background = URLImage.createToStorage(placeholder, menu.getImg(), "");
            Label lbName = new Label(menu.getName());
            lbName.getAllStyles().setFgColor(ColorUtil.rgb(228, 53, 83));
            lbName.getAllStyles().setFont(Font.createSystemFont(lbName.getUnselectedStyle().getFont().getFace(), Font.STYLE_UNDERLINED, lbName.getUnselectedStyle().getFont().getSize()));
            Label lbDescription = new Label(menu.getDescription());
            Label lbDate = new Label(menu.getExpiration_date().toLocalDate().toString());
            Container labels = new Container(BoxLayout.y()).addAll(lbName, lbDescription, lbDate);
            global.add(background);
            global.add(labels);

            lbName.addPointerReleasedListener(evt -> new MenuUpdateForm(menu).show());
            global.setLeadComponent(lbName);

            return global;

        } catch (Exception e) {
            e.printStackTrace();
            return new Container();
        }
    }
}
