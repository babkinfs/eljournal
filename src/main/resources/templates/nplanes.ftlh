<#import "parts/common.ftlh" as c>
<#import "parts/cmptitles.ftlh" as ct>
<#import "parts/cmpnt.ftlh" as cm>
<#import "parts/testsession.ftlh" as ts>
<#import "parts/viewdata.ftlh" as vdt>

<@c.page>
    <@ts.tsession/>
    <form id="frm1" method="post" action="/teacher1/nplanes">
        <@ct.title ></@ct.title>
        <@vdt.viewsdatasString dates1 dates2 dates3 dates4/>
    </form>
</@c.page>