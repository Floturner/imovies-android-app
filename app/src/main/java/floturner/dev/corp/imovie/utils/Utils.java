package floturner.dev.corp.imovie.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static String formateDateString(String dateString) throws ParseException {
        @SuppressLint("SimpleDateFormat") Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

        assert date != null;
        return dateFormatter.format(date);
    }
}
