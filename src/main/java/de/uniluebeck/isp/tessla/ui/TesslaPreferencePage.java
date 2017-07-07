package de.uniluebeck.isp.tessla.ui;

import java.time.ZoneId;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.PathEditor;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.ScaleFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

public class TesslaPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	public TesslaPreferencePage() {
		super(GRID);
	}

	protected void createFieldEditors() {
		addField(new IntegerFieldEditor("launchCount", "Number of times it has been launched", getFieldEditorParent()));
		IntegerFieldEditor offset = new IntegerFieldEditor("offset", "Current offset from GMT", getFieldEditorParent());
		offset.setValidRange(-14, +12);
		addField(offset);
		String[][] data = ZoneId.getAvailableZoneIds()//
				.stream().sorted().map(s -> new String[] { s, s }) //
				.collect(Collectors.toList()).toArray(new String[][] {});
		addField(new ComboFieldEditor("favorite", "Favorite time zone", data, getFieldEditorParent()));
		addField(new BooleanFieldEditor("tick", "Boolean value", getFieldEditorParent()));
		addField(new ColorFieldEditor("colour", "Favourite colour", getFieldEditorParent()));
		addField(new ScaleFieldEditor("scale", "Scale", getFieldEditorParent(), 0, 360, 10, 90));
		addField(new FileFieldEditor("file", "Pick a file", getFieldEditorParent()));
		addField(new DirectoryFieldEditor("dir", "Pick a directory", getFieldEditorParent()));
		addField(new PathEditor("path", "Path", "Directory", getFieldEditorParent()));
		addField(new RadioGroupFieldEditor("group", "Radio choices", 3, data, getFieldEditorParent(), true));
	}

	public void init(IWorkbench workbench) {
		setPreferenceStore(new ScopedPreferenceStore(InstanceScope.INSTANCE, "de.uniluebeck.isp.tessla.ui"));
	}
}