package ac.grim.grimac;

import ac.grim.grimac.events.anticheat.GenericMovementCheck;
import ac.grim.grimac.events.anticheat.PacketWorldReader;
import ac.grim.grimac.events.bukkit.PlayerJoinLeaveListener;
import ac.grim.grimac.events.bukkit.PlayerLagback;
import ac.grim.grimac.events.bukkit.PlayerVelocityPackets;
import ac.grim.grimac.events.bukkit.UseFireworkEvent;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.settings.PacketEventsSettings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class GrimAC extends JavaPlugin {
    public static HashMap<Player, GrimPlayer> playerGrimHashMap = new HashMap<>();
    public static Plugin plugin;

    @Override
    public void onLoad() {
        PacketEvents.create(this);
        PacketEventsSettings settings = PacketEvents.get().getSettings();
        settings.checkForUpdates(false).compatInjector(false);
        PacketEvents.get().loadAsyncNewThread();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        PacketEvents.get().terminate();
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        registerEvents();
        registerPackets();
        registerChecks();

        for (Player player : Bukkit.getOnlinePlayers()) {
            playerGrimHashMap.put(player, new GrimPlayer(player));
        }

    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new PlayerJoinLeaveListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerLagback(), this);
        //Bukkit.getPluginManager().registerEvents(new MovementVelocityCheck(), this);
        Bukkit.getPluginManager().registerEvents(new UseFireworkEvent(), this);
    }

    public void registerPackets() {
        PacketEvents.get().registerListener(new GenericMovementCheck());
        PacketEvents.get().registerListener(new PlayerVelocityPackets());
        PacketEvents.get().registerListener(new PacketWorldReader());
        PacketEvents.get().init();
    }

    public void registerChecks() {
        //GenericMovementCheck.registerCheck(new MovementVelocityCheck());
        //GenericMovementCheck.registerCheck(new Timer());
    }
}
