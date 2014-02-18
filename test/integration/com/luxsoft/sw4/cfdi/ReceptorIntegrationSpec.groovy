package com.luxsoft.sw4.cfdi



import spock.lang.*

/**
 *
 */
class ReceptorIntegrationSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "Agregar un nuevo receptor"() {
        given:'Un receptor nuevo'
        def receptor=new Receptor(
            nombre:"PAPELSA BAJIO, S.A. DE C.V."
            ,rfc:'PBA0511077F9'
            ,email:'gbarron_papelsabajio@prodigy.net.mx'
        )
        
        when:'Salvamos el receptor'
        receptor.save()
        
        then:'El receptor es persistido en la base de datos'
        receptor.errors.errorCount==0
        receptor.id
        Receptor.get(receptor.id).rfc==receptor.rfc
    }
    
    void "Actualizar un receptor"(){
        given:'Un receptor existente'
        def receptor=new Receptor(
            nombre:"PAPELSA BAJIO, S.A. DE C.V."
            ,rfc:'PBA0511077F9'
            ,email:'gbarron_papelsabajio@prodigy.net.mx'
        ).save(failOnError:true)
        
        when:'Modificamos una propiedad'
        def found=Receptor.get(receptor.id)
        found.rfc='XXXXXXXXXXXX'
        found.save(failOnError:true)
        
        then:'El cambio esta registrado en la base de datos'
        Receptor.get(found.id).rfc==found.rfc
    }
    
    void "Eliminar un receptor"(){
        given:'Un receptor existente'
        def receptor=new Receptor(
            nombre:"PAPELSA BAJIO, S.A. DE C.V."
            ,rfc:'PBA0511077F9'
            ,email:'gbarron_papelsabajio@prodigy.net.mx'
        ).save(failOnError:true)
        
        when:'Eliminamos el receptor'
        def found=Receptor.get(receptor.id)
        found.delete(flush:true)
        
        then:'El receptor ya no existe en la base de datos'
        !Receptor.exists(found.id)
    }
}
