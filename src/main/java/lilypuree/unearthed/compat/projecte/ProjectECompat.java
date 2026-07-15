package lilypuree.unearthed.compat.projecte;

import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;

/**
 * STUB: ProjectE integration is currently disabled.
 *
 * The ProjectE build available for NeoForge 1.21.1 (PE1.1.0) changed its Java API package
 * layout — moze_intel.projecte.api.imc no longer exists — so the old IMC-based EMC/transmutation
 * registration code no longer compiles. Since ProjectE compat isn't essential, it's stubbed out
 * here rather than ported. If ProjectE support is wanted later, check the current
 * moze_intel.projecte.api.* package structure for this ProjectE build and re-implement
 * addEMCValues()/addTransmutation() against it.
 */
public class ProjectECompat {

    public static void init(InterModEnqueueEvent event) {
        // no-op: ProjectE compat disabled for this port
    }
}
