package malna314.hyppolit.alexa.handlers;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.Card;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import malna314.hyppolit.alexa.services.AlexaService;
import malna314.hyppolit.alexa.services.RelayService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LightBringerIntentHandler implements IntentHandler{
    protected Logger logger = LoggerFactory.getLogger(LightBringerIntentHandler.class);
    private AlexaService alexaService;
    private RelayService relayService;
    private String message;

    @Autowired
    private void setAlexaService(AlexaService alexaService){
        this.alexaService=alexaService;
    }

    @Autowired
    private void setRelayService(RelayService relayService){
        this.relayService=relayService;
    }


    @Override
    public SpeechletResponse handleIntent(Intent intent, IntentRequest request, Session session) {
        Slot status = intent.getSlot("Status");
        String statusValue = status == null ? null : StringUtils.trimToNull(status.getValue());
        if (statusValue==null){
            statusValue="toggle";
            message = "I switched the light";
        }else{
        message = "I turned "+statusValue+" the light.";}

        relayService.toggleRelay(statusValue);

        Card card = alexaService.newCard("Lightbringer", message);
        PlainTextOutputSpeech speech = alexaService.newSpeech(message, alexaService.inConversationMode(session));

        return alexaService.newSpeechletResponse( card, speech, session, false);
    }
}
