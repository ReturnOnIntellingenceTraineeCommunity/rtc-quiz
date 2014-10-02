<#import "spring.ftl" as spring>
<html>
<head></head>
<body>
<form name="question" action="<@spring.url "/question/check"/>" method="post">
    <#escape x as x?html>
    <#list questions as question>
    <h3>Question ${question_index+1}</h3>
    <p>${question.text}</p>
        <#list question.answers as answer>
            ${answer.text}
            <input type="checkbox" value="${question._id}#${answer.text}"
                   name="answers">
            <br/>
        </#list>
    </#list>
    </#escape>
    <input type="submit" value="Answer" />
</form>
</body>
</html>
