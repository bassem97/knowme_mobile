package com.esprit.knowme_mobile.GUI.Ingredient;

import com.codename1.components.FloatingActionButton;
import com.codename1.components.ToastBar;
import com.codename1.io.FileSystemStorage;
import com.codename1.io.Log;
import com.codename1.ui.*;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
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

public class IngredientAddForm extends Form {
    private TextField tfDescription;
    private FloatingActionButton btnSave;
    ComboBox<Menu> menuComboBox;





    public IngredientAddForm(){
        super("nouveau inggredient", BoxLayout.y());
        addGUI();
    }

    private void addGUI() {

        FontImage icon = FontImage.createMaterial(FontImage.MATERIAL_ARROW_BACK, "TitleCommand", 5);
        this.getToolbar().addCommandToLeftBar(null,icon,evt1 -> new IngredientListForm().show());
        btnSave = FloatingActionButton.createFAB(FontImage.MATERIAL_ADD);
        tfDescription = new TextField();
        tfDescription.setHint("Description");
        menuComboBox = new ComboBox<>();
        MenuService.getInstance().findAll().forEach(menu -> menuComboBox.addItem(menu));

        Validator val = new Validator();
        val.addConstraint(tfDescription, new LengthConstraint(5));



        this.addAll(
                new Container(BoxLayout.xCenter()).add(tfDescription),
                menuComboBox,
                btnSave);



        btnSave.addActionListener(evt -> {
            if( tfDescription.getText().length() == 0 ){
                ToastBar.showErrorMessage("les champs sont obligatoire !");
            }else{
                Ingredient ingredient = new Ingredient();
                ingredient.setDescription(tfDescription.getText());
                ingredient.setMenu(menuComboBox.getSelectedItem());

                if(IngredientService.getInstance().save(ingredient)){
                    Dialog.show("Succée d'ajout", tfDescription.getText()+"est ajouté !", "OK", null);
                    new IngredientListForm().show();
                }else{
                    ToastBar.showErrorMessage("Erreur d'ajout");
                }
            }

        });



    }

}
