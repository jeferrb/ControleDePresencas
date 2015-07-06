package com.example.controledepresencas;

public class ItemPresencaAlunoTurma {
	String dataChamada;
	boolean isPresente;
	
	public ItemPresencaAlunoTurma() {
		
	}
	
	public ItemPresencaAlunoTurma(String dataChamada, boolean isPresente) {
		super();
		this.dataChamada = dataChamada;
		this.isPresente = isPresente;
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
