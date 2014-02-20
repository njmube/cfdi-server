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
import org.springframework.context.ResourceLoaderAware
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader

import java.awt.Image
import java.text.MessageFormat
import javax.imageio.ImageIO
import com.luxsoft.sw4.cfdi.QRCodeUtils
/**
 *
 * @author rcancino
 */
class CfdiParameters implements ResourceLoaderAware{
    
    private ResourceLoader resourceLoader
    
    Map resolverParametros(Cfdi cfdi){
        
        Comprobante comprobante=cfdi.getComprobante()
        
        def map=[]
        // Datos tomados del Comprobante fiscal digital XML
        map["FOLIO"]=comprobante.getSerie()+"-"+comprobante.getFolio()
        map['NUM_CERTIFICADO']=comprobante.getNoCertificado()
        map['SELLO_DIGITAL']=comprobante.getSello()
        map['NOMBRE']=comprobante.receptor.nombre
        map['RFC']=comprobante.receptor.rfc
        map['FECHA']=comprobante.fecha.getTime()
        map['NFISCAL']=comprobante.serie+"-"+comprobante.folio
        //map['IMPORTE']=cfdi.getImporteBruto()-cfdi.getImporteDescuento()
        map['IMPORTE']=cfdi.importe
        map['IMPUESTO']=comprobante.getImpuestos().getTotalImpuestosTrasladados()
        map['TOTAL']=comprobante.total
        map['DIRECCION']=getDireccionEnFormatoEstandar(comprobante.receptor.direccion) 
        map['CUENTA']=comprobante.getNumCtaPago()
        map['METODO_PAGO']=comprobante.getMetodoDePago()
        map['IMP_CON_LETRA']=ImporteALetra.aLetra(comprobante.total)
        map['SUCURSAL']=cfdi.sucursal
        map["CLAVCTE"]= 'cfdi.getClave()'		
        map["SUC"]=cfdi.getSucursal()
        map["TEL"]=cfdi.getCliente().getTelefonosRow()		
        map["D_REV"]=cfdi.getDiaRevision()
        map["D_PAG"]=cfdi.getDiaDelPago()
        map["COB"]=cfdi.cobradorId
        map["VEND"]=cfdi.vendedorId
        map["PLAZO"]=cfdi.plazo
        map["FREV"]=cfdi.revision
        map["SOCIO"]=cfdi.socio
        map["TIPO"]=cfdi.origenDeOperacion
        map["DOCTO"]=cfdi.comprobante.folio		
        map["TAR_COM_IMP"]=cfdi.cargos
        map["COMENTARIO"]=cfdi.getComentario() 
        map["PCE"]=cfdi.isContraEntrega()?"COD":"PAGADO CON" 
        map["ENVIO"]=cfdi.getPedidoFormaDeEntrega()=="LOCAL"?"PASAN":"ENVIO"
        map["PEDIDO"]=cfdi.pedido
        map["IP"]=cfdi.pedidoIp()
        map["ELAB_VTA"]=cfdi.pedidoCreateUser
        map["PUESTO"]=cfdi.puesto?"**PUESTO**":""
        map["DIR_ENTREGA"]=cfdi.direccionEntrega
        if(cfdi.socio && cfdi.getMisma()){
            map["DIR_ENTREGA"]=cfdi.getSocio().getDireccion()
        }
        map["KILOS"]= cfdi.kilos
        map["MONEDA"]=comprobante.moneda
        map["IMP_DESC"]= cfdi.subTotal2
        map["CORTES"]=cfdi.importeCortes
        map["FLETE"]=cfdi.flete // cfdi
        map["CARGOS"]=cfdi.cargos // cfdi
        map["FPAGO"]= cfdi.formaDePago
        map["ELAB_FAC"]= cfdi.elaboro
        map["SURTIDOR"]= cfdi.surtidor
        map["IMPORTE_BRUTO"]= cfdi.importeBruto
        map["SUBTOTAL_2"]= cfdi.importeBruto-cfdi.getImporteDescuento() 
        map["DESCUENTO"]= cfdi.descuentoGlobal
        map["DESCUENTOS"]= cfdi.getImporteDescuento()
        map["PINT_IVA"]=MonedaUtils.IVA*100
        map["TIPOX"]=cfdi.operacionOrigen
        
        if(cfdi.rfc=="XAXX010101000"){			
            def factor=1+MonedaUtils.IVA
            map["IMPORTE_BRUTO"]= factor*cfdi.importeBruto
            map["DESCUENTOS"]= 	factor*cfdi.importeDescuento
            map["SUBTOTAL_2"]= 	factor*(cfdi.importeBruto-cfdi.importeDescuento)
            map["IMP_DESC"]=factor*cfdi.subtotal2
            map["CORTES"]=factor*cfdi.importeCortes
            map["FLETE"]= factor*cfdi.flete
            map["CARGOS"]= factor*cfdi.cargos
            map["IMPORTE"]=comprobante.total 
        }
        Emisor emisor=comprobante.emisor
        map["EMISOR_NOMBRE"]=emisor.nombre
        map["EMISOR_RFC"]=emisor.rfc
        String pattern="{0} {1}  {2}" +
				"\n{3}" +
				"\n{4}" +
				"\n{5}  {6}"
        String direccionEmisor=MessageFormat.format(pattern
            ,emisor.getDomicilioFiscal().getCalle()
            ,emisor.getDomicilioFiscal().getNoExterior()
            ,StringUtils.defaultIfEmpty(emisor.getDomicilioFiscal().getNoInterior(),"")
            ,emisor.getDomicilioFiscal().getColonia()
            ,emisor.getDomicilioFiscal().getMunicipio()
            ,emisor.getDomicilioFiscal().getCodigoPostal()
            ,emisor.getDomicilioFiscal().getEstado())
        
        map["EMISOR_DIRECCION"]= direccionEmisor
        
        if (emisor.getExpedidoEn() != null){
            TUbicacion expedido=emisor.getExpedidoEn();
            
            String pattern2="{0} {1}  {2}" +
				"\n{3}" +
				"\n{4}" +
				"\n{5}  {6}";
            String expedidoDir=MessageFormat.format(pattern2
                ,expedido.getCalle()
                ,expedido.getNoExterior()
                ,StringUtils.defaultIfEmpty(expedido.getNoInterior(),"")
                ,expedido.getColonia()
                ,expedido.getMunicipio()
                ,expedido.getCodigoPostal()
                ,expedido.getEstado()
            );
            map["EXPEDIDO_DIRECCION"]= expedidoDir
        }
        else
            map["EXPEDIDO_DIRECCION"]= "SNA"
        if (cfdi.anticipoAplicado)
            map["ANTICIPO"]= MonedaUtils.calcularImporteSinIva(cfdi.anticipoAplicado)
        //Especiales para CFDI
        if(cfdi.getTimbreFiscal()!=null){
            map["QR_CODE"]= QRCodeUtils.generarQR(cfdi.getComprobante())
            TimbreFiscal timbre=cfdi.getTimbreFiscal();
            map["FECHA_TIMBRADO"]= timbre.FechaTimbrado
            map["FOLIO_FISCAL"]= timbre.UUID
            map["SELLO_DIGITAL_SAT"]= timbre.selloSAT
            map["CERTIFICADO_SAT"]= timbre.noCertificadoSAT
            map["CADENA_ORIGINAL_SAT"]= timbre.cadenaOriginal()
        }
        
        Resource rs=resourceLoader.getResource("images/companyLogo.jpg")
        Image logo=ImageIO.read(rs.getInputStream())
        return map;
    }
	 
    String getDireccionEnFormatoEstandar(TUbicacion u){
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

