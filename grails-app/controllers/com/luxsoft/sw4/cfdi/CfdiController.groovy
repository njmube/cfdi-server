package com.luxsoft.sw4.cfdi

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
    
    def enviarCorreo(){
        Date fecha=Date.parse('dd/MM/yyyy','19/12/2013')
        cfdiMailService.enviarComprobantes(fecha)
        redirect action:'index'
    }
    
    def importar(){
        Date f1=Date.parse('dd/MM/yyyy','19/12/2013')
        
        importadorService.importar(f1,f1)
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
