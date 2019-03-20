package malna314.hyppolit.alexa.services;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import org.springframework.stereotype.Service;

@Service
public class RelayService {
    final GpioPinDigitalOutput pin1;
    final GpioPinDigitalOutput pin2;

    public RelayService(){
        final GpioController gpio = GpioFactory.getInstance();
        pin1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "Lamp1", PinState.LOW);
        pin1.setShutdownOptions(true, PinState.LOW);

        pin2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "Cam", PinState.LOW);
        pin2.setShutdownOptions(true, PinState.LOW);
    }

    public void toggleRelay(String status, String pinValue) {
        GpioPinDigitalOutput pin = null;
        switch (pinValue) {
        case "light":
            pin = pin1;
            break;
        case "cam":
            pin = pin2;
            break;
        }
        if(pin != null && status.equals("on")){
        pin.high();}
        if(pin != null && status.equals("on")){
        pin.low();}
        if(pin != null && status.equals("toggle")){
        pin.toggle();}
    }
}
