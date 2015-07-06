package com.example.controledepresencas.model;

public class ItemAlunoTurma {
	String idAluno, nomeAluno, usuarioAluno;
	
	public ItemAlunoTurma() {
	
	}
	
	public ItemAlunoTurma(String idAluno, String nomeAluno, String usuarioAluno) {
		super();
		this.idAluno = idAluno;
		this.nomeAluno = nomeAluno;
		this.usuarioAluno = usuarioAluno;
	}

	public String getIdAluno() {
		return idAluno;
	}

	public void setIdAluno(String idAluno) {
		this.idAluno = idAluno;
	}

	public String getNomeAluno() {
		return nomeAluno;
	}

	public void setNomeAluno(String nomeAluno) {
		this.nomeAluno = nomeAluno;
	}

	public String getUsuarioAluno() {
		return usuarioAluno;
	}

	public void setUsuarioAluno(String usuarioAluno) {
		this.usuarioAluno = usuarioAluno;
	}	

}
