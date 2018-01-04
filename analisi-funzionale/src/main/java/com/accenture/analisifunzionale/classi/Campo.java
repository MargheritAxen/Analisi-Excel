package com.accenture.analisifunzionale.classi;

public class Campo {

	private String nome;
	private String tipo;

	// Campo è vuoto e va riempito con le celle di excel
	public Campo() {
	}

	public Campo(String name, String type) {
		this.setNome(name);
		this.setTipo(type);
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		builder.append(" \"nome\" : \"");
		builder.append(this.getNome());
		builder.append("\", \"tipo\" : \"");
		builder.append(this.getTipo());
		builder.append("\"}");
		return builder.toString();
	}

	@Override
	public boolean equals(Object obj) {

		if (super.equals(obj)) {
			return true;
		}
		if (obj instanceof Campo) {
			Campo other = (Campo) obj;
			return other.nome.equals(this.nome) && other.tipo.equals(this.tipo);
		}
		return false;
	}

	public boolean omonimous(Campo other) {
		if (other == null) {
			return false;
		}
		return other.nome.equals(this.nome);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

}
