package com.esprit.knowme_mobile.GUI.Menu;

import com.codename1.components.FloatingActionButton;
import com.codename1.components.ToastBar;
import com.codename1.io.FileSystemStorage;
import com.codename1.io.Log;
import com.codename1.ui.*;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.list.DefaultListCellRenderer;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.util.ImageIO;
import com.codename1.ui.validation.LengthConstraint;
import com.codename1.ui.validation.NumericConstraint;
import com.codename1.ui.validation.Validator;
import com.esprit.knowme_mobile.entities.Categorie;
import com.esprit.knowme_mobile.entities.Menu;
import com.esprit.knowme_mobile.services.CategorieService;
import com.esprit.knowme_mobile.services.MenuService;
import com.esprit.knowme_mobile.utils.FileChooser.FileChooser;
import javafx.scene.chart.CategoryAxis;

import java.io.IOException;
import java.io.OutputStream;

public class MenuAddForm extends Form {
    private TextField tfName, tfDescription;
    private FloatingActionButton btnSave;
    ComboBox<Categorie> categorieComboBox;
    private Button  btnImg;
    private Image img ;
    private String imageName;




    public MenuAddForm(){
        super("nouveau menu", BoxLayout.y());
        addGUI();
    }

    private void addGUI() {

        FontImage icon = FontImage.createMaterial(FontImage.MATERIAL_ARROW_BACK, "TitleCommand", 5);
        this.getToolbar().addCommandToLeftBar(null,icon,evt1 -> new MenuListForm().show());
        btnSave = FloatingActionButton.createFAB(FontImage.MATERIAL_ADD);
        btnImg = new Button("Image");
        tfName = new TextField();
        tfDescription = new TextField();
        tfName.setHint("Nom");
        tfDescription.setHint("Description");
        categorieComboBox = new ComboBox<>();
        CategorieService.getInstance().findAll().forEach(categorie -> categorieComboBox.addItem(categorie));

        Validator val = new Validator();
        val.addConstraint(tfName, new LengthConstraint(3));
        val.addConstraint(tfDescription, new LengthConstraint(5));

        Container upload = new Container(new FlowLayout(Component.CENTER,Component.CENTER));
        upload.add(btnImg).add(img);
        upload.setLeadComponent(btnImg);


        this.addAll(
                upload,
                new Container(BoxLayout.xCenter()).add(tfName),
                new Container(BoxLayout.xCenter()).add(tfDescription),
                categorieComboBox,
//                new Container(new BoxLayout(BoxLayout.Y_AXIS)).add(categorieComboBox),
                btnSave);



        btnSave.addActionListener(evt -> {
            if(tfName.getText().length() == 0 || tfDescription.getText().length() == 0 || imageName.length() == 0 ){
                ToastBar.showErrorMessage("les champs sont obligatoire !");
            }else{
                Menu menu = new Menu();
                menu.setName(tfName.getText());
                menu.setDescription(tfDescription.getText());
                menu.setImg(imageName);
                menu.setCategorie(categorieComboBox.getSelectedItem());

                if(MenuService.getInstance().save(menu)){
                    Dialog.show("Succée d'ajout", tfName.getText()+"est ajouté !", "OK", null);
                    new MenuListForm().show();
                }else{
                    ToastBar.showErrorMessage("Erreur d'ajout");
                }
            }

        });

        btnImg.addActionListener((ActionEvent e) -> {
            if (FileChooser.isAvailable()) {
                // FileChooser.setOpenFilesInPlace(true);
                FileChooser.showOpenDialog(false, ".jpg, .jpeg, .png", (ActionEvent e2) -> {
                    String file = (String) e2.getSource();
                    if (file == null) {
                        add("No file was selected");
                        revalidate();
                    } else {
                        Image logo;

                        try {
                            logo = Image.createImage(file).scaledHeight(500);
                            add(logo);
                            if (file.lastIndexOf(".") > 0) {
                                StringBuilder hi = new StringBuilder(file);
                                if (file.startsWith("file://")) {
                                    hi.delete(0, 7);
                                }
                                Log.p(hi.toString());
                                String namePic = saveFileToDevice(file);
                                this.imageName = namePic;

                                revalidate();


                            }
                            String imageFile = FileSystemStorage.getInstance().getAppHomePath() + imageName;

                            try (OutputStream os = FileSystemStorage.getInstance().openOutputStream(imageFile)) {
                                ImageIO.getImageIO().save(logo, os, ImageIO.FORMAT_PNG, 1);
                            } catch (IOException err) {
                            }
                        } catch (IOException ex) {
                        }
                    }
                });
            }
        });

    }

    private String saveFileToDevice(String file) {
        int index = file.lastIndexOf("/");
        file = file.substring(index + 1);
        return file;
    }
}
