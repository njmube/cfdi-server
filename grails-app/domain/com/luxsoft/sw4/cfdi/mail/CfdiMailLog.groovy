package com.luxsoft.sw4.cfdi.mail

import com.luxsoft.sw4.cfdi.Cfdi

class CfdiMailLog {
    
    String emisor
    String rfc
    String email
    String message
    String error
    Date fecha
	
    Date dateCreated
    Date lastUpdated

    static constraints = {
        email nullable:true
        message nullable:true,maxSize:600
        error nullable:true,maxSize:600
    }
}
