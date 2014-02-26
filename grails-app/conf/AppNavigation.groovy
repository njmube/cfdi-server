/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author rcancino
 */
navigation={
    app{
        home(titleText:'Inicio')
        catalogos(controller:'home')
        cfdi(titleText:'Comprobantes'){
            importar()
            enviarCorreo()
            correos()
            pdf(titleText:'PDF')
        }  
    }
    admin{
        
    }
}