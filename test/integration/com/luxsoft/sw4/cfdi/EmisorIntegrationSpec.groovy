package com.luxsoft.sw4.cfdi



import spock.lang.*

/**
 *
 */
class EmisorIntegrationSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "Agregar un emisor nuevo"(){
        given:'Un emisor completamente nuevo'
        def emisor=new Emisor(nombre:"PAPELSA BAJIO, S.A. DE C.V.",rfc:'PBA0511077F9')
        
        when:'Salvamos el  emisor'
        emisor.save()
        
        then:' El emisor es persistido exitosamente'
        emisor.errors.errorCount==0
        emisor.id
        Emisor.get(emisor.id).nombre==emisor.nombre
    }
    
    void "Actualizar un emisor"(){
        given:'Un emisor existente'
        def emisor=new Emisor(
            nombre:"PAPELSA BAJIO, S.A. DE C.V."
            ,rfc:'PBA0511077F9'
            ,email:'gbarron_papelsabajio@prodigy.net.mx'
        ).save(flushOnError:true)
        
        when:'Modificamos alguna propiedad y salvamos el emisor'
        def found=Emisor.get(emisor.id)
        found.rfc='XXXXXXXXXXXX'
        found.save(failOnError:true)
        
        then:'Los cambios son persistidos en la base de datos'
        Emisor.get(emisor.id).rfc=='XXXXXXXXXXXX'
    }
    
    void "Delete un emisor"(){
        given:'Un emisor existente'
        def emisor=new Emisor(
            nombre:"PAPELSA BAJIO, S.A. DE C.V."
            ,rfc:'PBA0511077F9'
            ,email:'gbarron_papelsabajio@prodigy.net.mx'
        ).save(flushOnError:true)
        
        when:'El emisor es eliminado'
        def found=Emisor.get(emisor.id)
        found.delete(flush:true)
        
        then:'El emisor es eliminado de la base de datos'
        !Emisor.exists(emisor.id)
        
    }
    
    void "Error al salvar un emisor con datos incorrectos"(){
        given:'Un emisor con errores de validacion'
        def emisor=new Emisor(
            nombre:"PAPELSA BAJIO, S.A. DE C.V."
            ,rfc:'PBA0511077F9'
            ,email:'esto no es un email' 
        )
        
        when:'Validamos el emisor'
        emisor.validate()
        
        then:
        emisor.hasErrors()
        "email.invalid"==emisor.errors.getFieldError('email').code
    }
}
