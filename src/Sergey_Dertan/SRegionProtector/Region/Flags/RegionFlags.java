package Sergey_Dertan.SRegionProtector.Region.Flags;

import Sergey_Dertan.SRegionProtector.Region.Flags.Flag.RegionFlag;
import Sergey_Dertan.SRegionProtector.Region.Flags.Flag.RegionSellFlag;
import Sergey_Dertan.SRegionProtector.Region.Flags.Flag.RegionTeleportFlag;
import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.permission.Permission;
import cn.nukkit.plugin.PluginManager;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class RegionFlags {

    public static final int FLAG_INVALID = -1;
    public static final int FLAG_BUILD = 0;
    public static final int FLAG_INTERACT = 1;
    public static final int FLAG_USE = 2;
    public static final int FLAG_PVP = 3;
    public static final int FLAG_EXPLODE = 4;
    public static final int FLAG_LIGHTER = 5;
    public static final int FLAG_MAGIC_ITEM_USE = 6;
    public static final int FLAG_HEAL = 7;
    public static final int FLAG_INVINCIBLE = 8;
    public static final int FLAG_TELEPORT = 9;
    public static final int FLAG_SELL = 10;
    public static final int FLAG_POTION_LAUNCH = 11;
    public static final int FLAG_MOVE = 12;

    public static final int FLAG_AMOUNT = 13;

    public static FlagList defaultFlagList;
    public static RegionFlag[] defaults;
    public static Permission[] permissions;

    public static void init(boolean[] flagsDefault) {
        defaults = new RegionFlag[FLAG_AMOUNT];

        defaults[FLAG_BUILD] = new RegionFlag(flagsDefault[FLAG_BUILD]);
        defaults[FLAG_INTERACT] = new RegionFlag(flagsDefault[FLAG_INTERACT]);
        defaults[FLAG_USE] = new RegionFlag(flagsDefault[FLAG_USE]);
        defaults[FLAG_PVP] = new RegionFlag(flagsDefault[FLAG_PVP]);
        defaults[FLAG_EXPLODE] = new RegionFlag(flagsDefault[FLAG_EXPLODE]);
        defaults[FLAG_LIGHTER] = new RegionFlag(flagsDefault[FLAG_LIGHTER]);
        defaults[FLAG_MAGIC_ITEM_USE] = new RegionFlag(flagsDefault[FLAG_MAGIC_ITEM_USE]);
        defaults[FLAG_HEAL] = new RegionFlag(flagsDefault[FLAG_HEAL]);
        defaults[FLAG_INVINCIBLE] = new RegionFlag(flagsDefault[FLAG_INVINCIBLE]);
        defaults[FLAG_TELEPORT] = new RegionTeleportFlag(flagsDefault[FLAG_TELEPORT]);
        defaults[FLAG_SELL] = new RegionSellFlag(flagsDefault[FLAG_SELL]);
        defaults[FLAG_POTION_LAUNCH] = new RegionFlag(flagsDefault[FLAG_POTION_LAUNCH]);
        defaults[FLAG_MOVE] = new RegionFlag(flagsDefault[FLAG_MOVE]);

        defaultFlagList = new FlagList(defaults);

        PluginManager pluginManager = Server.getInstance().getPluginManager();

        permissions = new Permission[FLAG_AMOUNT];

        permissions[FLAG_BUILD] = pluginManager.getPermission("sregionprotetor.region.flag.build");
        permissions[FLAG_INTERACT] = pluginManager.getPermission("sregionprotetor.region.flag.interact");
        permissions[FLAG_USE] = pluginManager.getPermission("sregionprotetor.region.flag.use");
        permissions[FLAG_PVP] = pluginManager.getPermission("sregionprotetor.region.flag.pvp");
        permissions[FLAG_EXPLODE] = pluginManager.getPermission("sregionprotetor.region.flag.explode");
        permissions[FLAG_LIGHTER] = pluginManager.getPermission("sregionprotetor.region.flag.lighter");
        permissions[FLAG_MAGIC_ITEM_USE] = pluginManager.getPermission("sregionprotetor.region.flag.magic_item_use");
        permissions[FLAG_HEAL] = pluginManager.getPermission("sregionprotetor.region.flag.heal");
        permissions[FLAG_INVINCIBLE] = pluginManager.getPermission("sregionprotetor.region.flag.invincible");
        permissions[FLAG_TELEPORT] = pluginManager.getPermission("sregionprotetor.region.flag.teleport");
        permissions[FLAG_SELL] = pluginManager.getPermission("sregionprotetor.region.flag.sell");
        permissions[FLAG_POTION_LAUNCH] = pluginManager.getPermission("sregionprotetor.region.flag.potion_launch");
        permissions[FLAG_MOVE] = pluginManager.getPermission("sregionprotetor.region.flag.move");
    }

    private static void registerFlag() { //TODO

    }

    public static FlagList loadFlagList(Map<String, Map<String, Object>> data) {
        if (data == null) return getDefaultFlagList();
        RegionFlag[] flags = new RegionFlag[FLAG_AMOUNT];
        for (Map.Entry<String, Map<String, Object>> flagData : data.entrySet()) {
            int id = getFlagIdByName(flagData.getKey());
            if (id == -1) continue;
            switch (id) {
                default:
                    flags[id] = new RegionFlag((boolean) flagData.getValue().get("state"));
                    break;
                case FLAG_TELEPORT:
                    flags[id] = new RegionTeleportFlag(false);
                    Map<String, Object> posData = (Map<String, Object>) flagData.getValue().get("position");
                    if (posData != null) {
                        double x = (double) posData.get("x");
                        double y = (double) posData.get("y");
                        double z = (double) posData.get("z");
                        String levelName = (String) posData.get("level");
                        if (x == 0 && y == 0 && z == 0) continue;
                        Level level = Server.getInstance().getLevelByName(levelName);
                        if (level == null) continue;
                        ((RegionTeleportFlag) flags[id]).position = new Position(x, y, z, level);
                    }
                    flags[id].state = (boolean) flagData.getValue().get("state");
                    break;
                case FLAG_SELL:
                    flags[id] = new RegionSellFlag();
                    int price = (int) flagData.getValue().getOrDefault("price", 0);
                    if (price < 0) continue;
                    ((RegionSellFlag) flags[id]).price = price;
                    flags[id].state = (boolean) flagData.getValue().get("state");
                    break;
            }
        }
        return fixMissingFlags(flags);
    }

    public static FlagList getDefaultFlagList() {
        return defaultFlagList.clone();
    }

    public static Permission getFlagPermission(int flagID) {
        return permissions[flagID];
    }

    public static String getFlagName(int flagID) {
        switch (flagID) {
            default:
                return "";
            case FLAG_BUILD:
                return "build";
            case FLAG_INTERACT:
                return "interact";
            case FLAG_USE:
                return "use";
            case FLAG_PVP:
                return "pvp";
            case FLAG_EXPLODE:
                return "tnt";
            case FLAG_LIGHTER:
                return "lighter";
            case FLAG_MAGIC_ITEM_USE:
                return "magicitem";
            case FLAG_HEAL:
                return "heal";
            case FLAG_INVINCIBLE:
                return "invincible";
            case FLAG_TELEPORT:
                return "teleport";
            case FLAG_SELL:
                return "sell";
            case FLAG_POTION_LAUNCH:
                return "potion_launch";
            case FLAG_MOVE:
                return "move";
        }
    }

    public static int getFlagIdByName(String name) {
        switch (name) {
            default:
                return -1;
            case "build":
                return FLAG_BUILD;
            case "interact":
                return FLAG_INTERACT;
            case "use":
                return FLAG_USE;
            case "pvp":
                return FLAG_PVP;
            case "tnt":
                return FLAG_EXPLODE;
            case "lighter":
                return FLAG_LIGHTER;
            case "magic_item":
            case "magic_item_use":
            case "magicitem":
            case "magic":
                return FLAG_MAGIC_ITEM_USE;
            case "heal":
                return FLAG_HEAL;
            case "invincible":
                return FLAG_INVINCIBLE;
            case "teleport":
            case "tp":
                return FLAG_TELEPORT;
            case "sell":
                return FLAG_SELL;
            case "potion_launch":
                return FLAG_POTION_LAUNCH;
            case "move":
                return FLAG_MOVE;
        }
    }

    public static boolean getStateFromString(String state) {
        switch (state) {
            case "yes":
            case "enable":
            case "enabled":
            case "вкл":
            case "true":
            case "allow":
                return false;
            case "no":
            case "disable":
            case "disabled":
            case "выкл":
            case "false":
            case "deny":
            default:
                return true;
        }
    }

    public static FlagList fixMissingFlags(RegionFlag[] flags) {
        if (flags.length == defaults.length) return new FlagList(flags);
        List<RegionFlag> fix = Arrays.asList(flags);
        for (int i = flags.length; i < FLAG_AMOUNT; ++i) {
            fix.add(i, defaults[i]);
        }
        return new FlagList(fix.toArray(new RegionFlag[FLAG_AMOUNT]));
    }

    public static FlagList fixMissingFlags(FlagList flagList) {
        return fixMissingFlags(flagList.getFlags());
    }
}