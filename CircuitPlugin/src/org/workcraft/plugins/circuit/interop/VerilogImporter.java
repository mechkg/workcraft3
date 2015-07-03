/*
 *
 * Copyright 2008,2009 Newcastle University
 *
 * This file is part of Workcraft.
 *
 * Workcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Workcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Workcraft.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.workcraft.plugins.circuit.interop;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.workcraft.exceptions.DeserialisationException;
import org.workcraft.exceptions.FormatException;
import org.workcraft.interop.Importer;
import org.workcraft.plugins.circuit.Circuit;
import org.workcraft.plugins.circuit.CircuitModelDescriptor;
import org.workcraft.plugins.circuit.javacc.ParseException;
import org.workcraft.plugins.circuit.javacc.VerilogParser;
import org.workcraft.plugins.circuit.javacc.VerilogParser.Module;
import org.workcraft.workspace.ModelEntry;

public class VerilogImporter implements Importer {

	@Override
	public boolean accept(File file) {
		return file.getName().endsWith(".v");
	}

	@Override
	public String getDescription() {
		return "Verilog (.v)";
	}

	@Override
	public ModelEntry importFrom(InputStream in) throws DeserialisationException {
		return new ModelEntry(new CircuitModelDescriptor(), importCircuit(in));
	}



	public Circuit importCircuit(InputStream in) throws DeserialisationException {
		try {
			VerilogParser parser = new VerilogParser(in);
			List<VerilogParser.Module> modules = parser.parseCircuit();
//			printDebugInfo(modules);
			HashSet<VerilogParser.Module> topModules = getTopModule(modules);
			printDebugInfo(topModules);
			return new Circuit();
		} catch (FormatException e) {
			throw new DeserialisationException(e);
		} catch (ParseException e) {
			throw new DeserialisationException(e);
		}
	}

	private HashSet<VerilogParser.Module> getTopModule(List<Module> modules) {
		HashSet<VerilogParser.Module> result = new HashSet<>();

		HashSet<String> availableModules = new HashSet<>();
		HashSet<String> instantiatedModules = new HashSet<>();
		HashSet<String> emptyModules = new HashSet<>();
		for (VerilogParser.Module module: modules) {
			if (module.name == null) continue;
			availableModules.add(module.name);
			if (module.instances.isEmpty()) {
				emptyModules.add(module.name);
			}
			for (VerilogParser.Instance instance: module.instances) {
				if (instance.moduleName == null) continue;
				instantiatedModules.add(instance.moduleName);
			}
		}
		availableModules.removeAll(emptyModules);
		availableModules.removeAll(instantiatedModules);
		for (String topName: availableModules) {
			for (VerilogParser.Module module: modules) {
				if (topName.equals(module.name)) {
					result.add(module);
				}
			}
		}
		return result;
	}

	private void printDebugInfo(Collection<VerilogParser.Module> modules) {
		for (VerilogParser.Module module: modules) {
			System.out.println("Module: '" + module.name + "'");
			for (VerilogParser.Port port: module.ports) {
				System.out.println("  Port: '" + port.name + "' (" + port.type +")");
			}
			for (VerilogParser.Instance instance: module.instances) {
				System.out.println("  Instance: '" + instance.name + "' (" + instance.moduleName +")");
				for (VerilogParser.Connection connection: instance.connections) {
					System.out.println("    Connection: '" + connection.name + "' (" + connection.netName +")");
				}
			}
		}
	}
}