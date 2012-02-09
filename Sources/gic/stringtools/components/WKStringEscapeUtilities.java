package gic.stringtools.components;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class WKStringEscapeUtilities {

    /**
     * Appends str to <code>out</code> while performing HTML attribute escaping.
     * 
     * @param out
     * @param str
     * @throws IOException
     */
    public static void escapeHTMLAttribute(Writer out, String str) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("The Writer must not be null");
        }
        if (str == null) {
            return;
        }
        int sz;
        sz = str.length();
        for (int i = 0; i < sz; i++) {
            char ch = str.charAt(i);
            
            switch (ch) {
            case '&':
                out.write("&amp;");
                break;
            case '"':
                out.write("&quot;");
                break;
            case '\t':
                out.write("&#9;");
                break;
            case '\n':
                out.write("&#10;");
                break;
            case '\r':
                out.write("&#13;");
                break;
            case '<':
                out.write("&lt;");
                break;
            case '>':
                out.write("&gt;");
                break;
            default :
                out.write(ch);
                break;
            }


        }
    }
    
    /**
     * @param str
     * @return a code snippet ready to copy and paste into java code.
     */
    public static String escapedJavaStringConstant(String str) {
        if (str == null) {
            return null;
        }
        try {
            StringWriter writer = new StringWriter(str.length() * 2);
            writer.write("String s = \"");
            escapeJavaStringConstant(writer, str, true);
            writer.write("\";");
            return writer.toString();
        } catch (IOException ioe) {
            // this should never ever happen while writing to a StringWriter
            ioe.printStackTrace();
            return null;
        }
    }
    
    /**
     * Pulled from apache lang commons project (StringUtils).
     * 
     * <p>Worker method for the {@link #escapeJavaScript(String)} method.</p>
     * 
     * @param out write to receieve the escaped string
     * @param str String to escape values in, may be null
     * @param escapeSingleQuote escapes single quotes if <code>true</code>. For Java this is usually false and for Javascript it is true.
     * @throws IOException if an IOException occurs
     */
    private static void escapeJavaStringConstant(Writer out, String str, boolean isPrettyLegible) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("The Writer must not be null");
        }
        if (str == null) {
            return;
        }
        String prettyLegibleLineBreak = "\"\n    + \"";
        int sz;
        sz = str.length();
        
        // \r, \n, or \r\n sequence
        boolean isLineTerminating = false;
        
        for (int i = 0; i < sz; i++) {
        	
            char ch = str.charAt(i);
            
            // Just use \n for all line terminations if pretty legible
            if (isPrettyLegible && ch == '\r' ) {
            	if (i < (sz-1) && str.charAt(i+1) == '\n') {
					// \r\n, so skip the \r.
            		// Go to next iteration skipping this.
					continue;
				} else {
					// \r on its own, so replace with \n
					ch = '\n';
				}

			}
            // handle unicode
            if (ch > 0xfff) {
                out.write("\\u" + hex(ch));
            } else if (ch > 0xff) {
                out.write("\\u0" + hex(ch));
            } else if (ch > 0x7f) {
                out.write("\\u00" + hex(ch));
            } else if (ch < 32) {
                switch (ch) {
                    case '\b':
                        out.write('\\');
                        out.write('b');
                        break;
                    case '\n':
                        out.write('\\');
                        out.write('n');
                        if (isPrettyLegible) {
							out.write(prettyLegibleLineBreak);
						}
                        break;
                    case '\t':
                    	if (isPrettyLegible) {
							out.write("    ");
						} else {
	                        out.write('\\');
	                        out.write('t');							
						}
                        break;
                    case '\f':
                        out.write('\\');
                        out.write('f');
                        break;
                    case '\r':
                        out.write('\\');
                        out.write('r');
                        if (isPrettyLegible) {
							out.write(prettyLegibleLineBreak);
						}
                        break;
                    default :
                        if (ch > 0xf) {
                            out.write("\\u00" + hex(ch));
                        } else {
                            out.write("\\u000" + hex(ch));
                        }
                        break;
                }
            } else {
                switch (ch) {
                    case '\'':
                        out.write('\'');
                        break;
                    case '"':
                        out.write('\\');
                        out.write('"');
                        break;
                    case '\\':
                        out.write('\\');
                        out.write('\\');
                        break;
//                    case '/':
//                        out.write('\\');
//                        out.write('/');
//                        break;
                    default :
                        out.write(ch);
                        break;
                }
            }
        }
    }
    
    /**
     * Pulled from apache lang commons project (StringUtils).
     * 
     * <p>Worker method for the {@link #escapeJavaScript(String)} method.</p>
     * 
     * @param str String to escape values in, may be null
     * @param escapeSingleQuotes escapes single quotes if <code>true</code>
     * @return the escaped string
     */
    private static String escapeJavaStyleString(String str, boolean escapeSingleQuotes) {
        if (str == null) {
            return null;
        }
        try {
            StringWriter writer = new StringWriter(str.length() * 2);
            escapeJavaStyleString(writer, str, escapeSingleQuotes);
            return writer.toString();
        } catch (IOException ioe) {
            // this should never ever happen while writing to a StringWriter
            ioe.printStackTrace();
            return null;
        }
    }
    
    /**
     * Pulled from apache lang commons project (StringUtils).
     * 
     * <p>Worker method for the {@link #escapeJavaScript(String)} method.</p>
     * 
     * @param out write to receieve the escaped string
     * @param str String to escape values in, may be null
     * @param escapeSingleQuote escapes single quotes if <code>true</code>. For Java this is usually false and for Javascript it is true.
     * @throws IOException if an IOException occurs
     */
    private static void escapeJavaStyleString(Writer out, String str, boolean escapeSingleQuote) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("The Writer must not be null");
        }
        if (str == null) {
            return;
        }
        int sz;
        sz = str.length();
        for (int i = 0; i < sz; i++) {
            char ch = str.charAt(i);

            // handle unicode
            if (ch > 0xfff) {
                out.write("\\u" + hex(ch));
            } else if (ch > 0xff) {
                out.write("\\u0" + hex(ch));
            } else if (ch > 0x7f) {
                out.write("\\u00" + hex(ch));
            } else if (ch < 32) {
                switch (ch) {
                    case '\b':
                        out.write('\\');
                        out.write('b');
                        break;
                    case '\n':
                        out.write('\\');
                        out.write('n');
                        break;
                    case '\t':
                        out.write('\\');
                        out.write('t');
                        break;
                    case '\f':
                        out.write('\\');
                        out.write('f');
                        break;
                    case '\r':
                        out.write('\\');
                        out.write('r');
                        break;
                    default :
                        if (ch > 0xf) {
                            out.write("\\u00" + hex(ch));
                        } else {
                            out.write("\\u000" + hex(ch));
                        }
                        break;
                }
            } else {
                switch (ch) {
                    case '\'':
                        if (escapeSingleQuote) {
                          out.write('\\');
                        }
                        out.write('\'');
                        break;
                    case '"':
                        out.write('\\');
                        out.write('"');
                        break;
                    case '\\':
                        out.write('\\');
                        out.write('\\');
                        break;
                    case '/':
                        out.write('\\');
                        out.write('/');
                        break;
                    default :
                        out.write(ch);
                        break;
                }
            }
        }
    }
    
    /**
     * Pulled from apache lang commons project (StringUtils).
     * 
     * <p>Returns an upper case hexadecimal <code>String</code> for the given
     * character.</p>
     * 
     * @param ch The character to convert.
     * @return An upper case hexadecimal <code>String</code>
     */
    private static String hex(char ch) {
        return Integer.toHexString(ch).toUpperCase();
    }
}
