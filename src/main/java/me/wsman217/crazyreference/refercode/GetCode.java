package me.wsman217.crazyreference.refercode;

import me.wsman217.crazyreference.CrazyReference;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GetCode {

    /**
     * Get the code of a given player.
     *
     * @param p The player's code to get.
     * @return a new code object that holds the player's code or null if a code could not be created.
     */
    public static Code getCode(Player p) {
        try {
            return new Code(p.getUniqueId().toString());
        } catch (InvalidCodeException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get a player from a given code.
     *
     * @param code The code to turn into a player.
     * @return a player object of null if it is not a valid code.
     */
    public static Player getPlayer(String code) {
        String uuid = Code.reverse(code);
        if (uuid == null)
            return null;
        return CrazyReference.getInstance().getServer().getPlayer(UUID.fromString(uuid));
    }

    public static class Code {
        public String value;

        /**
         * Create a new code object.
         *
         * @param value The player's name who will be converted into a referral code.
         * @throws InvalidCodeException is thrown when the code was either not parsed correctly or if the player was inputted wrongly.
         */
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

        /**
         * Reverse the process of changing a player's name into a referral code.
         *
         * @param value The inputted code.
         * @return Player from the given code or null if the code is incorrect.
         */
        static String reverse(String value) {
            if (value.length() != 32)
                return null;
            String str1 = value.substring(0, 8);
            String str2 = value.substring(8, 12);
            String str3 = value.substring(12, 16);
            String str4 = value.substring(16, 20);
            String str5 = value.substring(20, 32);
            return str1 + "-" + str2 + "-" + str3 + "-" + str4 + "-" + str5;
        }
    }
}
