<#macro login path isRegisterForm>
    <form action="${path}" method="post">
        <div class="form-group row">
            <label class="col-sm-2 col-form-label">Электронная почта:</label>
            <div class="col-sm-4" >
                <input type="text" name="username"  value="<#if user??>${user.username}</#if>"
                       class="form-control ${(usernameError??)?string('is-invalid', '')}"
                       placeholder="Введите адрес электронной почты"/>
                <#if usernameError??>
                    <div class="invalid-feedback">
                        ${usernameError}
                    </div>
                </#if>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label">Пароль:</label>
            <div class="col-sm-4" >
                <input type="password" name="password"
                       class="form-control ${(passwordError??)?string('is-invalid', '')}"
                       placeholder="Введите пароль" />
                <#if passwordError??>
                    <div class="invalid-feedback">
                        ${passwordError}
                    </div>
                </#if>
            </div>
        </div>
        <#if isRegisterForm>
            <div class="form-group row">
                <label class="col-sm-2 col-form-label">Подтверждение:</label>
                <div class="col-sm-4" >
                    <input type="password" name="password2"
                           class="form-control ${(password2Error??)?string('is-invalid', '')}"
                           placeholder="Подтвердите пароль" />
                    <#if password2Error??>
                        <div class="invalid-feedback">
                            ${password2Error}
                        </div>
                    </#if>
                </div>
            </div>
            <div class="form-group row">
                <label class="col-sm-2 col-form-label">Фамилия:</label>
                <div class="col-sm-4">
                    <input type="text" name="lastname"  value="<#if lastname??>${lastname}</#if>"
                           class="form-control ${(lastnameError??)?string('is-invalid', '')}"
                           placeholder="Введите фамилию"/>
                    <#if lastnameError??>
                        <div class="invalid-feedback">
                            ${lastnameError}
                        </div>
                    </#if>
                </div>
            </div>

            <div class="form-group row">
                <label class="col-sm-2 col-form-label">Имя:</label>
                <div class="col-sm-4">
                    <input type="text" name="firstname"  value="<#if firstname??>${firstname}</#if>"
                           class="form-control ${(firstnameError??)?string('is-invalid', '')}"
                           placeholder="Введите имя"/>
                    <#if firstnameError??>
                        <div class="invalid-feedback">
                            ${firstnameError}
                        </div>
                    </#if>
                </div>
            </div>
            <div class="form-group row">
                <label class="col-sm-2 col-form-label">Отчество:</label>
                <div class="col-sm-4">
                    <input type="text" name="secondname"  value="<#if secondname??>${secondname}</#if>"
                           class="form-control ${(secondnameError??)?string('is-invalid', '')}"
                           placeholder="Введите отчество"/>
                    <#if secondnameError??>
                        <div class="invalid-feedback">
                            ${secondnameError}
                        </div>
                    </#if>
                </div>
            </div>

        </#if>

        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <div class="form-group row">
            <div class="col-sm-2">
                <#if !isRegisterForm><a href="registration">Добавить пользователя</a></#if>
            </div>
            <div class="ml-3">
                <button class="btn btn-primary" type="submit"><#if isRegisterForm>Создать<#else>Войти</#if></button>
            </div>
        </div>
    </form>
</#macro>
<#macro logout>
    <div>
        <form action="/logout" method="post">
            <button class="btn btn-primary" type="submit" >Выход</button>
            <input type="hidden" name="_csrf" value="${_csrf.token}">
        </form>
    </div>
</#macro>
