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

package org.workcraft.plugins.stg;

import org.workcraft.annotations.DisplayName;
import org.workcraft.annotations.VisualClass;
import org.workcraft.observation.PropertyChangedEvent;
import org.workcraft.serialisation.xml.NoAutoSerialisation;

@DisplayName("Dummy transition")
@VisualClass(org.workcraft.plugins.stg.VisualDummyTransition.class)
public class DummyTransition extends NamedTransition {
    public static final String PROPERTY_NAME = "Name";
    private String name;

    @NoAutoSerialisation
    public void setName(String name) {
        this.name = name;
        sendNotification(new PropertyChangedEvent(this, PROPERTY_NAME));
    }

    @NoAutoSerialisation
    @Override
    public String getName() {
        return name;
    }

}
