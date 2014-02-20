/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.luxsoft.sw4.cfdi

import org.springframework.context.ResourceLoaderAware
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader
import java.awt.Image
import java.text.MessageFormat
import javax.imageio.ImageIO
import org.codehaus.groovy.grails.plugins.jasper.JasperExportFormat
import org.codehaus.groovy.grails.plugins.jasper.JasperReportDef

/**
 *
 * @author Ruben Cancino
 */
class PdfGenerator implements ResourceLoaderAware{
    
    private ResourceLoader resourceLoader
    
    def jasperService
    
    byte[] generar(comprobante){
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
            name:'CfdiFactura.jrxml',
            fileFormat:JasperExportFormat.PDF_FORMAT,
            reportData:modelData,
            parameters:resolverParametros(comprobante)
        )
        ByteArrayOutputStream out=jasperService.generateReport(reportDef)
        return out.toByteArray()
        
    }
    
    Map resolverParametros(comprobante){
        def map=[:]
        Resource rs=resourceLoader.getResource("images/companyLogo.jpg")
        Image logo=ImageIO.read(rs.getInputStream())
        println 'Logo cargado: '+logo
        map['LOGO',logo]
        
        //QRCode
        map["QR_CODE"]= QRCodeUtils.generarQR(comprobante)
        TimbreFiscal timbre=new TimbreFiscal(comprobante)
        map["FECHA_TIMBRADO"]= timbre.FechaTimbrado
        map["FOLIO_FISCAL"]= timbre.UUID
        map["SELLO_DIGITAL_SAT"]= timbre.selloSAT
        map["CERTIFICADO_SAT"]= timbre.noCertificadoSAT
        map["CADENA_ORIGINAL_SAT"]= timbre.cadenaOriginal()
            
        return map
    }
    
    void setResourceLoader(ResourceLoader resourceLoader){
        this.resourceLoader=resourceLoader
    }
    
    
}

