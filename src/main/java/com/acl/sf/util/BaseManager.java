package com.acl.sf.util;

import java.util.Locale;

import org.springframework.context.MessageSource;

/**
 * Base class for Scribes and managers to derive from
 * Class provides implementations of Locale dependent
 * message source
 * 
 * @author iandrosov
 *
 */
public class BaseManager {
	private Locale locale = Locale.US;
	private MessageSource messageSource;
	 
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public String getMessageSource(String prop){
		String msg = "";
		if (this.messageSource != null)
			msg = this.messageSource.getMessage(prop, null, this.locale);
		return msg;
	}

	public String getMessageSource(String prop, Locale loc){
		String msg = "";
		if (this.messageSource != null)
			msg = this.messageSource.getMessage(prop, null, loc);
		return msg;
	}
	
	public String getMessageSource(String prop, String txt){
		String msg = txt;
		if (this.messageSource != null){
			String m = this.messageSource.getMessage(prop, null, this.locale);
			if (m != null && m.length() > 0)
				msg = m;
			else
				msg = txt;
		}
		return msg;
	}

	public String getMessageSource(String prop, String txt, Locale loc){
		String msg = txt;
		if (this.messageSource != null){
			String m = this.messageSource.getMessage(prop, null, loc);
			if (m != null && m.length() > 0)
				msg = m;
			else
				msg = txt;
		}
		return msg;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	
	
}
