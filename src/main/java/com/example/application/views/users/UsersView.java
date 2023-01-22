package com.example.application.views.users;

import com.example.application.entities.User;
import com.example.application.services.UsersService;
import com.example.application.views.shared.AreYouSureDialog;
import com.example.application.views.shared.MainTab;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Registered Users")
@Route("users")
public class UsersView  extends VerticalLayout {

    public UsersView(UsersService usersService) {
        MainTab mt = new MainTab("users");
        mt.getStyle().set("width", "100%");
        add(mt);

        // create user dialog
        Dialog cud = new CreateUserDialog(usersService).getDialog();
        add(cud);

        Button addUserBtn = new Button("Create User");
        addUserBtn.getElement().getStyle().set("margin-top", "1rem");
        addUserBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addUserBtn.addClickListener(ev -> cud.open());
        add(addUserBtn);


        Grid<User> usersGrid = new Grid<>(User.class);
        usersGrid.setColumns("id", "firstName", "lastName", "email", "createdAt");
        usersGrid.setItems(usersService.findAll());
        usersGrid.addComponentColumn(user -> {
            Button updateBtn = new Button("Edit");
            updateBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            updateBtn.addClickListener(ev -> {
                Dialog d = new CreateUserDialog(usersService, user).getDialog();
                add(d);
                d.open();
            });

            Button deleteBtn = new Button("Delete");
            deleteBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);
            deleteBtn.addClickListener(ev -> {
                Dialog ays =  new AreYouSureDialog(d -> {
                        try {
                            usersService.delete(user);
                            UI.getCurrent().getPage().reload();
                        } catch(Exception e) {
                            d.close();
                            add(new Dialog(new Text("Could not delete user!")));

                            throw e;
                        }
                    }, d -> d.close()).getDialog();
                add(ays);
                ays.open();
            });

            return new HorizontalLayout(updateBtn, deleteBtn);
        });
        add(usersGrid);

    }
}
