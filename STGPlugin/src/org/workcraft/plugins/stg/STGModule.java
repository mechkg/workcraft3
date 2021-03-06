package org.workcraft.plugins.stg;

import org.workcraft.CompatibilityManager;
import org.workcraft.Framework;
import org.workcraft.Initialiser;
import org.workcraft.Module;
import org.workcraft.PluginManager;
import org.workcraft.Tool;
import org.workcraft.dom.ModelDescriptor;
import org.workcraft.gui.propertyeditor.Settings;
import org.workcraft.interop.Exporter;
import org.workcraft.interop.Importer;
import org.workcraft.plugins.stg.interop.DotGExporter;
import org.workcraft.plugins.stg.interop.DotGImporter;
import org.workcraft.plugins.stg.serialisation.DotGSerialiser;
import org.workcraft.plugins.stg.serialisation.ImplicitPlaceArcDeserialiser;
import org.workcraft.plugins.stg.serialisation.ImplicitPlaceArcSerialiser;
import org.workcraft.plugins.stg.tools.DummyToSignalTransitionConverterTool;
import org.workcraft.plugins.stg.tools.MakePlacesExplicitTool;
import org.workcraft.plugins.stg.tools.MakePlacesImplicitTool;
import org.workcraft.plugins.stg.tools.PetriNetToStgConverterTool;
import org.workcraft.plugins.stg.tools.SignalMirrorTool;
import org.workcraft.plugins.stg.tools.SignalToDummyTransitionConverterTool;
import org.workcraft.plugins.stg.tools.StgToPetriNetConverterTool;
import org.workcraft.plugins.stg.tools.NamedTransitionContractorTool;
import org.workcraft.plugins.stg.tools.TransitionMergerTool;
import org.workcraft.serialisation.ModelSerialiser;
import org.workcraft.serialisation.xml.XMLDeserialiser;
import org.workcraft.serialisation.xml.XMLSerialiser;

public class STGModule implements Module {

    @Override
    public String getDescription() {
        return "Signal Transition Graphs";
    }

    @Override
    public void init() {
        initPluginManager();
        initCompatibilityManager();
    }

    private void initPluginManager() {
        final Framework framework = Framework.getInstance();
        final PluginManager pm = framework.getPluginManager();
        pm.registerClass(ModelDescriptor.class, StgDescriptor.class);

        pm.registerClass(XMLSerialiser.class, ImplicitPlaceArcSerialiser.class);
        pm.registerClass(XMLDeserialiser.class, ImplicitPlaceArcDeserialiser.class);

        pm.registerClass(Exporter.class, DotGExporter.class);
        pm.registerClass(Importer.class, DotGImporter.class);

        pm.registerClass(ModelSerialiser.class, DotGSerialiser.class);
        pm.registerClass(Settings.class, STGSettings.class);

        pm.registerClass(Tool.class, new Initialiser<Tool>() {
            @Override
            public Tool create() {
                return new SignalMirrorTool();
            }
        });

        pm.registerClass(Tool.class, new Initialiser<Tool>() {
            @Override
            public Tool create() {
                return new MakePlacesImplicitTool();
            }
        });

        pm.registerClass(Tool.class, new Initialiser<Tool>() {
            @Override
            public Tool create() {
                return new MakePlacesExplicitTool();
            }
        });

        pm.registerClass(Tool.class, new Initialiser<Tool>() {
            @Override
            public Tool create() {
                return new SignalToDummyTransitionConverterTool();
            }
        });

        pm.registerClass(Tool.class, new Initialiser<Tool>() {
            @Override
            public Tool create() {
                return new DummyToSignalTransitionConverterTool();
            }
        });

        pm.registerClass(Tool.class, new Initialiser<Tool>() {
            @Override
            public Tool create() {
                return new NamedTransitionContractorTool();
            }
        });

        pm.registerClass(Tool.class, new Initialiser<Tool>() {
            @Override
            public Tool create() {
                return new PetriNetToStgConverterTool();
            }
        });

        pm.registerClass(Tool.class, new Initialiser<Tool>() {
            @Override
            public Tool create() {
                return new StgToPetriNetConverterTool();
            }
        });

        pm.registerClass(Tool.class, new Initialiser<Tool>() {
            @Override
            public Tool create() {
                return new TransitionMergerTool();
            }
        });
    }

    private void initCompatibilityManager() {
        final Framework framework = Framework.getInstance();
        final CompatibilityManager cm = framework.getCompatibilityManager();

        cm.registerMetaReplacement(
                "<descriptor class=\"org.workcraft.plugins.stg.STGModelDescriptor\"/>",
                "<descriptor class=\"org.workcraft.plugins.stg.StgDescriptor\"/>");
    }

}
