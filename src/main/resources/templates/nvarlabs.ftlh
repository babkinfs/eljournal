<#import "parts/common.ftlh" as c>
<#import "parts/cmptitles.ftlh" as ct>
<#import "parts/cmpnt.ftlh" as cm>
<#import "parts/testsession.ftlh" as ts>
<#import "parts/viewdata.ftlh" as vdt>

<@c.page>
    <@ts.tsession/>
    <form id="frm1" method="post" action="/teacher1/nvarlabs"  enctype="multipart/form-data">
        <@ct.title ></@ct.title>
        <#if exerciselist??>
            <@vdt.viewsVarLab exerciselist/>
        </#if>
        <#if themelist??>
            <@vdt.viewsThemeList themelist/>
        </#if>
    </form>
</@c.page>
