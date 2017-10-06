/* Author: Ricardo Sena
 * Author: Iago Mordente
 *
 *
 * */

/*
---------------------------------------------------------------------------------------------

teste
---------------------------------------------------------------------------------------------




To do:
	criar um metodo para limpar do arquivo principal os registros marcados para a remocao
	criar um metodo de pesquisa sem ser sequencial
	criar outros metodos de remocao para outras chaves de pesquisa
*/


import java.util.*;
import java.io.*;

class Livro{

	public int idLivro;
	public String nomeLivro;
	public String autor;
	public double preco;
	public String genero;
	public char delete;
	//------------------------------------
	//
	public static final int TAM_NOME = 100;
	public static final int TAM_AUTOR = 100;
	public static final int TAM_GENERO = 20;

	public Livro (){
		char[] defaultNome = new char[TAM_NOME];
		Arrays.fill(defaultNome, ' ');
		char[] defaultAutor = new char [TAM_AUTOR];
		Arrays.fill(defaultAutor, ' ');
		char[] defaultGenero = new char [TAM_GENERO];
		Arrays.fill(defaultGenero, ' ');


		idLivro = 0;
		nomeLivro = new String(defaultNome);
		autor = new String (defaultAutor);
		preco = 0.00;
		genero = new String (defaultGenero);
		delete = ' ';
	}

	public String toString(){
		return idLivro + "|" + nomeLivro.trim() + "|" + autor.trim() + "|" + preco + "|" + genero.trim();
	}

	//------------------------------------------
	//metodos set

	public void setNome(String nomeIn){
		String aux = "";
		if(nomeIn.length() > TAM_NOME){
			aux = nomeIn.substring(0,TAM_NOME-1);
			nomeLivro = aux;
		}else{
			char[] comp = new char[TAM_NOME-nomeIn.length()];
			Arrays.fill(comp, ' ');
			aux = nomeIn.concat(new String (comp));
			nomeLivro = aux;
		}
	}
	public void setID(int idIn){
		idLivro = idIn;
	}
	public void setAutor(String autorIn){
		String aux = "";
		if (autorIn.length() > TAM_AUTOR){
			aux = autorIn.substring(0,TAM_AUTOR-1);
			autor = aux;
		}else	{
			char[] comp = new char[TAM_AUTOR-autorIn.length()];
			Arrays.fill(comp, ' ');
			aux = autorIn.concat(new String (comp));
		 	autor = aux;
		}
	}
	public void setPreco(double precoIn){
		preco = precoIn;
	}
	public void setGenero(String generoIn){
		String aux = "";
		if (generoIn.length() > TAM_GENERO){
			aux = generoIn.substring(0,TAM_GENERO-1);
			autor = aux;
		}else	{
			char[] comp = new char[TAM_GENERO-generoIn.length()];
			Arrays.fill(comp, ' ');
			aux = generoIn.concat(new String (comp));
			generoIn = aux;
		}
	}

	public void setDelete(char deleteIn){
		if(deleteIn == ' ' || deleteIn == '*'){
			delete = deleteIn;
		}
	}
	//------------------------------------------
	//metodos get
	public String getNome(){
		return nomeLivro;
	}
	public int getID(){
		return idLivro;
	}
	public String getAutor(){
		return autor;
	}

	public double getPreco(){
		return preco;
	}

	public String getGenero(){
		return genero;
	}

	public char getDelete(){
		return delete;
	}

	//------------------------------------------

	public void writeObject(RandomAccessFile file) throws IOException{
		file.writeChar(delete);
		file.writeInt(idLivro);
		file.writeUTF(nomeLivro);
		file.writeUTF(autor);
		file.writeUTF(genero);
		file.writeDouble(preco);
	}

	public void readObject(RandomAccessFile file) throws IOException{
		if(file.getFilePointer() != file.length() && file.length()!=0){
			setDelete(file.readChar());
			setID(file.readInt());
			setNome(file.readUTF());
			setAutor(file.readUTF());
			setGenero(file.readUTF());
			setPreco(file.readDouble());
		}
	}
//--------------------------------------

