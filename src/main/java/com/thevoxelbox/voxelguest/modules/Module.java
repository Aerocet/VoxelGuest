package com.thevoxelbox.voxelguest.modules;

import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;

import java.util.Map;
import java.util.Set;

/**
 * @author MikeMatrix
 * @author Monofraps
 */
public interface Module
{
    /**
     * Gets called when the {@link com.thevoxelbox.voxelguest.ModuleManager ModuleManager} enables the module.
     */
    void onEnable();

    /**
     * Gets called when the {@link com.thevoxelbox.voxelguest.ModuleManager ModuleManager} disables the module.
     */
    void onDisable();

    /**
     * Returns a boolean determining whether the module is currently enabled or not.
     *
     * @return a boolean indicating the current status of the module
     */
    boolean isEnabled();

    /**
     * Returns a HashSet of listeners the module wants the module manager to register.
     *
     * @return HashSet of listeners to register
     */
    Set<Listener> getListeners();

    /**
     * Returns the name of the module.
     *
     * @return the module name
     */
    String getName();

    Object getConfiguration();

    String getConfigFileName();

    Map<String, CommandExecutor> getCommandMappings();
}
