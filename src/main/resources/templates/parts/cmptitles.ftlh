<#macro studenttitle>
    <div>
        Сегодня: ${aDateTime} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${student.getFullFio()}
    </div>
    <#if raspisanieList??>
        <div class="list-group-item d-flex justify-content-between lh-sm">
            <table>
                <thead>
                <tr>
                    <td>&nbsp;Дата&nbsp;</td>
                    <td>&nbsp;Время&nbsp;</td>
                    <td>&nbsp;Курс&nbsp;</td>
                    <td>&nbsp;Тема&nbsp;</td>
                    <td>&nbsp;Тип&nbsp;</td>
                    <td>&nbsp;№&nbsp;</td>
                    <td>&nbsp;Преподаватель&nbsp;</td>
                </tr>
                </thead>
                <tbody>
                <#list raspisanieList! as raspisanie>
                    <tr>
                        <td>&nbsp;${raspisanie.getActiondate()}&nbsp;</td>
                        <td>&nbsp;${raspisanie.getCall().getName()}&nbsp;</td>
                        <td>&nbsp;${raspisanie.getTheme().getCourse().getNameCourseFull()}&nbsp;</td>
                        <td>&nbsp;${raspisanie.getTheme().getNameteme()}&nbsp;</td>
                        <td>&nbsp;${raspisanie.getTheme().getTypezan()}&nbsp;</td>
                        <td>&nbsp;${raspisanie.getNumber()}&nbsp;</td>
                        <td>&nbsp;${raspisanie.getTheme().getCourse().getTeacher().getFullFio()}&nbsp;</td>
                    </tr>
                </#list>
                </tbody>
            </table>
        </div>
    </#if>
    <#if dnevnikList??>
        <div class="list-group-item d-flex justify-content-between lh-sm">
            <div  class="form-group row">
                &nbsp;&nbsp;&nbsp;&nbsp;
                ${dnevnik.getRaspisanie().getTheme().getCourse().getNameCourseFull()}
                (${dnevnik.getRaspisanie().getTheme().getTypezan()})
                ${dnevnik.getRaspisanie().getCall().getName()}
            </div>
        </div>
    <#--if dnevnikList??-->
        <div class="list-group-item d-flex justify-content-between lh-sm">
            <div class="form-group row">
                Укажите путь чтобы получить инструкции
                <input type="text" name="copied" value="" />
            </div>
            <div class="custom-file">
                <div  class="form-group row">
                    Выбрать файл для отправки в формате .pdf
                    &nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="file" name="fullnamecop">
                </div>
                <input type="text" hidden id="btn_prep" name="btn_prep" value="" />
            </div>
        </div>
    <#--if dnevnikList??-->
        <div class="list-group-item d-flex justify-content-between lh-sm">
            <table>
                <thead>
                <tr>
                    <td>&nbsp;Дата проведения занятия&nbsp;</td>
                    <td>&nbsp;Тема&nbsp;</td>
                    <td>&nbsp;Инструкция&nbsp;</td>
                    <td>&nbsp;Присутствие на занятиях&nbsp;</td>
                    <td>&nbsp;Отправить файл в формате .pdf&nbsp;</td>
                    <td>&nbsp;Дата отправки&nbsp;</td>
                    <td>&nbsp;Дата проверки&nbsp;</td>
                    <td>&nbsp;Оценка&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
                </thead>
                <tbody>
                <#assign index = 0>
                <#list dnevnikList! as dnevnikAndFilesForDnevnik>
                    <tr>
                        <td>&nbsp;${dnevnikAndFilesForDnevnik.dnevnik.getRaspisanie().getNumber()}&nbsp;</td>
                        <td>&nbsp;${dnevnikAndFilesForDnevnik.dnevnik.getRaspisanie().getActiondate()}&nbsp;</td>
                        <td>&nbsp;${dnevnikAndFilesForDnevnik.dnevnik.getRaspisanie().getTheme().getNameteme()}&nbsp;</td>
                        <td>&nbsp;
                            <div class="f">
                                <button id="btn_out1" name="btn_out" value="${index}"
                                        class="btn btn-primary my-3" type="submit">Получить</button>
                                <input type="hidden" name="_csrf" value="${_csrf.token}">
                            </div>
                            &nbsp;</td>
                        <td>&nbsp;${dnevnikAndFilesForDnevnik.dnevnik.getPropuski()}&nbsp;</td>
                        <td>&nbsp;
                            <div class="s">
                                <input type="text" hidden name="numberString"
                                       value="${dnevnikAndFilesForDnevnik.dnevnik.getRaspisanie().getNumber()}" />
                                <button id="btn_out2" <#if index != 0> disabled </#if> name="btn_out" value="${index}"
                                        class="btn btn-primary my-3" type="submit">Отправить</button>
                                <input type="hidden" name="_csrf" value="${_csrf.token}">
                            </div>
                            &nbsp;</td>
                        <td>&nbsp;<div>
                                <#if (dnevnikAndFilesForDnevnik.filesForDnevnik)??>
                                    ${(dnevnikAndFilesForDnevnik.filesForDnevnik).getDatesdachi()}
                                </#if>
                            </div>
                        <td>&nbsp;  <#if (dnevnikAndFilesForDnevnik.filesForDnevnik)??>
                                <#if (dnevnikAndFilesForDnevnik.filesForDnevnik).getDatecontrol()??>
                                    ${(dnevnikAndFilesForDnevnik.filesForDnevnik).getDatecontrol()}
                                </#if>
                            </#if>
                            &nbsp;</td>
                        <td>&nbsp;  <#if (dnevnikAndFilesForDnevnik.filesForDnevnik)??>
                                <#if (dnevnikAndFilesForDnevnik.filesForDnevnik).getOcenka()??>
                                    ${(dnevnikAndFilesForDnevnik.filesForDnevnik).getOcenka()}
                                </#if>
                            </#if>
                            &nbsp;</td>
                        <td>&nbsp;
                            <div class="t">
                                <#if (dnevnikAndFilesForDnevnik.filesForDnevnik)??>
                                    <#if (dnevnikAndFilesForDnevnik.filesForDnevnik.getPathteacher())??>
                                        <input type="text" hidden name="prepAddr"
                                               value="${dnevnikAndFilesForDnevnik.filesForDnevnik.getPathteacher()}" />
                                        <#if (dnevnikAndFilesForDnevnik.filesForDnevnik).getOcenka()??>
                                            <#if (dnevnikAndFilesForDnevnik.filesForDnevnik).getOcenka() == "" >
                                                Вам пришло сообщение
                                                <button id="btn_out3" name="btn_out" value="${index}"
                                                        class="btn btn-primary my-3" type="submit">Получить</button>
                                                <input type="hidden" name="_csrf" value="${_csrf.token}">
                                            </#if>
                                        </#if>
                                    </#if>
                                <#else >
                                    <input type="text" hidden name="prepAddr" value="">
                                </#if>
                        </td>
                    </tr>
                    <#assign index = index+1>
                </#list>
                </tbody>
            </table>
        </div>
    </#if>
    <script type="text/javascript">
        $(document).ready(function () {
            $(".f").on('click', function () {
                $("#btn_prep").val("1");
            });
            $(".s").on('click', function () {
                $("#btn_prep").val("2");
            });
            $(".t").on('click', function () {
                $("#btn_prep").val("3");
            });
        });
    </script>
