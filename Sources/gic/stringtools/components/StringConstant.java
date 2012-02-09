package gic.stringtools.components;

import org.apache.log4j.Logger;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;

public class StringConstant extends WOComponent {
	
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(StringConstant.class);
	
    private String _inputValue;
	private String _outputValue;

	public StringConstant(WOContext context) {
        super(context);
    }

	/**
	 * @return the inputValue
	 */
	public String inputValue() {
		if (_inputValue == null) {
			_inputValue = "<Enter your text here>";
		}
		return _inputValue;
	}

	/**
	 * @param inputValue the inputValue to set
	 */
	public void setInputValue(String inputValue) {
		_inputValue = inputValue;
	}

	/**
	 * @return the outputValue
	 */
	public String outputValue() {
		return _outputValue;
	}

	/**
	 * @param outputValue the outputValue to set
	 */
	public void setOutputValue(String outputValue) {
		_outputValue = outputValue;
	}

	public WOActionResults submit() {
		if (log.isDebugEnabled())
			log.debug("_inputValue = " + (_inputValue == null ? "null" : _inputValue.toString()));
		
		_outputValue = WKStringEscapeUtilities.escapedJavaStringConstant(_inputValue);
		
		if (log.isDebugEnabled())
			log.debug("_outputValue = " + (_outputValue == null ? "null" : _outputValue.toString()));

		return null;
	}
	
	
	
}