package server;

import java.util.HashMap;
import java.util.Map;

public class ProgramArgumentParser {
    private final Map<String, String> arguments = new HashMap<>();

    /** Method to add key value pair for input parameters in map */
    public void parseArgs(String[] args) {
        for (int i = 0; i < args.length; i += 2) {
            if (i + 1 < args.length) {
                arguments.put(args[i], args[i + 1]);
            }
        }
    }

    /** Method to get key value pair for input parameters from map */
    public String getArgument(String argName) {
        if(argName.equals("-threads")) {
            if (arguments.get(argName) == null) {
                return "1";
            }
            else {
                return arguments.get(argName);
            }
        }
        return this.arguments.get(argName);
    }
}