</#macro>

<#macro title>
    <a id="main" href="/todir"></a>
    <input type="checkbox" id="dtclick" name="dtclick" value="ok" hidden>
    <div>
        Сегодня: ${aDateTime} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Раздел "${razdel}"
    </div>
    <div class="list-group-item d-flex justify-content-between lh-sm">
        <@cm.listString searchid="yearId" nameid="year" title="Год:" items=years ids=yearId/>
        <@cm.listString searchid="semestrId" nameid="semestr" title="Семестр:" items=semestrs ids=semestrId/>
        <@cm.listString searchid="facultatId" nameid="facultat" title="Факультет:" items=facultats ids=facultatId/>
        <@cm.listString searchid="typezId" nameid="typez" title="Тип занятий:" items=typezs ids=typezId/>
        <div>
            <h6 class="my-0">
                Кол-во пар
            </h6>
            <small class="text-muted">
                ${kolzan}
            </small>
        </div>
        <@cm.listString searchid="periodId" nameid="period" title="Период:" items=periods ids=periodId/>
        <#if viewdateUdalit>
            <button id="btn_out" name="btn_out" value="delete" class="btn btn-primary" type="submit"> Удалить </button>
        <#else >
            <!--button  id="btn_out" name="btn_out" value="delete" class="btn btn-primary" type="submit"> Удалить </button-->
        </#if>
    </div>
    <div  style="z-index: 300; position: relative" class="list-group-item d-flex justify-content-between lh-sm">
        <@cm.listString searchid="grouppId" nameid="groupp" title="Группа:" items=groups ids=grouppId/>
        <@cm.listString searchid="courseId" nameid="course" title="Дисциплина:" items=courses ids=courseId/>

        <#if viewdate>
            <div class="form-group row">
                <div id="A" class=" mr-3">
                    <h6 class="my-0">
                        Дата начала:
                    </h6>
                    <small style="z-index: 10; position: relative"  class="text-muted">
                        <input id="calc_fysgc" name="calc_fysgc" type="text" value="${tdate}" onfocus="this.select();lcs(this)"
                               onclick="event.cancelBubble=true;this.select();lcs(this)">
                    </small>
                </div>
                <div>
                    <h6>
                        <input type="checkbox" id="blockdate" >Блокировать
                    </h6>
                </div>
            </div>
        </#if>
        <#if viewcall>
            <@cm.listString searchid="callId" nameid="call" title="Время начала:" items=calls ids=callId/>
        <#else >
        </#if>

        <#if viewbuttonzagruzit>
            <div class="form-group row">
                <input type="file" name="file">
                <button class="btn btn-primary mr-3" type="submit">Загрузить</button>
            </div>
        </#if>
        <div class="form-group row mr-1">

            <#if text??><div class=" mr-6">${text}&nbsp;</div></#if>

            <#if viewbuttonsohranit>
                <#if activSohranit>
                    <button id="btn_out" name="btn_out" value="save" class="btn btn-primary" type="submit">Сохранить</button>
                <#else>
                    <button id="btn_out" disabled name="btn_out" value="save" class="btn btn-primary" type="submit">Сохранить</button>
                </#if>
            </#if>
        </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}">
    </div>
    <script type="text/javascript">
        function callNext1(myref) {
            var tree = document.createDocumentFragment();
            var link = document.createElement("a");
            link.setAttribute("id", "idLinkText");
            link.setAttribute("href", "/teacher1/${todir}/" + myref  + "/${razdel}");

            //alert("/teacher1/$ {todir}/" + myref + "/$ {razdel}")

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
            $("#calc_fysgc").on('click', function () {
                document.getElementById("dtclick").checked = true;
            });
        });
    </script>


    <script type="text/javascript">
        /* --- Swazz Javascript Calendar ---
/* --- v 1.0 3rd November 2006
By Oliver Bryant
http://calendar.swazz.org

Update:
Gene Bechtold
http://www.bechtold.biz
12/11/2011

C помощью разных добрых людей:
tullin

                alert($("#calc_fysgc").val())
                alert("checkClick")

*/

        function getObj(objID)
        {
            if (document.getElementById) {return document.getElementById(objID);}
            else if (document.all) {return document.all[objID];}
            else if (document.layers) {return document.layers[objID];}
        }

        function checkClick(e) {
            e?evt=e:evt=event;
            CSE=evt.target?evt.target:evt.srcElement;
            if (CSE.tagName!='SPAN')
                if (getObj('fc'))
                    if (!isChild(CSE,getObj('fc')))
                        getObj('fc').style.display='none';
            if(document.getElementById('dtclick').checked) {
                var myref =  "999" + "/" + $("#calc_fysgc").val() + "/" + "${periodId}"
                    + "/" + "${yearId}" + "/" + "${semestrId}" + "/" + "${facultatId}" + "/" + "${typezId}"
                    + "/" + "${grouppId}" + "/" + "${courseId}" + "/" + "${callId}" + "/" + $("#calc_fysgc").val()
                    + "/";
                if($('#blockdate').prop('checked')) {
                    myref = myref + "true";
                } else {
                    myref = myref + "false";
                }
                + $("#blockdate").val();
                callNext1(myref);
            }
            document.getElementById("dtclick").checked = false;
        }

        function isChild(s,d) {
            while(s) {
                if (s==d)
                    return true;
                s=s.parentNode;
            }
            return false;
        }

        function Left(obj)
        {
            var curleft = 0;
            if (obj.offsetParent)
            {
                while (obj.offsetParent)
                {
                    curleft += obj.offsetLeft
                    obj = obj.offsetParent;
                }
            }
            else if (obj.x)
                curleft += obj.x;
            return curleft;
        }

        function Top(obj)
        {
            var curtop = 0;
            if (obj.offsetParent)
            {
                while (obj.offsetParent)
                {
                    curtop += obj.offsetTop
                    obj = obj.offsetParent;
                }
            }
            else if (obj.y)
                curtop += obj.y;
            return curtop +35;
        }

        // Calendar script
        var now = new Date;
        var sccd=now.getDate();
        var sccm=now.getMonth();
        var sccy=now.getFullYear();
        var ccm=now.getMonth();
        var ccy=now.getFullYear();

        // For current selected date
        var selectedd, selectedm, selectedy;

        document.write('<table id="fc" style="z-index: 33;position:absolute;border-collapse:collapse;background:#FFFFFF;border:1px solid #FFD088;display:none;-moz-user-select:none;-khtml-user-select:none;user-select:none;" cellpadding="2">');
        document.write('<tr style="font:bold 13px Arial" onselectstart="return false"><td style="cursor:pointer;font-size:15px" onclick="upmonth(-1)">&laquo;</td><td colspan="5" id="mns" align="center"></td><td align="right" style="cursor:pointer;font-size:15px" onclick="upmonth(1)">&raquo;</td></tr>');
        document.write('<tr style="background:#FF9900;font:12px Arial;color:#FFFFFF"><td align=center>П</td><td align=center>В</td><td align=center>С</td><td align=center>Ч</td><td align=center>П</td><td align=center>С</td><td align=center>В</td></tr>');
        for(var kk=1;kk<=6;kk++) {
            document.write('<tr>');
            for(var tt=1;tt<=7;tt++) {
                num=7 * (kk-1) - (-tt);
                document.write('<td id="cv' + num + '" style="width:18px;height:18px">&nbsp;</td>');
            }
            document.write('</tr>');
        }
        document.write('<tr><td colspan="7" align="center" style="cursor:pointer;font:13px Arial;background:#FFC266" onclick="today()">Сегодня: '+addnull(sccd,sccm+1,sccy)+'</td></tr>');
        document.write('</table>');

        document.all?document.attachEvent('onclick',checkClick):document.addEventListener('click',checkClick,false);




        var updobj;
        function lcs(ielem) {
            //alert("lcs")
            updobj=ielem;
            getObj('fc').style.left=Left(ielem)+'px';
            getObj('fc').style.top=Top(ielem)+ielem.offsetHeight+'px';
            getObj('fc').style.display='';

            // First check date is valid
            curdt=ielem.value;
            curdtarr=curdt.split('-');
            isdt=true;
            for(var k=0;k<curdtarr.length;k++) {
                if (isNaN(curdtarr[k]))
                    isdt=false;
            }
            if (isdt&(curdtarr.length==3)) {
                ccm=curdtarr[1]-1;
                ccy=curdtarr[2];

                selectedd=parseInt ( curdtarr[0], 10 );
                selectedm=parseInt ( curdtarr[1]-1, 10 );
                selectedy=parseInt ( curdtarr[2], 10 );

                prepcalendar(curdtarr[0],curdtarr[1]-1,curdtarr[2]);
            }

        }

        function evtTgt(e){
            var el;
            if(e.target)el=e.target;
            else if(e.srcElement)el=e.srcElement;
            if(el.nodeType==3)el=el.parentNode; // defeat Safari bug
            return el;
        }
        function EvtObj(e){if(!e)e=window.event;return e;}
        function cs_over(e) {
            evtTgt(EvtObj(e)).style.background='#FFEBCC';
        }
        function cs_out(e) {
            evtTgt(EvtObj(e)).style.background='#FFFFFF';
        }
        function cs_click(e) {
            updobj.value=calvalarr[evtTgt(EvtObj(e)).id.substring(2,evtTgt(EvtObj(e)).id.length)];
            getObj('fc').style.display='none';
        }

        var mn=new Array('Январь','Февраль','Март','Апрель','Май','Июнь','Июль','Август','Сентрябрь','Октябрь','Ноябрь','Декабрь');
        var mnn=new Array('31','28','31','30','31','30','31','31','30','31','30','31');
        var mnl=new Array('31','29','31','30','31','30','31','31','30','31','30','31');
        var calvalarr=new Array(42);

        function f_cps(obj) {
            obj.style.background='#FFFFFF';
            obj.style.font='10px Arial';
            obj.style.color='#333333';
            obj.style.textAlign='center';
            obj.style.textDecoration='none';
            obj.style.border='1px solid #FFD088';//'1px solid #606060';
            obj.style.cursor='pointer';
        }

        function f_cpps(obj) {
            obj.style.background='#C4D3EA';
            obj.style.font='10px Arial';
            obj.style.color='#FF9900';
            obj.style.textAlign='center';
            obj.style.textDecoration='line-through';
            obj.style.border='1px solid #6487AE';
            obj.style.cursor='default';
        }

        function f_hds(obj) {
            obj.style.background='#FFF799';
            obj.style.font='bold 10px Arial';
            obj.style.color='#333333';
            obj.style.textAlign='center';
            obj.style.border='1px solid #6487AE';
            obj.style.cursor='pointer';
        }

        // day selected
        function prepcalendar(hd,cm,cy) {
            now=new Date();
            //alert("465")
            //alert($("#nowdate").val())
            //now=$("#nowdate").val();
            //alert(now)
            sd=now.getDate();
            td=new Date();
            td.setDate(1);
            td.setFullYear(cy);
            td.setMonth(cm);
            cd=td.getDay();
            if (cd==0)cd=6; else cd--;
            getObj('mns').innerHTML=mn[cm]+'&nbsp;<span style="cursor:pointer" onclick="upmonth(-12)">&lt;</span>'+cy+'<span style="cursor:pointer" onclick="upmonth(12)">&gt;</span>';
            marr=((cy%4)==0)?mnl:mnn;
            for(var d=1;d<=42;d++) {
                cv=getObj('cv'+parseInt(d));
                f_cps(cv);
                if ((d >= (cd -(-1)))&&(d<=cd-(-marr[cm]))) {
                    dip=((d-cd < sd)&&(cm==sccm)&&(cy==sccy));
                    htd=((hd!='')&&(d-cd==hd));

                    cv.onmouseover=cs_over;
                    cv.onmouseout=cs_out;
                    cv.onclick=cs_click;

                    // if today
                    if (sccm == cm && sccd == (d-cd) && sccy == cy)
                        cv.style.color='#FF9900';

                    // if selected date
                    if (cm == selectedm && cy == selectedy && selectedd == (d-cd) )
                    {
                        cv.style.background='#FFEBCC';
                        //cv.style.color='#e0d0c0';
                        //cv.style.fontSize='1.1em';
                        //cv.style.fontStyle='italic';
                        //cv.style.fontWeight='bold';

                        // when use style.background
                        cv.onmouseout=null;
                    }

                    cv.innerHTML=d-cd;

                    calvalarr[d]=addnull(d-cd,cm-(-1),cy);
                }
                else {
                    cv.innerHTML='&nbsp;';
                    cv.onmouseover=null;
                    cv.onmouseout=null;
                    cv.onclick=null;
                    cv.style.cursor='default';
                }
            }
        }

        prepcalendar('',ccm,ccy);

        function upmonth(s)
        {
            marr=((ccy%4)==0)?mnl:mnn;

            ccm+=s;
            if (ccm>=12)
            {
                ccm-=12;
                ccy++;
            }
            else if(ccm<0)
            {
                ccm+=12;
                ccy--;
            }
            prepcalendar('',ccm,ccy);
        }

        function today() {
            updobj.value=addnull(now.getDate(),now.getMonth()+1,now.getFullYear());
            getObj('fc').style.display='none';
            prepcalendar('',sccm,sccy);
        }

        function addnull(d,m,y)
        {
            var d0='',m0='';
            if (d<10)d0='0';
            if (m<10)m0='0';

            return ''+d0+d+'-'+m0+m+'-'+y;
        }
    </script>
</#macro>