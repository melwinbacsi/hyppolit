package malna314.hyppolit.alexa.handlers;

import malna314.hyppolit.alexa.services.AlexaService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.speechlet.SpeechletV2;
import com.amazon.speech.ui.Card;
import com.amazon.speech.ui.PlainTextOutputSpeech;

@Service
public class HandlerSpeechlet implements SpeechletV2 {
    protected Logger logger = LoggerFactory.getLogger(HandlerSpeechlet.class);
    private BeanFactory beanFactory;
    private AlexaService alexaService;


    @Autowired
   private void setBeanFactory(BeanFactory beanFactory){
                this.beanFactory=beanFactory; }

    @Autowired
    private void setAlexaService(AlexaService alexaService){
        this.alexaService=alexaService;
    }

    public HandlerSpeechlet() {
    }

    @Override
    public void onSessionStarted(SpeechletRequestEnvelope<SessionStartedRequest> requestEnvelope) {

        // This is invoked when a new Alexa session is started.  Any initialization logic would go here.
        // You can store stuff in the Alexa session, for example, by calling:
        // 		Session session = requestEnvelope.getSession();
    }

    @Override
    public SpeechletResponse onLaunch(SpeechletRequestEnvelope<LaunchRequest> requestEnvelope) {

        // This is called when the skill is started with no specific intent.
        // Such as "Alexa, ask MyDemoApp."
        // When this happens, we should provide a welcome message and prompt
        // the user to ask a question.

        // Set a session variable so that we know we're in conversation mode.
        Session session = requestEnvelope.getSession();
        alexaService.setConversationMode(session, true);

        // Create the initial greeting speech.
        String speechText = "Hello. " + alexaService.SamplesHelpText;

        Card card = alexaService.newCard("Welcome!", speechText);
        PlainTextOutputSpeech speech = alexaService.newSpeech(speechText, false);

        return alexaService.newSpeechletResponse(card, speech, session, false);
    }

    @Override
    public SpeechletResponse onIntent(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {

        // This is invoked whenever an intent is invoked for our application.  We need
        // to figure out when intent it is, then delegate to a handler for that specific
        // intent.
        IntentRequest request = requestEnvelope.getRequest();
        Session session = requestEnvelope.getSession();

        // Get the intent
        Intent intent = request.getIntent();
        if ( intent != null ) {
            // Derive the handler's bean name
            String intentName = intent.getName();
            String handlerBeanName = intentName + "Handler";

            // If this is an Amazon Intent, change the handler name to better
            // match up to a Spring bean name.  For example, the intent AMAZON.HelpIntent should
            // be changed to AmazonHelpIntent.
            handlerBeanName = StringUtils.replace(handlerBeanName, "AMAZON.", "Amazon");
            handlerBeanName = handlerBeanName.substring(0, 1).toLowerCase() + handlerBeanName.substring(1);

            if ( logger.isInfoEnabled() )
                logger.info("About to invoke handler '" + handlerBeanName + "' for intent '" + intentName + "'.");

            // Handle the intent by delegating to the designated handler.
            try {
                Object handlerBean = beanFactory.getBean(handlerBeanName);

                if ( handlerBean != null ) {

                    if ( handlerBean instanceof IntentHandler ) {
                        IntentHandler intentHandler = (IntentHandler) handlerBean;
                        return intentHandler.handleIntent(intent, request, session);
                    }
                }
            }
            catch (Exception e) {
                logger.error("Error handling intent " + intentName, e);
            }
        }


        // Handle unknown intents.  Ask the user for more info.
        // Start a conversation (if not started already) and say that we did not understand the intent
        alexaService.setConversationMode(session, true);

        String errorText = "I don't know what that means. " + alexaService.SamplesHelpText;

        Card card = alexaService.newCard("Dazed and Confused", errorText);
        PlainTextOutputSpeech speech = alexaService.newSpeech(errorText, false);

        return alexaService.newSpeechletResponse(card, speech, session, false);
    }

    @Override
    public void onSessionEnded(SpeechletRequestEnvelope<SessionEndedRequest> requestEnvelope) {

    }


}