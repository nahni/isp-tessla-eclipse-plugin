package de.uniluebeck.isp.tessla.ui;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class TesslaPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	
	public static final String DOCKER_FILE_PREFERENCE = "dockerFile";
	public static final String CONTAINER_DIR_PREFERENCE = "test";
	
	public TesslaPreferencePage() {
		super(GRID);
		
		// Set the preference store for the preference page.
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	protected void createFieldEditors() {
		addField(new FileFieldEditor(DOCKER_FILE_PREFERENCE, "Docker File: ", getFieldEditorParent()));
		addField(new DirectoryFieldEditor(CONTAINER_DIR_PREFERENCE, "Container dir: ", getFieldEditorParent()));
	}

	public void init(IWorkbench workbench) {
	}
}