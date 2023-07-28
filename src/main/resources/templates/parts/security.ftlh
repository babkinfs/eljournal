<#assign
known = Session.SPRING_SECURITY_CONTEXT??
>
<#if known>
    <#assign
    user = Session.SPRING_SECURITY_CONTEXT.authentication.principal
    name = user.getUsername()
    isAdmin = user.isAdmin()
    isTeacher = user.isTeacher()
    isLector = user.isLector()
    >
<#else>
    <#assign name = "unknown"
    isAdmin = false
    isTeacher = false
    isLector = false
    >
</#if>