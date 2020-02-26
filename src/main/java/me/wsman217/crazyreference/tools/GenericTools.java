package me.wsman217.crazyreference.tools;

import me.wsman217.crazyreference.CrazyReference;
import org.bukkit.configuration.file.FileConfiguration;

public class GenericTools {

    private static CrazyReference instance = CrazyReference.getInstance();
    private static FileConfiguration configFile = instance.getFileManager().getFile(FileManager.Files.CONFIG);

    public static boolean isNumber(String str) {
        try {
            Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static void sendConsoleMessageWithVerbose(String message) {
        if (configFile.getBoolean("Verbose"))
            instance.getServer().getConsoleSender().sendMessage(Constants.prefix + message);
    }
}
