package com.luxsoft.sw4.cfdi

import grails.transaction.Transactional
import groovy.sql.Sql
import org.springframework.jdbc.datasource.SingleConnectionDataSource

class ImportadorService {
    
    def dataSource_importacion

    def importar(Date fechaInicial,Date fechaFinal) {
        println "Importando comprobantes $fechaInicial - $fechaFinal"
        Sql sql=new Sql(dataSource_importacion)
        sql.eachRow("""
            SELECT * FROM SX_CFDI WHERE DATE(CREADO) BETWEEN ? AND ? AND UUID IS NOT NULL
            """,[fechaInicial,fechaFinal]){row->
                Cfdi cfdi=new Cfdi(
                    serie:row.serie,
                    folio:row.folio,
                    xml:row.xml
                    
                )
                println 'Procesando CFDI: '+cfdi.timbreFiscal
            }
    }
    
    def importar(Date fecha){
		
        log.info("Importando CFDIs para ${fecha}")
		
        def emisores=EntidadEmisora.findAllByActivo(true)
        emisores.each{ importador->
            println 'Importando Cfdis desde '+importador.clave
            SingleConnectionDataSource ds=new SingleConnectionDataSource(
                driverClassName:importador.driverClassName,
                url:importador.url,
                username:importador.usuario,
                password:importador.password)
            Sql sql=new Sql(ds)
            sql.eachRow("select * from sx_cfdi where date(creado)=?",[fecha]){ row->
                println 'Importando cfdi: '+row
            }
        }
        
    }
	
}


