package org.jcodec.common.tools;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jcodec.common.StringUtils;

public class MainUtils {

    public static class Cmd {
        public Map<String, String> flags;
        public String[] args;

        public Cmd(Map<String, String> flags, String[] args) {
            this.flags = flags;
            this.args = args;
        }

        public Long getLongFlag(String flagName, Long defaultValue) {
            return flags.containsKey(flagName) ? new Long(flags.get(flagName)) : defaultValue;
        }

        public Integer getIntegerFlag(String flagName, Integer defaultValue) {
            return flags.containsKey(flagName) ? new Integer(flags.get(flagName)) : defaultValue;
        }

        public Double getDoubleFlag(String flagName, Long defaultValue) {
            return flags.containsKey(flagName) ? new Double(flags.get(flagName)) : defaultValue;
        }

        public String getStringFlag(String flagName, String defaultValue) {
            return flags.containsKey(flagName) ? flags.get(flagName) : defaultValue;
        }

        public int[] getMultiIntegerFlag(String flagName, int[] defaultValue) {
            if (!flags.containsKey(flagName))
                return defaultValue;
            String[] split = StringUtils.split(flags.get(flagName), ",");
            int[] result = new int[split.length];
            for (int i = 0; i < split.length; i++)
                result[i] = Integer.parseInt(split[i]);
            return result;
        }

        public Long getLongFlag(String flagName) {
            return this.getLongFlag(flagName, null);
        }

        public Integer getIntegerFlag(String flagName) {
            return getIntegerFlag(flagName, null);
        }

        public Double getDoubleFlag(String flagName) {
            return getDoubleFlag(flagName, null);
        }

        public String getStringFlag(String flagName) {
            return getStringFlag(flagName, null);
        }

        public int[] getMultiIntegerFlag(String flagName) {
            return getMultiIntegerFlag(flagName, new int[0]);
        }

        public String getArg(int i) {
            return i < args.length ? args[i] : null;
        }

        public int argsLength() {
            return args.length;
        }
    }

    private static Pattern flagPattern = Pattern.compile("^--([^=]+)=(.*)$");

    public static Cmd parseArguments(String[] args) {
        Map<String, String> flags = new HashMap<String, String>();
        int firstArg = 0;
        for (; firstArg < args.length; firstArg++) {
            if (args[firstArg].startsWith("--")) {
                Matcher matcher = flagPattern.matcher(args[firstArg]);
                if (matcher.matches()) {
                    flags.put(matcher.group(1), matcher.group(2));
                }
            } else
                break;
        }

        return new Cmd(flags, Arrays.copyOfRange(args, firstArg, args.length));
    }

    public static void printHelp(Map<String, String> flags, String... params) {
        System.out.print("Syntax:");
        StringBuilder sample = new StringBuilder();
        StringBuilder detail = new StringBuilder();
        for (Entry<String, String> entry : flags.entrySet()) {
            sample.append(" [--" + entry.getKey() + "=<value>]");
            detail.append("\t--" + entry.getKey() + "\t\t" + entry.getValue() + "\n");
        }
        for (String string : params) {
            sample.append(" <" + string + ">");
        }
        System.out.println(sample);
        System.out.println("Where:");
        System.out.println(detail);
    }

    public enum ANSIColor {
        BLACK, RED, GREEN, BROWN, BLUE, MAGENTA, CYAN, GREY
    }

    public static String color(String str, ANSIColor fg) {
        return "\033[" + (30 + (fg.ordinal() & 0x7)) + "m" + str + "\033[0m";
    }

    public static String color(String str, ANSIColor fg, boolean bright) {
        return "\033[" + (30 + (fg.ordinal() & 0x7)) + ";" + (bright ? 1 : 2) + "m" + str + "\033[0m";
    }

    public static String color(String str, ANSIColor fg, ANSIColor bg) {
        return "\033[" + (30 + (fg.ordinal() & 0x7)) + ";" + (40 + (bg.ordinal() & 0x7)) + ";1m" + str + "\033[0m";
    }

    public static String color(String str, ANSIColor fg, ANSIColor bg, boolean bright) {
        return "\033[" + (30 + (fg.ordinal() & 0x7)) + ";" + (40 + (bg.ordinal() & 0x7)) + ";" + (bright ? 1 : 2) + "m"
                + str + "\033[0m";
    }
}