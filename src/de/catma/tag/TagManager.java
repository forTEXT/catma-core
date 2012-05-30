package de.catma.tag;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import de.catma.util.Pair;

public class TagManager {
	
	public enum TagManagerEvent {
		tagsetDefinitionChanged,
		tagsetDefinitionSynchronized,
		tagDefinitionChanged,
		tagLibraryChanged, 
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
		if (tagLibrary == null) {
			throw new IllegalArgumentException("tagLibrary cannot be null!");
		}
		currentTagLibraries.add(tagLibrary);
		this.propertyChangeSupport.firePropertyChange(
				TagManagerEvent.tagLibraryChanged.name(),
				null, tagLibrary);
	}
	
	public void removeTagLibrary(ITagLibrary tagLibrary) {
		if (tagLibrary == null) {
			throw new IllegalArgumentException("tagLibrary cannot be null!");
		}
		
		currentTagLibraries.remove(tagLibrary);
		this.propertyChangeSupport.firePropertyChange(
				TagManagerEvent.tagLibraryChanged.name(),
				tagLibrary, null);
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
		tagsetDefinition.setVersion();
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
		Version oldVersion = td1.getVersion();
		td1.synchronzizeWith(td2);
		this.propertyChangeSupport.firePropertyChange(
				TagManagerEvent.tagsetDefinitionSynchronized.name(),
				oldVersion, td1);
	}
}