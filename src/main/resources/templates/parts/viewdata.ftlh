<#macro viewsVarLab exerciselist>
    <table class="timeline" align="left" width="100%">
        <tbody>
        <table>
            <#assign oldtheme = "" >
            <#list exerciselist! as exercise>
                <tr >
                <#if oldtheme != exercise.getTheme().getNameteme()>
                    <td>&nbsp; ${exercise.getTheme().getNameteme()} &nbsp;</td>
                    </tr>
                    <tr>
                    <#assign oldtheme = exercise.getTheme().getNameteme()/>
                </#if>
                <td>&nbsp; ${exercise.getStartdata().getLastname().getName()} &nbsp;
                    &nbsp; ${exercise.getStartdata().getFirstname().getName()} &nbsp;
                    &nbsp; ${exercise.getStartdata().getSecondname().getName()} &nbsp;</td>
                <td>
                    &nbsp;${exercise.getBody()} &nbsp;
                </td>

                </tr>
            </#list>
        </table>
        </tbody>
    </table>
</#macro>

<#macro viewsdatasRaspisanie tdatas>
    <div id="fff" class="list-group-item d-flex justify-content-between lh-sm">
        <table>
            <thead>
            <tr>
                <th>&nbsp;</th>
                <th>&nbsp;Дата</th>
                <th>&nbsp;Время</th>
                <th>&nbsp;Тема</th>
                <th>&nbsp;Тип занятия&nbsp;</th>
                <th>&nbsp;Имя файла шаблона&nbsp;</th>
                <th>&nbsp;Имя файла инструкции&nbsp;</th>
            </tr>
            </thead>
            <tbody>
            <#list tdatas! as tdata>
                <tr >
                    <td>&nbsp; ${tdata.getNumber()} &nbsp;</td>
                    <td>&nbsp; ${tdata.getActiondate()} &nbsp;</td>
                    <td>&nbsp; ${tdata.getCall().getName()} &nbsp;</td>
                    <#if  tdata.theme?? >
                        <td>&nbsp; ${tdata.getTheme().getNameteme()} &nbsp;</td>
                        <td>&nbsp; ${tdata.getTheme().getTypezan()} &nbsp;</td>
                        <td>&nbsp; ${tdata.getTheme().getFileshablon()} </td>
                        <td>&nbsp; ${tdata.getTheme().getFileforstudent()} </td>
                    <#else >
                        <td></td>
                        <td></td>
                        <td></td>
                    </#if>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>
</#macro>

<#macro viewsdatasString dates1 dates2 dates3 dates4>
    <div class="row">
    <div class="col-sm-3">
        <@cm.listdatas tdatas=dates1 startindex=1 />
    </div>
    <div class="col-sm-3">
        <@cm.listdatas tdatas=dates2 startindex=10 />
    </div>
    <div class="col-sm-3">
        <@cm.listdatas tdatas=dates3 startindex=19 />
    </div>
    <div class="col-sm-3">
        <@cm.listdatas tdatas=dates4 startindex=28 />
    </div>
    <script type="text/javascript">

        function callNext2(myref) {
            var tree = document.createDocumentFragment();
            var link = document.createElement("a");
            link.setAttribute("id", "idLinkText");
            link.setAttribute("href", myref);
            var div = document.createElement("div");
            div.setAttribute("id", "id2");
            tree.appendChild(link);
            tree.appendChild(div);
            document.getElementById("main").appendChild(tree);
            //console.log("a");
            //console.log($('#idLinkText'))
            document.getElementById("idLinkText").click();
        }
    </script>

    <script type="text/javascript">
        $(document).ready(function () {
            $(".datesclass").on('click', function () {
                var myref = "/teacher1/datescalls/" +  this.textContent + "/" + "${periodId}"
                    + "/" + "${yearId}" + "/" + "${semestrId}" + "/" + "${facultatId}" + "/" + "${typezId}"
                    + "/" + "${grouppId}" + "/" + "${courseId}"  + "/" + "${callId}" + "/" + $("#calc_fysgc").val();
                this.style.backgroundColor = "lightblue";
                callNext2(myref);
            });
        });
    </script>
</#macro>