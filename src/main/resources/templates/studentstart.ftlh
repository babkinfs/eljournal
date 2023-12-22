<#import "parts/common.ftlh" as c>
<#import "parts/cmptitles.ftlh" as ct>
<#import "parts/cmpnt.ftlh" as cm>
<#import "parts/testsession.ftlh" as ts>

<@c.page>
    <@ts.tsession/>
    <form id="frm1" method="post" action="/student" enctype="multipart/form-data">
        <input hidden name="fullnamecop" <#if fullnamecopied??> value="${fullnamecopied}"</#if> >
        <@ct.studenttitle></@ct.studenttitle>

    </form>
</@c.page>
