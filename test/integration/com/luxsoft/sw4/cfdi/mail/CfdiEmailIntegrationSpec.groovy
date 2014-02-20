package com.luxsoft.sw4.cfdi.mail



import spock.lang.*

/**
 *
 */
class CfdiEmailIntegrationSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "Salvar un CfdiEmail"() {
        given:'Un nuevo CfdiEmail'
        def cfdiEmail=CfdiEmail.build()
        
        when:'Salvamos el bean'
        cfdiEmail.save()
        
        then:'El CfdiEmail es persistido exitosamente en la base de datos'
        
    }
}
