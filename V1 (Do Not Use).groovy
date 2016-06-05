/**
 *  Battery Level Alert
 *
 *  Author: Aaron Crocco
 *  Date: 2014-01-29
 *	This app will poll chosen devices that use a battery and send an alert when it's low.
 *	As written, this will:
 *		Poll on the 1st & 15th every month at 10AM
 *		Alert when the batteries are lower than 13%.
 */
preferences {
		section("Moniter battery level on...") {
		input "batteryDevices", "capability.battery", title: "Which?", multiple: true
	}
}

def installed() {
	log.debug "Installed with settings: ${settings}"
 	subscribe (batteryDevices, "battDevs", batteryThings) 
	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"
	unsubscribe()
    subscribe (batteryDevices, "battDevs", batteryThings) 
	initialize()
}

def initialize() {

	//Schedule a check of the battery at 10am on the 1st and 15th days of the month
    schedule("0 0 10am 1,15 * ?", checkBattery)
}

def checkBattery() {

	//Gets the current battry level

	log.debug "checking batteries"
    def battery = batteryDevices.currentValue("battery")      
    def whichDevice
	def x=0

    while (x < 50) {
		if (battery[x] < 13) {
        	whichDevice = batteryDevices[x]
            if (whichDevice != null) {
            	log.debug "The $whichDevice battery is low"
                sendPush("The $whichDevice battery is low.")
			}
        }   
        x=x+1
    } 
}

def batteryDevices (evt) { }
