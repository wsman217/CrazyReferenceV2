package me.wsman217.crazyreference.refercode;

import me.wsman217.crazyreference.CrazyReference;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GetCode {

    public static Code getCode(Player p) {
        try {
            return new Code(p.getUniqueId().toString());
        } catch (InvalidCodeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Player getPlayer(String code) {
        String uuid = Code.reverse(code);
        if (uuid == null)
            return null;
        return CrazyReference.getInstance().getServer().getPlayer(UUID.fromString(uuid));
    }

    public static class Code {
        public String value;

        Code(String value) throws InvalidCodeException {
            if (value.length() <= 36) {
                value = value.replaceAll("-", "");
                if (value.length() != 32)
                    throw new InvalidCodeException("Code " + value + " was either not parsed correctly or was never correct.");
                else
                    this.value = value;
            } else
                throw new InvalidCodeException("Code " + value + " was either not parsed correctly or was never correct.");
        }

        //Sample UUID
        //53c178a3-be85-49dd-9001-969ac86068d2
        //012345678901234567890123456789012345
        //Edited UUID
        //53c178a3 be85 49dd 9001 969ac86068d2
        //01234567 8901 2345 6789 012345678901
        static String reverse(String value) {
            if (value.length() != 32)
                return null;
            String str1 = value.substring(0, 8);
            String str2 = value.substring(8, 12);
            String str3 = value.substring(12, 16);
            String str4 = value.substring(16,20);
            String str5 = value.substring(20, 32);
            return str1 + "-" + str2 + "-" + str3 + "-" + str4 + "-" + str5;
        }
    }
}
