package de.catma.tag;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import de.catma.util.Pair;

public class TagManager {
	
	public enum TagManagerEvent {
		tagsetDefinitionChanged,
		tagDefinitionChanged,
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
	
	//TODO: taglibary events, tagLibraries are held to cover tagdef move operations between tagsetdefs, not implemented yet 
	public void addTagLibrary(TagLibrary tagLibrary) {
		if (tagLibrary == null) {
			throw new IllegalArgumentException("tagLibrary cannot be null!");
		}
		currentTagLibraries.add(tagLibrary);
		this.propertyChangeSupport.firePropertyChange(
				TagManagerEvent.tagLibraryChanged.name(),
				null, tagLibrary);
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

	public void addTagDefintion(TagsetDefinition tagsetDefinition,
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
	
	public void synchronize(TagsetDefinition td1, TagsetDefinition td2) {
		logger.info("synching " + td1 + " with " + td2);
		td1.synchronizeWith(td2);
		// no event needed, since synchronization goes always along with
		// modifications to UserMarkupCollections, we handle the two
		// things together in the UserMarkupCollectionManager
	}

	public void removeUserDefinedPropertyDefinition(
			PropertyDefinition propertyDefinition, TagDefinition tagDefinition) {
		tagDefinition.removeUserDefinedPropertyDefinition(propertyDefinition);
		
	}
}
