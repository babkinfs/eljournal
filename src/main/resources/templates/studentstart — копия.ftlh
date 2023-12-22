<#import "parts/common.ftlh" as c>
<#import "parts/cmptitles.ftlh" as ct>
<#import "parts/cmpnt.ftlh" as cm>
<#import "parts/testsession.ftlh" as ts>

<@c.page>
    <@ts.tsession/>
    <form id="frm1" method="post" action="/student" enctype="multipart/form-data">
        <input hidden name="fullnamecop" <#if fullnamecopied??> value="${fullnamecopied}"</#if> >
        <@ct.studenttitle></@ct.studenttitle>
        <#if listOtpravOtv??>
            Ответ преподавателя
            <div class="list-group-item d-flex justify-content-between lh-sm">
                <div class="form-group row">
                    Укажите путь чтобы получить ответ преподавателя&nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="text" name="fromteacher" value="" />
                </div>
            </div>
            <div class="list-group-item d-flex justify-content-between lh-sm">
                <table>
                    <thead>
                    <tr>
                        <td>&nbsp;Сдано&nbsp;</td>
                        <td>&nbsp;Проверено&nbsp;</td>
                        <td>&nbsp;Оценка&nbsp;</td>
                        <td>&nbsp;Получить документ&nbsp;</td>
                    </tr>
                    </thead>
                    <tbody>
                    <#assign index = 0>
                    <#list listOtpravOtv! as filesForDnevnik>
                        <tr>
                            <td>&nbsp;${filesForDnevnik.getDatesdachi()}&nbsp;</td>
                            <td>&nbsp;${filesForDnevnik.getDatecontrol()}&nbsp;</td>
                            <td>&nbsp;${filesForDnevnik.getOcenka()}&nbsp;</td>
                            <td>&nbsp;
                                <div class="f">
                                    <button id="btn_out11" name="btn_out" value="${index}"
                                            class="btn btn-primary my-3" type="submit">Получить</button>
                                    <input type="hidden" name="_csrf" value="${_csrf.token}">
                                </div>
                                &nbsp;</td>
                            <td>&nbsp;
                                &nbsp;</td>
                            <td>&nbsp;<div>
                                </div>
                            <td>&nbsp;
                                &nbsp;</td>
                            <td>&nbsp;
                                &nbsp;</td>
                            <td>&nbsp;
                            </td>
                        </tr>
                        <#assign index = index+1>
                    </#list>
                    </tbody>
                </table>
            </div>
        <#else >
            <input type="hidden" name="fromteacher" value="@" />
        </#if>
    </form>
</@c.page>
