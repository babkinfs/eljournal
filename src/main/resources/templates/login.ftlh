<#import "parts/common.ftlh" as c>
<#import "parts/login.ftlh" as l>
<#import "parts/testsession.ftlh" as tses>

<@c.page>
    <@tses.tsession />
    <@l.login "/login " false/>
</@c.page>