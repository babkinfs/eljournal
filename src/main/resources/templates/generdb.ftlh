<#import "parts/common.ftlh" as c>
<#import "parts/testsession.ftlh" as tses>

<@c.page>
    <@tses.tsession />
    <div >
        <table>
            <thead>
            <tr>
                <th>Считаны следующие файлы:</th>
            </tr>
            </thead>
            <tbody>
            <#if results??>
                <#list results as result>
                    <tr>
                        <td>${result}</td>
                    </tr>
                </#list>

            </#if>
            </tbody>
        </table>
    </div>
    <h3>Генерация данных в базе завершена!</h3>

</@c.page>