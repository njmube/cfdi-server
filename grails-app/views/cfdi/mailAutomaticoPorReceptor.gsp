<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Corre automatico</title>
    </head>
    <body>
    	<h2>${emisor.nombre}</h2>
        <h1>Atención: ${cliente}</h1>
        <p>
        <h3>Estimado cliente, por este medio le hacemos llegar los comprobantes fiscales digitales (CFDI) y
            sus representaciones impresas en formato PDF de las compras efectuadas en fecha:<strong>
        	<g:formatDate date="${fecha}" format="dd/MM/yyyy"/>
        </strong> 
        </h3>
        </p>
    </body>
</html>
