package com.paperreview.paperreview.presentazioneArticolo.forms;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.model.structure.MultiSelectionField;
import com.dlsc.formsfx.model.validators.CustomValidator;
import com.dlsc.formsfx.view.controls.SimpleCheckBoxControl;

import java.util.List;

public class ModificaArticoloTopicFormModel {

    private MultiSelectionField<String> topicField;
    private final boolean isEditable;

    public ModificaArticoloTopicFormModel(List<String> opzioniDisponibili, List<String> selezionati, boolean isEditable) {
        this.isEditable = isEditable;

        List<Integer> selectedIndices = selezionati.stream()
                .map(opzioniDisponibili::indexOf)
                .filter(i -> i >= 0)
                .toList();

        topicField = Field.ofMultiSelectionType(opzioniDisponibili, selectedIndices)
                .label("Topic")
                .editable(isEditable)
                .styleClass("form-field")
                .required("Seleziona almeno un topic!")
                .render(new SimpleCheckBoxControl<>());
    }


    public Form createForm() {
        return Form.of(Group.of(topicField));
    }

    public List<String> getSelectedTopics() {
        return topicField.getSelection();
    }

    public void setSelectedTopics(List<String> selezionati) {
        topicField.getSelection().setAll(selezionati);
    }


}





