<#import "parts/common.ftlh" as c>
<#import "parts/cmptitles.ftlh" as ct>
<#import "parts/cmpnt.ftlh" as cm>
<#import "parts/testsession.ftlh" as ts>
<#import "parts/viewdata.ftlh" as vdt>

<@c.page>
    <@ts.tsession/>
    <form id="frm1" method="post" action="/teacher1/${dirteacher}">
        <@ct.title ></@ct.title>
        <@vdt.viewsdatasRaspisanie tdatas />
    </form>
</@c.page>
