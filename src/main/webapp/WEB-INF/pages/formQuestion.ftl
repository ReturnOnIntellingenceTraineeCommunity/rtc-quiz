<#import "spring.ftl" as spring>
<html>
<head></head>
<body>
    <form name="question" action="<@spring.url "/question/save"/>" method="post">
        Difficulty:<@spring.formSingleSelect "question.difficulty" difficulties/> <br/>
        Type: <@spring.formSingleSelect "question.type" types/><br/>
        Text: <@spring.formTextarea "question.text"/><br/>  <br/>
        Answers:<br/>
        <#list question.answers as answer>
            Answer ${answer_index}:
            <@spring.formInput "question.answers[${answer_index}].text"/>
            <@spring.formCheckbox "question.answers[${answer_index}].right"/> <br/>
        </#list>
        <input type="submit" value="Save" />
    </form>
</body>
</html>
