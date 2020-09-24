package org.falcon;


import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;

@ScriptMeta(developer = "Falcon", name = "Professional Cutter", desc = "Cutting trees.", version = 1.0)
public class Main extends Script {
    int treesCut = 0;

    public void waitUntilConditionDone(boolean action, int timeout) {
        Time.sleepUntil(() -> action, () -> !action, timeout);
    }

    @Override
    public int loop() {
        // Informs user about script.
        SceneObject Tree = SceneObjects.getNearest("Tree");
        SceneObjects.newQuery().names("Tree");

        if (Tree.getName() != null) {
            Tree.interact("Chop down");
            waitUntilConditionDone(SceneObjects.newQuery()
                    .on(Tree.getPosition())
                    .actions("Chop down")
                    .results()
                    .isEmpty(), 3000);
            treesCut++;
            Log.fine("Trees cut %s", treesCut);
        }
        return Random.low(2000, 5000);
    }
}
