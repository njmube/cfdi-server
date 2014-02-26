package com.luxsoft.sw4.cfdi

class EntidadEmisora {

    String clave
    String grupo
    String url
    String usuario
    String password
    String driverClassName='com.mysql.jdbc.Driver'
    boolean activo
    
    Date dateCreated
    Date lastUpdated

    static constraints = {
        clave(size:5..15,unique:true)
        grupo(nullable:true,size:3..15)
        url()
    }
    
    String toString(){
        return "$clave ($grupo) URL:$url"
    }
}
