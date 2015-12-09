/*   
 *   CATMA Computer Aided Text Markup and Analysis
 *   
 *   Copyright (C) 2009-2013  University Of Hamburg
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.catma.tag;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import de.catma.document.source.ContentInfoSet;
import de.catma.util.Pair;

/**
 * This class bundles operations upon {@link TagLibary TagLibraries} and its
 * content.
 * 
 * @author marco.petris@web.de
 *
 */
public class TagManager {
	
	/**
	 * Events issued by this manager.
	 */
	public enum TagManagerEvent {
		/**
		 * <p>{@link PropertyDefinition} added:
		 * <li>{@link PropertyChangeEvent#getNewValue()} = a {@link Pair} of the new 
		 * {@link PropertyDefinition} and its {@link TagDefinition}</li>
		 * <li>{@link PropertyChangeEvent#getOldValue()} = <code>null</code></li>
		 * </p><br />
		 * <p>{@link PropertyDefinition} removed:
		 * <li>{@link PropertyChangeEvent#getNewValue()} = <code>null</code></li>
		 * <li>{@link PropertyChangeEvent#getOldValue()} = a {@link Pair} of the 
		 * removed {@link PropertyDefinition} and its {@link TagDefinition}</li>
		 * </p><br />
		 * <p>{@link PropertyDefinition} changed:
		 * <li>{@link PropertyChangeEvent#getNewValue()} = the changed {@link PropertyDefinition}</li>
		 * <li>{@link PropertyChangeEvent#getOldValue()} = the {@link TagDefinition}</li>
		 * </p>
		 */
		userPropertyDefinitionChanged,
		/**
		 * <p>{@link TagsetDefinition} added:
		 * <li>{@link PropertyChangeEvent#getNewValue()} = a {@link Pair} of the new 
		 * {@link TagsetDefinition} and its {@link TagLibrary} Pair&lt;TagLibrary,TagsetDefinition&gt;</li>
		 * <li>{@link PropertyChangeEvent#getOldValue()} = <code>null</code></li>
		 * </p><br />
		 * <p>{@link TagsetDefinition} removed:
		 * <li>{@link PropertyChangeEvent#getNewValue()} = <code>null</code></li>
		 * <li>{@link PropertyChangeEvent#getOldValue()} = a {@link Pair} of the 
		 * removed {@link TagsetDefinition} and its {@link TagLibrary} Pair&lt;TagLibrary,TagsetDefinition&gt;</li>
		 * </p><br />
		 * <p>{@link TagsetDefinition} changed:
		 * <li>{@link PropertyChangeEvent#getNewValue()} = the changed {@link TagsetDefinition}</li>
		 * <li>{@link PropertyChangeEvent#getOldValue()} = the old name</li>
		 * </p>
		 */
		tagsetDefinitionChanged,
		/**
		 * <p>{@link TagDefinition} added:
		 * <li>{@link PropertyChangeEvent#getNewValue()} = a {@link Pair} of the new 
		 * {@link TagDefinition} and its {@link TagsetDefinition}Pair&lt;TagsetDefinition,TagDefinition&gt;</li>
		 * <li>{@link PropertyChangeEvent#getOldValue()} = <code>null</code></li>
		 * </p><br />
		 * <p>{@link TagDefinition} removed:
		 * <li>{@link PropertyChangeEvent#getNewValue()} = <code>null</code></li>
		 * <li>{@link PropertyChangeEvent#getOldValue()} = a {@link Pair} of the 
		 * removed {@link TagDefinition} and its {@link TagsetDefinition} Pair&lt;TagsetDefinition,TagDefinition&gt;</li>
		 * </p><br />
		 * <p>{@link TagsetDefinition} changed:
		 * <li>{@link PropertyChangeEvent#getNewValue()} = the changed {@link TagsetDefinition}</li>
		 * <li>{@link PropertyChangeEvent#getOldValue()} = a pair of the old name and the old color Pair&lt;String,String&gt;</li>
		 */
		tagDefinitionChanged,
		/**
		 * <p>{@link TagLibrary} added:
		 * <li>{@link PropertyChangeEvent#getNewValue()} = the new library</li>
		 * <li>{@link PropertyChangeEvent#getOldValue()} = <code>null</code></li>
		 * </p><br />
		 * <p>{@link TagDefinition} removed:
		 * <li>{@link PropertyChangeEvent#getNewValue()} = <code>null</code></li>
		 * <li>{@link PropertyChangeEvent#getOldValue()} = the removed library</li>
		 * </p><br />
		 * <p>{@link TagsetDefinition} changed:
		 * <li>{@link PropertyChangeEvent#getNewValue()} = the changed {@link TagLibrary}</li>
		 * <li>{@link PropertyChangeEvent#getOldValue()} = the {@link ContentInfoSet old bibliographical data }</li>
		 */
		tagLibraryChanged, 
		;
	}
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private List<TagLibrary> currentTagLibraries;
	
