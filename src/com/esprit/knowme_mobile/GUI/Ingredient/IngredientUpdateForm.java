package com.esprit.knowme_mobile.GUI.Ingredient;

import com.codename1.components.ImageViewer;
import com.codename1.components.ToastBar;
import com.codename1.io.FileSystemStorage;
import com.codename1.io.Log;
import com.codename1.ui.*;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.util.ImageIO;
import com.codename1.ui.validation.LengthConstraint;
import com.codename1.ui.validation.Validator;
import com.esprit.knowme_mobile.entities.Categorie;
import com.esprit.knowme_mobile.entities.Ingredient;
import com.esprit.knowme_mobile.entities.Menu;
import com.esprit.knowme_mobile.services.CategorieService;
import com.esprit.knowme_mobile.services.IngredientService;
import com.esprit.knowme_mobile.services.MenuService;
import com.esprit.knowme_mobile.utils.FileChooser.FileChooser;

import java.io.IOException;
import java.io.OutputStream;

public class IngredientUpdateForm extends Form {
    private Ingredient ingredient;
    private TextField tfName, tfDescription;
    private Label lbExpiration,lbCategorie;
    ComboBox<Menu> menusComboBox;
    private ImageViewer imageViewer;
    private String imageName;



    public IngredientUpdateForm(Ingredient ingredient) {
        super("", BoxLayout.y());
        this.ingredient = ingredient;
        tfName = new TextField();
        tfDescription = new TextField();
        lbExpiration = new Label();
        lbCategorie = new Label();
        menusComboBox = new ComboBox<>();
        menusComboBox.addItem(new Menu("Selection un menu"));
        MenuService.getInstance().findAll().forEach(menu -> menusComboBox.addItem(menu));

        addGUI();


    }

    private void addGUI() {
        FontImage icon = FontImage.createMaterial(FontImage.MATERIAL_ARROW_BACK, "TitleCommand", 5);
        FontImage delete = FontImage.createMaterial(FontImage.MATERIAL_DELETE, "TitleCommand", 5);
        FontImage save = FontImage.createMaterial(FontImage.MATERIAL_SAVE, "TitleCommand", 5);

        this.getToolbar().addCommandToLeftBar(null,icon,evt1 -> new IngredientListForm().show());
        this.getToolbar().addCommandToRightBar(null,delete,evt1 ->{
            if(Dialog.show("Confirmation", "Are u sure to delete "+ingredient.getDescription()+" ?", "Yes", "Cancel" ))
            {
                IngredientService.getInstance().delete(ingredient.getId());
                new IngredientListForm().show();
            }
        });
        this.getToolbar().addCommandToRightBar(null,save,evt1 -> {
            if(menusComboBox.getSelectedIndex() == 0 || tfDescription.getText().length() == 0 ){
                ToastBar.showErrorMessage("les champs sont obligatoire !");
            }else{
                Ingredient ingredient1 = new Ingredient();
                ingredient1.setId(ingredient.getId());
                ingredient1.setDescription(tfDescription.getText());
                if (menusComboBox.getSelectedIndex() !=0)
                    ingredient1.setMenu(menusComboBox.getSelectedItem());
                else
                    ingredient1.setMenu(ingredient.getMenu());

                if(IngredientService.getInstance().update(ingredient1)){
                    Dialog.show("Successful", "ingredient updated !" , "OK", null);
                    new IngredientListForm().show();
                }
                else
                    Dialog.show("Failure", " cannot updated !" , "OK", null);
            }


        });

        Validator val = new Validator();
        val.addConstraint(tfDescription, new LengthConstraint(5));


        tfDescription.setText(ingredient.getDescription());
        menusComboBox.setSelectedItem(ingredient.getMenu());


        TextField menuu =new TextField();
        menuu.setText(ingredient.getMenu().getName());
        menuu.setEditable(false);



        this.addAll(
                new Container(BoxLayout.y()).add(new Label("Description")).add(tfDescription),
                new Container(BoxLayout.y()).add(new Label("Menu")).add(menuu),
                menusComboBox);
    }

}
