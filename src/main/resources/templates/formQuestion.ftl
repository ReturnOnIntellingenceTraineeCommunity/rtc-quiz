Difficulty:<@spring.formSingleSelect "question.level" difficulties/> <br/>
Type: <@spring.formSingleSelect "question.type" types/><br/>
Text: <@spring.formTextarea "question.text"/><br/>  <br/>
Answers:<br/>
<#list question.answers as answer>
    Answer ${answer_index}:
    <@spring.formInput "question.answers[${answer_index}].text"/>
    <@spring.formCheckbox "question.answers[${answer_index}].right"/> <br/>
</#list>
<@spring.formHiddenInput "question._id" />

