package frc.robot;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;

public final class PortManager {
    public static class PortConfiguration {
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

        @Override
        public String toString() {
            return "Port Configuration: NAME:" + name + " PORTS:" + Arrays.toString(ports); 
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
        Path path = Paths.get("/");
        if (prependUserDirectory) {
            path = path.resolve(System.getProperty("user.home"));
        }
        path = path.resolve(filePath);

        System.out.println("Attempting to load map from " + path.toFile());

        // Attempt to load file
        JSONObject jsonData;
        try {
            jsonData = new JSONObject(Files.readString(path));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        // Load port data into objects
        try {
            // Iterate over keys to get all objects
            for (String portName: jsonData.keySet()) {
                JSONObject portData = (JSONObject) jsonData.get(portName);
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
