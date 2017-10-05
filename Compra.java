/* Author: Ricardo Sena
 * Author: Iago Mordente
 *
 *
 * */


import java.io.*;
class Compra{

	public int idCompra;
	public int idCliente;
	public int [] itens;
	public char delete;

	public Compra(){
		idCompra = 0;
		idCliente = 0;
		itens = new int[0];
		delete = ' ';
	}

	
	public String toString(){
	   String result = "";
	   Livro livro = null;
	   RandomAccessFile file = null;
		  try{
			 file = new RandomAccessFile("livraria.txt", "rw");
		 }catch(Exception e){
			e.printStackTrace();
		 }
		  
	   result += idCompra + "|" + idCliente + "| Itens: \n";
	   for(int i = 0; i < itens.length; i++){
		  try{
			 livro = Livro.pesquisarLivro(itens[i], file);
		 }catch(Exception e){
			e.printStackTrace();
		 }
		  
		  if(livro != null){
			 result += livro.toString() + "\n";
		 }  
	  }
		return result;
	}

	//------------------------------------------
	//metodos set

	public void setIdCompra(int idCompraIn){
		idCompra = idCompraIn;
	}

	public void setIdCliente(int idClienteIn){
		idCliente = idClienteIn;
	}

	public void setItens(int []itensIn){
		itens = itensIn;
	}

	public void setDelete(char deleteIn){
		if(deleteIn == ' ' || deleteIn == '*'){
			delete = deleteIn;
		}
	}

	//------------------------------------------
	//metodos get

	public int getidCompra(){
		return idCompra;
	}

	public int [] getItens(){
		return itens;
	}

	public int getIdCliente(){
		return idCliente;
	}

	public char getDelete(){
		return delete;
	}

	//------------------------------------------
	//metodos read e write object

	public void writeObject(RandomAccessFile file)throws Exception{
		file.writeInt(idCompra);
		file.writeInt(idCliente);
		file.writeInt(itens.length);
		for (int i = 0; i<itens.length;i++){
			file.writeInt(itens[i]);
		}
	}//end writeobject

	public void readObject(RandomAccessFile file)throws Exception{
		if(file.getFilePointer() != file.length() && file.length()!=0){
			setIdCompra(file.readInt());
			setIdCliente(file.readInt());
			int n = file.readInt();
			int [] itensIn = new int [n];
			for (int i = 0; i<n; i++){
				itens[i] = file.readInt();
			}
			setItens(itensIn);
		}
	}//end readobject


	//------------------------------------------
	//CRUD

	public void cadastrarCompra(RandomAccessFile file){
		int codigo;
		try {
			if(file.length() == 0){
				file.writeInt(0);
			}
			file.seek(0);
			codigo = file.readInt();
			setIdCompra(codigo);
			file.seek(file.length());
			writeObject(file);
			file.seek(0);
			file.writeInt(codigo+1);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void deletarCompraTmp(int codigo, RandomAccessFile file)throws Exception{
		long pos = 0;
		Compra compra = new Compra();
		boolean stop = false;
		if(file.length()!=0){
			file.seek(0);
			file.readInt();
			while(!stop && file.getFilePointer() < file.length()){
				pos = file.getFilePointer();
				compra.readObject(file);
				if(compra.getidCompra() == codigo){
					compra.setDelete('*');
					stop = true;
				}//end if
			}

			file.seek(pos);
			compra.writeObject(file);
		}//end if principal

	}
	
	public void mostrarCompras(RandomAccessFile file)throws IOException{
      		file.seek(0);
      		try{
      			int n = file.readInt();
      			Compra compra = new Compra();
      			for (int i = 0; i < n; i++){
      				compra.readObject(file);
      				if(compra.getDelete() != '*'){
      					System.out.println(compra.toString());
      				}
      			}
      		}catch (EOFException e){
      			System.out.println("Arquivo vazio.");
      		}catch(Exception e){
      			e.printStackTrace();
      		}
      	}
   }