	public void cadastrarLivro(RandomAccessFile file){
		int codigo;
		try {
			if(file.length() == 0){
				file.writeInt(0);
			}
			file.seek(0);
			codigo = file.readInt();
			setID(codigo);
			file.seek(file.length());
			writeObject(file);
			file.seek(0);
			file.writeInt(codigo+1);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void deletarLivroTmp(int codigo, RandomAccessFile file)throws IOException{
		long pos = 0;
		Livro livro = new Livro();
		boolean stop = false;
		if(file.length()!=0){
			file.seek(0);
			file.readInt();
			while(!stop && file.getFilePointer() < file.length()){
				pos = file.getFilePointer();
				livro.readObject(file);
				if(livro.getID() == codigo){
					livro.setDelete('*');
					stop = true;
				}//end if
			}

			file.seek(pos);
			livro.writeObject(file);
		}//end if principal
	}



	public static void editarLivro(int codigo, String campo, String mod, RandomAccessFile file)throws IOException{
		Livro livro = new Livro();
		boolean stop = false, found = false;
		long i = 0;
		int j = 0, n = 0;
		if(file.length() != 0){
			file.seek(0);
			n = file.readInt();
			do {
				try{
					i = file.getFilePointer();//armazena onde começa o objeto lido
					livro.readObject(file);
					if(livro.getID() == codigo){
						found = true;
						stop = true;
					}
				}catch(EOFException e){
					stop = true;
				}
				j++;
			} while (livro.getID()!=codigo && !stop && j < n);

			if(found){
				if(campo.equals("nomeLivro")){
					livro.setNome(mod);
				}else if(campo.equals("autor")){
					livro.setAutor(mod);
				}else if(campo.equals("preco")){
					try{
						livro.setPreco(Double.parseDouble(mod));
					}catch(NumberFormatException e){
						e.printStackTrace();
					}
				}else if(campo.equals("genero")){
					livro.setGenero(mod);
				}

				file.seek(i);//volta a posicao do objeto a ser alterado
				livro.writeObject(file);
			}else{
				System.out.println("Alteracao nao realizada, livro nao encontrado.");
			}

		}//end if principal
	}

	public static void mostrarLivros(RandomAccessFile file)throws Exception{
		file.seek(0);
		try{
			int n = file.readInt();
			Livro livro = new Livro();
			for (int i = 0; i < n; i++){
				livro.readObject(file);
				if(livro.getDelete() != '*'){
					System.out.println(livro.toString());
				}
			}
		}catch (EOFException e){
			System.out.println("Arquivo vazio.");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

public static Livro pesquisarLivro(int idLivro, RandomAccessFile file)throws Exception{
	boolean found = false;
	Livro livro = null;
	if (file.length() != 0){
		file.seek(0);
		file.readInt();
		while(file.getFilePointer() < file.length() && !found){
		   	livro = new Livro();
			livro.readObject(file);
			if(livro.getID() == idLivro && livro.getDelete()!='*'){
				found = true;
			}//end if
		}//end while
	}//end main if
	if(!found){
	   livro = null;
	}
	return livro;
}

/* deixar pronto caso haja necessidade de pesquisar pelo nomeLivro tambem
public static boolean pesquisarLivro(String modo, String livro, RandomAccessFile file){
		boolean result = false;
		Livro	livro = new Livro();
		if(modo.equals("idLivro")){
			while(file.getFilePointer() < file.length() && !result){
				livro.readObject(file);
				if(livro.getID() == Integer.parseInt(livro)){
					result = true;
				}
			}//end while
		}else if (modo.equals("nomeLivro")){
			while(file.getFilePointer() < file.length() && !result){
				livro.readObject(file);
				if(livro.getNome().equals(livro)){
					result = true;
				}
			}
		}//end else if
		return result;
	}
*/
}//end class
