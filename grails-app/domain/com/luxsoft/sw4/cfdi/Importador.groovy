package com.luxsoft.sw4.cfdi

class Importador {
    
    Emisor emisor
    String clave
    String grupo
    String descripcion
    String url
    
    Date dateCreated
    Date lastUpdated

    static constraints = {
        clave(size:5..15,unique:true)
        grupo(size:5..15)
        descripcion size:1..255
        url url:true
    }
}
