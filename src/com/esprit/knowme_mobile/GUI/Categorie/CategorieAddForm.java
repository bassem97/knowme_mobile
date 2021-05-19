package com.esprit.knowme_mobile.GUI.Categorie;

import com.codename1.components.FloatingActionButton;
import com.codename1.components.ToastBar;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.Style;
import com.esprit.knowme_mobile.entities.Categorie;
import com.esprit.knowme_mobile.services.CategorieService;
import javafx.scene.chart.CategoryAxis;

public class CategorieAddForm extends Form {
    private TextField tfName, tfDescription;
    private FloatingActionButton btnSave;


    public CategorieAddForm(){
        super("nouveau categorie", BoxLayout.y());
        addGUI();
    }

    private void addGUI() {

        FontImage icon = FontImage.createMaterial(FontImage.MATERIAL_ARROW_BACK, "TitleCommand", 5);
        this.getToolbar().addCommandToLeftBar(null,icon,evt1 -> new CategorieListForm().show());
        btnSave = FloatingActionButton.createFAB(FontImage.MATERIAL_ADD);
        tfName = new TextField();
        tfDescription = new TextField();
        tfName.setHint("Nom");
        tfDescription.setHint("Description");

        this.addAll(new Container(BoxLayout.xCenter()).add(tfName),
                new Container(BoxLayout.xCenter()).add(tfDescription),
                btnSave);



        btnSave.addActionListener(evt -> {
            if(tfName.getText().length() == 0 || tfDescription.getText().length() == 0 ){
                ToastBar.showErrorMessage("les champs sont obligatoire !");
            }else{
                Categorie categorie = new Categorie();
                categorie.setNom(tfName.getText());
                categorie.setDescription(tfDescription.getText());
                if(CategorieService.getInstance().save(categorie)){
                    Dialog.show("Succée d'ajout", tfName.getText()+"est ajouté !", "OK", null);
                    new CategorieListForm().show();
                }else{
                    ToastBar.showErrorMessage("Erreur d'ajout");
                }
            }

        });

    }
}
