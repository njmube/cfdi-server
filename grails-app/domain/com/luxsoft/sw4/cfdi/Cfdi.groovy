package com.luxsoft.sw4.cfdi

import mx.gob.sat.cfd.x3.ComprobanteDocument
import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante

class Cfdi {
    
    Emisor emisor
    Receptor receptor
	String grupo
    Date fecha
    String serie
    String folio
    String uuid
    Date fechaTimbrado
    BigDecimal importe
    BigDecimal descuentos
    BigDecimal subtotal
    BigDecimal impuestosRetenidos
    BigDecimal total
    String origen
    byte[] xml
    
    ComprobanteDocument comprobanteDocument
    
    Date dateCreated
    Date lastUpdated
    

    static constraints = {
        emisor()
        receptor()
        fecha()
        serie(size:1..255)
        folio(size:1..255)
        uuid(nullable:true,maxSize:255)
        fechaTimbrado(nullable:true)
        origen(maxSize:255)
        xml maxSize:(1024 * 512)  // 50kb para almacenar el xml
        
    }
    
    static transients = ['comprobanteDocument','comprobante','timbreFiscal']
    
    public ComprobanteDocument getComprobanteDocument(){
        if(this.comprobanteDocument==null){
            loadComprobante()
        }
        return this.comprobanteDocument
    }
    
    public Comprobante getComprobante(){
        return getComprobanteDocument().getComprobante()
    }
    
    void loadComprobante(){
        ByteArrayInputStream is=new ByteArrayInputStream(getXml())
        this.comprobanteDocument=ComprobanteDocument.Factory.parse(is)
    }
    
    TimbreFiscal getTimbreFiscal(){
        return new TimbreFiscal(getComprobante())
    }
}
