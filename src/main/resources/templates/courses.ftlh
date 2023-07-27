<#import "parts/common.ftlh" as c>
<#import "parts/testsession.ftlh" as ts>

<@c.page>
    <@ts.tsession/>

    <div class="list-group-item d-flex justify-content-between lh-sm">
        <div class="align-items-center ">
            <p>Загрузить данные по новым курсам</p>
        </div>
        <form action="/teacher1/courses" method="post" enctype="multipart/form-data">
            <div class="form-group row ">
                <input type="file" name="file" >
                <button class="btn btn-primary mr-3" type="submit">Загрузить</button>
            </div>
            <input type="hidden" name="_csrf" value="${_csrf.token}">
        </form>
    </div>
    <div class="list-group-item d-flex justify-content-between lh-sm">
        <table class="timeline" align="left" width="100%">
            <thead>
            <tr>
                <th>&nbsp;№</th>
                <th>&nbsp;Наименование курса</th>
                <th>&nbsp;Факультет</th>
                <th>&nbsp;Группа</th>
                <th>&nbsp;Подгруппа</th>
                <th>&nbsp;Тип занятия</th>
                <th>&nbsp;Год</th>
                <th>&nbsp;Семестр</th>
                <th>&nbsp;Кол-во пар</th>
                <th>&nbsp;Преподаватель</th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <#assign index = 1>
            <#if courses??>
                <#list courses as course>
                    <tr>
                        <td class="timeline-item">&nbsp;${index}</td>
                        <td>&nbsp;<a href="/teacher1/viewcourse/${course.getId()}">${course.getNameCourseFull()}</a></td>
                        <td>&nbsp;${course.getGroupp().getFacultat().getName()}</td>
                        <td>&nbsp;${course.getGroupp().getNamegroupp()}</td>
                        <td>&nbsp;${course.getGroupp().getSubgroupp().namesubgroupp}</td>
                        <td>&nbsp;${course.getTypeZn(course.getGroupp().getSubgroupp().namesubgroupp)}</td>
                        <td>&nbsp;${course.getGroupp().getYear().getFirstnameyear()}-${course.getGroupp().getYear().getSecondnameyear()}</td>
                        <td>&nbsp;${course.getGroupp().getSemestr().getName()}</td>
                        <td>&nbsp;${course.getKolzan()}</td>
                        <td>&nbsp;${course.getTeacher().lastname.getName()}
                            ${course.getTeacher().firstname.getIni()}
                            ${course.getTeacher().secondname.getIni()}</td>
                    </tr>
                    <#assign index = index + 1 >
                </#list>

            </#if>
            </tbody>
        </table>

    </div>
</@c.page>