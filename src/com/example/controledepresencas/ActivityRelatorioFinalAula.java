package com.example.controledepresencas;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;

public class ActivityRelatorioFinalAula extends Activity {
	private ArrayList<Boolean> nomesAlunos;
	private ArrayList<Boolean> presencas;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity_relatorio_final_aula);
		//TODO exibir relatorio recebido da ActivityAula
		
	}
}
