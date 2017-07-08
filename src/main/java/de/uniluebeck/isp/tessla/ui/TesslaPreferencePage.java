package de.uniluebeck.isp.tessla.ui;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

public class TesslaPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	public TesslaPreferencePage() {
		super(GRID);
	}

	protected void createFieldEditors() {
		addField(new IntegerFieldEditor("launchCount", "Number of times it has been launched", getFieldEditorParent()));
		
		addField(new FileFieldEditor("dockerFile", "Pick Docker file", getFieldEditorParent()));
		addField(new DirectoryFieldEditor("containerDir", "Pick a Container Dir", getFieldEditorParent()));
	}

	public void init(IWorkbench workbench) {
		setPreferenceStore(new ScopedPreferenceStore(InstanceScope.INSTANCE, "de.uniluebeck.isp.tessla.ui"));
	}
}