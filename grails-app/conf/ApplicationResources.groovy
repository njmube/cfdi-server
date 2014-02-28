modules = {
    application {
        resource url:'js/application.js'
    }
	
	luxor4{
		dependsOn 'bootstrap'
		resource url:'css/luxor4.css'
	}
}