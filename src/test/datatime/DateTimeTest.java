// $Id: $
// $Date: $

package datatime;

import org.junit.Before;
import org.junit.Test;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class DateTimeTest
{
    private ZoneId london;

    @Before
    public void setup()
    {
        this.london = ZoneId.of("Europe/London");
    }

    @Test
    public void classes()
    {
        /**
         * <pre>
         *         Classes
         * 
         *         Clock
         *         Duration
         *         Instant
         *         LocalDate
         *         LocalDateTime
         *         LocalTime
         *         MonthDay
         *         OffsetDateTime
         *         OffsetTime
         *         Period
         *         Year
         *         YearMonth
         *         ZonedDateTime
         *         ZoneId
         *         ZoneOffset
         * 
         *         Enums
         * 
         *         DayOfWeek
         *         Month - This has been fixed so that January has a value of 1 (one), 
         *                 wheras Calender.JANUARY was 0 (zero)
         * 
         *         Exceptions
         * 
         *         DateTimeException
         * </pre>
         */
    }

    @Test
    public void basics()
    {
        System.out.println("================================================");

        {
            /*
             * Get the time now, equivalent to new Date()
             */
            LocalDateTime dt = LocalDateTime.now();
            System.out.println(dt);

            LocalDate ld = dt.toLocalDate();
            System.out.println(ld);

            /*
             * Truncate to seconds in order to ignore the milliseconds
             */
            LocalTime lt = dt.toLocalTime();
            System.out.println(lt.truncatedTo(ChronoUnit.SECONDS));
        }

        /*
         * Use #from and #to methods to create new instances of LocalDate,
         * LocalTime and LocalDateTime
         */
        {
            LocalDate ld = LocalDate.of(2016, 1, 31);
            System.out.println(ld);
        }

        {
            LocalDate ld = LocalDate.from(LocalDateTime.now());
            System.out.println(ld);
        }

        {
            /*
             * Time information
             */
            LocalTime time = LocalTime.of(15, 30);
            assertEquals(15, time.getHour());
            assertEquals(0, time.getSecond());
            assertEquals(30, time.getMinute());
            assertEquals(55800, time.toSecondOfDay());
        }

        {
            Year currentYear = Year.now();

            Year twoThousand = Year.of(2000);
            assertTrue(twoThousand.isLeap());
            assertEquals(366, twoThousand.length());

            /*
             * Get a specific day
             */
            LocalDate date = Year.of(2014).atDay(64);
        }
    }

    @Test
    public void dateArithmetic()
    {
        {
            /*
             * Get tomorrow's date LocalDate tomorrow =
             */
            LocalDate tomorrow = LocalDate.now().plusDays(1);

            /*
             * Go back 5 and a half hours.
             */
            LocalDateTime dateTime =
                    LocalDateTime.now().minusHours(5).minusMinutes(30);
        }

        /*
         * Temporal adjusters
         */
        {
            LocalDate date = LocalDate.of(2014, Month.FEBRUARY, 25);

            /*
             * First day of february 2014 (2014-02-01)
             */
            LocalDate firstDayOfMonth =
                    date.with(TemporalAdjusters.firstDayOfMonth());
            assertEquals(1, firstDayOfMonth.getDayOfMonth());

            /*
             * Last day of february 2014 (2014-02-28)
             */
            LocalDate lastDayOfMonth =
                    date.with(TemporalAdjusters.lastDayOfMonth());
            assertEquals(28, lastDayOfMonth.getDayOfMonth());
        }
    }

    @Test
    public void periods()
    {
        /*
         * Get teh period between two dates
         */
        LocalDate firstDate = LocalDate.of(2010, 5, 17); // 2010-05-17
        LocalDate secondDate = LocalDate.of(2015, 3, 7); // 2015-03-07
        Period period = Period.between(firstDate, secondDate);
        assertEquals(18, period.getDays());
        assertEquals(9, period.getMonths());
        assertEquals(4, period.getYears());
        assertFalse(period.isNegative());

        /*
         * Add a period to a date
         */
        Period twoMonthsAndFiveDays = Period.ofMonths(2).plusDays(5);
        LocalDate sixthOfJanuary = LocalDate.of(2014, 1, 6);

        /*
         * Add two months and five days to 2014-01-06, result is 2014-03-11
         */
        LocalDate eleventhOfMarch = sixthOfJanuary.plus(twoMonthsAndFiveDays);
    }

    @Test
    public void durations()
    {
        Instant firstInstant = Instant.ofEpochSecond(1294881180); // 2011-01-13
                                                                  // 01:13
        Instant secondInstant = Instant.ofEpochSecond(1294708260); // 2011-01-11
                                                                   // 01:11
        Duration between = Duration.between(firstInstant, secondInstant);

        /*
         * Negative because firstInstant is after secondInstant (-172920)
         */
        assertEquals(-172920, between.getSeconds());

        /*
         * Get absolute result in minutes (2882)
         */
        assertEquals(2882, between.abs().toMinutes());

        /*
         * two hours in seconds (7200)
         */
        assertEquals(7200, Duration.ofHours(2).getSeconds());
    }

    @Test
    public void zone_id()
    {
        System.out.println("================================================");

        // Get the current date and time
        ZonedDateTime date1 =
                ZonedDateTime.parse("2007-12-03T10:15:30+05:30[Asia/Karachi]");
        System.out.println("date1: " + date1);

        ZoneId id = ZoneId.of("Europe/Paris");
        System.out.println("ZoneId: " + id);

        ZoneId currentZone = ZoneId.systemDefault();
        System.out.println("CurrentZone: " + currentZone);

        System.out.println(ZoneId.getAvailableZoneIds().size());
        System.out.println(ZoneId.getAvailableZoneIds());
    }

    @Test
    public void clock_change_forward()
    {
        System.out.println("================================================");

        // Get the current date and time
        ZonedDateTime date1 =
                ZonedDateTime.of(2016,
                                 Month.MARCH.getValue(),
                                 26,
                                 1,
                                 0,
                                 0,
                                 0,
                                 this.london);

        System.out.println(date1);

        /*
         * Correctly changes the time offset
         */
        ZonedDateTime date2 = date1.plusDays(1);

        System.out.println(date2);

        /*
         * Correctly changes the time offset
         */
        ZonedDateTime date3 = date1.plusHours(23).plusMinutes(30);

        System.out.println(date3);
    }

    @Test
    public void clock_change_back()
    {
        System.out.println("================================================");

        /*
         * During the time when the clocks go back, there is an overlap of an
         * hour. This makes parsing the time 01:30am ambiguous. According to the
         * documentation, in the case of an overlap, ZonedDate time will use the
         * previous offset (the summertime offset) by default. However we can
         * override this behaviour with the
         * ZonedDateTime#withLaterOffsetAtOverlap method
         */

        ZonedDateTime date1 =
                ZonedDateTime.of(2016,
                                 Month.OCTOBER.getValue(),
                                 30,
                                 1,
                                 30,
                                 0,
                                 0,
                                 this.london);

        System.out.println(date1);

        ZonedDateTime date2 =
                ZonedDateTime.of(2016,
                                 Month.OCTOBER.getValue(),
                                 30,
                                 1,
                                 30,
                                 0,
                                 0,
                                 this.london).withLaterOffsetAtOverlap();

        System.out.println(date2);

        /*
         * We can also convert the initial date on the fly, we should be careful
         * with this however as it does not change the initial date and could
         * lead to confusion.
         */
        System.out.println(date1.withLaterOffsetAtOverlap());
    }

    @Test
    public void comtel_dates()
    {
        System.out.println("================================================");

        try
        {
            final LocalDateTime ldt =
                    LocalDateTime.parse("2016-01-15T25:00:00");
            System.out.println(ldt);
        }
        catch (DateTimeException e)
        {
            /*
             * We cannot parse times over 23, so we cannot directly parse comtel
             * dates.
             */
            System.out.println(e.getMessage());
        }

        /*
         * For some reason the format builder does not seem to respect the
         * leniency.
         */
        DateTimeFormatterBuilder formatBuilder = new DateTimeFormatterBuilder();
        formatBuilder.append(DateTimeFormatter.ISO_DATE_TIME);
        formatBuilder.parseLenient();
        DateTimeFormatter badformatter = formatBuilder.toFormatter();

        /*
         * Set the leniency using this technique, and we can parse
         */
        DateTimeFormatter formatter =
                DateTimeFormatter.ISO_DATE_TIME.withResolverStyle(ResolverStyle.LENIENT);

        final LocalDateTime ldt =
                LocalDateTime.parse("2016-01-15T25:00:00", formatter);
        System.out.println(ldt);

        assertEquals(2016, ldt.getYear());
        assertEquals(Month.JANUARY, ldt.getMonth());
        assertEquals(16, ldt.getDayOfMonth());
        assertEquals(1, ldt.getHour());
        assertEquals(0, ldt.getMinute());
        assertEquals(0, ldt.getSecond());
    }

    @Test
    public void backward_compatability()
    {
        System.out.println("================================================");

        /*
         * Get the current date
         */
        Date currentDate = new Date();

        /*
         * Note the rather confused depiction of Date through the #toString
         * method
         */
        System.out.println("Current date: " + currentDate);

        /*
         * New #toInstant() method on the java.util.Date class
         */
        Instant now = currentDate.toInstant();
        ZoneId currentZone = ZoneId.systemDefault();

        LocalDateTime localDateTime = LocalDateTime.ofInstant(now, currentZone);
        System.out.println("Local date: " + localDateTime);

        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(now, currentZone);
        System.out.println("Zoned date: " + zonedDateTime);

        /*
         * Convert back to java.util.Date is not the prettiest
         */
        Date newDate = new Date(zonedDateTime.toEpochSecond() * 1000);
        System.out.println(newDate);

        /*
         * Create a zoned date time from a calender.
         */
        ZonedDateTime zonedDateTimeFromGregorianCalendar =
                new GregorianCalendar(TimeZone.getTimeZone("America/Los_Angeles")).toZonedDateTime();

        assertEquals(ZoneId.of("America/Los_Angeles"),
                     zonedDateTimeFromGregorianCalendar.getZone());
    }

    @Test
    public void backward_compatability_xml()
            throws DatatypeConfigurationException
    {
        System.out.println("================================================");

        /*
         * We parse XML files using JAXB which returns specific data types, we
         * can convert these to the new classes.
         */

        /*
         * Get the current date
         */
        DatatypeFactory dtFactory = DatatypeFactory.newInstance();
        XMLGregorianCalendar xmlCal =
                dtFactory.newXMLGregorianCalendar("2016-03-21T12:30:45");

        System.out.println("XMLGregorianCalendar: " + xmlCal);

        LocalDateTime localDateTime =
                LocalDateTime.ofInstant(xmlCal.toGregorianCalendar().getTime().toInstant(),
                                        this.london);

        System.out.println("XML Duration: " + localDateTime);

        final javax.xml.datatype.Duration xmlDuration =
                dtFactory.newDuration("P5Y2M10D");

        System.out.println(xmlDuration);// .getTimeInMillis(new Date(0)));
        System.out.println(xmlDuration.getTimeInMillis(new Date(0)));

        final java.time.Duration converted =
                java.time.Duration.ofMillis(xmlDuration.getTimeInMillis(new Date(0)));

        /*
         * It only displays time not the years and days. This should be used
         * with care as it will not know when leap years happen.
         */
        System.out.println(converted);
        System.out.println(converted.toMillis());
    }
}
