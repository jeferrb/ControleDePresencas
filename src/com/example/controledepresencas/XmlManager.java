package com.example.controledepresencas;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

import com.example.controledepresencas.model.ItemAlunoTurma;
import com.example.controledepresencas.model.ItemConsultaTurma;
import com.example.controledepresencas.model.ItemPresencaAlunoTurma;

public class XmlManager {
	static String TAG = "XmlManager";
	// static String test =
	// "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<LoginUsuario>\n<sucess>true</sucess>\n<tipo>Aluno</tipo>\n</LoginUsuario>";
	static final String text_network_fail = "Falha de rede, por favor verifique sua conexão e tente novamente";
	static final String text_unknow_error = "Erro desconhecido, por favor contate o administrador";

	public static String[] manageXmlLogin(String rawXml) {
		// rawXml = test;
		/*
		 * return[0]: status return[1]:
		 */

		/*
		 * input example: <?xml version="1.0" encoding="UTF-8"
		 * standalone="yes"?> <LoginUsuario> <sucess>true</sucess>
		 * <tipo>Aluno</tipo> </LoginUsuario>
		 */
		XmlPullParserFactory xmlFactoryObject;
		int event;
		String text = null;
		String[] retorno = new String[2];
		retorno[0] = "false";
		retorno[1] = text_network_fail;
		try {
			InputStream stream = new ByteArrayInputStream(rawXml.getBytes("UTF-8"));
			xmlFactoryObject = XmlPullParserFactory.newInstance();
			XmlPullParser myParser = xmlFactoryObject.newPullParser();

			myParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			myParser.setInput(stream, null);
			event = myParser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				String name = myParser.getName();
				switch (event) {
				case XmlPullParser.START_TAG:
					break;
				case XmlPullParser.TEXT:
					text = myParser.getText();
					break;
				case XmlPullParser.END_TAG:
					if (name.equals("sucess")) {
						if (text.equals("false")) {
							return retorno;
						}
					}
					if (name.equals("tipo")) {
						retorno[0] = text;
					}
					if (name.equals("chave")) {
						Log.e(TAG, "Chave: " + text);
						retorno[1] = text;
					}
					break;
				}
				event = myParser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retorno;
	}

	public static boolean manageXmlTick(String rawXml) {
		/*
		 * return isRecebido
		 */
		/*
		 * input example:
		 * 
		 * <ticket><acabouAula>false</acabouAula><isRecebido>true</isRecebido></ticket>
		 * <ticket><acabouAula>true</acabouAula><isRecebido>false</isRecebido></ticket>
		 * 
		 * <ticket>
			<acabouAula>true</acabouAula>
			<isRecebido>false</isRecebido>
			</ticket>
		 */
		XmlPullParserFactory xmlFactoryObject;
		int event;
		String text = null;
		String[] retorno = new String[2];
		retorno[0] = text_network_fail;
		try {
			InputStream stream = new ByteArrayInputStream(rawXml.getBytes("UTF-8"));
			xmlFactoryObject = XmlPullParserFactory.newInstance();
			XmlPullParser myParser = xmlFactoryObject.newPullParser();

			myParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			myParser.setInput(stream, null);
			event = myParser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				String name = myParser.getName();
				switch (event) {
				case XmlPullParser.START_TAG:
					break;
				case XmlPullParser.TEXT:
					text = myParser.getText();
					break;
				case XmlPullParser.END_TAG:
					if (name.equals("isRecebido")) {
						return Boolean.parseBoolean(text);
					}
					/*if (name.equals("acabouAula")) {
						retorno[0] = text;
					}*/
					break;
				}
				event = myParser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static String manageXmlLogout(String rawXml) {
		// rawXml = test;
		/*
		 * return: 0 in case of fail 1 in case of Login or Password incorrect 2
		 * reserved 3 in case of student 4 in case of professor
		 */

		/*
		 * input examples: <?xml version="1.0" encoding="UTF-8"
		 * standalone="yes"?> <LoginUsuario> <sucess>true</sucess>
		 * </LoginUsuario>
		 * 
		 * <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
		 * <LoginUsuario> <sucess>false</sucess> <tipo>Chave errada</tipo>
		 * </LoginUsuario>
		 */
		XmlPullParserFactory xmlFactoryObject;
		int event;
		String text = null;
		int[] retorno = new int[2];
		retorno[0] = 0;
		try {
			InputStream stream = new ByteArrayInputStream(rawXml.getBytes("UTF-8"));
			xmlFactoryObject = XmlPullParserFactory.newInstance();
			XmlPullParser myParser = xmlFactoryObject.newPullParser();

			myParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			myParser.setInput(stream, null);
			event = myParser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				String name = myParser.getName();
				switch (event) {
				case XmlPullParser.START_TAG:
					break;
				case XmlPullParser.TEXT:
					text = myParser.getText();
					break;
				case XmlPullParser.END_TAG:
					if (name.equals("sucess")) {
						if (text.equals("true")) {
							return "Sucesso";
						}
					}
					if (name.equals("tipo")) {
						return text;
					}
					break;
				}
				event = myParser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return text_network_fail;
	}

	public static ArrayList<String[]> manageXmlFinalizarChamada(String rawXml) {
		/*
		 * retorna: retorno[0]= descrisção da falha
		 * 
		 * OU
		 * 
		 * um arraylist de: [nomeAluno, isFinalizada, isPresente]
		 */

		/*
		 * input examples: 
		 * <finalizaChamadas>
		 * <FinalizaAula>
		 * <causa>Chamadacontinuaaberta</causa>
		 * <isFinalizada>false</isFinalizada>
		 * </FinalizaAula>
		 * </finalizaChamadas>
		 * 
		 * 
		 <?xml version="1.0" encoding="UTF-8" standalone="yes"?>

    <finalizaChamadas>

        <FinalizaAula>

            <Aluno>
Lele
            </Aluno>

            <isFinalizada>
true
            </isFinalizada>

            <isPresente>
false
            </isPresente>
        </FinalizaAula>

        <FinalizaAula>

            <Aluno>
Joao Luis
            </Aluno>

            <isFinalizada>
true
            </isFinalizada>

            <isPresente>
false
            </isPresente>
        </FinalizaAula>

        <FinalizaAula>

            <Aluno>
Lala
            </Aluno>

            <isFinalizada>
true
            </isFinalizada>

            <isPresente>
false
            </isPresente>
        </FinalizaAula>

        <FinalizaAula>

            <Aluno>
Lulu
            </Aluno>

            <isFinalizada>
true
            </isFinalizada>

            <isPresente>
false
            </isPresente>
        </FinalizaAula>
    </finalizaChamadas>

		 
		 
		 * <?xml version="1.0" encoding="UTF-8" standalone="yes"?><finalizaChamadas><FinalizaAula><Aluno>Lele</Aluno><isFinalizada>true</isFinalizada><isPresente>false</isPresente></FinalizaAula><FinalizaAula><Aluno>Joao Luis</Aluno><isFinalizada>true</isFinalizada><isPresente>false</isPresente></FinalizaAula><FinalizaAula><Aluno>Lala</Aluno><isFinalizada>true</isFinalizada><isPresente>false</isPresente></FinalizaAula><FinalizaAula><Aluno>Lulu</Aluno><isFinalizada>true</isFinalizada><isPresente>false</isPresente></FinalizaAula></finalizaChamadas>
		 */
		
		ArrayList<String[]> retorno = new ArrayList<String[]>();
		String[] current = new String[3];
		XmlPullParserFactory xmlFactoryObject;
		int event;
		String text = null;
		try {
			InputStream stream = new ByteArrayInputStream(rawXml.getBytes("UTF-8"));
			xmlFactoryObject = XmlPullParserFactory.newInstance();
			XmlPullParser myParser = xmlFactoryObject.newPullParser();

			myParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			myParser.setInput(stream, null);
			event = myParser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				String name = myParser.getName();
				switch (event) {
				case XmlPullParser.START_TAG:
					break;
				case XmlPullParser.TEXT:
					text = myParser.getText();
					break;
				case XmlPullParser.END_TAG:
					if (name.equals("causa")) {
						current = new String[1];
						current[0] = text;
						retorno.add(current);
						return retorno;
					}
					if (name.equals("Aluno")) {
						current[0] = text;
					}
					if (name.equals("isFinalizada")) {
						current[1] = text;
					}
					if (name.equals("isPresente")) {
						current[2] = text;
						retorno.add(new String[]{current[0], current[1], current[2]});
					}
					break;
				}
				event = myParser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retorno;
	}

	public static ArrayList<ItemConsultaTurma> manageXmlTurmas(String rawXml) {
		/*
		 * return a array of classes with each [i] corresponding a each
		 * discipline an j: 0 = idTurma; 1 = discipline name; 2 = opened or not
		 */

		/*
		 * input example:
		<turmaLogins>
		<LoginTurma>
		<chamadaAberta>false</chamadaAberta>
		<idTurma>1</idTurma>
		<nomeDisciplina>Engenharia de software</nomeDisciplina>
		</LoginTurma>
		<LoginTurma>
		<chamadaAberta>false</chamadaAberta>
		<idTurma>2</idTurma>
		<nomeDisciplina>test</nomeDisciplina>
		</LoginTurma>
		</turmaLogins>
		 */

		ArrayList<ItemConsultaTurma> retorno = new ArrayList<ItemConsultaTurma>();
		ItemConsultaTurma itemConsultaTurma = new ItemConsultaTurma();
		
		XmlPullParserFactory xmlFactoryObject;
		int event;
		String text = null;
		try {
			InputStream stream = new ByteArrayInputStream(rawXml.getBytes("UTF-8"));
			xmlFactoryObject = XmlPullParserFactory.newInstance();
			XmlPullParser myParser = xmlFactoryObject.newPullParser();

			myParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			myParser.setInput(stream, null);
			event = myParser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				String name = myParser.getName();
				switch (event) {
				case XmlPullParser.START_TAG:
					break;
				case XmlPullParser.TEXT:
					text = myParser.getText();
					break;
				case XmlPullParser.END_TAG:
					if (name.equals("chamadaAberta")) {
						itemConsultaTurma.setChamadaAberta(Boolean.parseBoolean(text));
					}
					if (name.equals("idTurma")) {
						itemConsultaTurma.setIdTurma(text);
					}
					if (name.equals("nomeDisciplina")) {
						itemConsultaTurma.setNomeDisciplina(text);
						retorno.add(new ItemConsultaTurma(itemConsultaTurma.isChamadaAberta(), itemConsultaTurma.getNomeDisciplina(), itemConsultaTurma.getIdTurma()));
					}
					break;
				}
				event = myParser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retorno;
	}

	public static String[] manageXmlInicairChamada(String rawXml) {
		/*
		 * output example: [isInicializada, chamdaID, tempoTicket]
		 * output example: [false, causa]
		 * 
		 * prof:
		 * <InicializaAula> <isInicializada>true</isInicializada>
		 * </InicializaAula>
		 * 
		 *  OU
		 *  
		 *  aluno:
			<InicializaAula>
			<chamdaID>42</chamdaID> 
			<isInicializada>true</isInicializada> 
			<tempoTicket>2</tempoTicket> 
			</InicializaAula>
  
		 */
		XmlPullParserFactory xmlFactoryObject;
		int event;
		String text = null;
		String[] retorno = new String[3];
		try {
			InputStream stream = new ByteArrayInputStream(rawXml.getBytes("UTF-8"));
			xmlFactoryObject = XmlPullParserFactory.newInstance();
			XmlPullParser myParser = xmlFactoryObject.newPullParser();

			myParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			myParser.setInput(stream, null);
			event = myParser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				String name = myParser.getName();
				switch (event) {
				case XmlPullParser.START_TAG:
					break;
				case XmlPullParser.TEXT:
					text = myParser.getText();
					break;
				case XmlPullParser.END_TAG:
					if (name.equals("isInicializada")) {
						retorno[0]=text;
					}
					if (name.equals("causa")) {
						retorno[0] = "false";
						retorno[1] = text;
						return retorno;
					}
					if (name.equals("chamdaID")) {
						retorno[1] = text;
					}
					if (name.equals("tempoTicket")) {
						retorno[2] = text;
					}
					break;
				}
				event = myParser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		retorno[1] = text_network_fail;
		return retorno;
	}
	public static String manageXmlCheckIn(String rawXml) {
		/*
		 * input example:
		 * 
		 * <InicializaAula> <isInicializada>true</isInicializada>
		 * </InicializaAula>
		 */
		XmlPullParserFactory xmlFactoryObject;
		int event;
		String text = null;
		int[] retorno = new int[2];
		retorno[0] = 0;
		try {
			InputStream stream = new ByteArrayInputStream(rawXml.getBytes("UTF-8"));
			xmlFactoryObject = XmlPullParserFactory.newInstance();
			XmlPullParser myParser = xmlFactoryObject.newPullParser();

			myParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			myParser.setInput(stream, null);
			event = myParser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				String name = myParser.getName();
				switch (event) {
				case XmlPullParser.START_TAG:
					break;
				case XmlPullParser.TEXT:
					text = myParser.getText();
					break;
				case XmlPullParser.END_TAG:
					if (name.equals("isInicializada")) {
						if (text.equals("true")) {
							return "true";
						}
						return text;
					}
					break;
				}
				event = myParser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return text_network_fail;
	}
	public static String manageXmlCheckOut(String rawXml) {
		/*
		 * input examples:
		 * 
		 * 
		 * 
		 * 
		 * 
		 * <InicializaAula> <causa>Chamada ja aberta</causa>
		 * <isInicializada>false</isInicializada> </InicializaAula>
		 */
		XmlPullParserFactory xmlFactoryObject;
		int event;
		String text = null;
		int[] retorno = new int[2];
		retorno[0] = 0;
		try {
			InputStream stream = new ByteArrayInputStream(rawXml.getBytes("UTF-8"));
			xmlFactoryObject = XmlPullParserFactory.newInstance();
			XmlPullParser myParser = xmlFactoryObject.newPullParser();

			myParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			myParser.setInput(stream, null);
			event = myParser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				String name = myParser.getName();
				switch (event) {
				case XmlPullParser.START_TAG:
					break;
				case XmlPullParser.TEXT:
					text = myParser.getText();
					break;
				case XmlPullParser.END_TAG:
					if (name.equals("sucess")) {
						if (text.equals("true")) {
							return "true";
						}
					}
					if (name.equals("causa")) {
						return text;
					}
					break;
				}
				event = myParser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Falha de rede!";
	}

	
	public static boolean manageXmlCheckOutAluno(String rawXml) {
		
		/*
		 * 
		 * 
		 * return isPresente;
		 * 
		 *<InicializaAula><isInicializada>false</isInicializada><isPresente>true</isPresente></InicializaAula>
		 * 
		<InicializaAula>
		<causa>Chamada fechada</causa>
		<isInicializada>false</isInicializada>
		</InicializaAula>
		*/
		XmlPullParserFactory xmlFactoryObject;
		int event;
		String text = null;
		try {
			InputStream stream = new ByteArrayInputStream(rawXml.getBytes("UTF-8"));
			xmlFactoryObject = XmlPullParserFactory.newInstance();
			XmlPullParser myParser = xmlFactoryObject.newPullParser();

			myParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			myParser.setInput(stream, null);
			event = myParser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				String name = myParser.getName();
				switch (event) {
				case XmlPullParser.START_TAG:
					break;
				case XmlPullParser.TEXT:
					text = myParser.getText();
					break;
				case XmlPullParser.END_TAG:
					if (name.equals("isPresente")) {
						return Boolean.parseBoolean(text);
					}/*
					if (name.equals("idTurma")) {
						current[0] = text;
					}
					if (name.equals("nomeDisciplina")) {
						current[1] = text;
						retorno.add(current);
						current = new String[3];
					}*/
					break;
				}
				event = myParser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// TODO
		return false;
	}

	public static ArrayList<ItemPresencaAlunoTurma>  manageXmlPresencaAlunoTurma(String rawXml){
		
		ItemPresencaAlunoTurma itemPresAlunoTurma = new ItemPresencaAlunoTurma();
		ArrayList<ItemPresencaAlunoTurma> retorno = new ArrayList<ItemPresencaAlunoTurma>();
		
		Log.i("XMLManager --> XML CRU",rawXml+"\n\n");
		
		
		XmlPullParserFactory xmlFactoryObject;
		int event;
		String text = null;
		try {
			InputStream stream = new ByteArrayInputStream(rawXml.getBytes("UTF-8"));
			xmlFactoryObject = XmlPullParserFactory.newInstance();
			XmlPullParser myParser = xmlFactoryObject.newPullParser();
			myParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			myParser.setInput(stream, null);
			event = myParser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				String name = myParser.getName();
				switch (event) {
				case XmlPullParser.START_TAG:
					break;
				case XmlPullParser.TEXT:
					text = myParser.getText();
					break;
				case XmlPullParser.END_TAG:
					if (name.equals("diaChamada")) {
						itemPresAlunoTurma.setDataChamada(text);
					}
					if (name.equals("isPresente")) {
						//Log.i("XMLManager --> boolean XML",text);
						itemPresAlunoTurma.setPresente(Boolean.parseBoolean(text));
						//Log.i("XMLManager --> item", itemPresAlunoTurma.getDataChamada()+ "-"+ String.valueOf(itemPresAlunoTurma.isPresente()));
						retorno.add(new ItemPresencaAlunoTurma(itemPresAlunoTurma.getDataChamada(), itemPresAlunoTurma.isPresente()));
					}
					break;
				}
				event = myParser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for(int i=0; i< retorno.size(); i++){
			//Log.i("XMLManager->ArrayList", retorno.get(i).getDataChamada() +" - "+  String.valueOf(retorno.get(i).isPresente()));
		}
		return retorno;
	}

	
public static ArrayList<ItemAlunoTurma>  manageXmlAlunoTurma(String rawXml){
		
		ItemAlunoTurma itemAlunoTurma = new ItemAlunoTurma();
		ArrayList<ItemAlunoTurma> retorno = new ArrayList<ItemAlunoTurma>();
		
		Log.i("XMLManager --> XML CRU",rawXml+"\n\n");
		
		
		XmlPullParserFactory xmlFactoryObject;
		int event;
		String text = null;
		try {
			InputStream stream = new ByteArrayInputStream(rawXml.getBytes("UTF-8"));
			xmlFactoryObject = XmlPullParserFactory.newInstance();
			XmlPullParser myParser = xmlFactoryObject.newPullParser();
			myParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			myParser.setInput(stream, null);
			event = myParser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				String name = myParser.getName();
				switch (event) {
				case XmlPullParser.START_TAG:
					break;
				case XmlPullParser.TEXT:
					text = myParser.getText();
					break;
				case XmlPullParser.END_TAG:
					if (name.equals("id")) {
						itemAlunoTurma.setIdAluno(text);
					}
					if (name.equals("nome")) {
						itemAlunoTurma.setNomeAluno(text);
					}
					if (name.equals("usuario")) {
						itemAlunoTurma.setUsuarioAluno(text);
						Log.i("XMLManager --> item", itemAlunoTurma.getIdAluno()+ " - "+ itemAlunoTurma.getNomeAluno()+" - "+itemAlunoTurma.getUsuarioAluno());
						retorno.add(new ItemAlunoTurma(itemAlunoTurma.getIdAluno(), itemAlunoTurma.getNomeAluno(), itemAlunoTurma.getUsuarioAluno()));
					}
					break;
				}
				event = myParser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for(int i=0; i< retorno.size(); i++){
			Log.i("XMLManager->ArrayList", retorno.get(i).getIdAluno() +" - "+ retorno.get(i).getNomeAluno()+" - "+retorno.get(i).getUsuarioAluno());
		}
		return retorno;
	}
	
}
