package com.francobm.simplefly.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ChangeFlyEvent extends Event implements Cancellable {
    private final Player player;
    private final Status status;
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private boolean isCancelled;

    public ChangeFlyEvent(Player player, Status status){
        this.player = player;
        this.status = status;
        this.isCancelled = false;
    }

    public Player getPlayer() {
        return player;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.isCancelled = b;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    public enum Status {
        FLY_ON,
        FLY_OFF
    }
}
