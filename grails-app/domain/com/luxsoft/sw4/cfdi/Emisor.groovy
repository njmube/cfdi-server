package com.luxsoft.sw4.cfdi

class Emisor {
    
    String nombre
    String rfc
    String email
    
    Date dateCreated
    Date lastUpdated

    static constraints = {
        nombre size:1..300
        rfc size:12..13,unique:true
        email nullable:true,email:true
    }
}
