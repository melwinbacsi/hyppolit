package malna314.hyppolit.alexa.services;

import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.Card;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.StandardCard;
import org.springframework.stereotype.Service;

@Service
public final class AlexaService {
	protected static final String SESSION_CONVERSATION_FLAG = "conversation";
	
	public static final String SamplesHelpText = "Here are some things you can say: Tell me something about a random year.  Or, what happened in nineteen eighty-nine?";
	public static final String RepromptText = "What else can I tell you?  Say \"Help\" for some suggestions.";

	public Card newCard( String cardTitle, String cardText ) {
		
		StandardCard card = new StandardCard();
		card.setTitle( (cardTitle == null) ? "MyDemoApp" : cardTitle );
		card.setText(cardText);

		/*
		Image cardImage = new Image();
		cardImage.setSmallImageUrl("https://www.cutlerstew.com/static/images/cutlerstew-720x480.png");
		cardImage.setLargeImageUrl("https://www.cutlerstew.com/static/images/cutlerstew-1200x800.png");
		
		card.setImage(cardImage);
		*/

		return card;
	}
	
	public PlainTextOutputSpeech newSpeech( String speechText, boolean appendRepromptText ) {
		
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText( appendRepromptText ? speechText + "\n\n" + AlexaService.RepromptText : speechText);

		return speech;
	}
	
	public SpeechletResponse newSpeechletResponse(Card card, PlainTextOutputSpeech speech, Session session, boolean shouldEndSession)  {
		
		// Say it...
		if (inConversationMode(session) && !shouldEndSession) {
			PlainTextOutputSpeech repromptSpeech = new PlainTextOutputSpeech();
			repromptSpeech.setText(AlexaService.RepromptText);
			
			Reprompt reprompt = new Reprompt();
			reprompt.setOutputSpeech(repromptSpeech);
			
			return SpeechletResponse.newAskResponse(speech, reprompt, card);
		}
		else {		
			return SpeechletResponse.newTellResponse(speech, card);
		}
	}


	
	
	public void setConversationMode(Session session, boolean conversationMode) {
		if ( conversationMode )
			session.setAttribute(SESSION_CONVERSATION_FLAG, "true");
		else
			session.removeAttribute(SESSION_CONVERSATION_FLAG);
	}

	public boolean inConversationMode(Session session) {
		 return session.getAttribute(SESSION_CONVERSATION_FLAG) != null;
	}	
	

}
