package com.luxsoft.sw4.utils

import grails.validation.Validateable;
import java.text.SimpleDateFormat

@Validateable
class Periodo {
	
	Date fechaInicial
	Date fechaFinal
	
	static SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy")
	
	String toString(){
		return "${dateFormat(fechaInicial) - dateFormat(fechaFinal)}"
	}

}
