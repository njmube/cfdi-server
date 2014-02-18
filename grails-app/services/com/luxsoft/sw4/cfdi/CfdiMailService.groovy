package com.luxsoft.sw4.cfdi

import grails.transaction.Transactional
import groovy.sql.Sql
import mx.gob.sat.cfd.x3.ComprobanteDocument
import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante
import org.codehaus.groovy.grails.plugins.jasper.JasperExportFormat
import org.codehaus.groovy.grails.plugins.jasper.JasperReportDef
import org.apache.commons.io.FileUtils


class CfdiMailService {
    
   
    def dataSource_importacion
    
    def jasperService
    
    

    def enviarComprobantes() {
        println 'Mandando CFDIs por correo electronico '
        Sql sql=new Sql(dataSource_importacion)
        //Sql sql=Sql.newInstance('jdbc:mysql://localhost/cfdi_server','root','sys','com.mysql.jdbc.Driver')
        def fecha=Date.parse('dd/MM/yyyy','19/12/2013')
        println 'Fecha: '+fecha
        def params=[Date.parse('dd/MM/yyyy','28/12/2013')]
        def res=sql.eachRow("""
            select * from SX_CFDI c where date(c.creado)=? and uuid is not null
            """
            ,params){cfdi->
                def sdir=System.properties['user.home']+'/pruebas/cfdi/envios'
                File dir=new File(sdir)
                if(!dir.exists())
                    dir.mkdir()
                println 'Importando: '+cfdi.uuid+'To file: '+dir.absolutePath
                byte[] data=cfdi.xml
                ByteArrayInputStream is=new ByteArrayInputStream(data)
                ComprobanteDocument docto=ComprobanteDocument.Factory.parse(is)
                Comprobante comprobante=docto.comprobante
                println 'Comprobante: '+new TimbreFiscal(comprobante)
                File xml=new File(dir,"$cfdi.XML_FILE")
                xml.setBytes(data)
            def conceptos=comprobante.getConceptos().getConceptoArray()
            def modelData=conceptos.collect { cc ->
		def res=[
		'cantidad':cc.getCantidad(),\
                'NoIdentificacion':cc.getNoIdentificacion(),\
                'descripcion':cc.getDescripcion(),\
                'unidad':cc.getUnidad(),\
                'ValorUnitario':cc.getValorUnitario(),\
                'Importe':cc.getImporte()
			 ]
		if(cc.informacionAduaneraArray){
                    res.PEDIMENTO_FECHA=cc.informacionAduaneraArray[0]?.fecha.getTime()
                    res.PEDIMENTO=cc.informacionAduaneraArray[0]?.numero
                    res.ADUANA=cc.informacionAduaneraArray[0]?.aduana
		}
		if(cc.cuentaPredial){
                    res.CUENTA_PREDIAL=cc.cuentaPredial.numero
		}
		return res
            }
            JasperReportDef reportDef=new JasperReportDef(
                name:'CFDI.jrxml',
                fileFormat:JasperExportFormat.PDF_FORMAT,
                reportData:modelData,
                parameters:CfdiParameters.resolverParametros(comprobante)
            )
            ByteArrayOutputStream out=jasperService.generateReport(reportDef)
            String pdfName=cfdi.XML_FILE
            pdfName=pdfName.replaceAll(".xml",".pdf")
            FileUtils.writeByteArrayToFile(new File(dir,pdfName))
                //File dest=new File(System['user.home'])
            }
         
    }
}
