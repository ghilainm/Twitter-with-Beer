package TwitterApiImplementation;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;


public class StandardDate implements Serializable{
	private static final long serialVersionUID = -1148883502191170498L;
	private String date;
	
	public StandardDate() {
		// TODO Generate a date of the day using some commun reference clock
		 final TimeZone tz = TimeZone.getTimeZone("GMT:00");
		 final DateFormat dfGMT = DateFormat.getDateTimeInstance(DateFormat.DATE_FIELD , DateFormat.FULL );
		 dfGMT.setTimeZone( tz );
		 date = dfGMT.format(new Date());
	}
	
	public StandardDate(final String dateStr) throws ParseException{
		final TimeZone tz = TimeZone.getTimeZone("GMT:00");
		final DateFormat dfGMT = DateFormat.getDateTimeInstance(DateFormat.DATE_FIELD, DateFormat.FULL );
		dfGMT.setTimeZone( tz );
		try {
			date = dfGMT.format(dfGMT.parse(dateStr));
		} catch (final ParseException e) {
			throw new ParseException("Date could not be parsed "+ dateStr,1);
		}
	}
	
	@Override
	public String toString(){
		 return date;
	}
	
	public Date toDate() throws ParseException{
		final TimeZone tz = TimeZone.getTimeZone("GMT:00");
		final DateFormat dfGMT = DateFormat.getDateTimeInstance(DateFormat.DATE_FIELD, DateFormat.FULL );
		dfGMT.setTimeZone( tz );
		try {
			return dfGMT.parse(date);
		} catch (final ParseException e) {
			throw  new ParseException("Date could not be parsed "+ date,1);
		}
	}
	
	public static StandardDate parseDate(final String strToParse) throws ParseException{
		return new StandardDate(strToParse);
	}
	
}
