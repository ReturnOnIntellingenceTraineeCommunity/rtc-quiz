<#import "spring.ftl" as spring>
<html>
<head></head>
<body>
<form name="question" action="<@spring.url "/question/save"/>" method="post">
    <#include "formQuestion.ftl"/>
    <input type="submit" value="Save" />
</form>
</body>
</html>
