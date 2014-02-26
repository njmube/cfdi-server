package com.luxsoft.sw4.cfdi

import groovy.sql.Sql
import org.springframework.jdbc.datasource.SingleConnectionDataSource



class HomeController {

    def index() { }
    
    def catalogos(){
        //Abrir una conexion de base de datosI
        SingleConnectionDataSource ds=new SingleConnectionDataSource(
        //BasicDataSource ds=new BasicDataSource(
            driverClassName:'com.mysql.jdbc.Driver',
            url:'jdbc:mysql://localhost/produccion',
            username:'root',
            password:'sys')
        Sql sql=new Sql(ds)
        def row=sql.firstRow("select count(*) from SW_SUCURSALES")
        println row
    }
}
