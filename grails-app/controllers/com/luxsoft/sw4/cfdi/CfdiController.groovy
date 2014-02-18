package com.luxsoft.sw4.cfdi

class CfdiController {
    static scaffold = true
    
    def cfdiMailService
    def importadorService
    
    def index(){
        println 'Generando lista de comprobantes fiscales'
    }
    
    def list(){
        //println 'Listar todos los Cfdis'
        
    }
    
    def enviarCorreo(){
        cfdiMailService.enviarComprobantes()
        redirect action:'index'
    }
    
    def importar(){
        Date f1=Date.parse('dd/MM/yyyy','19/12/2013')
        
        importadorService.importar(f1,f1)
        redirect action:'index'
    }
    
    
}
