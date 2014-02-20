

import com.luxsoft.sw4.cfdi.PdfGenerator

// Place your Spring DSL code here
beans = {
    
    pdfGenerator(PdfGenerator){bean ->
        bean.autowire = 'byName'
    }
}