	private PropertyChangeSupport propertyChangeSupport;
	
	public TagManager() {
		this.propertyChangeSupport = new PropertyChangeSupport(this);
		currentTagLibraries = new ArrayList<TagLibrary>();
	}
	
	public void addTagLibrary(TagLibrary tagLibrary) {
		if (tagLibrary == null) {
			throw new IllegalArgumentException("tagLibrary cannot be null!");
		}
		currentTagLibraries.add(tagLibrary);
		this.propertyChangeSupport.firePropertyChange(
				TagManagerEvent.tagLibraryChanged.name(),
				null, tagLibrary);
	}

	public void removeTagLibrary(TagLibraryReference tagLibraryReference) {
		TagLibrary tagLibrary = getTagLibrary(tagLibraryReference);
		if (tagLibrary != null) {
			removeTagLibrary(tagLibrary);
		}
	}
	
	public TagLibrary getTagLibrary(TagLibraryReference tagLibraryReference) {
		for (TagLibrary tagLibrary : currentTagLibraries) {
			if (tagLibraryReference.getId().equals(tagLibrary.getId())) {
				return tagLibrary;
			}
		}
		return null;
	}

	public void removeTagLibrary(TagLibrary tagLibrary) {
		if (tagLibrary == null) {
			throw new IllegalArgumentException("tagLibrary cannot be null!");
		}
		
		currentTagLibraries.remove(tagLibrary);
		this.propertyChangeSupport.firePropertyChange(
				TagManagerEvent.tagLibraryChanged.name(),
				tagLibrary, null);
	}
	
	public void addTagsetDefinition(
			TagLibrary tagLibrary, TagsetDefinition tagsetDefinition) {
		tagLibrary.add(tagsetDefinition);
		this.propertyChangeSupport.firePropertyChange(
			TagManagerEvent.tagsetDefinitionChanged.name(),
			null, 
			new Pair<TagLibrary, TagsetDefinition>(
					tagLibrary, tagsetDefinition));
	}

	public void addPropertyChangeListener(TagManagerEvent propertyName,
			PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(
				propertyName.name(), listener);
	}

	public void removePropertyChangeListener(TagManagerEvent propertyName,
			PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(propertyName.name(),
				listener);
	}

	public void setTagsetDefinitionName(
			TagsetDefinition tagsetDefinition, String name) {
		String oldName = tagsetDefinition.getName();
		tagsetDefinition.setName(name);
		tagsetDefinition.setVersion();
		this.propertyChangeSupport.firePropertyChange(
				TagManagerEvent.tagsetDefinitionChanged.name(),
				oldName,
				tagsetDefinition);
	}

	public void removeTagsetDefinition(
			TagLibrary tagLibrary, TagsetDefinition tagsetDefinition) {
		tagLibrary.remove(tagsetDefinition);
		this.propertyChangeSupport.firePropertyChange(
				TagManagerEvent.tagsetDefinitionChanged.name(),
				new Pair<TagLibrary, TagsetDefinition>(tagLibrary, tagsetDefinition),
				null);
	}

