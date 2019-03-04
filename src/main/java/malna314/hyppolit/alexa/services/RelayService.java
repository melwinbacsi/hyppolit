package malna314.hyppolit.alexa.services;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import org.springframework.stereotype.Service;

@Service
public class RelayService {
    final GpioPinDigitalOutput pin;

    public RelayService(){
        final GpioController gpio = GpioFactory.getInstance();
        pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "Lamp1", PinState.LOW);

        pin.setShutdownOptions(true, PinState.LOW);

    }

    public void toggleRelay(String status) {
        if(status.equals("on")){
        pin.high();}
        if(status.equals("on")){
        pin.low();}
        if(status.equals("toggle")){
        pin.toggle();}
    }
}
