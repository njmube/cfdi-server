package com.luxsoft.sw4.cfdi

class Receptor {
    
    String nombre
    String rfc
    String email
    
    Date dateCreated
    Date lastUpdated

    static constraints = {
        importFrom Emisor,include:['nombre','rfc','email']
    }
}
