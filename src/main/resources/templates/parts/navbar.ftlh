<#include "security.ftlh">
<#import "login.ftlh" as l>

<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="#">Журнал контактной работы</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse"
            data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
            aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item">
                <a class="nav-link" href="/">Home </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/raspisanie">Расписание</a>
            </li>
            <#if isTeacher>
                <!--
            <li class="nav-item">
                <a class="nav-link" href="/teacher/filldnevnik">запДневник</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/teacher/onestudent">Один студент</a>
            </li>
                <li class="nav-item">
                    <a class="nav-link" href="/teacher/addstudents">Добавить студентов</a>
                </li>
            -->
                <li class="nav-item">
                    <a class="nav-link" href="/teacher1/proverka">Проверка</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/teacher1/students">Студенты</a>
                </li>
                <!--li class="nav-item">
                    <a class="nav-link" href="/teacher1/zadaniya">Задания</a>
                </li-->
                <li class="nav-item">
                    <a class="nav-link" href="/teacher1/nplanes">Планы</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/teacher1/nvarlabs">Варианты лаб</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/teacher1/courses">Предметы</a>
                </li>
            </#if>
            <#if isAdmin>
                <li class="nav-item">
                    <a class="nav-link" href="/user">Пользователи</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/generdb">Генерация данных в базе</a>
                </li>
                <!--
                <li class="nav-item">
                    <a class="nav-link" href="/exportdb">Экспорт базы</a>
                </li>
                -->
            </#if>
            <#if user??>
                <li class="nav-item">
                    <a class="nav-link" href="/user/profile">Профиль</a>
                </li>
            </#if>
            <li class="nav-item">
                <a class="nav-link" href="/starting">О программе</a>
            </li>
        </ul>
        <div class="navbar-text mr-3">${name}</div>
        <@l.logout/>
    </div>

</nav>
