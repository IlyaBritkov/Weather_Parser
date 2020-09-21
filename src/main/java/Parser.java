import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private static int rowIndex = 0; // row index

    public static void main(String[] args) throws Exception {
        Document page = getPage();
        Element tableWeather = page.select("table[class=wt]").first();
        Elements names = tableWeather.select("tr[class=wth]"); // dates
        Elements valueRows = tableWeather.select("tr[valign=top]"); // values weather
        for (Element name : names) {
            String date = getDateFromString(name.select("th[id=dt]").text());
            System.out.println(date + "     Явления      Температура     Давление     Ветер");
            printDailyWeatherRows(valueRows);
        }


    }

    private static Document getPage() throws IOException {
        return Jsoup.parse(new URL("https://www.pogoda.spb.ru/"), 5000);
    }

    private final static Pattern patternDate = Pattern.compile("\\d{2}\\.\\d{2}"); // date pattern


    /**
     * return only date from string
     **/
    private static String getDateFromString(String textDate) throws Exception {
        Matcher matcher = patternDate.matcher(textDate);
        if (matcher.find()) {
            return matcher.group();
        }
        throw new Exception("Can't extract date from string");
    }

    /**
     * return data about the today weather
     **/
    private static void printDailyWeatherRows(Elements values) {
        boolean flagIsNextDay = false;
        for (int i = 0; i < 4; i++) {
            Element row = values.get(rowIndex); //
            for (Element td : row.select("td")) {
                if (i != 0 && td.text().trim().contains("Утро")) {
                    flagIsNextDay = true;
                    break;
                }
                System.out.print(td.text() + "     ");
            }
            if (flagIsNextDay) {
                break;
            }
            System.out.println();
            rowIndex++;
        }
        System.out.println();
    }

}
