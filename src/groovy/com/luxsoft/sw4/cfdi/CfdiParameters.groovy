/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.luxsoft.sw4.cfdi

import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante
import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante.Emisor
import mx.gob.sat.cfd.x3.TUbicacion

import org.apache.commons.lang.StringUtils
import java.text.MessageFormat
/**
 *
 * @author rcancino
 */
class CfdiParameters {
    
    static resolverParametros(Comprobante comprobante){
        def parametros=[:]
        parametros.put("SERIE", 		comprobante.getSerie());
	parametros.put("FOLIO", 		comprobante.getFolio());
	parametros.put("NUM_CERTIFICADO", 	comprobante.getNoCertificado());
	parametros.put("SELLO_DIGITAL", 	comprobante.getSello());
	parametros.put("RECEPTOR_NOMBRE", 	comprobante.getReceptor().getNombre()); //Recibir como Parametro
	parametros.put("RECEPTOR_RFC", 		comprobante.getReceptor().getRfc());
	parametros.put("FECHA", 		comprobante.getFecha().getTime().toString());
	parametros.put("NFISCAL", 		comprobante.getSerie()+" - "+comprobante.getFolio());
	parametros.put("IMPORTE", 		comprobante.getSubTotal());
	parametros.put("IVA", 			comprobante.getImpuestos().getTotalImpuestosTrasladados());
	parametros.put("TOTAL", 		comprobante.getTotal());
	parametros.put("RECEPTOR_DIRECCION", 	getDireccionEnFormatoEstandar(comprobante.getReceptor().getDomicilio()) );
	parametros.put("NUM_CTA_PAGO", 		comprobante.getNumCtaPago());
	parametros.put("METODO_PAGO", 		comprobante.getMetodoDePago());
	//Datos tomado de la aplicacion
	//parametros.put("IMP_CON_LETRA", 	ImporteALetra.aLetra(comprobante.getTotal()));
        /*
        parametros['IMP_CON_LETRA']=''
	parametros['FORMA_DE_PAGO']=comprobante.formaDePago
	parametros['PINT_IVA']='16 '
	parametros['TIPO_DE_COMPROBANTE']=comprobante.tipoDeComprobante.toString().toUpperCase()
	parametros["DESCUENTOS"]= comprobante.getDescuento()?:0.0
        */
       return parametros
        
    }
	
    static String getDireccionEnFormatoEstandar(TUbicacion u){
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
}

