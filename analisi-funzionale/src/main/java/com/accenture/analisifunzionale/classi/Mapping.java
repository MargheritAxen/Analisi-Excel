package com.accenture.analisifunzionale.classi;

public class Mapping {

	private String classeFrom;
	private String classeTo;
	private String tipoMapping;
	private AssociazioneCampi associazioneCampi;

	public Mapping() {
	}


	public Mapping(String classeFrom, String classeTo, String tipoCampo) {
		super();
		this.classeFrom = classeFrom;
		this.classeTo = classeTo;
		this.tipoMapping = tipoCampo;
	}

	public AssociazioneCampi getAssociazioneCampi() {
		return associazioneCampi;
	}

	public void setAssociazioneCampi(AssociazioneCampi associazioneCampi) {
		this.associazioneCampi = associazioneCampi;
	}

	public String getClasseFrom() {
		return classeFrom;
	}

	public void setClasseFrom(String classeFrom) {
		this.classeFrom = classeFrom;
	}

	public String getClasseTo() {
		return classeTo;
	}

	public void setClasseTo(String classeTo) {
		this.classeTo = classeTo;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("classeFrom = ");
		builder.append(classeFrom);
		builder.append(", classeTo = ");
		builder.append(classeTo);
		builder.append(", tipoMapping = ");
		builder.append(tipoMapping);
		return builder.toString();
	}
}
