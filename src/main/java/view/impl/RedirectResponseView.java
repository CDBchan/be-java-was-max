package view.impl;

import view.View;

public class RedirectResponseView extends View {

	public RedirectResponseView(String viewName) {
		super(viewName);
		setBody(render(viewName));
	}

	@Override
	public byte[] render(String view) {
		return new byte[0];
	}
}
