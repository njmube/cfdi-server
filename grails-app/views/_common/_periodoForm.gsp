<div id="periodoForm" class="modal fade" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">Periodo</h4>
			</div>
			<div class="modal-body">
				<g:form class="form-horizontal" role="form" action="${action}" >
					<div class="form-group">
						<label for="inputFechaInicial" class="col-sm-2 control-label">Fecha Inicial</label>
						<div class="col-sm-10">
							<g:datePicker id="inputFechaInicial" class="form-control" name="periodo.fechaInicial" relativeYears="[-2..7]" default="${new Date() }"/>
						</div>
					</div>
					<div class="form-group">
						<label for="inputFechaFinal" class="col-sm-2 control-label">Fecha Final</label>
						<div class="col-sm-10">
							<g:datePicker id="inputFechaFinal" class="form-control" name="periodo.fechaFinal" relativeYears="[-2..7]" />
						</div>
					</div>
					
					<div class="modal-footer">
						<div class="col-sm-offset-2 col-sm-10">
							<button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
							<g:submitButton class="btn btn-primary" name="aceptar"	value="Aceptar" />
						</div>
					</div>
				</g:form>
			</div>

		</div>
	</div>

</div>