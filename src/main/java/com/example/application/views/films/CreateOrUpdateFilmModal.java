package com.example.application.views.films;

import com.example.application.entities.Film;
import com.example.application.entities.FilmAuthor;
import com.example.application.services.FilmAuthorsService;
import com.example.application.services.FilmsService;
import com.example.application.services.Shared;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;

import java.util.List;

public class CreateOrUpdateFilmModal {
    private FilmsService filmsService;

    private FilmAuthorsService filmAuthorsService;

    private Film filmEntity;

    private Dialog dialog;

    public CreateOrUpdateFilmModal(FilmsService filmsService, FilmAuthorsService filmAuthorsService) {
        this.filmsService = filmsService;
        this.filmAuthorsService = filmAuthorsService;

        createModal();
    }

    public CreateOrUpdateFilmModal(FilmsService filmsService, FilmAuthorsService filmAuthorsService, Film filmEntity) {
        this.filmsService = filmsService;
        this.filmEntity = filmEntity;
        this.filmAuthorsService = filmAuthorsService;

        createModal();
    }

    private VerticalLayout authorsForm;
    private VerticalLayout initAuthorForm() {
        TextField firstName = new TextField("First Name");
        TextField lastName = new TextField("Last Name");
        DatePicker dateOfBirth = new DatePicker("Date of birth");
        Button createBtn = new Button("Create", ev -> {
            filmAuthorsService.create(
                firstName.getValue(),
                lastName.getValue(),
                dateOfBirth.getValue()
            );
            authorsSelect.setItems(filmAuthorsService.findAll());
            authorsForm.getStyle().set("display", "none");
            Shared.alert("author created! select it from list");
        });
        Button leaveBtn = new Button("Leave", ev -> authorsForm.getStyle().set("display", "none"));

        authorsForm = new VerticalLayout(firstName, lastName, dateOfBirth, new HorizontalLayout(createBtn, leaveBtn));
        authorsForm.getStyle().set("display", "none");

        return authorsForm;
    }

    private ComboBox<FilmAuthor> authorsSelect;
    private VerticalLayout initFilmForm() {
        // create film
        TextField title = new TextField("Title", filmEntity == null ? "" : filmEntity.getTitle(), "");
        DatePicker debutDate = new DatePicker("Debut date", filmEntity == null ? null : filmEntity.getDebutDate());
        authorsSelect = new ComboBox<FilmAuthor>();
        authorsSelect.setItems(filmAuthorsService.findAll());
        authorsSelect.setItemLabelGenerator(f -> f.getFirstName() + " " + f.getLastName());
        if(filmEntity != null) {
            authorsSelect.setValue(filmEntity.getAuthor());
        }

        HorizontalLayout hl = new HorizontalLayout(
            new Button(filmEntity == null ? "Create" : "Update", ev -> {

                // Create
                if(filmEntity == null) {
                    filmsService.create(
                        title.getValue(),
                        debutDate.getValue(),
                        authorsSelect.getValue()
                    );

                    Shared.alert("Film created with success!");
                }

                // Otherwise delete
                else {
                    filmEntity.setTitle(title.getValue());
                    filmEntity.setDebutDate(debutDate.getValue());
                    filmEntity.setAuthor(authorsSelect.getValue());
                    filmsService.update(filmEntity);
                    Shared.alert("Film updated with success!");
                }

                UI.getCurrent().getPage().reload();
            }),
            new Button("Cancel", ev -> dialog.close())
        );

        return new VerticalLayout(
            title,
            debutDate,
            new HorizontalLayout(
                authorsSelect,
                new Button("+", ev -> authorsForm.getStyle().set("display", "flex"))
            ),
            hl
        );
    }

    private void createModal() {
        dialog = new Dialog();
        dialog.setHeaderTitle(filmEntity == null ? "Create film" : "Update film #"+filmEntity.getId());
        dialog.setMinWidth("500px");
        dialog.setMaxWidth("75%");
        dialog.setWidthFull();

        VerticalLayout authorsForm = initAuthorForm();
        VerticalLayout filmsForm = initFilmForm();

        dialog.add(new HorizontalLayout(filmsForm, authorsForm));
    }

    public Dialog getDialog() {
        return dialog;
    }
}
