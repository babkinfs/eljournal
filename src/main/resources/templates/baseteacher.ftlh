<#import "parts/common.ftlh" as c>
<#import "parts/testsession.ftlh" as ts>

<@c.page>
    <@ts.tsession/>
<form id="frm2" method="post" action="/teacher1/checking">
    <input type="hidden" name="nameCheckValue" id="nameCheckValue" />
    <input type="hidden" name="nameNotCheckValue" id="nameNotCheckValue" />
    <!--input hidden name="plan" value="1">
    <input hidden name="theme" value="theme">
    <input hidden name="zadanie" value="1">
    <input hidden name="calcstd" value="$ {aDateTime}">
    <input hidden name="search_call" value="1"-->
    <div>
        Сегодня: ${aDateTime}   <!--$ {time}--> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Раздел "${razdel}"
    </div>
    <#if currentRaspisanie??>
    <div class="list-group-item d-flex justify-content-between lh-sm">
        ${currentRaspisanie.getCall().getName()}&nbsp;
        Курс: "${currentRaspisanie.getTheme().getCourse().getNameCourseFull()}"&nbsp;
        Тема: "${currentRaspisanie.getTheme().getNameteme()}"&nbsp;
        ${currentRaspisanie.getTheme().getTypezan()}&nbsp;№${currentRaspisanie.getTheme().getNumber()}&nbsp;
            <div class="sideDiv" style="display: none">
            <button val="12qw" id="btnhid2"  class="btn btn-primary" type="submit" >Сохранить</button>
            </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}">
    </div>
    </#if>
    <#if studentFulls??>
        <div class="list-group-item d-flex justify-content-between lh-sm">
            <table>
                <thead>
                <tr>
                    <td>&nbsp;&nbsp;№&nbsp;&nbsp;</td>
                    <td>&nbsp;&nbsp;Фамилия имя отчество&nbsp;&nbsp;</td>
                    <td>&nbsp;&nbsp;Состояние&nbsp;&nbsp;</td>
                    <td>&nbsp;&nbsp;Очно&nbsp;&nbsp;</td>
                </tr>
                </thead>
                <tbody>
                <#assign number = 1/>
                <#list studentFulls! as studentfull>
                    <tr>
                        <td>&nbsp;&nbsp;${number}&nbsp;&nbsp;</td>
                        <td>&nbsp;&nbsp;${studentfull.getStudent().getShortName()}&nbsp;&nbsp;</td>
                        <td>&nbsp;&nbsp;${studentfull.isPresent()?string('присутствует', ' ')} &nbsp;&nbsp;</td>
                        <td>&nbsp;&nbsp;
                                <input type="checkbox" name="${studentfull.getStudent().getId()}"
                                       value="${number}" onchange="setValue();"
                                        ${studentfull.isOchnoZaochno()?string('checked="checked"', ' ')}
                                       />
                            &nbsp;&nbsp;</td>
                    </tr>
                    <#assign number = number+1 />
                </#list>
                </tbody>
            </table>
        </div>
    </#if>
    <#if raspisanieList??>
        <div class="list-group-item d-flex justify-content-between lh-sm">
            <table>
                <thead>
                <tr>
                    <td>&nbsp;№&nbsp;</td>
                    <td>&nbsp;Дата&nbsp;</td>
                    <td>&nbsp;Время&nbsp;</td>
                    <td>&nbsp;Курс&nbsp;</td>
                    <td>&nbsp;Тема&nbsp;</td>
                    <td>&nbsp;Тип занятия&nbsp;</td>
                    <td>&nbsp;Группа&nbsp;</td>
                </tr>
                </thead>
                <tbody>
                <#assign number = 1/>
                <#list raspisanieList! as raspisanie>
                    <tr>
                        <td>&nbsp;${number}&nbsp;</td>
                        <td>&nbsp;${raspisanie.getActiondate()}&nbsp;</td>
                        <td>&nbsp;${raspisanie.getCall().getName()} &nbsp;</td>
                        <td>&nbsp;${raspisanie.getTheme().getCourse().getNameCourseFull()} &nbsp;</td>
                        <td>&nbsp;${raspisanie.getTheme().getNameteme()} &nbsp;</td>
                        <td>&nbsp;${raspisanie.getTheme().getTypezan()} ${raspisanie.getNumber()}&nbsp;</td>
                        <td>&nbsp;${raspisanie.getTheme().getCourse().getGroupp().getNamegroupp()}&nbsp;</td>
                    </tr>
                    <#assign number = number+1 />
                </#list>
                </tbody>
            </table>
        </div>
    </#if>
</form>
    <script>
        setValue();//loads once when the form is initialized

        function setValue(){
            var checkValue = new Array();
            var checkNotValue = new Array();

            $.each($('input:checkbox'),function(){
                if(this.checked){
                    checkValue.push($(this).val());
                    $('.sideDiv').show();
                }
                else
                {
                    checkNotValue.push($(this).val());
                }
            });
            $("#nameCheckValue").val(checkValue.join(","));
            $("#nameNotCheckValue").val(checkNotValue.join(","));
        }
    </script>
</@c.page>