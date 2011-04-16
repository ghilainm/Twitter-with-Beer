package TwitterApiImplementation;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;


public class StandardDate{
	private String date;
	
	public StandardDate() {
		// TODO Generate a date of the day using some commun reference clock
		 final TimeZone tz = TimeZone.getTimeZone("GMT:00");
		 final DateFormat dfGMT = DateFormat.getDateTimeInstance(DateFormat.DATE_FIELD , DateFormat.FULL );
		 dfGMT.setTimeZone( tz );
		 date = dfGMT.format(new Date());
	}
	
	public StandardDate(final String dateStr) throws parsingException{
		final TimeZone tz = TimeZone.getTimeZone("GMT:00");
		final DateFormat dfGMT = DateFormat.getDateTimeInstance(DateFormat.DATE_FIELD, DateFormat.FULL );
		dfGMT.setTimeZone( tz );
		try {
			date = dfGMT.format(dfGMT.parse(dateStr));
		} catch (final ParseException e) {
			throw  new parsingException("Date could not be parsed "+ dateStr);
		}
	}
	
	public String toString(){
		 return date;
	}
	
	public Date toDate() throws parsingException{
		final TimeZone tz = TimeZone.getTimeZone("GMT:00");
		final DateFormat dfGMT = DateFormat.getDateTimeInstance(DateFormat.DATE_FIELD, DateFormat.FULL );
		dfGMT.setTimeZone( tz );
		try {
			return dfGMT.parse(date);
		} catch (final ParseException e) {
			throw  new parsingException("Date could not be parsed "+ date);
		}
	}
	
	public static StandardDate parseDate(final String strToParse) throws parsingException{
		return new StandardDate(strToParse);
	}
	
}
