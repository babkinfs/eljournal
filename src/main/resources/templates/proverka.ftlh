<#import "parts/common.ftlh" as c>
<#import "parts/testsession.ftlh" as ts>

<@c.page>
    <@ts.tsession/>
    <div class="row">
        <form id="frm1" method="post" action="/teacher1/proverka/">
            <!-- Сейчас $ {aDateTime} -->
            <#if filesForDnevniks??>
                <div class="list-group-item d-flex justify-content-between lh-sm">
                    <table>
                        <thead>
                        <tr>
                            <td>&nbsp;&nbsp;Дата сохранения&nbsp;</td>
                            <td>&nbsp;&nbsp;&nbsp;&nbsp;Время&nbsp;&nbsp;&nbsp;&nbsp;</td>
                            <td>&nbsp;&nbsp;&nbsp;&nbsp;Путь файла&nbsp;</td>
                            <td>&nbsp;&nbsp;&nbsp;&nbsp;Студент&nbsp;</td>
                        </tr>
                        </thead>
                        <tbody>
                        <#assign index = 0>
                        <#list filesForDnevniks! as files>
                        <#--                    <#if !files.getOcenka()?? || files.getOcenka()?? && files.getOcenka() != "Проверено, замечаний нет.">
                                            -->
                            <tr>
                                <td>&nbsp;${files.getDatesdachi()}&nbsp;</td>
                                <td>
                                    ${files.getDnevnik().getRaspisanie().getCall().getName()}
                                </td>
                                <td>&nbsp;
                                    ${files.getPathstudent()}&nbsp;
                                </td>
                                <td>&nbsp;&nbsp;&nbsp;${files.getDnevnik().getStudent().getShortName()}&nbsp;&nbsp;&nbsp;</td>
                                <td>
                                    <input type="hidden"  name="pathstudent"
                                           value="${files.getPathstudent()}" />
                                    <input type="hidden"  name="course"
                                           value="${files.getDnevnik().getRaspisanie().getCourse().getNameCourseFull()}" />
                                    <input type="hidden"  name="theme"
                                           value="${files.getDnevnik().getRaspisanie().getTheme().getNameteme()}" />
                                    <input type="hidden"  name="lengthfilesfordnevnik"
                                           value="${files.getStringId()}" />
                                    <#if buttonvisible == "true">
                                        <button id="btn_out" name="btn_out" value="${index}"
                                                class="btn btn-primary my-3" type="submit">Проверить</button>
                                        <input type="hidden" name="_csrf" value="${_csrf.token}">
                                    </#if>
                                </td>
                            </tr>
                            <#assign index = index+1>
                        <#--            </#if> -->
                        </#list>
                        </tbody>
                    </table>
                </div>
            </#if>
        </form>
    </div>
</@c.page>