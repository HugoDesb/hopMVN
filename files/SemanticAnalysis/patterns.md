% title Framenet Semantic Patterns for guideline extraction : documentation
% author Hugo Desbiolles
# Patterns documentation

## Introduction

This document is the documentation for 

## Patterns format

## Methodology

Two approaches were used for the creation of patterns

## Patterns

### 1: Addiction[Addictant+Degree],Mental_Activity[Topic+Expressor+Content];

#### Frame Definition
- **Addiction** : This frame concerns an Addict's physiological or psychological compulsive dependence on an Addictant, the substance or behavior to which the Addict is addicted. 
    - **Addictant** : Addictant is the substance or behavior to which the Addict is dependent.
    - **Degree** : The Degree to which an Addict is addicted to the Addictant.
- **Mental_activity** : a Sentient_entity has some activity of the mind operating on a particular Content or about a particular Topic. The particular activity may be perceptual, emotional, or more generally cognitive.
    - **Topic** : A description of the kind of Content in the Sentient_entity's attention in terms of an idea which is salient throughout the Content.
    - **Content** : The situation or state-of-affairs that the Sentient_entity's attention is focused on. 
    - **Expressor** : The body part that reveals the Sentient_entity's state to the observer. 
### Why ?
An addictant can be a trigger for an altered state of mind to be treated. Hence, we assign Addictant as premise and Mental activity as conclusion.

### 2: ;Process_continue[Event],Supply[Theme]
#### Frame Definition 
- **Process_continue** : An Event continues at a certain Place through Time. <a name="hop"></a>
    - **Event** : Name of the Event which is continuing.
- **Supply** : A Supplier gives a Theme to a Recipient to fulfill a need or purpose (Imposed_purpose) of the Recipient. 
    - **Theme** : The object that is given to the Recipient . 
#### Why ?
To supply a Theme continuously, can be interpreted as healthcare and/or treatment.

