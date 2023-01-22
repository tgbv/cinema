package com.example.application.views.films;

import com.example.application.entities.Film;
import com.example.application.services.BroadcastPeriodsService;
import com.example.application.services.FilmAuthorsService;
import com.example.application.services.FilmsService;
import com.example.application.views.shared.AreYouSureDialog;
import com.example.application.views.shared.MainTab;
import com.example.application.views.users.CreateUserDialog;
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

import java.util.concurrent.atomic.AtomicInteger;

@PageTitle("Registered Films")
@Route("films")
public class FilmsView extends VerticalLayout {

    public FilmsView(
        FilmsService filmsService,
        FilmAuthorsService filmAuthorsService,
        BroadcastPeriodsService bps
    ) {
        MainTab mt = new MainTab("films");
        mt.getStyle().set("width", "100%");
        add(mt);

        Dialog coufm = new CreateOrUpdateFilmModal(filmsService, filmAuthorsService).getDialog();

        Button createFilmBtn = new Button("Create film", ev -> coufm.open());
        createFilmBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add( createFilmBtn );

        Grid<Film> filmsGrid = new Grid<>(Film.class);
        AtomicInteger i = new AtomicInteger(1);
        filmsGrid.addColumn(film -> new Text(String.valueOf(i.getAndIncrement())) ).setHeader("#");
        filmsGrid.setColumns("title", "debutDate", "author");
        filmsGrid.setItems(filmsService.findAll());
        filmsGrid.addComponentColumn(film -> {
            Button updateBtn = new Button("Edit");
            updateBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            updateBtn.addClickListener(ev -> {
                Dialog d = new CreateOrUpdateFilmModal(filmsService, filmAuthorsService, film).getDialog();
                add(d);
                d.open();
            });

            Button deleteBtn = new Button("Delete");
            deleteBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);
            deleteBtn.addClickListener(ev -> {
                Dialog ays =  new AreYouSureDialog(d -> {
                    try {
                        filmsService.delete(film);
                        UI.getCurrent().getPage().reload();
                    } catch(Exception e) {
                        d.close();
                        add(new Dialog(new Text("Could not delete film!")));
                        throw e;
                    }
                }, d -> d.close()).getDialog();
                add(ays);
                ays.open();
            });

            return new HorizontalLayout(updateBtn, deleteBtn);
        });

        add(filmsGrid);

    }
}
