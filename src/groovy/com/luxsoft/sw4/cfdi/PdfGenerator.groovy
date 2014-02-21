/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.luxsoft.sw4.cfdi

import mx.gob.sat.cfd.x3.TUbicacion;

import org.springframework.context.ResourceLoaderAware
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader
import java.awt.Image
import java.text.MessageFormat
import javax.imageio.ImageIO
import org.codehaus.groovy.grails.plugins.jasper.JasperExportFormat
import org.codehaus.groovy.grails.plugins.jasper.JasperReportDef

import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante

/**
 *
 * @author Ruben Cancino
 */
class PdfGenerator implements ResourceLoaderAware{
    
    private ResourceLoader resourceLoader
    
    def jasperService
    
    byte[] generar(comprobante,complemento){
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
            name:'FacturaCFDI.jrxml',
            fileFormat:JasperExportFormat.PDF_FORMAT,
            reportData:modelData,
            parameters:resolverParametros(comprobante,complemento)
        )
        ByteArrayOutputStream out=jasperService.generateReport(reportDef)
        return out.toByteArray()
        
    }
    
    Map resolverParametros(Comprobante comprobante,complemento){
        def map=[:]
       
		 Resource rs=resourceLoader.getResource("images/empresaFacLogo.jpg")
        Image logo=ImageIO.read(rs.getInputStream())
        println 'Logo cargado: '+logo
        map['LOGO_EMPRESA']=logo
		
		
		Image img2=ImageIO.read(resourceLoader.getResource("images/facUSD.jpg").getInputStream())
		println 'Image 2:'+img2
		map['FAC_USD']=img2
		
		println 'Procesando complemento: '+complemento
		map['CLAVCTE']=complemento.CLAVCTE
		map['NOMBRE']=complemento.NOMBRE
        map.TEL=complemento.TEL
		
		
		map.CLAVCTE=complemento.CLAVCTE
		map.TEL=complemento.TEL
		map.D_REV=complemento.D_REV
		map.D_PAG=complemento.D_PAG
		map.COB=complemento.COB
		map.VEND=complemento.VEND
		map.PLAZO=complemento.PLAZO
		map.FREV=complemento.FREV
		map.ENVIO=complemento.ENVIO
		map.TIPO=complemento.TIPO
		map.COMENTARIO=complemento.COMENTARIO
		map.DIR_ENTREGA=complemento.DIR_ENTREGA
		map.PUESTO=complemento?.PUESTO?'***PUESTO***':''
		map.SOCIO=complemento.SOCIO
		map.PEDIDO=complemento.PEDIDO
		map.ELAB_VTA=complemento.ELAB_VTA
		map.KILOS=complemento.KILOS
		map.SURTIDOR=complemento.SURTIDOR
		map.ELAB_FAC=complemento.ELAB_FAC
		map.IP=complemento.IP
		map.SUC=complemento.SUC
		map.PCE=complemento.PCE?'COD':''
		map.FPAGO=complemento.FPAGO
		map.MONEDA=complemento.MONEDA
		map.CARGO_ID=complemento.CARGO_ID
		
		//Emisor emisor=comprobante.emisor
		map.EMISIOR=comprobante.emisor.nombre
		map["EMISOR_RFC"]=comprobante.emisor.rfc
		map.DIRECCION_EMISOR=getDireccionEnFormatoEstandar(comprobante.emisor.getDomicilioFiscal())
		//map.REGIMEN=comprobante.emisor.getRegimenFiscalArray().class
		map.REGIMEN='A DONDE ?'
		
		map.FECHA=comprobante.fecha.getTime()
		map.CONDICIONES_DE_PAGO=comprobante.getMetodoDePago()
		map.METODO_PAGO=comprobante.getFormaDePago()
		
		map.NFISCAL=comprobante.serie+"-"+comprobante.folio
		map.TIPO_CFDI=comprobante.getTipoDeComprobante()
		map.NUM_CERTIFICADO=comprobante.getNoCertificado()
		map.SELLO_DIGITAL=comprobante.getSello()
		//map.LEYENDA DE CFDI=comprobante.LEYENDA DE CFDI
		//map.EXPEDIDO_DIRECCION=comprobante.EXPEDIDO_DIRECCION
		map.CUENTA=comprobante.getNumCtaPago()
		
		
		
		
		map.RFC=comprobante.receptor.rfc
		map.DIRECCION=getDireccionEnFormatoEstandar(comprobante.receptor.getDomicilio())
		
		//map.COMENTARIO_FAC=comprobante.COMENTARIO_FAC
		map.IMP_CON_LETRA=ImporteALetra.aLetra(comprobante.total)
		map.PINT_IVA=MonedaUtils.IVA*100
		
		
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
	
	String getDireccionEnFormatoEstandar(def u){
		String pattern="{0} {1} {2} {3}" +
				" {4} {5} {6}" +
				" {7} {8}";
		//StringUtils.
		return MessageFormat.format(pattern
			,u.getCalle() !=null?u.getCalle():""
			,(u.getNoExterior()!=null && !u.getNoExterior().equals(".") )?"NO."+u.getNoExterior():""
			,(u.getNoInterior()!=null && !u.getNoInterior().equals(".") )?"INT."+u.getNoInterior():""
			,u.getColonia()!=null?","+u.getColonia():""
			,u.getCodigoPostal() !=null?","+u.getCodigoPostal():""
			,u.getMunicipio()!=null?","+u.getMunicipio():""
			,u.getLocalidad()!=null?","+u.getLocalidad():""
			,u.getEstado()!=null?","+u.getEstado()+",":""
			,u.getPais()!=null?u.getPais():""
		);
	}
    
    void setResourceLoader(ResourceLoader resourceLoader){
        this.resourceLoader=resourceLoader
    }
    
    
}

