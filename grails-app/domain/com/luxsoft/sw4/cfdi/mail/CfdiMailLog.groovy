package com.luxsoft.sw4.cfdi.mail

import com.luxsoft.sw4.cfdi.Cfdi

class CfdiMailLog {
    
    Cfdi cfdi
    String email
    String message
    String estatus
    
    Date dateCreated
    Date lastUpdated

    static constraints = {
        email nullable:true
        message nullable:true
        estatus nullable:true
    }
}
