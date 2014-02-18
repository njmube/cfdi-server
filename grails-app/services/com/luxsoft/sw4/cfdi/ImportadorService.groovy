package com.luxsoft.sw4.cfdi

import grails.transaction.Transactional
import groovy.sql.Sql


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
}
