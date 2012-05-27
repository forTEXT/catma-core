package de.catma.core.tag;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import de.catma.core.util.Pair;

public class TagManager {
	
	public enum TagManagerEvent {
		tagsetDefinitionChanged,
		tagDefinitionChanged,
		;
	}
	
	private List<ITagLibrary> currentTagLibraries;
	
	private PropertyChangeSupport propertyChangeSupport;
	
	public TagManager() {
		this.propertyChangeSupport = new PropertyChangeSupport(this);
		currentTagLibraries = new ArrayList<ITagLibrary>();
	}
	
	//TODO: taglibary events, tagLibraries are held to cover tagdef move operations between tagsetdefs, not implemented yet 
	public void addTagLibrary(ITagLibrary tagLibrary) {
		currentTagLibraries.add(tagLibrary);
	}
	
	public void removeTagLibrary(ITagLibrary tagLibrary) {
		currentTagLibraries.remove(tagLibrary);
	}
	
	public void addTagsetDefinition(
			ITagLibrary tagLibrary, TagsetDefinition tagsetDefinition) {
		tagLibrary.add(tagsetDefinition);
		this.propertyChangeSupport.firePropertyChange(
			TagManagerEvent.tagsetDefinitionChanged.name(),
			null, 
			new Pair<ITagLibrary, TagsetDefinition>(
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
		this.propertyChangeSupport.firePropertyChange(
				TagManagerEvent.tagsetDefinitionChanged.name(),
				oldName,
				tagsetDefinition);
	}

	public void removeTagsetDefinition(
			ITagLibrary tagLibrary, TagsetDefinition tagsetDefinition) {
		tagLibrary.remove(tagsetDefinition);
		this.propertyChangeSupport.firePropertyChange(
				TagManagerEvent.tagsetDefinitionChanged.name(),
				new Pair<ITagLibrary, TagsetDefinition>(tagLibrary, tagsetDefinition),
				null);
	}

	public void addTagDefintion(TagsetDefinition tagsetDefinition,
			TagDefinition tagDefinition) {
		tagsetDefinition.addTagDefinition(tagDefinition);
		this.propertyChangeSupport.firePropertyChange(
			TagManagerEvent.tagDefinitionChanged.name(),
			null,
			new Pair<TagsetDefinition, TagDefinition>(
					tagsetDefinition, tagDefinition));
	}

	public void removeTagDefinition(TagsetDefinition tagsetDefinition,
			TagDefinition tagDefinition) {
	
		tagsetDefinition.remove(tagDefinition);
		this.propertyChangeSupport.firePropertyChange(
				TagManagerEvent.tagDefinitionChanged.name(),
				new Pair<TagsetDefinition, TagDefinition>(tagsetDefinition, tagDefinition),
				null);
	}
	
	public void setTagDefinitionTypeAndColor(
			TagDefinition tagDefinition, String type, String colorRgbAsString) {
		String oldType = tagDefinition.getName();
		String oldRgb =tagDefinition.getColor();
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
			this.propertyChangeSupport.firePropertyChange(
					TagManagerEvent.tagDefinitionChanged.name(),
					new Pair<String, String>(oldType, oldRgb),
					tagDefinition);
		}
	}

	public void update(ITagLibrary tagLibrary, TagsetDefinition tagsetDefinition) {
		tagLibrary.replace(tagsetDefinition);
	}

}