	public void addTagDefinition(TagsetDefinition tagsetDefinition,
			TagDefinition tagDefinition) {
		tagsetDefinition.addTagDefinition(tagDefinition);
		tagsetDefinition.setVersion();
		this.propertyChangeSupport.firePropertyChange(
			TagManagerEvent.tagDefinitionChanged.name(),
			null,
			new Pair<TagsetDefinition, TagDefinition>(
					tagsetDefinition, tagDefinition));
	}

	public void removeTagDefinition(TagsetDefinition tagsetDefinition,
			TagDefinition tagDefinition) {
		tagsetDefinition.remove(tagDefinition);
		tagsetDefinition.setVersion();
		this.propertyChangeSupport.firePropertyChange(
				TagManagerEvent.tagDefinitionChanged.name(),
				new Pair<TagsetDefinition, TagDefinition>(tagsetDefinition, tagDefinition),
				null);
	}
	
	public void setTagDefinitionTypeAndColor(
			TagDefinition tagDefinition, String type, String colorRgbAsString) {
		String oldType = tagDefinition.getName();
		String oldRgb = tagDefinition.getColor();
		boolean tagDefChanged = false;
		if (!oldType.equals(type)) {
			tagDefinition.setName(type);
			tagDefChanged = true;
		}
		
		if (!oldRgb.equals(colorRgbAsString)) {
			tagDefinition.setColor(colorRgbAsString);
			tagDefChanged = true;
		}
		
		if (tagDefChanged) {
			tagDefinition.setVersion();
			this.propertyChangeSupport.firePropertyChange(
					TagManagerEvent.tagDefinitionChanged.name(),
					new Pair<String, String>(oldType, oldRgb),
					tagDefinition);
		}
	}
	
	/**
	 * Synchronizes td1 with td2 via {@link TagsetDefinition#synchronizeWith(TagsetDefinition)}}
	 * @param td1
	 * @param td2
	 */
	public void synchronize(TagsetDefinition td1, TagsetDefinition td2) {
		logger.info("synching " + td1 + " with " + td2);
		td1.synchronizeWith(td2);
		// no event needed, since synchronization goes always along with
		// modifications of UserMarkupCollections, we handle the two
		// things together in the UserMarkupCollectionManager
	}

	public void removeUserDefinedPropertyDefinition(
			PropertyDefinition propertyDefinition, TagDefinition tagDefinition) {
		tagDefinition.removeUserDefinedPropertyDefinition(propertyDefinition);
		tagDefinition.setVersion();
		this.propertyChangeSupport.firePropertyChange(
				TagManagerEvent.userPropertyDefinitionChanged.name(),
				new Pair<PropertyDefinition, TagDefinition>(
						propertyDefinition, tagDefinition),
				null);
	}
	
	public void updateTagLibrary(
			TagLibraryReference tagLibraryReference, ContentInfoSet contentInfoSet) {
		ContentInfoSet oldContentInfoSet = tagLibraryReference.getContentInfoSet();
		tagLibraryReference.setContentInfoSet(contentInfoSet);
		
		this.propertyChangeSupport.firePropertyChange(
			TagManagerEvent.tagLibraryChanged.name(),
			oldContentInfoSet,
			tagLibraryReference);
	}

	public void addUserDefinedPropertyDefinition(TagDefinition td,
			PropertyDefinition propertyDefinition) {
		td.addUserDefinedPropertyDefinition(propertyDefinition);
		td.setVersion();
		this.propertyChangeSupport.firePropertyChange(
				TagManagerEvent.userPropertyDefinitionChanged.name(),
				null,
				new Pair<PropertyDefinition, TagDefinition>(propertyDefinition, td));
	}

	public void updateUserDefinedPropertyDefinition(
			TagDefinition td,
			PropertyDefinition propertyDefinition) {
		td.setVersion();
		
		this.propertyChangeSupport.firePropertyChange(
				TagManagerEvent.userPropertyDefinitionChanged.name(),
				td,
				propertyDefinition);
	}
}
