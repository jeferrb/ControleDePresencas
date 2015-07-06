package com.example.controledepresencas;

import java.util.ArrayList;

import com.example.controledepresencas.model.ItemPresencaAlunoTurma;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityProfConsAlunoTurma extends Activity {
	private String userName;
	private String nomeDisciplina;
	private String turmaId;
	private ArrayList<ItemPresencaAlunoTurma> ret;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prof_cons_aluno_turma);
		
		Intent intent = getIntent();
		if (intent != null) {
			Bundle params = intent.getExtras();
			if (params != null) {
				this.turmaId = params.getString("turmaID");
				this.nomeDisciplina = params.getString("nomeDisciplina");
				showToastMessage("TurmaID: " + this.turmaId + "\n" + "Disciplina: " + this.nomeDisciplina);
			}
		}
		ajustaTelaActivity();
		listarAlunos();
	}
	
	public void ajustaTelaActivity(){
		TextView textNomeDisciplina =  (TextView) findViewById(R.id.textViewNomeDisciplina);
		textNomeDisciplina.setText(this.nomeDisciplina);
		TextView textNomeTurma = (TextView) findViewById(R.id.textViewTurma);
		textNomeTurma.append(" "+this.turmaId);;
		
	}

	public void listarAlunos() {
		// aqui vai a chamada rest para listar todos os nomesAlunos de uma
		// determinada turma...

		Spinner listaAlunos = (Spinner) findViewById(R.id.spinnerAlunos);

		// Aqui vai o evento de click que vai atualizar o listview
		// com as datas e presencas dos nomesAlunos
		// Aqui dentro precisamos chamar o listarpresencas() toda vez que o
		// usuario selecionar um aluno diferente

		listaAlunos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				listarPresencas(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
	}

	public void listarPresencas(int pos) {
		//http:IP:8080/CPresenca/api/presenca/usuario/Joao/turmaId/1

		String retorno = RestClient.doRequisition("presenca/usuario/" + this.userName + "/turmaId/" + this.turmaId);
		
		
		this.ret = XmlManager.manageXmlPresencaAlunoTurma(retorno);
		
		Log.i("ItemPresencaAlunoTurma", this.ret.toString());
		
		ListView lv = (ListView) findViewById(R.id.listViewPresencas);
		lv.setAdapter(new AdapterPresencas(this, this.ret));

		// aqui vai o metodo que vai alterar a presenca do aluno
		// Vai ser um onClickListener para o listview...
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				confirmaAlterarPresenca(position);
				return true;
			}
		});
		lv.setOnItemClickListener(new OnItemClickListener() {

		    @Override
		    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		    	showToastMessage("Longo click para alterar a presença");
		    }

		});
	}
	
	private void confirmaAlterarPresenca(int position){
		//TODO rest alterar
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		//currentPosition = position;
		builder.setMessage("Tem certeza que desaja alterar a presença de "+userName+"?").setPositiveButton("Sim", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				enviarSolicitacaoAlterarPresenca();
			}
		}).setNegativeButton("Não", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				// Stay here!
			}
		});
		builder.show();
	}
	private void enviarSolicitacaoAlterarPresenca() {
		//TODO
		//int aluno = idAlunos[currentPosition];
		
	}

	@Override
	public void onBackPressed() {
		finish();
	}
	public void showToastMessage(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
	}
}
