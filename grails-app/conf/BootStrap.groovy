import com.luxsoft.sw4.cfdi.*
class BootStrap {

    def init = { servletContext ->
        
        environments{
            development{
                
                // Generamos algunos emisores de prueba
                Emisor.findOrCreateWhere(
                    nombre:"PAPELSA BAJIO, S.A. DE C.V.",
                    rfc:'PBA0511077F9',
                    email:'gbarron_papelsabajio@prodigy.net.mx'
                ).save(failOnError:true)
                
                Emisor.findOrCreateWhere(
                    nombre:'PAPEL S.A. de C.V.',
                    rfc:'PAP830101CR3'
                ).save(failOnError:true)
                
                Receptor.findOrCreateWhere(
                    nombre:"PAPELSA BAJIO, S.A. DE C.V.",
                    rfc:'PBA0511077F9',
                    email:'gbarron_papelsabajio@prodigy.net.mx'
                ).save(failOnError:true)
                		 
                Receptor.findOrCreateWhere(
                    nombre:"FAST DESING, S.A. DE C.V.",
                    rfc:'FDE9310013X7',
                    email:'facturas@fdimpresos.com'
                ).save(failOnError:true)
                
                EntidadEmisora.findOrCreateWhere(
                    clave:'TACUBA',
                    grupo:'PAPEL',
                    url:"jdbc:mysql://localhost/tacuba",
                    driverClassName:'com.mysql.jdbc.Driver',
                    usuario:'root',
                    password:'sys',
                    activo:true).save(failOnError:true)
            }
        }
        
    }
    def destroy = {
    }
} 
