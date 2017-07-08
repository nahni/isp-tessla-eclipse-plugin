package de.uniluebeck.isp.tessla.ui;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;

public class TesslaPerspective implements IPerspectiveFactory{

	@Override
	public void createInitialLayout(IPageLayout layout) {
		defineActions(layout);
	    defineLayout(layout);
	}

	public void defineActions(IPageLayout layout) {
        // Add "new wizards".
        layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.folder");
        layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.file");

        // Add "show views".
        layout.addShowViewShortcut(IPageLayout.ID_RES_NAV);
        layout.addShowViewShortcut(IPageLayout.ID_BOOKMARKS);
        layout.addShowViewShortcut(IPageLayout.ID_OUTLINE);
        layout.addShowViewShortcut(IPageLayout.ID_PROP_SHEET);
        layout.addShowViewShortcut(IPageLayout.ID_TASK_LIST);
	}
	
	public void defineLayout(IPageLayout layout) {
        // Editors are placed for free.
        String editorArea = layout.getEditorArea();

        // Place navigator and outline to left of
        // editor area.
        IFolderLayout left =
                layout.createFolder("left", IPageLayout.LEFT, (float) 0.2, editorArea);
        left.addView(IPageLayout.ID_RES_NAV);
        
        IFolderLayout bottom = 
        		layout.createFolder("bottom", IPageLayout.BOTTOM, (float) 0.75, editorArea);
        bottom.addView(IConsoleConstants.ID_CONSOLE_VIEW);
	}

}
