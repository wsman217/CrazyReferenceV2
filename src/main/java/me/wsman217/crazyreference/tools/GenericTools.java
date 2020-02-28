package me.wsman217.crazyreference.tools;

import me.wsman217.crazyreference.CrazyReference;
import org.bukkit.configuration.file.FileConfiguration;

public class GenericTools {

    private static CrazyReference instance = CrazyReference.getInstance();
    private static FileConfiguration configFile = instance.getFileManager().getFile(FileManager.Files.CONFIG);

    /**
     * Check if a string is a number.
     *
     * @param str Inputted string.
     * @return false if the string is not a number, true otherwise.
     */
    public static boolean isNumber(String str) {
        try {
            Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Send a message to the console if verbose is enabled in the config.
     *
     * @param message Message to be sent.
     */
    public static void sendConsoleMessageWithVerbose(String message) {
        if (configFile.getBoolean("Verbose"))
            instance.getServer().getConsoleSender().sendMessage(Constants.prefix + message);
    }
}
