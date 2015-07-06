package com.example.controledepresencas;

import java.util.ArrayList;

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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityProfConsAlunoTurma extends Activity {
	/*private Boolean[] presencas;
	private String[] datas;
	private String[] nomesAlunos;
	private int[] idAlunos;
	private int currentPosition = 0;*/
	private String userName;
	private String userType;
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
		listarPresencas(1);
		//listarAlunos();
		//ajustarTela();
		//listarTurmas();
		
		
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

		//nomesAlunos = new String[] { "Alan", "Pedro", "Jeferson", "Joao", "Fulano1", "Fulano2" };
		//ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, nomesAlunos);
		Spinner listaAlunos = (Spinner) findViewById(R.id.spinnerAlunos);

		//listaAlunos.setAdapter(adapter);

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
		
		//Ex. Endere�o Rest:
		//http://EndIP:8080/CPresenca/api/presenca/usuario/Joao/turmaId/1
		
		//O username associado ao Aluno obtido pelo evento select do spinner
		//precisa ser obtido pra passar como parametro na URL...
		// fazer a chamada rest aqui
		/*ArrayList<Boolean> presencas = new ArrayList<Boolean>();
		presencas.add(true);
		presencas.add(false);
		presencas.add(true);
		presencas.add(true);
		presencas.add(false);
		ArrayList<String> datas = new ArrayList<String>();
		datas.add("03/07/2014");
		datas.add("04/07/2014");
		datas.add("05/07/2014");
		datas.add("06/07/2014");
		datas.add("09/07/2014");*/
		// TODO 
		//String retorno = RestClient.doRequisition("presenca/usuario/" + this.userName + "/turmaId/" + this.turmaId);
		
		String retorno = RestClient.doRequisition("presenca/usuario/Joao"+ "/turmaId/1");
		
		//Log.i("XML", retorno);
		
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
		//TODO Pedrir confirmação e enviar alteração
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		//currentPosition = position;
		builder.setMessage("Tem certeza que desaja alterar a presença de "/* TODO + nomesAlunos[position]*/).setPositiveButton("Sim", new DialogInterface.OnClickListener() {
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
