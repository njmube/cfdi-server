package com.luxsoft.sw4.cfdi

import grails.transaction.Transactional
import groovy.sql.Sql
import mx.gob.sat.cfd.x3.ComprobanteDocument
import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante
import org.codehaus.groovy.grails.plugins.jasper.JasperExportFormat
import org.codehaus.groovy.grails.plugins.jasper.JasperReportDef
import org.apache.commons.validator.EmailValidator


class CfdiMailService {
    
   
    def dataSource_importacion
    
    
    
    def pdfGenerator
    

    def enviarComprobantes(Date fecha) {
        println "Enviando comprobantes para el $fecha"
        Sql sql=new Sql(dataSource_importacion)
        def select="""select c.rfc,count(*) from sx_cfdi c left join sx_cxc_cargos_cancelados x on(x.CARGO_ID=c.ORIGEN_ID)
                where date(c.creado)=?
                and c.tipo=? 
                and c.RFC<>?
                and  x.CARGO_ID is null
                and c.uuid is not null
                group by c.rfc 
                having count(*)>1 
                order by count(*) desc
                
                """
        def params=[fecha,'FACTURA','XAXX010101000']
        //Procesamos receptor por receptor
        sql.eachRow(select,params){row->
            String rfc=row.rfc
            try{
                enviarComprobantes(fecha,rfc)
            }catch (Exception ex){
                ex.printStackTrace()
                log.error ex
            }
        }
        
         
    }
    
    def enviarComprobantes(Date fecha,String rfc){
        def xmlFiles=[:]
        def cfdSelect="""select * from sx_cfdi c where date(c.creado)=? and c.rfc=? 
            """
        Sql sql=new Sql(dataSource_importacion)
        sql.eachRow(cfdSelect,[fecha,rfc]){ cfdi->
            //println 'Agregndo archivo: '+cfdi.XML_FILE
            xmlFiles[cfdi.XML_FILE]=cfdi.xml
            
        }
        
        //Detectando correo electronico destino
        def row=sql.firstRow("""select x.email1 as email,c.nombre as cliente
                    from SX_CLIENTES_CFDI_MAILS x join sx_clientes c on(x.cliente_id=c.cliente_id) 
                    where c.rfc=?
                    """
            ,[rfc])
        // Si  no localizo email en SX_CLIENTES_CFDI
        if(!row){
            println 'Buscando email en tabla de clientes'
            row=sql.firstRow("select c.email1 as email from SX_CLIENTES  c where c.rfc=?",[rfc])
            if(!row){
                println "No se puede enviar correo de cfdis a $rfc por no localizar cuenta apropiada de correo"
                return
            }
        }
        if(!EmailValidator.getInstance().isValid(row.email)){
            println "Correo invalido $row.email para RFC:$rfc"
            return
        }
        println "Enviando correos a: $rfc  a $row.email "
        // Generando PDFs
        def pdfFiles=[:]
        xmlFiles.each{k,v ->
             
             ByteArrayInputStream is=new ByteArrayInputStream(v)
             ComprobanteDocument docto=ComprobanteDocument.Factory.parse(is)
             Comprobante comprobante=docto.comprobante
             byte[] pdf=pdfGenerator.generar(comprobante,obtenerParametrosComplementarios(null))
             pdfFiles[k.replaceAll('.xml','.pdf')]=pdf
        }
        /*
        sendMail{
            multipart true
            to 'rubencancino6@gmail.com'
            cc 'lquintanillab@gmail.com'
            subject 'Comprobantes fiscales digitales '
            html view:'/cfdi/mailAutomaticoPorReceptor',model:[cliente:row.cliente]
            xmlFiles.each{ k,v ->
                attach k,"application/xml",v
            }
            pdfFiles.each{k,v ->
                attach k,"application/pdf",v
            }
        }
        println "Enviando enviados a: $rfc  a $row.email "
        */
       
    }
	
