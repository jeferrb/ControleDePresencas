package com.example.controledepresencas.model;

public class ItemConsultaTurma {
	boolean chamadaAberta;
	String nomeDisciplina;
	String idTurma;
	
	public ItemConsultaTurma() {
	}
	public ItemConsultaTurma(boolean chamadaAberta, String nomeDisciplina, String idTurma) {
		this.chamadaAberta = chamadaAberta;
		this.nomeDisciplina = nomeDisciplina;
		this.idTurma = idTurma;
	}
	public boolean isChamadaAberta() {
		return chamadaAberta;
	}

	public void setChamadaAberta(boolean chamadaAberta) {
		this.chamadaAberta = chamadaAberta;
	}

	public String getNomeDisciplina() {
		return nomeDisciplina;
	}

	public void setNomeDisciplina(String nomeDisciplina) {
		this.nomeDisciplina = nomeDisciplina;
	}

	public String getIdTurma() {
		return idTurma;
	}

	public void setIdTurma(String idTurma) {
		this.idTurma = idTurma;
	}
}
