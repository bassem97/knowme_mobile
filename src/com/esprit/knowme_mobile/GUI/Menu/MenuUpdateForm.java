package com.esprit.knowme_mobile.GUI.Menu;

import com.codename1.components.ImageViewer;
import com.codename1.components.ToastBar;
import com.codename1.io.FileSystemStorage;
import com.codename1.io.Log;
import com.codename1.ui.*;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.list.DefaultListCellRenderer;
import com.codename1.ui.util.ImageIO;
import com.codename1.ui.validation.LengthConstraint;
import com.codename1.ui.validation.Validator;
import com.esprit.knowme_mobile.GUI.Categorie.CategorieListForm;
import com.esprit.knowme_mobile.entities.Categorie;
import com.esprit.knowme_mobile.entities.Menu;
import com.esprit.knowme_mobile.services.CategorieService;
import com.esprit.knowme_mobile.services.MenuService;
import com.esprit.knowme_mobile.utils.FileChooser.FileChooser;

import javax.swing.text.html.ImageView;
import java.io.IOException;
import java.io.OutputStream;

public class MenuUpdateForm extends Form {
    private Menu menu;
    private TextField tfName, tfDescription;
    private Label lbExpiration,lbCategorie;
    ComboBox<Categorie> categorieComboBox;
    private ImageViewer imageViewer;
    private String imageName;



    public MenuUpdateForm(Menu menu) {
        super("", BoxLayout.y());
        this.menu = menu;
        tfName = new TextField();
        tfDescription = new TextField();
        lbExpiration = new Label();
        lbCategorie = new Label();
        categorieComboBox = new ComboBox<>();
        categorieComboBox.addItem(new Categorie("Selection une categorie"));
        CategorieService.getInstance().findAll().forEach(categorie -> categorieComboBox.addItem(categorie));

        addGUI();


    }

    private void addGUI() {
        FontImage icon = FontImage.createMaterial(FontImage.MATERIAL_ARROW_BACK, "TitleCommand", 5);
        FontImage delete = FontImage.createMaterial(FontImage.MATERIAL_DELETE, "TitleCommand", 5);
        FontImage save = FontImage.createMaterial(FontImage.MATERIAL_SAVE, "TitleCommand", 5);

        this.getToolbar().addCommandToLeftBar(null,icon,evt1 -> new MenuListForm().show());
        this.getToolbar().addCommandToRightBar(null,delete,evt1 ->{
            if(Dialog.show("Confirmation", "Are u sure to delete "+menu.getName()+" ?", "Yes", "Cancel" ))
            {
                MenuService.getInstance().delete(menu.getId());
                new MenuListForm().show();
            }
        });
        this.getToolbar().addCommandToRightBar(null,save,evt1 -> {
            if(tfName.getText().length() == 0 || tfDescription.getText().length() == 0 ){
                ToastBar.showErrorMessage("les champs sont obligatoire !");
            }else{
                Menu menu2 = new Menu();
                menu2.setId(menu.getId());
                menu2.setName(tfName.getText());
                menu2.setDescription(tfDescription.getText());
                if (categorieComboBox.getSelectedIndex() !=0)
                    menu2.setCategorie(categorieComboBox.getSelectedItem());
                else
                    menu2.setCategorie(menu.getCategorie());
                if(imageName.length() == 0)
                    menu2.setImg(menu.getImg());
                else
                    menu2.setImg(imageName);

                if(MenuService.getInstance().update(menu2)){
                    com.codename1.ui.Dialog.show("Successful", "menu updated !" , "OK", null);
                    new MenuListForm().show();
                }
                else
                    Dialog.show("Failure", " cannot updated !" , "OK", null);
            }


        });

        Validator val = new Validator();
        val.addConstraint(tfName, new LengthConstraint(3));
        val.addConstraint(tfDescription, new LengthConstraint(5));


        EncodedImage placeholder = EncodedImage.createFromImage(Image.createImage(800, 500, 0xffff0000), true);
        URLImage background = URLImage.createToStorage(placeholder, menu.getImg(), "");

        tfName.setText(menu.getName());
        tfDescription.setText(menu.getDescription());
        lbExpiration.setText(menu.getExpiration_date().toString());
        categorieComboBox.setSelectedItem(menu.getCategorie());
        imageViewer = new ImageViewer(background);
        imageViewer.setPreferredSize(new Dimension(1200,800));


        TextField cat =new TextField();
        cat.setText(menu.getCategorie().getNom());
        cat.setEditable(false);

        imageViewer.addLongPressListener(evt -> {
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
                                try {
                                    String namePic = saveFileToDevice(file);
                                    this.imageName = namePic;
                                    System.out.println(namePic);
                                } catch (IOException ex) {
                                }

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


        this.addAll(
                new Container(BoxLayout.xCenter()).add(imageViewer),
                new Container(BoxLayout.y()).add(new Label("Nom")).add(tfName),
                new Container(BoxLayout.y()).add(new Label("Description")).add(tfDescription),
                new Container(BoxLayout.y()).add(new Label("Categorie")).add(cat),
                new Container(BoxLayout.y()).add(new Label("Date d'expiration")).add(lbExpiration),
                categorieComboBox);
    }

    protected String saveFileToDevice(String hi) throws IOException {
        int index = hi.lastIndexOf("/");
        hi = hi.substring(index + 1);
        return hi;

    }
}
