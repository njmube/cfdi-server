package com.luxsoft.sw4.cfdi.mail


import com.luxsoft.sw4.cfdi.Emisor

class CfdiEmisorEmail {
    
    Emisor emisor
    
    Date dateCreated
    Date lastUpdated

    static hasMany= [mails:CfdiEmail]
    
    static constraints = {
    }
}
