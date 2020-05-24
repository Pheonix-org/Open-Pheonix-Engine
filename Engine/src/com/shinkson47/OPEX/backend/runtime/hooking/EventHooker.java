package com.shinkson47.OPEX.backend.runtime.hooking;

import com.shinkson47.OPEX.backend.errormanagement.EMSHelper;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

/**
 *
 */
public class EventHooker {

    public EventHooker(){

    }

    /*
     * Store of all OPEXRunnables managed by this hook updater.
     */
    public List<OPEXRegisteredHook> Hooks = new ArrayList<OPEXRegisteredHook>();

    /**
     * Triggers a call of all events registered, using OPEXHook.updateEvent().
     *
     * @Throws ConcurrentModificationException if registered hooks list was modified during method call.
     */
    public void triggerUpdate() throws ConcurrentModificationException {
                for (OPEXRegisteredHook i : Hooks) {
                    i.getHook().updateEvent();
                }

    }
    /**
     * Add a new update hook to the HookUpdater's jurisdiction.
     *
     * This will issue it a new ID, and cause it to be updated automatically.
     *
     * @return the issued ID.
     */
    public int registerUpdateHook(OPEXHook hook, String name) {
        OPEXRegisteredHook regHook = new OPEXRegisteredHook(hook, name, getUnusedId());
        Hooks.add(regHook);
        hook.enterUpdateEvent();
        return regHook.getID();
    }

    /**
     * Removes all hooks from memory, effecively unregistering them all.
     */
    public void clear() {
        Hooks.clear();
    }

    /**
     * Remove an update hook. This will remove it from the HookUpdater's memory,
     * release the ID, and stop the thread from being updated in the future.
     *
     * If there's no matching hook, the call is ignored and a warning is issued.
     */
    public void deregisterUpdateHook(int i) {
        int index = 0;
        for (OPEXRegisteredHook h : Hooks) {
            if (h.getID() == i) {
                Hooks.remove(index);
                return;
            }
            index++;
        }
        EMSHelper.warn("Could not deregister hook '" + i + "', ID is unused.");
    }


    /**
     * Deregisters an update hook by name If there's no matching hook, the call is
     * ignored and a warning is issued.
     */
    public void deregisterUpdateHook(String name) {
        int Index = getIndexByName(name);
        if (Index == -1) {
            EMSHelper.warn("Failed to remove hook, there's no hook with that name.");
        }

        deregisterUpdateHook(Index);
    }

    /**
     * Searches through all registered hooks to find the lowest next available ID.
     * ID's are incremental indexes, but do not represent a position in a set or
     * order they were registered.
     *
     * @return
     */
    public int getUnusedId() {
        int NextID = 0;

        if (Hooks.size() == 0) {
            return NextID;
        }

        for (OPEXRegisteredHook i : Hooks) {
            if (i.ID != NextID) {
                return NextID;
            } else {
                NextID++;
                continue;
            }
        }
        return 0;
    }

    /**
     * TODO GetHookByName
     * @param name
     * @return
     */
    public OPEXRegisteredHook GetHookByName(String name) {
        return null;
    }

    public int getIndexByName(String name) {
        int index = 0;
        for (OPEXRegisteredHook i : Hooks) {
            if (i.getName() == name) {
                return index;
            }
            index++;
        }
        EMSHelper.warn("Could not find a registered hook named '" + name + "'. Returning an index of -1");
        return -1;
    }

    public OPEXRegisteredHook getHookByName(String name) {
        int index = getIndexByName(name);
        if (index == -1) {
            EMSHelper.warn("Could not find a registered hook named '" + name + "'. Returning null.");
            return null;
        }
        return Hooks.get(index);
    }

    /**
     * Searches for a registered hook with the specified id.
     *
     * @param id - The id to search for
     * @return the hook registered with a matching id.
     * @return null if there's no matches.
     */
    public OPEXRegisteredHook getHookByID(int id) {
        for (OPEXRegisteredHook i : Hooks) {
            if (i.ID == id) {
                return i;
            }
        }
        return null;
    }

    public int getIDByName(String name) {
        return getHookByName(name).ID;
    }
}
