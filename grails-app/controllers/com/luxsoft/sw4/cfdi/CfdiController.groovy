package com.luxsoft.sw4.cfdi

import com.luxsoft.sw4.cfdi.mail.CfdiMailLog;
import com.luxsoft.sw4.utils.Periodo;

class CfdiController {
    static scaffold = true
    
    def cfdiMailService
    def importadorService
    
    def index(){
        //println 'Generando lista de comprobantes fiscales'
    }
    
    def list(){
        //println 'Listar todos los Cfdis'
        
    }
    
    def correos(){
		/*
		println 'Parametros para lista de correos: '+params
		def fecha=new Date()
		if(params.fecha){
			println 'Formateando fecha: '+params.fecha
			fecha=Date.parse("dd/MM/yyyy",params.fecha)
		}
		print 'Lista de correos enviados para: '+fecha
		*/
		params.max = Math.min(params.max ? params.int('max') : 10, 100)
		params.sort='id'
		params.order='asc'
		
        //def cfdiMailLogs=CfdiMailLog.findAll("from CfdiMailLog c where date(c.fecha)=?",[fecha])
		//def cfdiMailLogs=CfdiMailLog.findAllByFecha(fecha,params)
		def cfdiMailLogs=CfdiMailLog.list(params)
		def ultimaFecha=CfdiMailLog.last('dateCreated')?.dateCreated
		//def totalMails=CfdiMailLog.countByFecha(fecha)
		def totalMails=CfdiMailLog.count()
		[cfdiMailLogs:cfdiMailLogs,ultimaFecha:ultimaFecha,totalMails:totalMails]
    }
	
	
	
	def enviarCorreosAutomaticos(){
		
		String fechaString="$params.fecha_day/$params.fecha_month/$params.fecha_year"
		def fecha=Date.parse("dd/MM/yyyy", fechaString)
		println 'Enviando correos  para: '+fecha
		cfdiMailService.enviarComprobantes(fecha)
		redirect action:'correos',params:[fecha:fechaString]
	}
    
    def importar(Periodo cmd){
		log.info 'Importando con: '+cmd
        //Date f1=Date.parse('dd/MM/yyyy','19/12/2013')
        //importadorService.importar(f1)
        redirect action:'index'
    }
    
    def pdf(){
        def data=cfdiMailService.testPdf()
        def name='test.pdf'
        response.setContentType("application/pdf")
        //response.setHeader("Content-disposition", "filename=${name}")
        //response.setHeader "Content-disposition", "attachment; filename=\"${name}\"";
        response.setHeader("Content-disposition", "attachment;filename=${name}")
        response.outputStream << data
    }
    
    
}
