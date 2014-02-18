package com.luxsoft.sw4.cfdi

class Cfdi {
    
    Emisor emisor
    Receptor receptor
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
        
    }
}
