package planes;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import constants.API_credentials;


public class get_data_about_planes_above_poland {
    public static ArrayList<String[]> refresh() throws IOException {
        // http request for json with planes above poland territory
        URL url = new URL("https://opensky-network.org/api/states/all?lamin=49.0273953314&lomin=14.0745211117&lamax=54.8515359564&lomax=24.0299857927");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        // if code doesn't work it is probably because there is no API_credentials file in constants dir
        // it has the same structure as API_credentials_example
        // username and password are obtained from https://opensky-network.org/index.php?option=com_users&view=registration

        // if you don't want to deal with it you can just comment next 3 lines and "import constants.API_credentials;"
        String userpass = API_credentials.username + ":" + API_credentials.password;
        String basicAuth = new String(Base64.getEncoder().encode(userpass.getBytes()));
        con.setRequestProperty("Authorization", "Basic " + basicAuth);


        BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder stringbuilder = new StringBuilder();
        while ((inputLine = bufferedreader.readLine()) != null) {
            stringbuilder.append(inputLine);
        }
        bufferedreader.close();
        con.disconnect();

        // string modifications to get at the end list of list of string where each inner list is a plane
        String data_in_string = stringbuilder.toString();
        String sub_data = data_in_string.substring(29, data_in_string.length() - 3);
        String[] array_of_strings_with_planes = sub_data.split("],");
        ArrayList<String[]> list_of_planes = new ArrayList<>();
        for (String plane_str : array_of_strings_with_planes) {
            plane_str = plane_str.substring(1);
            String[] plane_list = plane_str.split(",");
            list_of_planes.add(plane_list);
        }

        return list_of_planes;
    }
}