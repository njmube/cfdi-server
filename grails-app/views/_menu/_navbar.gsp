<!-- Fixed main navigation
        ===============================================-->
<nav class="navbar navbar-default navbar-inverse" role="navigation">
    <div class="container">

        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#mein-menu">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="${createLink(uri: '/')}">
                <g:message code="application.brand" default="${meta(name:'app.name')}"/>
                <small>v${meta(name:'app.version')}</small>
            </a>
        </div>
        <div class="collapse navbar-collapse" id="mein-menu">
            <nav:primary class="nav navbar-nav"/>
        </div>
    </div>
</nav>