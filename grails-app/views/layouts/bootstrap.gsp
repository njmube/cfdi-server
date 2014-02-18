<!DOCTYPE html>
<html lang="en" class="no-js">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <title><g:layoutTitle default="Grails"/></title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <g:layoutHead/>
        <g:javascript library="application"/>		
        <r:require modules="bootstrap"/>
        <r:layoutResources />
    </head>
    
    <body>
        <!-- Main navigation
        ===============================================-->
        <g:render template="/_menu/navbar"/>
        
        <g:layoutBody/>
        
        <g:render template="/_menu/footer"/>
        
        <div id="spinner" class="spinner" style="display:none;">
            <g:message code="spinner.alt" default="Loading&hellip;"/>
        </div>
        
    <r:layoutResources />
    </body>
</html>
