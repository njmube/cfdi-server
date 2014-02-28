<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="layout" content="bootstrap"/>
        <title>Envío de CFDIs por correo</title>
    </head>
    <body>
    
        <div class="container">
            <div class="row">
            	<div class="col-md-12">
                    <nav:secondary class="nav nav-tabs"/>
                </div>
            </div>
            
            
            
            <div class="row"> <!-- Document Row -->
                
                <div class="col-md-3"><!-- Task panel -->
                	
                	<div class="panel panel-primary">
                		<div class="panel-heading"><h3 class="panel-title">Opciones</h3></div>
                		<div class="panel-body">
                			<ul class="nav nav-pills nav-stacked">
                				<li><a href="#" class="btn" data-toggle="modal" data-target="#envioMailForm">Enviar por fecha</a></li>
                				<li><g:link class="btn">Buscar</g:link></li>
                			</ul>
                		</div>
                	</div>
                    
                </div><!-- Fin Task panel -->
                
                <div class="col-md-9"> <!-- Document panel -->
                	<div class="panel panel-info">
                		<div class="panel-heading">
                			<h3 class="panel-title">Correos enviados
                				<small class="pull-right">Ultima generación:<g:formatDate format="dd/MM/yyyy hh:ss" date="${ultimaFecha}"/></small>
                			</h3>
                			
                		</div>
                	</div>
                	<table class="table table-striped table-hover table-bordered table-condensed">
                			<thead>
                				<tr>
                					<g:sortableColumn class="header" property="rfc" title="RFC"/>
                					<g:sortableColumn property="email" title="Email"/>
                					<g:sortableColumn property="fecha" title="Fecha"/>
                					<g:sortableColumn property="dateCreated" title="Generado"/>
                					<th>Comprobantes</th>
                					<th>Estatus</th>
                				</tr>
                			</thead>
                			<tbody>
                				<g:each in="${cfdiMailLogs}" var="log">
                					<tr>
                						<td>${fieldValue(bean:log, field: "rfc")}</td>
                						<td>${fieldValue(bean:log, field: "email")}</td>
                						<td><g:formatDate format="dd/MM/yyyy" date="${log.fecha}"/> </td>
                						<td><g:formatDate format="dd/MM/yyyy hh:mm" date="${log.dateCreated}"/> </td>
                						<td>${log.message?.split(',')?.size()}</td>
                						<td>${log.error?'Error':'OK'}</td>
                					</tr>
                				</g:each>
                			</tbody>
                		</table>
                		<g:paginate total="${totalMails}" />
                </div>
                
            </div> <!-- End Document Row -->
            
        </div>
        
        <div id="envioMailForm" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        	<div class="modal-dialog">
        		<div class="modal-content">
        			<div class="modal-header">
        				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        				<h4 class="modal-title" id="myModalLabel">Envio por fecha </h4>
        			</div>
        			<div class="modal-body">
        				<g:form  class="form-horizontal" role="form" action="enviarCorreosAutomaticos" controller="cfdi">
        					<div class="form-group">
        						<label for="inputFecha" class="col-sm-2 control-label">Fecha</label>
        						<div class="col-sm-10">
        							<g:datePicker id="inputFecha"  class="form-control" name="fecha" precision="day" relativeYears="[-2..7]"/>
        						</div>
        					</div>
        					<div class="modal-footer">
        						<div class="col-sm-offset-2 col-sm-10"></div>
        						<button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
        						
        						<g:submitButton class="btn btn-primary" name="enviar" value="Enviar"/>
        					</div>
        				</g:form>
        			</div>
        			
        		</div>
        	</div>
        	
        </div>
        
    </body>
</html>
