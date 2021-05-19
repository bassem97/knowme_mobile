package com.esprit.knowme_mobile.GUI.Categorie;

import com.codename1.components.ToastBar;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.TextField;
import com.codename1.ui.layouts.BoxLayout;
import com.esprit.knowme_mobile.entities.Categorie;
import com.esprit.knowme_mobile.services.CategorieService;

import java.awt.*;

public class CategorieUpdateForm extends Form {

    private TextField tfName, tfDescription;
    private  Categorie categorie;


    public CategorieUpdateForm(Categorie categorie) {
        super("", BoxLayout.y());
        this.categorie = categorie;
        addGUI(categorie);
    }

    private void addGUI(Categorie categorie) {
        FontImage icon = FontImage.createMaterial(FontImage.MATERIAL_ARROW_BACK, "TitleCommand", 5);
        FontImage delete = FontImage.createMaterial(FontImage.MATERIAL_DELETE, "TitleCommand", 5);
        FontImage save = FontImage.createMaterial(FontImage.MATERIAL_SAVE, "TitleCommand", 5);
        this.getToolbar().addCommandToLeftBar(null,icon,evt1 -> new CategorieListForm().show());
        this.getToolbar().addCommandToRightBar(null,delete,evt1 ->{
            if(Dialog.show("Confirmation", "Are u sure to delete "+categorie.getNom()+" ?", "Yes", "Cancel" ))
            {
                CategorieService.getInstance().delete(categorie.getId());
                new CategorieListForm().show();
            }
        });
        this.getToolbar().addCommandToRightBar(null,save,evt1 -> {
            if(tfName.getText().length() == 0 || tfDescription.getText().length() == 0 ){
                ToastBar.showErrorMessage("les champs sont obligatoire !");
            }else{
                Categorie categorie1 = new Categorie();
                categorie1.setId(categorie.getId());
                categorie1.setNom(tfName.getText());
                categorie1.setDescription(tfDescription.getText());
//            user2.setImage(img.getImageName());
                if(CategorieService.getInstance().update(categorie1)){
                    com.codename1.ui.Dialog.show("Successful", "categorie updated !" , "OK", null);
                    new CategorieListForm().show();
                }
                else
                    Dialog.show("Failure", " cannot updated !" , "OK", null);
            }


        });


        tfName = new TextField();
        tfDescription = new TextField();
        tfName.setHint("Nom");
        tfDescription.setHint("Description");
        tfName.setText(categorie.getNom());
        tfDescription.setText(categorie.getDescription());

        this.addAll(new com.codename1.ui.Container(BoxLayout.xCenter()).add(tfName),
                new Container(BoxLayout.xCenter()).add(tfDescription));

        tfName.getAllStyles().setBgColor(Color.BLACK.getRGB());
        tfName.getAllStyles().setFgColor(Color.BLACK.getRGB());
        tfName.getStyle().setBgColor(Color.BLACK.getRGB());
        tfName.getStyle().setFgColor(Color.BLACK.getRGB());
        tfDescription.getAllStyles().setBgColor(Color.BLACK.getRGB());
        tfDescription.getAllStyles().setFgColor(Color.BLACK.getRGB());
        tfDescription.getStyle().setBgColor(Color.BLACK.getRGB());
        tfDescription.getStyle().setFgColor(Color.BLACK.getRGB());
    }
}
