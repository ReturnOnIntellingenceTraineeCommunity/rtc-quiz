<#escape x as x?html>
<#import "spring.ftl" as spring>
<html>
<head></head>
<body>
<form name="question" action="<@spring.url "/question/update"/>" method="post">


    <#include "formQuestion.ftl"/>
    <input type="submit" value="Update" />
</form>
</body>
</html>
</#escape>
