package org.falcon;


import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;

import java.util.Arrays;

@ScriptMeta(developer = "Falcon", name = "Professional Cutter", desc = "Cutting trees.", version = 1.1)
public class Main extends Script {
    int treesCut = 0;

    @Override
    public int loop() {
        Item[] inventoryLogs = Inventory.getItems(item -> item.getName().equals("Logs"));

        // Drop logs when inventory is full.
        if (Inventory.isFull()) {
            for (Item logs : inventoryLogs) {
                logs.interact("Drop");
                Time.sleep(50, 125);
            }
            Time.sleep(1500, 2000);
            inventoryLogs = Inventory.getItems(item -> item.getName().equals("Logs"));
            Log.fine(inventoryLogs.length);
        }


        // Informs user about script.
        SceneObject Tree = SceneObjects.getNearest("Tree");
        SceneObjects.newQuery().names("Tree");

        if (Tree.getName() != null) {
            Tree.interact("Chop down");

            // Wait until tree is chopped down.
            Time.sleepUntil(() -> SceneObjects.newQuery()
                    .on(Tree.getPosition())
                    .actions("Chop down")
                    .results()
                    .isEmpty(), () -> !SceneObjects.newQuery()
                    .on(Tree.getPosition())
                    .actions("Chop down")
                    .results()
                    .isEmpty(), 3000);

            // Check if user chopped down tree.
            treesCut += Inventory.getItems(item -> item.getName().equals("Logs")).length - inventoryLogs.length;

            Log.fine("Trees cut %s", treesCut);
        }
        return Random.low(2000, 5000);
    }
}
