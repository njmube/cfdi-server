<%@ page contentType="text/html;charset=UTF-8" %>
<!doctype html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="layout" content="bootstrap"/>
        <title>Comprobantes fiscales CFDI</title>
    </head>
    <body>
    	<g:set var="fechaInicial" value="${new Date()}"/>
    	<g:set var="fechaFinal"   value="${new Date()}"/>
        <div class="container">
            <div class="row">
                <div class="col-md-12">
                    <nav:secondary class="nav nav-tabs"/>
                    <li><a href="#" class="btn" data-toggle="modal" data-target="#periodoForm">Fecha</a></li>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                   <div class="page-header">
                   		<h3>Comprobantes fiscales digitales <small>${fechaInicial} - ${fechaFinal}</small></h3>
                   </div>
                   <!-- CFDI Table grid -->
                   <g:render template="cfdiTable" />
                </div>
            </div>
        </div>
        <g:render template="/_common/periodoForm" model="[action:'importar']"></g:render>
    </body>
</html>
