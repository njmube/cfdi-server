

package com.luxsoft.sw4.cfdi

import java.awt.Image
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.text.MessageFormat
import net.glxn.qrgen.QRCode
import net.glxn.qrgen.image.ImageType
import mx.gob.sat.cfd.x3.ComprobanteDocument
import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante
import org.apache.commons.lang.StringUtils
import org.apache.commons.lang.exception.ExceptionUtils

/**
 *
 * @author rcancino
 */
class QRCodeUtils {
    
    public static Image generarQR(Comprobante cfdi) {
        try {
            TimbreFiscal timbre=new TimbreFiscal(cfdi);
            BigDecimal total=cfdi.getTotal();
            String pattern="?re={0}&rr={1}&tt={2,number,##########.######}&id,{3}";
            String qq=MessageFormat.format(pattern, cfdi.getEmisor().getRfc(),cfdi.getReceptor().getRfc(),cfdi.getTotal(),timbre.UUID);
			
            File file=QRCode.from(qq).to(ImageType.GIF).withSize(250, 250).file();
			
            BufferedImage img=ImageIO.read(file);
            return img;
        } catch (Exception e) {
            throw new RuntimeException(ExceptionUtils.getRootCauseMessage(e),e);
        }
		
    }
}

