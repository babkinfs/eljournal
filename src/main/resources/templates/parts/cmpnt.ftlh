<#macro listdatas tdatas startindex>
    <table class="timeline" align="left" width="100%">
        <tbody>
        <#assign index = startindex >
        <if datas??>
            <table>
                <#list tdatas! as tdata>
                    <tr class="datesclass" >
                        <td>&nbsp; ${index}&nbsp;</td>
                        <td>&nbsp; ${tdata.getFirst()} &nbsp;</td>
                        <td>&nbsp; ${tdata.getSecond()} &nbsp;</td>
                    </tr>
                    <#assign index = index + 1 >
                </#list>
            </table>
        </if>
        </tbody>
    </table>
</#macro>

<#macro listString nameid searchid title ids items>
    <div >
        <h6 class="my-0">
            ${title}
        </h6>
        <small class="text-muted">
            <#assign count = 0>
            <SELECT name="${nameid}" id="${searchid}">
                <if items??>
                    <#list items! as item>
                        <#if count == ids>
                        <option selected value="${count}">
                        <#else >
                            <option value="${count}">
                        </#if>
                        ${item}</option>
                        <#assign count = count + 1>
                    </#list>
                </if>
            </SELECT>
        </small>
    </div>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#${searchid}").unbind("change").on('change', function () {
                //alert("cmpnt 46  " + "${grouppId}" + " callId=" + "${callId}")
                var myref = this.value + "/" + this.id + "/" + "${periodId}"
                    + "/" + "${yearId}" + "/" + "${semestrId}" + "/" + "${facultatId}" + "/" + "${typezId}"
                    + "/" + "${grouppId}" + "/" + "${courseId}"  + "/" + "${callId}" + "/" + $("#calc_fysgc").val()
                    + "/";
                if($('#blockdate').prop('checked')) {
                    myref = myref + "true";
                } else {
                    myref = myref + "false";
                }
                + $("#blockdate").val();

                //alert("cmpnt 58  " + "${grouppId}" + " callId=" + "${callId}")
                callNext1(myref);
            });
        });
    </script>
</#macro>