	def obtenerParametrosComplementarios(String cargoId){
		String select="""
			SELECT V.CARGO_ID,V.CLAVE AS CLAVCTE,V.NOMBRE
			,CONCAT(IFNULL((SELECT CONCAT('1) ',T.TELEFONO)  FROM sx_clientes_tels T WHERE T.CLIENTE_ID=V.CLIENTE_ID AND T.TIPO='TEL1'),' ')
			,IFNULL((SELECT CONCAT('2) ',T.TELEFONO)  FROM sx_clientes_tels T WHERE T.CLIENTE_ID=V.CLIENTE_ID AND T.TIPO='TEL2'),' ')
			,IFNULL((SELECT CONCAT('3) ',T.TELEFONO)  FROM sx_clientes_tels T WHERE T.CLIENTE_ID=V.CLIENTE_ID AND T.TIPO='TEL3'),' ')  
			,IFNULL((SELECT CONCAT('4) ',T.TELEFONO)  FROM sx_clientes_tels T WHERE T.CLIENTE_ID=V.CLIENTE_ID AND T.TIPO='TEL4'),' ') 
			,IFNULL((SELECT CONCAT('CEL ',T.TELEFONO)  FROM sx_clientes_tels T WHERE T.CLIENTE_ID=V.CLIENTE_ID AND T.TIPO='CEL'),' ')	
			,IFNULL((SELECT CONCAT('FAX ',T.TELEFONO)  FROM sx_clientes_tels T WHERE T.CLIENTE_ID=V.CLIENTE_ID AND T.TIPO='FAX'),' ')
			) AS TEL
			,V.DIA_DE_REV AS D_REV,V.DIA_DEL_PAGO AS D_PAG,V.COBRADOR_ID AS COB,V.VENDEDOR_ID AS VEND,V.PLAZO,(CASE WHEN V.REVISION IS TRUE THEN 'R' ELSE 'F' END) AS FREV
           ,V.PEDIDO_FENTREGA AS ENVIO,(CASE WHEN V.ORIGEN='CRE' THEN 'CREDITO' ELSE 'CONTADO' END) AS TIPO,V.COMENTARIO,V.INSTRUCCION_ENTREGA AS DIR_ENTREGA,V.PUESTO
           ,(SELECT S.NOMBRE FROM sx_socios S WHERE S.SOCIO_ID=V.SOCIO_ID) AS SOCIO
           ,V.PEDIDO_FOLIO AS PEDIDO,V.MODIFICADO_USERID AS ELAB_VTA,V.KILOS,V.SURTIDOR,V.CREADO_USERID AS ELAB_FAC,V.PEDIDO_IP_CREATED AS IP
           ,(SELECT S.CLAVE FROM sw_sucursales S WHERE S.SUCURSAL_ID=V.SUCURSAL_ID) AS SUC
           ,V.CE AS PCE,V.FPAGO
          ,V.MONEDA
          FROM SX_VENTAS V 
          WHERE V.CARGO_ID=?
		"""
		Sql sql=new Sql(dataSource_importacion)
		def row=sql.firstRow(select,[cargoId])
		return row
	}
    
    def testPdf(){
        def id='00000000-43120afd-0143-12919ac1-002d'
        Sql sql=new Sql(dataSource_importacion)
        def row=sql.firstRow("select * from SX_CFDI where CFD_ID=?",[id])
        ByteArrayInputStream is=new ByteArrayInputStream(row.xml)
        ComprobanteDocument docto=ComprobanteDocument.Factory.parse(is)
        Comprobante comprobante=docto.comprobante
		//println 'Buscando parametros complementarios para CARGO_ID: '+row.ORIGEN_ID
		def complemento=obtenerParametrosComplementarios(row.ORIGEN_ID)
		//println 'Complemento: '+complemento
        byte[] pdf=pdfGenerator.generar(comprobante,complemento)
        return pdf
        
    }
    /*
    def generarPdf(cfdi){
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
            parameters:cfdiParameters.resolverParametros(cfdi)
        )
        ByteArrayOutputStream out=jasperService.generateReport(reportDef)
        return out
    }*/
}


