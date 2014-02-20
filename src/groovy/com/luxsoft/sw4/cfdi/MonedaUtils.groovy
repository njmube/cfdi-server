

package com.luxsoft.sw4.cfdi

import java.math.RoundingMode

/**
 *
 * @author Ruben Cancino 7
 */
class MonedaUtils {
    
    public final static Currency PESOS=Currency.getInstance(new Locale("es","mx"))
    public final static Currency DOLARES=Currency.getInstance(Locale.US)
    public final static Currency EUROS=Currency.getInstance("EUR")
    
    static final BigDecimal IVA=0.16
    static RoundingMode rm=RoundingMode.HALF_EVEN
    //static MathConte
    
    public static BigDecimal calcularImporteSinIva(BigDecimal total){
	
	BigDecimal factor=1d+IVA
	return total.divide(factor,rm)
    }
    
}

