import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONException;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public final class PortManager {
    public class PortConfiguration {
        private String name;
        private int[] ports;

        PortConfiguration(String name, int[] ports) {
            this.name = name;
            this.ports = ports;
        }

        public String name() {
            return name;
        }

        public int[] ports() {
            return ports;
        }
    }

    private static ArrayList<PortConfiguration> ports = new ArrayList<>();

    static PortConfiguration portConfigurationWithName(String name) {
        for (PortConfiguration port: ports) {
            if (port.name == name) {
                return port;
            }
        }

        return null;
    }

    // Returns success
    static boolean loadMap(String filePath, boolean prependUserDirectory) {
        // Generate path to load
        Path path = Paths.get("");
        if (prependUserDirectory) {
            path.resolve(System.getProperty("user.dir"));
        }
        path.resolve(filePath);

        // Attempt to load file
        Object jsonData;
        try {
            File f = path.toFile();
            jsonData = new JSONParser().parse(new FileReader(f));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return false;
        }

        // Load port data into objects
        try {
            JSONObject jsonMain = (JSONObject) jsonData;
            // Iterate over keys to get all objects
            for (String portName: jsonMain.keySet()) {
                JSONObject portData = (JSONObject) jsonMain.get(portName);
                // Load port values and create object
                JSONArray portList = portData.getJSONArray("ports");
                int[] portArray = new int[portList.length()];
                for (int i = 0; i < portList.length(); ++i) {
                    portArray[i] = portList.getInt(i);
                }
                ports.add(new PortConfiguration(portName, portArray));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}