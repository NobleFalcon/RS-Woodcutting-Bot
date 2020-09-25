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

@ScriptMeta(developer = "Falcon", name = "Professional Cutter", desc = "Cutting trees.", version = 1.1)
public class Main extends Script {
    int totalLogsCut = 0;

    @Override
    public int loop() {
        Time.sleep(500, 850);
        Item[] inventoryLogs = Inventory.getItems(item -> item.getName().equals("Logs"));

        // Drop logs when inventory is full.
        if (Inventory.isFull()) {
            for (Item logs : inventoryLogs) {
                logs.interact("Drop");
                Time.sleep(50, 125);
            }
            Time.sleep(1500, 2000);
            inventoryLogs = Inventory.getItems(item -> item.getName().equals("Logs"));
        }


        // Informs user about script.
        SceneObject Tree = SceneObjects.getNearest("Tree");

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

            // Check how many logs user has chopped on the tree.
            int logsChopped = Inventory.getItems(item -> item.getName().equals("Logs")).length - inventoryLogs.length;

            if (logsChopped > 0) {
                totalLogsCut += logsChopped;
                Log.fine("Trees cut %s", totalLogsCut);
            }
        }
        return Random.low(2000, 5000);
    }
}
