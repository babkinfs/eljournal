<#import "parts/common.ftlh" as c>
<#import "parts/cmptitles.ftlh" as ct>
<#import "parts/cmpnt.ftlh" as cm>
<#import "parts/testsession.ftlh" as ts>
<#import "parts/viewdata.ftlh" as vdt>

<@c.page>
    <@ts.tsession/>
    <form id="frm1" method="post" action="/teacher1/${dirteacher}"  enctype="multipart/form-data">
        <@ct.title ></@ct.title>
        <#assign razd = "${razdel}"/>
        <#if "${razdel}"="Планы">
            <@vdt.viewsdatasString dates1 dates2 dates3 dates4/>
        </#if>
        <#if "${razdel}"="Задания">
            <@vdt.viewsdatasRaspisanie tdatas />
        </#if>
    </form>
</@c.page>