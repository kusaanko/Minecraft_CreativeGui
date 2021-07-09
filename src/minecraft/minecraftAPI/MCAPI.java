package minecraftAPI;

import java.io.InputStream;

import net.minecraft.src.BaseMod;
import net.minecraft.src.Block;
import net.minecraft.src.Item;

public class MCAPI {
    public static String getModName(Block obj) {
        return "";
    }
    public static String getModName(Item obj) {
        return "";
    }

    public static void registerLanguage(BaseMod mod, String fName, String lang) {}
    public static void registerLanguage(InputStream stream, String lang) {}

    public static void setModName(Block obj, String name) {}
    public static void setModName(Item obj, String name) {}
}
