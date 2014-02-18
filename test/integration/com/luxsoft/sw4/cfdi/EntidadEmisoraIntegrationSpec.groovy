package com.luxsoft.sw4.cfdi



import spock.lang.*

/**
 *
 */
class EntidadEmisoraIntegrationSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "Salvar una entidad emisora"() {
        def entidad=EntidadEmisora.build(grupo:'PRUEBAS')
        
        when:'Salvamos la entidad'
        entidad.save()
        
        then:'La entidad se persiste exitosamente'
        entidad.errors.errorCount==0
        entidad.id
        EntidadEmisora.get(entidad.id).grupo=='PRUEBAS'
    }
    
    void "Actualizar una entidad emisora"(){
        given:'Una entidad existente'
        def entidad=EntidadEmisora.build(grupo:'ANDRADE').save(failOnError:true)
        
        when:'Actualizamos una propiedad'
        def found=EntidadEmisora.get(entidad.id)
        assert found
        found.descripcion='Sucursal Andrade'
        found.save(failOnError:true)
        
        then:'Los cambios se reglejan en la base de datos'
        EntidadEmisora.get(found.id).descripcion=='Sucursal Andrade'
        println found 
    }
    
    void "Eliminar una entidad emisora"(){
        given:'Una entidad existente'
        def entidad=EntidadEmisora.build(grupo:'ANDRADE').save(failOnError:true)
        
        when:'Eliminamos la entidad'
        def found=EntidadEmisora.get(entidad.id)
        found.delete(flush:true)
        
        then:'La entidad es eliminada exitodamente'
        !EntidadEmisora.exists(found.id)
    }
}
