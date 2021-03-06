package com.accenture.analisifunzionale.classi;

public class AssociazioneCampi {
	
	private Campo campoFrom;
	private Campo campoTo; 
	
	public Campo getCampoFrom() {
		return campoFrom;
	}
	public void setCampoFrom(Campo campoFrom) {
		this.campoFrom = campoFrom;
	}
	public Campo getCampoTo() {
		return campoTo;
	}
	public void setCampoTo(Campo campoTo) {
		this.campoTo = campoTo;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("campoFrom = ");
		builder.append(campoFrom);
		builder.append(", campoTo = ");
		builder.append(campoTo);
		return builder.toString();
	}
	
	
}
