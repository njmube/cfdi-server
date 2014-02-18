package com.luxsoft.sw4.cfdi



import spock.lang.*

/**
 *
 */
class CfdiIntegrationSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "Crear un nuevo Cfdi"() {
        given:'Un nuevo cfdi'
        def cfdi=Cfdi.build(folio:'1',serie:'A')
        
        when:'Salvamos el cfdi'
        cfdi.save()
        
        then:'El cfdi es persistido en la base de datos'
        cfdi.errors.errorCount==0
        cfdi.id
        Cfdi.get(cfdi.id).folio==cfdi.folio
        
    }
    
    void "Actualizar un Cfdi"(){
        given:'Un Cfdi existente'
        def cfdi=Cfdi.build()
        
        when:'Actualizamos una propiedad'
        def found=Cfdi.get(cfdi.id)
        found.serie='AA'
        found.folio='500'
        found.save(failOnError:true)
        
        then:'Los cambios son persistidos en la base de datos'
        Cfdi.get(found.id).folio==found.folio
        
       
    }
    
    void "Eliminar un Cfdi"(){
        given:'Un Cfdi existente'
        def cfdi=Cfdi.build()
        
        when:'Eliminamos el Cfdi'
        def found=Cfdi.get(cfdi.id)
        found.delete(flush:true)
        
        then:'El Cfdi ya no exista en la base de datos'
        !Cfdi.exists(found.id)
    }
}
