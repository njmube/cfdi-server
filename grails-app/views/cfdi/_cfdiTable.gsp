<!-- CFDI Table -->
<table
	class="table table-striped table-hover table-bordered table-condensed">
	<thead>
		<tr>
			<g:sortableColumn property="id" title="Id" />
			<g:sortableColumn property="serie" title="Serie" />
			<g:sortableColumn property="folio" title="Folio" />
			<g:sortableColumn property="fecha" title="Fecha" />
			<g:sortableColumn property="receptor" title="Receptor" />
			<g:sortableColumn property="rfc" title="Rfc" />
			<th>Total</th>
		</tr>
	</thead>
	
	<tbody>
		<g:each in="${cfdiList}" var="cfdi">
			<tr>
				<td><g:formatNumber number="${cfdi.id}" format="#####"/></td>
				<td>${fieldValue(bean:cfdi,field:serie) }</td>
				<td>${fieldValue(bean:cfdi,field:folio) }</td>
				<td><g:formatDate date="${cfdi.fecha}"/></td>
				<td>${fieldValue(bean:cfdi,field:receptor) }</td>
				<td>${fieldValue(bean:cfdi,field:rfc) }</td>
			</tr>
		</g:each>
	</tbody>

</table>