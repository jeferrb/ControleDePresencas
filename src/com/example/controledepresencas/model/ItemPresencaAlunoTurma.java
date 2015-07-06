package com.example.controledepresencas.model;

public class ItemPresencaAlunoTurma {
	String dataChamada;
	boolean isPresente;
	String nomeAluno;
	
	public ItemPresencaAlunoTurma() {
	}
	public ItemPresencaAlunoTurma(String dataChamada, boolean isPresente) {
		this.dataChamada = dataChamada;
		this.isPresente = isPresente;
	}
	public ItemPresencaAlunoTurma(boolean isPresente, String nomeAluno) {
		this.nomeAluno = nomeAluno;
		this.isPresente = isPresente;
	}
	public String getNomeAluno() {
		return nomeAluno;
	}
	public void setNomeAluno(String nomeAluno) {
		this.nomeAluno = nomeAluno;
	}
	public String getDataChamada() {
		return dataChamada;
	}
	public void setDataChamada(String dataChamada) {
		this.dataChamada = dataChamada;
	}
	public boolean isPresente() {
		return isPresente;
	}
	public void setPresente(boolean isPresente) {
		this.isPresente = isPresente;
	}
	

}
