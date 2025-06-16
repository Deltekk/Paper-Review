package com.paperreview.paperreview.presentazioneArticolo.forms;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.model.structure.MultiSelectionField;
import com.dlsc.formsfx.model.validators.CustomValidator;
import com.dlsc.formsfx.view.controls.SimpleCheckBoxControl;

import java.util.List;

public class SottomettiArticoloTopicFormModel {
    private MultiSelectionField<String> topicField;

    public SottomettiArticoloTopicFormModel(List<String> topics) {
        this.topicField = Field.ofMultiSelectionType(topics, List.of())  // lista di opzioni, lista selezionata vuota
                .required("Errore: Devi selezionare almeno 1 topic!")
                .styleClass("form-field")
                .render(new SimpleCheckBoxControl<>());
    }

    public Form createForm() {
        return Form.of(
                Group.of(
                        topicField
                )
        );
    }

    public List<String> getSelectedTopics() {
        return topicField.getSelection();
    }


}





