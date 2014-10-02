<#import "spring.ftl" as spring>
<html>
<head></head>
<body>
<h3>List Questions</h3>

<table>
    <tr>
        <td>Text</td>
        <td>Type</td>
        <td>Difficulty</td>
        <td></td>
    </tr>
<#escape x as x?html>
    <#list questions as q>
    <tr>
        <td>${q.text}</td>
        <td>${q.type}</td>
        <td>${q.difficulty}</td>
        <td><a href="<@spring.url "/question/edit/"+q._id/>">EDIT</a>/
            <a href="<@spring.url "/question/delete/"+q._id/>">REMOVE</a></td>
    </tr>
    </#list>
</#escape>
</table>
<a href="<@spring.url "/question/create"/>">Add new</a>
<a href="<@spring.url "/question/answer"/>">Answer</a>
</body>
</html>