### 3: ;Supply[Theme]--
#### Frame Definition
See [Supply](#Supply)
#### Why
In reference corpus, it covers long part of sentences, too long to be used alone

### 4: People_by_age[Ethnicity+Person+Age+Persistent_characteristic];
#### Frame definition
- **People_by_age** : words for individuals as viewed in terms of their age. The Person is conceived of as independent of other specific individuals with whom they have relationships and independent of their participation in any particular activity. They may have a Descriptor, Origin, Persistent_characteristic, or Ethnicity. A specific Age may sometimes be specified as well. 
    - **Ethnicity** : The Ethnicity is the religious, racial, national, socio-economic or cultural group to which the Person belongs.
    - **Person** : The human being of a certain age
    - **Age** : The Age is the length of time the Person has been alive. 
    - **Persistent_characteristic** : The Persistent_characteristic is a physiological characteristic or personality trait of the Person which is concieved of as persisting over time.
#### Why
Clinical guidelines may target different care for people of different age. Therefore it's part of the premise of a rule

### 5: Medical_conditions[Place+Patient+Ailment+Body_part+Cause+Name+Symptom+Place];
#### Frame definition
- **Medical_conditions** : Words in this frame name medical conditions or diseases that a patient suffers from, is being treated for, may be cured of, or die of. The condition or disease may be described in a variety of ways, including the part or area of the body (Body_part) affected by the condition (e.g. liver cancer, cardiovascular disease), the Cause of the condition (e.g. bacterial meningitis, viral pneumonia), a prominent Symptom of the condition (e.g. asymptomatic stenosis, blue ear disease), the Patient or population (originally) affected by the condition (e.g. bovine tuberculosis, juvenile diabetes), or the (proper) Name used to identify the condition (e.g. Munchausen Syndrome, Lou Gehrig's Disease). Annotation in this frame is done in respect to the name of the condition or disease.
    - **Place** : The location where the Ailment is effecting the Patients.
    - **Patient** : patient, entity, or population (originally) affected by the condition or illness.
    - **Ailment** : Any medical problem.
    - **Body_part**: the part or area of the body affected by the condition or disease.
    - **Cause** : the cause of a condition or disease.
    - **Name** : the Name that identifies the condition or disease.
    - **Symptom**: prominent symptom of the condition or disease. 
#### Why
Any disease and its definition is part of the premise because the guideline will describe what practice to adopt.

### 6. Medical_intervention[Medical_condition];
#### Frame definition
- **Medical_intervention** :Procedural or Medicine based Interventions are used on a Patient to attempt to alleviate a Medical_condition. These interventions can have a Frequency_of_success as well as Side_effects. This frame differs from Cure in that this frame deals only with attempts to alleviate a Medical_condition, whereas Cure deals with situations in which the Affliction or Medical_condition has been cured.
    - **Medical_condition** : A holistic description of the medical state of the patient (or a part of the state of the patient), which may or may not indicate the cause of a deviation from normal.
#### Why
Any medical condition is part of a premise, as stated in pattern 5.

### 7. ;Medical_intervention[Intervention+Result]
#### Frame definition
- **Medical_intervention** :Procedural or Medicine based Interventions are used on a Patient to attempt to alleviate a Medical_condition. These interventions can have a Frequency_of_success as well as Side_effects. This frame differs from Cure in that this frame deals only with attempts to alleviate a Medical_condition, whereas Cure deals with situations in which the Affliction or Medical_condition has been cured.
    - **Intervention** : A drug or procedure is administered or performed in order to treat a Medical_condition.
    - **Result** : The consequence of the Intervention.
#### Why
The result of a described intervention is its objective, therefore both the intervention and result frame elements are given in the conclusion of the rule.

### 8. ;Food[Food]
#### Frame definition
- **Food** : words referring to items of food. 
    - **Food**
#### Why
As eating healthy is part of any well-being practice, any diet recommandation should be selected as the conclusion of the rule.

### 9. ;Delivery[Theme+Goal]
#### Frame definition
- **Delivery** : A Deliverer hands off a Theme to a Recipient or (more indirectly) a Goal location, which is accessible to the Recipient. 
    - **Theme** : The objects being delivered. 
    - **Goal**: The end of the path and intended goal of the sending. 
#### Why
Clinically, delivery-related words can be used to describe a prescription of medication. Therefore, as course of action, it is intended in the conclusion of the rule.

### 10. Cure[Affliction+Healer+Reciprocation]; && Cure[Body_part+Treatment+Medication+Manner+Degree+Purpose]
#### Frame definition
- **Cure** : This frame deals with a Healer treating and curing an Affliction (the injuries, disease, or pain) of the Patient, sometimes also mentioning the use of a particular Treatment or Medication. This frame differs from Medical_intervention in that this frame deals only with cases in which the Patient is cured of the Affliction, not just treated for the Affliction.
    - **Affliction**: The Frame Element Affliction is generally the NP Object of a verb, frequently incorporating the Patient as a possessor
    - **Healer** : The Healer, anyone who treats or cures the Patient
    - **Reciprocation** : The Reason for which an intentional act is performed. 
    - **Body_part** : The Body_part is the specific area of the Patient's body which is treated.
    - **Treatment** : A method used to treat the Affliction
    - **Medication** : The injested, applied, injected, etc. substance designed to cure the Patient. 
    - **Manner** : Manner of performing an action
    - **Degree** : Degree to which event occurs
    - **Purpose** : The purpose for which an intentional act is performed. 
#### Why
Anything that motivates the use of a cure is to be in the premise of a rule, anything describing a cure is part of its conclusion

### Medical_intervention[Medical_condition];
#### Frame definition
#### Why
### Medical_intervention[Medical_condition];
#### Frame definition
#### Why
### Medical_intervention[Medical_condition];
#### Frame definition
#### Why
### Medical_intervention[Medical_condition];
#### Frame definition
#### Why
### Medical_intervention[Medical_condition];
#### Frame definition
#### Why
### Medical_intervention[Medical_condition];
#### Frame definition
#### Why
### Medical_intervention[Medical_condition];
#### Frame definition
#### Why
### Medical_intervention[Medical_condition];
#### Frame definition
#### Why
### Medical_intervention[Medical_condition];
#### Frame definition
#### Why
### Medical_intervention[Medical_condition];
#### Frame definition
#### Why
### Medical_intervention[Medical_condition];
#### Frame definition
#### Why
### Medical_intervention[Medical_condition];
#### Frame definition
#### Why
### Medical_intervention[Medical_condition];
#### Frame definition
#### Why
### Medical_intervention[Medical_condition];
#### Frame definition
#### Why
### Medical_intervention[Medical_condition];
#### Frame definition
#### Why
### Medical_intervention[Medical_condition];
#### Frame definition
#### Why
### Medical_intervention[Medical_condition];
#### Frame definition
#### Why
### Medical_intervention[Medical_condition];
#### Frame definition
#### Why
### Medical_intervention[Medical_condition];
#### Frame definition
#### Why
### Medical_intervention[Medical_condition];
#### Frame definition
#### Why
### Medical_intervention[Medical_condition];
#### Frame definition
#### Why
### Medical_intervention[Medical_condition];
#### Frame definition
#### Why
### Medical_intervention[Medical_condition];
#### Frame definition
#### Why
### Medical_intervention[Medical_condition];
#### Frame definition
#### Why
### Medical_intervention[Medical_condition];
#### Frame definition
#### Why
### Medical_intervention[Medical_condition];
#### Frame definition
#### Why
### Medical_intervention[Medical_condition];
#### Frame definition
#### Why
### Medical_intervention[Medical_condition];
#### Frame definition
#### Why
### Medical_intervention[Medical_condition];
#### Frame definition
#### Why
### Medical_intervention[Medical_condition];
#### Frame definition
#### Why
### Medical_intervention[Medical_condition];
#### Frame definition
#### Why
### Medical_intervention[Medical_condition];
#### Frame definition
#### Why
### Medical_intervention[Medical_condition];
#### Frame definition
#### Why





