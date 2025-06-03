package com.paperreview.paperreview.forms;

import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.MultiSelectionField;
import com.dlsc.formsfx.model.validators.CustomValidator;
import com.dlsc.formsfx.view.controls.SimpleCheckBoxControl;

import java.util.List;


public class TopicFormModel {

    private MultiSelectionField<String> topicField;

    public TopicFormModel(List<String> topics) {
        this.topicField = Field.ofMultiSelectionType(topics, List.of())  // lista di opzioni, lista selezionata vuota
                .label("Seleziona Topic")
                .validate(
                        CustomValidator.forPredicate(selected -> selected.size() >= 3, "Errore: devono essere selzionati almano 3 topic!")
                )
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
