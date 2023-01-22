package com.example.application.views.users;

import com.example.application.entities.User;
import com.example.application.services.UsersService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.hibernate.annotations.Check;


public class CreateUserDialog {
    private Dialog dialog;
    private User userEntity;

    private UsersService usersService;

    public CreateUserDialog(UsersService usersService, User userEntity) {
        this.usersService = usersService;
        this.userEntity = userEntity;

        this.createUserDialog(
            userEntity.getFirstName(),
            userEntity.getLastName(),
            userEntity.getEmail()
        );
    }

    public CreateUserDialog(UsersService usersService) {
        this.usersService = usersService;

        this.createUserDialog( "", "", "");
    }

    private void createUserDialog(
        String firstNameDef,
        String lastNameDef,
        String emailDef
    ) {
        dialog = new Dialog();
        dialog.setHeaderTitle(this.userEntity == null ? "New User" : "Edit user #"+userEntity.getId());

        VerticalLayout vl = new VerticalLayout();
        HorizontalLayout hl = new HorizontalLayout();

        TextField firstName = new TextField("First Name", firstNameDef, "");
        TextField lastName = new TextField("Last Name", lastNameDef, "");
        TextField email = new TextField("E-mail", emailDef, "");
        TextField password = new TextField("Password");

        Checkbox autogenPassword = new Checkbox("Autogenerate Password");
        autogenPassword.addValueChangeListener(ev -> password.getElement().setEnabled(!ev.getValue()));

        Checkbox sendPasswordViaEmail = new Checkbox("Send password via email");

        Button createBtn = new Button(this.userEntity == null ? "Create" : "Update");
        createBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        createBtn.addClickListener(ev -> handleCreateBtnClick(firstName, lastName, email, password, autogenPassword, sendPasswordViaEmail));

        Button exitBtn = new Button("Exit");
        exitBtn.addClickListener(ev -> dialog.close());
        exitBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);

        hl.add(createBtn, exitBtn);
        vl.add(firstName, lastName, email, autogenPassword, password, sendPasswordViaEmail, hl);

        dialog.add(vl);
    }

    public Dialog getDialog() {
        return dialog;
    }

    private void createUser() throws Exception {
        throw new Exception("something happened..");
    }

    private void fireSuccessDialog(Dialog parentDialog) {
        Dialog d = new Dialog();
        d.setHeaderTitle("User created with success!");

        Button b = new Button("Ok");
        b.addClickListener(ev -> {
            UI.getCurrent().getPage().reload();
        });
        d.add(b);

        parentDialog.add(d);

        d.open();
    }

    private void fireErrorDialog(Dialog parentDialog) {
        Dialog d = new Dialog();
        d.setHeaderTitle("User could not be created...");

        Button b = new Button("Ok");
        b.addClickListener(ev -> d.close());
        d.add(b);

        parentDialog.add(d);

        d.open();
    }

    private void handleCreateBtnClick(
        TextField firstName,
        TextField lastName,
        TextField email,
        TextField password,
        Checkbox autogenPassword,
        Checkbox sendPasswordViaEmail
    ) {
        try {
            String p = password.getValue();

            if(autogenPassword.getValue()) {
                p = UsersService.generatePassword();
            }

            if(userEntity == null) {
                usersService.create(firstName.getValue(), lastName.getValue(), email.getValue(), p);
            } else {
                userEntity.setEmail(email.getValue());
                userEntity.setFirstName(firstName.getValue());
                userEntity.setLastName(lastName.getValue());
                if(p.length() > 0) {
                    userEntity.setPassword(UsersService.hashPassword(p));
                }

                usersService.update(userEntity);
            }

            if(sendPasswordViaEmail.getValue()) {
                UsersService.sendPasswordViaEmail(email.getValue(), p);
            }

            dialog.close();
            this.fireSuccessDialog(dialog);
        } catch(Exception e) {
            this.fireErrorDialog(dialog);
            System.out.println(e);

            throw e;
        }
    }
}
