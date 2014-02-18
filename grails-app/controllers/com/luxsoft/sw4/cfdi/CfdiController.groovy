package com.luxsoft.sw4.cfdi

class CfdiController {
    static scaffold = true
    
    def cfdiMailService
    
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
    
    
}
